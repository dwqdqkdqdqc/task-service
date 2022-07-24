package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MessageCorrelationResultDto {
    private MessageCorrelationResultType resultType;
    private ExecutionDto execution;
    private ProcessInstanceDto processInstance;
    private Map<String, VariableValueDto> variables;
}
