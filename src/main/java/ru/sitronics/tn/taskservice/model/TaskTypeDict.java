package ru.sitronics.tn.taskservice.model;

import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;

@ToString(callSuper = true)
@Entity
@Table(schema = "dictionaries", name = "nci_task_type")
public class TaskTypeDict extends Dictionary {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TaskTypeDict that = (TaskTypeDict) o;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
