package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sitronics.tn.taskservice.model.base.BaseEntityUUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private String documentType;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "process_group_id")
    private List<Process> processes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessGroup that)) return false;
        if (!super.equals(o)) return false;

        if (!getCreatedBy().equals(that.getCreatedBy())) return false;
        if (!getDocumentId().equals(that.getDocumentId())) return false;
        if (!getDocumentType().equals(that.getDocumentType())) return false;
        return getProcesses() != null ? getProcesses().equals(that.getProcesses()) : that.getProcesses() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getCreatedBy().hashCode();
        result = 31 * result + getDocumentId().hashCode();
        result = 31 * result + getDocumentType().hashCode();
        result = 31 * result + (getProcesses() != null ? getProcesses().hashCode() : 0);
        return result;
    }
}
