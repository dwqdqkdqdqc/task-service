package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.sitronics.tn.taskservice.model.Task;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    Integer countByAssigneeAndReadByAssignee(String assignee, boolean readByAssignee);
    Optional<Task> findByProcessEngineTaskId(String processEngineTaskId);
}
