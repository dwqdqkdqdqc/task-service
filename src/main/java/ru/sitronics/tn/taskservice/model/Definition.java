package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="definition", uniqueConstraints = { @UniqueConstraint(columnNames = { "type", "code" }, name = "uc_type_code") })
public class Definition extends BaseEntity {
    private String type;
    private String code;
    private String displayValue;
    private String shortDisplayValue;
    private int displayOrder;
    private boolean isActive;
}
