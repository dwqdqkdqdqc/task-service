package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sitronics.tn.taskservice.model.Process;

import java.util.UUID;

@Repository
public interface ProcessRepository extends JpaRepository<Process, UUID> {
}
