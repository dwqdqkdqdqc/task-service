package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sitronics.tn.taskservice.exception.ProcessDocumentMappingException;
import ru.sitronics.tn.taskservice.model.DocumentTypeProcessMapping;
import ru.sitronics.tn.taskservice.repository.DocumentTypeProcessMappingRepository;

@Service
@RequiredArgsConstructor
public class DocumentTypeProcessMappingServiceImpl implements DocumentTypeProcessMappingService {

    private final DocumentTypeProcessMappingRepository documentTypeProcessMappingRepository;

    public DocumentTypeProcessMapping getByDocumentType(String documentType) {
        return documentTypeProcessMappingRepository
                .findByDocumentType(documentType)
                .orElseThrow(() -> new ProcessDocumentMappingException(String.format("Couldn't find a process by document type: %s", documentType)));
    }
}
