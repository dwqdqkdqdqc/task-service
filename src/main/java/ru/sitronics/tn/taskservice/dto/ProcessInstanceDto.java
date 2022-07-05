package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ProcessInstanceDto {
    private String id;
    private String definitionId;
    private String contractId;
    private Map<String, VariableValueDto> variables;
}
