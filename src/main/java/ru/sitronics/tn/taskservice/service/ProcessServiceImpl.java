package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sitronics.tn.taskservice.dto.ProcessDto;
import ru.sitronics.tn.taskservice.model.Process;
import ru.sitronics.tn.taskservice.repository.ProcessRepository;
import ru.sitronics.tn.taskservice.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;

    @Override
    public ProcessDto createProcess(Process process) {
        return ObjectUtils.convertObject(processRepository.save(process), new ProcessDto());
    }
}
