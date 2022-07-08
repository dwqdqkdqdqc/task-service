package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.ProcessDto;
import ru.sitronics.tn.taskservice.model.Process;

public interface ProcessService {
    ProcessDto createProcess(Process process);
}
