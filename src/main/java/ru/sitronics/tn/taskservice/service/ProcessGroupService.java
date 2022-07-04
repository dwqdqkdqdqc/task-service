package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.ProcessGroupDto;
import ru.sitronics.tn.taskservice.model.ProcessGroup;

public interface ProcessGroupService {
    ProcessGroupDto createProcessGroup(ProcessGroupDto processGroupDto);
    ProcessGroup getProcessGroupByDocumentId(String documentId);
    ProcessGroup save(ProcessGroup processGroup);
}
