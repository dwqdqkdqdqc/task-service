package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CorrelationMessageDto {
    private String messageName;
    private String businessKey;
    private Map<String, VariableValueDto> correlationKeys;
    private Map<String, VariableValueDto> localCorrelationKeys;
    private Map<String, VariableValueDto> processVariables;
    private Map<String, VariableValueDto> processVariablesLocal;
    private String tenantId;
    private boolean withoutTenantId;
    private String processInstanceId;
    private boolean all;
    private boolean resultEnabled;
    private boolean variablesInResultEnabled;
}
