package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
@Table(name="process")
public class Process extends BaseEntityLongId {

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

        if (!processInstanceId.equals(process.processInstanceId)) return false;
        if (!Objects.equals(definitionId, process.definitionId))
            return false;
        if (!documentId.equals(process.documentId)) return false;
        return documentType.equals(process.documentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + processInstanceId.hashCode();
        result = 31 * result + (definitionId != null ? definitionId.hashCode() : 0);
        result = 31 * result + documentId.hashCode();
        result = 31 * result + documentType.hashCode();
        return result;
    }
}

//TODO Add status and update it from BPMS engine
