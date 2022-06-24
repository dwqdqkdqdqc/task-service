package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.sitronics.tn.taskservice.model.TaskStatusEnum.PENDING;

@Entity
@NoArgsConstructor
@Data
@Table(name="task")
public class Task extends BaseEntityLongId {

    @NotNull
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
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> candidateGroups;
    private String documentId;
    private String type;
    private String status = PENDING.toString();
    private boolean readByAssignee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        if (!super.equals(o)) return false;

        if (readByAssignee != task.readByAssignee) return false;
        if (!processEngineTaskId.equals(task.processEngineTaskId)) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(assignee, task.assignee)) return false;
        if (!Objects.equals(createdInProcessEngine, task.createdInProcessEngine))
            return false;
        if (!Objects.equals(due, task.due)) return false;
        if (!Objects.equals(followUp, task.followUp)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (!Objects.equals(processDefinitionId, task.processDefinitionId))
            return false;
        if (!Objects.equals(processInstanceId, task.processInstanceId))
            return false;
        if (!Objects.equals(taskDefinitionKey, task.taskDefinitionKey))
            return false;
        if (!Objects.equals(candidateGroups, task.candidateGroups))
            return false;
        if (!Objects.equals(documentId, task.documentId)) return false;
        if (!Objects.equals(type, task.type)) return false;
        return Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + processEngineTaskId.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (createdInProcessEngine != null ? createdInProcessEngine.hashCode() : 0);
        result = 31 * result + (due != null ? due.hashCode() : 0);
        result = 31 * result + (followUp != null ? followUp.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (processDefinitionId != null ? processDefinitionId.hashCode() : 0);
        result = 31 * result + (processInstanceId != null ? processInstanceId.hashCode() : 0);
        result = 31 * result + (taskDefinitionKey != null ? taskDefinitionKey.hashCode() : 0);
        result = 31 * result + (candidateGroups != null ? candidateGroups.hashCode() : 0);
        result = 31 * result + (documentId != null ? documentId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (readByAssignee ? 1 : 0);
        return result;
    }
}
