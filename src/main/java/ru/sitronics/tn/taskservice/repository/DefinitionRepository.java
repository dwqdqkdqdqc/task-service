package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sitronics.tn.taskservice.model.Definition;

import java.util.List;

public interface DefinitionRepository extends JpaRepository<Definition, Long> {
    List<Definition> findAllByType(String type);
}
