package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sitronics.tn.taskservice.model.base.BaseEntityUUID;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Data
@Table(name = "process")
public class Process extends BaseEntityUUID {

    private String processInstanceId;
    private String definitionId;
    private String documentId;
    @ManyToOne
    @JoinColumn(name="process_group_id")
    private ProcessGroup processGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process process)) return false;
        if (!super.equals(o)) return false;

        if (getProcessInstanceId() != null ? !getProcessInstanceId().equals(process.getProcessInstanceId()) : process.getProcessInstanceId() != null)
            return false;
        if (getDefinitionId() != null ? !getDefinitionId().equals(process.getDefinitionId()) : process.getDefinitionId() != null)
            return false;
        if (getDocumentId() != null ? !getDocumentId().equals(process.getDocumentId()) : process.getDocumentId() != null)
            return false;
        return getProcessGroup() != null ? getProcessGroup().equals(process.getProcessGroup()) : process.getProcessGroup() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getProcessInstanceId() != null ? getProcessInstanceId().hashCode() : 0);
        result = 31 * result + (getDefinitionId() != null ? getDefinitionId().hashCode() : 0);
        result = 31 * result + (getDocumentId() != null ? getDocumentId().hashCode() : 0);
        result = 31 * result + (getProcessGroup() != null ? getProcessGroup().hashCode() : 0);
        return result;
    }
}

//TODO Add status and update it from BPMS engine
