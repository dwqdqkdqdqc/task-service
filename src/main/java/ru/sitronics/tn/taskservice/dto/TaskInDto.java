package ru.sitronics.tn.taskservice.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TaskInDto {
    private UUID id;
    @NotBlank
    private String processEngineTaskId;
    private String name;
    private String assignee;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdInProcessEngine;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime due;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime followUp;
    private String description;
    private String processDefinitionId;
    private String executionId;
    private String processInstanceId;
    private String taskDefinitionKey;
    private List<String> candidateGroups;
    private String documentId;
    private String type;
    private String status;
    private boolean readByAssignee;
    private String validationProcessKey;
    @NotBlank
    private String processGroupId;
}
