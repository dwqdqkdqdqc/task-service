package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sitronics.tn.taskservice.model.Definition;

import java.util.List;

@Repository
public interface DefinitionRepository extends JpaRepository<Definition, Long> {
    List<Definition> findAllByType(String type);
}
