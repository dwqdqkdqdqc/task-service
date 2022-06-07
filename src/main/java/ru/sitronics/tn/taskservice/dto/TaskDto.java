package ru.sitronics.tn.taskservice.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.sitronics.tn.taskservice.model.TaskStatus;
import ru.sitronics.tn.taskservice.model.TaskType;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
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
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String taskDefinitionKey;
    private List<String> candidateGroups;
    private String documentId;
    private TaskType type;
    private TaskStatus status;
    private boolean readByAssignee = false;
}
