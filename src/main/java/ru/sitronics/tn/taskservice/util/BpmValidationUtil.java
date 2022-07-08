package ru.sitronics.tn.taskservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.sitronics.tn.taskservice.dto.*;
import ru.sitronics.tn.taskservice.exception.BpmValidationException;
import ru.sitronics.tn.taskservice.exception.TaskException;
import ru.sitronics.tn.taskservice.model.Process;
import ru.sitronics.tn.taskservice.model.ProcessGroup;
import ru.sitronics.tn.taskservice.model.Task;
import ru.sitronics.tn.taskservice.repository.ProcessGroupRepository;
import ru.sitronics.tn.taskservice.service.ProcessService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BpmValidationUtil {

    private final CustomRestClient customRestClient;
    private final ProcessService processService;
    private final ProcessGroupRepository processGroupRepository;

    public void validateProcessStart(ProcessGroup processGroup, ProcessGroupDto processGroupDto, String validationProcessKey) {
        String endPointUri = String.format("/process-definition/key/%s/start", validationProcessKey);
        StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();

        VariableValueDto documentIdVariable = new VariableValueDto();
        documentIdVariable.setType("String");
        documentIdVariable.setValue(processGroup.getDocumentId());

        VariableValueDto startedByIdVariable = new VariableValueDto();
        startedByIdVariable.setType("String");
        startedByIdVariable.setValue(processGroup.getCreatedBy());

        VariableValueDto processGroupId = new VariableValueDto();
        processGroupId.setType("String");
        processGroupId.setValue(processGroup.getId().toString());

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);
        variableValueDtoMap.put("processGroupId", processGroupId);

        if (processGroupDto.getVariables() != null) {
            variableValueDtoMap.putAll(processGroupDto.getVariables());
        }

        startProcessInstanceDto.setVariables(variableValueDtoMap);
        startProcessInstanceDto.setWithVariablesInReturn(true);

        startValidationProcess(endPointUri, startProcessInstanceDto, processGroup.getDocumentId(), processGroup, null);
    }

    public void validateTaskCompletion(Task task, CompleteTaskDto completeTaskDto) {
        String endPointUri = String.format("/process-definition/key/%s/start", task.getValidationProcessKey());
        StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();

        VariableValueDto documentIdVariable = new VariableValueDto();
        documentIdVariable.setType("String");
        documentIdVariable.setValue(task.getDocumentId());

        VariableValueDto startedByIdVariable = new VariableValueDto();
        startedByIdVariable.setType("String");
        startedByIdVariable.setValue(task.getAssignee()); //TODO Assignee?

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);

        if (completeTaskDto.getVariables() != null) {
            variableValueDtoMap.putAll(completeTaskDto.getVariables());
        }

        startProcessInstanceDto.setVariables(variableValueDtoMap);
        startProcessInstanceDto.setWithVariablesInReturn(true);

        startValidationProcess(endPointUri, startProcessInstanceDto, task.getDocumentId(), null, task.getProcessGroupId());
    }

    private void startValidationProcess(String endPointUri,
                                        StartProcessInstanceDto startProcessInstanceDto,
                                        String documentId, ProcessGroup processGroup, String processGroupId) {
        ResponseEntity<ProcessInstanceDto> processEngineResponse =
                customRestClient.postJson(endPointUri, startProcessInstanceDto, ProcessInstanceDto.class);

        if (processEngineResponse.getStatusCode().is2xxSuccessful()) {
            Process process = new Process();
            try {
                ProcessInstanceDto processInstanceDto = Objects.requireNonNull(processEngineResponse.getBody());
                process.setProcessInstanceId(processInstanceDto.getId());
                process.setDefinitionId(processEngineResponse.getBody().getDefinitionId());
            } catch (NullPointerException e) {
                throw new TaskException("Validation process instance is null", e);
            }

            process.setDocumentId(documentId);

            process.setProcessGroup(Objects.requireNonNullElseGet(processGroup, () -> processGroupRepository
                    .findById(UUID.fromString(processGroupId)).orElse(new ProcessGroup())));

            processService.createProcess(process);

            //TODO NullPointerException check required

            String validationResult = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationResult").getValue().toString());
            String validationMessage = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationMessage").getValue().toString());

            if (Integer.parseInt(validationResult) < 0) {
                throw new BpmValidationException(validationMessage);
            }
        } else {
            throw new TaskException("Validation process start was unsuccessful");
        }
    }
}
