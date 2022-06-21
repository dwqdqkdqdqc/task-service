package ru.sitronics.tn.taskservice.model;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@MappedSuperclass
public abstract class Dictionary extends BaseEntityLongId {

    protected String code;
    protected String shortValue;
    protected String fullValue;
    protected boolean active;
    protected int ord;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dictionary item = (Dictionary) o;
        return super.equals(o) && Objects.equals(code, item.code)
                && Objects.equals(shortValue, item.shortValue)
                && Objects.equals(fullValue, item.fullValue)
                && Objects.equals(active, item.active)
                && Objects.equals(ord, item.ord);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
