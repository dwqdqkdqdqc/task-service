package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public class ExecutionDto {
    private String id;
    private String processInstanceId;
    private boolean ended;
    private String tenantId;
}
