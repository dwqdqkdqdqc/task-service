package ru.sitronics.tn.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sitronics.tn.taskservice.model.DocumentTypeProcessMapping;

import java.util.Optional;

@Repository
public interface DocumentTypeProcessMappingRepository extends JpaRepository<DocumentTypeProcessMapping, Long> {
    Optional<DocumentTypeProcessMapping> findByDocumentType(String documentType);
}
