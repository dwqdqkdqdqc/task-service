package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProcessGroupDto {
    private Long id;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String documentId;
    @NotBlank
    private String documentType;
}
