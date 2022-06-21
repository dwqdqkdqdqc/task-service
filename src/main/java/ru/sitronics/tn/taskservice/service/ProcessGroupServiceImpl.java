package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.sitronics.tn.taskservice.dto.ProcessGroupDto;
import ru.sitronics.tn.taskservice.dto.ProcessInstanceDto;
import ru.sitronics.tn.taskservice.dto.StartProcessInstanceDto;
import ru.sitronics.tn.taskservice.dto.VariableValueDto;
import ru.sitronics.tn.taskservice.exception.ProcessDocumentMappingException;
import ru.sitronics.tn.taskservice.exception.ProcessGroupException;
import ru.sitronics.tn.taskservice.model.Process;
import ru.sitronics.tn.taskservice.model.ProcessGroup;
import ru.sitronics.tn.taskservice.repository.DocumentTypeProcessMappingRepository;
import ru.sitronics.tn.taskservice.repository.ProcessGroupRepository;
import ru.sitronics.tn.taskservice.util.CustomRestClient;
import ru.sitronics.tn.taskservice.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProcessGroupServiceImpl implements ProcessGroupService {

    private final ProcessGroupRepository processGroupRepository;
    private final DocumentTypeProcessMappingRepository documentTypeProcessMappingRepository;
    private final CustomRestClient customRestClient;

    @Override
    @Transactional
    public ProcessGroupDto createProcessGroup(ProcessGroupDto processGroupDto) {

        String processKey = getProcessKeyByDocumentType(processGroupDto.getDocumentType());
        String endPointUri = String.format("/process-definition/key/%s/start", processKey);
        StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();

        VariableValueDto documentIdVariable = new VariableValueDto();
        documentIdVariable.setType("String");
        documentIdVariable.setValue(processGroupDto.getDocumentId());

        VariableValueDto startedByIdVariable = new VariableValueDto();
        startedByIdVariable.setType("String");
        startedByIdVariable.setValue(processGroupDto.getCreatedBy());

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);

        startProcessInstanceDto.setVariables(variableValueDtoMap);

        ResponseEntity<ProcessInstanceDto> processEngineResponse =
                customRestClient.postJson(endPointUri, startProcessInstanceDto, ProcessInstanceDto.class);

        if(processEngineResponse.getStatusCode().is2xxSuccessful()) {

            Process process = new Process();

            try {
                ProcessInstanceDto processInstanceDto = Objects.requireNonNull(processEngineResponse.getBody());
                process.setProcessInstanceId(processInstanceDto.getId());
                process.setDefinitionId(processEngineResponse.getBody().getDefinitionId());
            } catch (NullPointerException e) {
                throw new ProcessGroupException("Process instance is null", e);
            }

            process.setDocumentId(processGroupDto.getDocumentId());
            process.setDocumentType(processGroupDto.getDocumentType());

            ProcessGroup processGroup = ObjectUtils.convertObject(processGroupDto, new ProcessGroup());
            processGroup.getProcesses().add(process);
            return ObjectUtils.convertObject(processGroupRepository.save(processGroup), new ProcessGroupDto());
        } else {
            throw new ProcessGroupException("Process start was unsuccessful");
        }
    }

    private String getProcessKeyByDocumentType(String documentType) {
        return documentTypeProcessMappingRepository
                .findByDocumentType(documentType)
                .orElseThrow(() -> new ProcessDocumentMappingException(String.format("Couldn't find a process by document type: %s", documentType)))
                .getProcessKey();
    }
}
