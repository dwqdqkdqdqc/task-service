package ru.sitronics.tn.taskservice.validation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.sitronics.tn.taskservice.dto.*;
import ru.sitronics.tn.taskservice.exception.BpmValidationException;
import ru.sitronics.tn.taskservice.exception.TaskException;
import ru.sitronics.tn.taskservice.model.Process;
import ru.sitronics.tn.taskservice.model.ProcessGroup;
import ru.sitronics.tn.taskservice.model.ProcessGroupStatusEnum;
import ru.sitronics.tn.taskservice.model.Task;
import ru.sitronics.tn.taskservice.repository.ProcessGroupRepository;
import ru.sitronics.tn.taskservice.service.ProcessService;
import ru.sitronics.tn.taskservice.util.CustomRestClient;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BpmValidation {

    private static final Logger logger = LoggerFactory.getLogger(BpmValidation.class);
    private static final String PROCESS_START_VALIDATION = "Process start validation: {}";
    private static final String PROCESS_INSTANCE_IS_NULL = "Validation process instance is null: {}";
    private static final String PROCESS_START_VALIDATION_UNSUCCESSFUL = "Process start validation was unsuccessful: {}";
    private static final String PROCESS_START_VALIDATION_FAILED = "System failed to run process start validation: {}";
    private static final String TASK_COMPLETION_VALIDATION = "Task completion validation: {}";
    private static final String TASK_COMPLETION_VALIDATION_UNSUCCESSFUL = "Task completion validation was unsuccessful: {}";
    private static final String TASK_COMPLETION_VALIDATION_FAILED = "System failed to run task completion validation: {}";

    private final CustomRestClient customRestClient;
    private final ProcessService processService;
    private final ProcessGroupRepository processGroupRepository;

    @Transactional
    public void validateProcessStart(ProcessGroup processGroup, ProcessGroupDto processGroupDto, String validationProcessKey) {

        //TODO Refactor variable setting with ProcessGroupServiceImpl

        logger.info(PROCESS_START_VALIDATION, processGroup.getId());

        String endPointUri = String.format("/process-definition/key/%s/start", validationProcessKey);
        StartProcessInstanceDto startProcessInstanceDto = new StartProcessInstanceDto();

        VariableValueDto documentIdVariable = new VariableValueDto();
        documentIdVariable.setType("String");
        documentIdVariable.setValue(processGroup.getDocumentId());

        VariableValueDto startedByIdVariable = new VariableValueDto();
        startedByIdVariable.setType("String");
        startedByIdVariable.setValue(processGroup.getCreatedBy());

        Map<String, VariableValueDto> variableValueDtoMap = new HashMap<>();

        variableValueDtoMap.put("documentId", documentIdVariable);
        variableValueDtoMap.put("startedBy", startedByIdVariable);

        if (processGroupDto.getVariables() != null) {
            variableValueDtoMap.putAll(processGroupDto.getVariables());
        }

        startProcessInstanceDto.setVariables(variableValueDtoMap);
        startProcessInstanceDto.setWithVariablesInReturn(true);
        startProcessInstanceDto.setBusinessKey(processGroup.getId().toString());

        ResponseEntity<ProcessInstanceDto> processEngineResponse =
                customRestClient.postJson(endPointUri, startProcessInstanceDto, ProcessInstanceDto.class);

        if (processEngineResponse.getStatusCode().is2xxSuccessful()) {
            Process process = new Process();
            try {
                ProcessInstanceDto processInstanceDto = Objects.requireNonNull(processEngineResponse.getBody());
                process.setProcessInstanceId(processInstanceDto.getId());
                process.setDefinitionId(processEngineResponse.getBody().getDefinitionId());
            } catch (NullPointerException e) {
                logger.error(PROCESS_INSTANCE_IS_NULL, processGroup.getId());
                throw new TaskException("Validation process instance is null", e);
            }

            //TODO NullPointerException check required

            String validationResult = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationResult").getValue().toString());
            String validationMessage = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationMessage").getValue().toString());

            //TODO Add process group status updating

//            if (!Boolean.parseBoolean(validationResult)) {
//                processGroup.setStatus(ProcessGroupStatusEnum.ERROR.toString());
//                processGroup.setErrorMessage(validationMessage);
//            }

            process.setDocumentId(processGroup.getDocumentId());
            process.setProcessGroup(processGroup);

            processService.createProcess(process);

            if (!Boolean.parseBoolean(validationResult)) {
                logger.info(PROCESS_START_VALIDATION_UNSUCCESSFUL, processGroup.getId());
                throw new BpmValidationException(validationMessage);
            }
        } else {
            logger.error(PROCESS_START_VALIDATION_FAILED, processGroup.getId());
            throw new TaskException("System failed to run process start validation");
        }
    }

    @Transactional
    public void validateTaskCompletion(Task task, CompleteTaskDto completeTaskDto, String validationProcessKey) {

        //TODO Refactor variable setting with ProcessGroupServiceImpl

        logger.info(TASK_COMPLETION_VALIDATION, task.getId());

        String endPointUri = String.format("/process-definition/key/%s/start", validationProcessKey);
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
        startProcessInstanceDto.setBusinessKey(task.getProcessGroupId());

        ResponseEntity<ProcessInstanceDto> processEngineResponse =
                customRestClient.postJson(endPointUri, startProcessInstanceDto, ProcessInstanceDto.class);

        if (processEngineResponse.getStatusCode().is2xxSuccessful()) {
            Process process = new Process();
            try {
                ProcessInstanceDto processInstanceDto = Objects.requireNonNull(processEngineResponse.getBody());
                process.setProcessInstanceId(processInstanceDto.getId());
                process.setDefinitionId(processEngineResponse.getBody().getDefinitionId());
            } catch (NullPointerException e) {
                logger.error(PROCESS_INSTANCE_IS_NULL, task.getId());
                throw new TaskException("Validation process instance is null", e);
            }

            //TODO NullPointerException check required

            String validationResult = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationResult").getValue().toString());
            String validationMessage = Objects.requireNonNull(processEngineResponse
                    .getBody().getVariables().get("validationMessage").getValue().toString());

            process.setDocumentId(task.getDocumentId());
            process.setProcessGroup(processGroupRepository
                    .findById(UUID.fromString(startProcessInstanceDto
                            .getBusinessKey()))
                    .orElse(new ProcessGroup()));

            processService.createProcess(process);

            if (!Boolean.parseBoolean(validationResult)) {
                logger.info(TASK_COMPLETION_VALIDATION_UNSUCCESSFUL, task.getId());
                throw new BpmValidationException(validationMessage);
            }
        } else {
            logger.error(TASK_COMPLETION_VALIDATION_FAILED, task.getId());
            throw new TaskException("System failed to run task completion validation");
        }
    }
}
