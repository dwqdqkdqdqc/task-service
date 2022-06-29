package ru.sitronics.tn.taskservice.model;

import lombok.ToString;
import org.hibernate.Hibernate;
import ru.sitronics.tn.taskservice.model.base.Dictionary;

import javax.persistence.Entity;
import javax.persistence.Table;

@ToString(callSuper = true)
@Entity
@Table(schema = "dictionaries", name = "nci_task_status")
public class TaskStatusDict extends Dictionary {
}
