package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sitronics.tn.taskservice.model.ProcessGroup;

@Repository
public interface ProcessGroupRepository extends JpaRepository<ProcessGroup, Long> {
}
