package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProcessDto {
    private Long id;
    @NotBlank
    private String processEngineId;
    private String definitionId;
}
