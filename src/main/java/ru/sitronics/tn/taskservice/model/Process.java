package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sitronics.tn.taskservice.model.base.BaseEntityUUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
@Table(name="process")
public class Process extends BaseEntityUUID {

    @NotNull
    private String processInstanceId;
    private String definitionId;
    @NotNull
    private String documentId;
    @NotNull
    private String documentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Process process)) return false;
        if (!super.equals(o)) return false;

        if (!getProcessInstanceId().equals(process.getProcessInstanceId())) return false;
        if (getDefinitionId() != null ? !getDefinitionId().equals(process.getDefinitionId()) : process.getDefinitionId() != null)
            return false;
        if (!getDocumentId().equals(process.getDocumentId())) return false;
        return getDocumentType().equals(process.getDocumentType());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getProcessInstanceId().hashCode();
        result = 31 * result + (getDefinitionId() != null ? getDefinitionId().hashCode() : 0);
        result = 31 * result + getDocumentId().hashCode();
        result = 31 * result + getDocumentType().hashCode();
        return result;
    }
}

//TODO Add status and update it from BPMS engine
