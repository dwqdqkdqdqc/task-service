package ru.sitronics.tn.taskservice.model.base;

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
        Dictionary that = (Dictionary) o;
        return super.equals(o) && Objects.equals(code, that.getCode())
                && Objects.equals(shortValue, that.getShortValue())
                && Objects.equals(fullValue, that.getFullValue())
                && Objects.equals(active, that.isActive())
                && Objects.equals(ord, that.getOrd());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
