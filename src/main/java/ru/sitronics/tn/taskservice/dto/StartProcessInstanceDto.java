package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StartProcessInstanceDto {
    private Map<String, VariableValueDto> variables;
}
