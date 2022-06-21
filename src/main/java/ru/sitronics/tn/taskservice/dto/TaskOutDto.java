package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskOutDto {
    private Long id;
    private String processEngineTaskId;
    private String name;
    private String assignee;
    private LocalDateTime createdInProcessEngine;
    private LocalDateTime due;
    private LocalDateTime followUp;
    private String description;
    private String processDefinitionId;
    private String processInstanceId;
    private String taskDefinitionKey;
    private List<String> candidateGroups;
    private String documentId;
    private String type;
    private String status;
    private Boolean readByAssignee;
}
