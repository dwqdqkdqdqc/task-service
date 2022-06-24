package ru.sitronics.tn.taskservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Data
@Table(schema="dictionaries", name="map_doc_type_process")
public class DocumentTypeProcessMapping extends BaseEntityLongId {
    private String processKey;
    private String docType;
    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentTypeProcessMapping that)) return false;
        if (!super.equals(o)) return false;

        if (!processKey.equals(that.processKey)) return false;
        return docType.equals(that.docType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + processKey.hashCode();
        result = 31 * result + docType.hashCode();
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
