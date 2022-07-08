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
@Table(name="process_group")
public class ProcessGroup extends BaseEntityUUID {

    @NotNull
    private String createdBy;
    @NotNull
    private String documentId;
    @NotNull
    private String documentType;  //TODO Use doc_type or process_key?
    private String status = ProcessGroupStatusEnum.IN_PROGRESS.toString();
    private String errorMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessGroup that)) return false;
        if (!super.equals(o)) return false;

        if (!getCreatedBy().equals(that.getCreatedBy())) return false;
        if (!getDocumentId().equals(that.getDocumentId())) return false;
        if (!getDocumentType().equals(that.getDocumentType())) return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        return getErrorMessage() != null ? getErrorMessage().equals(that.getErrorMessage()) : that.getErrorMessage() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCreatedBy().hashCode();
        result = 31 * result + getDocumentId().hashCode();
        result = 31 * result + getDocumentType().hashCode();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getErrorMessage() != null ? getErrorMessage().hashCode() : 0);
        return result;
    }
}
