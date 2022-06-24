package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class ProcessGroupDto {
    private UUID id;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String documentId;
    @NotBlank
    private String documentType;
}
