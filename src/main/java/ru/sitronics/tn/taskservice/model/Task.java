package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static ru.sitronics.tn.taskservice.model.TaskStatus.PENDING;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="task")
public class Task extends BaseEntity {

    @NotNull
    private String processEngineTaskId;
    private String name;
    private String assignee;
    private LocalDateTime createdInProcessEngine;
    private LocalDateTime due;
    private LocalDateTime followUp;
    private String description;
    private int priority;
    private String processDefinitionId;
    private String processInstanceId;
    private String taskDefinitionKey;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> candidateGroups;
    private String documentId;
    @Enumerated(EnumType.STRING)
    private TaskType type;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = PENDING;
    private boolean readByAssignee = false;
}
