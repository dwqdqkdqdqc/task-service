package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="process")
public class Process extends BaseEntity {

    @NotNull
    private String processInstanceId;
    private String definitionId;
    @NotNull
    private String documentId;
    @NotNull
    private String documentType;
}
