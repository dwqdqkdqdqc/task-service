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
import ru.sitronics.tn.taskservice.util.CustomRestClient;
import ru.sitronics.tn.taskservice.util.ObjectUtils;
import ru.sitronics.tn.taskservice.validation.BpmValidation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProcessGroupServiceImpl implements ProcessGroupService {

    private final ProcessService processService;
    private final DocumentTypeProcessMappingService documentTypeProcessMappingService;
    private final CustomRestClient customRestClient;
    private final ProcessGroupRepository processGroupRepository;
    private final BpmValidation bpmValidation;

    @Override
    public ProcessGroupDto createProcessGroup(ProcessGroupDto processGroupDto) {

        //TODO Refactor variable setting with BpmValidation

        ProcessGroup processGroup = processGroupRepository
                .save(ObjectUtils.convertObject(processGroupDto, new ProcessGroup()));

        DocumentTypeProcessMapping documentTypeProcessMapping = documentTypeProcessMappingService
                .getByDocumentType(processGroupDto.getDocumentType());

        if (documentTypeProcessMapping.getValidationProcessKey() != null) {
            String validationProcessKey = documentTypeProcessMapping.getValidationProcessKey();
            bpmValidation.validateProcessStart(processGroup, processGroupDto, validationProcessKey);
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

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        if (processGroupDto.getVariables() != null) {
            variableValueDtoMap.putAll(processGroupDto.getVariables());
        }

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);

        startProcessInstanceDto.setBusinessKey(processGroup.getId().toString());
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
}
