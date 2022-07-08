package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.sitronics.tn.taskservice.dto.ProcessGroupDto;
import ru.sitronics.tn.taskservice.dto.ProcessInstanceDto;
import ru.sitronics.tn.taskservice.dto.StartProcessInstanceDto;
import ru.sitronics.tn.taskservice.dto.VariableValueDto;
import ru.sitronics.tn.taskservice.exception.ProcessGroupException;
import ru.sitronics.tn.taskservice.model.DocumentTypeProcessMapping;
import ru.sitronics.tn.taskservice.model.Process;
import ru.sitronics.tn.taskservice.model.ProcessGroup;
import ru.sitronics.tn.taskservice.model.ProcessGroupStatusEnum;
import ru.sitronics.tn.taskservice.repository.ProcessGroupRepository;
import ru.sitronics.tn.taskservice.util.BpmValidationUtil;
import ru.sitronics.tn.taskservice.util.CustomRestClient;
import ru.sitronics.tn.taskservice.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessGroupServiceImpl implements ProcessGroupService {

    private final ProcessService processService;
    private final DocumentTypeProcessMappingService documentTypeProcessMappingService;
    private final CustomRestClient customRestClient;
    private final ProcessGroupRepository processGroupRepository;
    private final BpmValidationUtil bpmValidationUtil;

    @Override
    public ProcessGroupDto createProcessGroup(ProcessGroupDto processGroupDto) {

        ProcessGroup processGroup = processGroupRepository
                .save(ObjectUtils.convertObject(processGroupDto, new ProcessGroup()));

        DocumentTypeProcessMapping documentTypeProcessMapping = documentTypeProcessMappingService
                .getByDocumentType(processGroupDto.getDocumentType());

        if (!documentTypeProcessMapping.getValidationProcessKey().isBlank()
                || documentTypeProcessMapping.getValidationProcessKey() != null) {
            String validationProcessKey = documentTypeProcessMapping.getValidationProcessKey();
            bpmValidationUtil.validateProcessStart(processGroup, processGroupDto, validationProcessKey);
        }

        String processKey = documentTypeProcessMapping.getProcessKey();

        String endPointUri = String.format("/process-definition/key/%s/start", processKey);
        StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();

        VariableValueDto documentIdVariable = new VariableValueDto();
        documentIdVariable.setType("String");
        documentIdVariable.setValue(processGroupDto.getDocumentId());

        VariableValueDto startedByIdVariable = new VariableValueDto();
        startedByIdVariable.setType("String");
        startedByIdVariable.setValue(processGroupDto.getCreatedBy());

        VariableValueDto processGroupIdVariable = new VariableValueDto();
        processGroupIdVariable.setType("String");
        processGroupIdVariable.setValue(processGroup.getId());

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        if (processGroupDto.getVariables() != null) {
            variableValueDtoMap.putAll(processGroupDto.getVariables());
        }

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);
        variableValueDtoMap.put("processGroupId", processGroupIdVariable);

        startProcessInstanceDto.setVariables(variableValueDtoMap);

        ResponseEntity<ProcessInstanceDto> processEngineResponse =
                customRestClient.postJson(endPointUri, startProcessInstanceDto, ProcessInstanceDto.class);

        if (processEngineResponse.getStatusCode().is2xxSuccessful()) {

            Process process = new Process();
            try {
                ProcessInstanceDto processInstanceDto = Objects.requireNonNull(processEngineResponse.getBody());
                process.setProcessInstanceId(processInstanceDto.getId());
                process.setDefinitionId(processEngineResponse.getBody().getDefinitionId());
            } catch (NullPointerException e) {
                throw new ProcessGroupException("Process instance is null", e);
            }
            process.setDocumentId(processGroupDto.getDocumentId());
            processGroup.setStatus(ProcessGroupStatusEnum.IN_PROGRESS.toString());
            process.setProcessGroup(processGroup);
            return processService.createProcess(process).getProcessGroupDto();
        } else {
            throw new ProcessGroupException("Process start was unsuccessful");
        }
    }

    @Override
    public ProcessGroup getProcessGroupById(String id) {
        return processGroupRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ProcessGroupException(String.format("Couldn't find a process group by id: %s", id)));
    }

    @Override
    @Transactional
    public void updateProcessGroupStatus(String processGroupId, String status, String errorMessage) {
        ProcessGroup processGroup = processGroupRepository.findById(UUID.fromString(processGroupId))
                .orElseThrow(() -> new ProcessGroupException(String.format("Couldn't find a process group by id: %s", processGroupId)));
        processGroup.setStatus(status);
        processGroup.setErrorMessage(errorMessage);
        processGroupRepository.save(processGroup);
    }
}
