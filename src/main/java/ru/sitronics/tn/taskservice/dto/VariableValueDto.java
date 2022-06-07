package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public class VariableValueDto {
    private String type;
    private Object value;
}
