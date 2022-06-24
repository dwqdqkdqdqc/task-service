package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sitronics.tn.taskservice.model.base.BaseEntityUUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static ru.sitronics.tn.taskservice.model.TaskStatusEnum.PENDING;

@Entity
@NoArgsConstructor
@Data
@Table(name="task")
public class Task extends BaseEntityUUID {

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

        if (isReadByAssignee() != task.isReadByAssignee()) return false;
        if (!getProcessEngineTaskId().equals(task.getProcessEngineTaskId())) return false;
        if (getName() != null ? !getName().equals(task.getName()) : task.getName() != null) return false;
        if (getAssignee() != null ? !getAssignee().equals(task.getAssignee()) : task.getAssignee() != null)
            return false;
        if (getCreatedInProcessEngine() != null ? !getCreatedInProcessEngine().equals(task.getCreatedInProcessEngine()) : task.getCreatedInProcessEngine() != null)
            return false;
        if (getDue() != null ? !getDue().equals(task.getDue()) : task.getDue() != null) return false;
        if (getFollowUp() != null ? !getFollowUp().equals(task.getFollowUp()) : task.getFollowUp() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(task.getDescription()) : task.getDescription() != null)
            return false;
        if (getProcessDefinitionId() != null ? !getProcessDefinitionId().equals(task.getProcessDefinitionId()) : task.getProcessDefinitionId() != null)
            return false;
        if (getProcessInstanceId() != null ? !getProcessInstanceId().equals(task.getProcessInstanceId()) : task.getProcessInstanceId() != null)
            return false;
        if (getTaskDefinitionKey() != null ? !getTaskDefinitionKey().equals(task.getTaskDefinitionKey()) : task.getTaskDefinitionKey() != null)
            return false;
        if (getCandidateGroups() != null ? !getCandidateGroups().equals(task.getCandidateGroups()) : task.getCandidateGroups() != null)
            return false;
        if (getDocumentId() != null ? !getDocumentId().equals(task.getDocumentId()) : task.getDocumentId() != null)
            return false;
        if (getType() != null ? !getType().equals(task.getType()) : task.getType() != null) return false;
        return getStatus() != null ? getStatus().equals(task.getStatus()) : task.getStatus() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getProcessEngineTaskId().hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAssignee() != null ? getAssignee().hashCode() : 0);
        result = 31 * result + (getCreatedInProcessEngine() != null ? getCreatedInProcessEngine().hashCode() : 0);
        result = 31 * result + (getDue() != null ? getDue().hashCode() : 0);
        result = 31 * result + (getFollowUp() != null ? getFollowUp().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getProcessDefinitionId() != null ? getProcessDefinitionId().hashCode() : 0);
        result = 31 * result + (getProcessInstanceId() != null ? getProcessInstanceId().hashCode() : 0);
        result = 31 * result + (getTaskDefinitionKey() != null ? getTaskDefinitionKey().hashCode() : 0);
        result = 31 * result + (getCandidateGroups() != null ? getCandidateGroups().hashCode() : 0);
        result = 31 * result + (getDocumentId() != null ? getDocumentId().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (isReadByAssignee() ? 1 : 0);
        return result;
    }
}
