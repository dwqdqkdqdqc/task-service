package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.ProcessGroupDto;
import ru.sitronics.tn.taskservice.model.ProcessGroup;

public interface ProcessGroupService {
    ProcessGroupDto createProcessGroup(ProcessGroupDto processGroupDto);
    ProcessGroup getProcessGroupById(String id);
    void updateProcessGroupStatus(String processGroupId, String status, String errorMessage);
}
