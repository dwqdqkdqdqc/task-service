package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public class ProcessInstanceDto {
    private String id;
    private String definitionId;
    private String contractId;
}
