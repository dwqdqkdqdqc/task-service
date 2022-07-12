package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CompleteTaskDto {
    private Map<String, VariableValueDto> variables;
}
