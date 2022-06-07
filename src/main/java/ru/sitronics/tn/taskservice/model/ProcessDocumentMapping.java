package ru.sitronics.tn.taskservice.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="process_document_mapping")
public class ProcessDocumentMapping extends BaseEntity {

    @Column(name = "process_name")
    private String processName;
    @Column(name = "document_type")
    private String documentType;
}
