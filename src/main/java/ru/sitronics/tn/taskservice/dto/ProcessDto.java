package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class ProcessDto {
    private UUID id;
    @NotBlank
    private String processInstanceId;
    private String definitionId;
}
