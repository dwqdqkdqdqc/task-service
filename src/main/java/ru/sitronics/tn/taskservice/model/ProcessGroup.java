package ru.sitronics.tn.taskservice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
@Table(name="process_group")
public class ProcessGroup extends BaseEntityLongId {

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

        if (!createdBy.equals(that.createdBy)) return false;
        if (!documentId.equals(that.documentId)) return false;
        if (!documentType.equals(that.documentType)) return false;
        return Objects.equals(processes, that.processes);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + createdBy.hashCode();
        result = 31 * result + documentId.hashCode();
        result = 31 * result + documentType.hashCode();
        result = 31 * result + (processes != null ? processes.hashCode() : 0);
        return result;
    }
}
