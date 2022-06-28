package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sitronics.tn.taskservice.model.base.BaseEntityLongId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Data
@Table(schema="dictionaries", name="map_doc_type_process")
public class
DocumentTypeProcessMapping extends BaseEntityLongId {
    private String processKey;
    @Column(name = "doc_type")
    private String documentType;
    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentTypeProcessMapping that)) return false;
        if (!super.equals(o)) return false;

        if (active != that.active) return false;
        if (!Objects.equals(processKey, that.processKey)) return false;
        return Objects.equals(documentType, that.documentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (processKey != null ? processKey.hashCode() : 0);
        result = 31 * result + (documentType != null ? documentType.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
