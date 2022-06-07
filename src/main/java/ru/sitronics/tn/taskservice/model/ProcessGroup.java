package ru.sitronics.tn.taskservice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="process_group")
public class ProcessGroup extends BaseEntity {

    @NotNull
    private String createdBy;
    @NotNull
    private String documentId;
    @NotNull
    private String documentType;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "process_group_id")
    private List<Process> processes = new ArrayList<>();
}
