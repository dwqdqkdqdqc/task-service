package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.model.DocumentTypeProcessMapping;

public interface DocumentTypeProcessMappingService {
    DocumentTypeProcessMapping getByDocumentType(String documentType);
}
