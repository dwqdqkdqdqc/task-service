package ru.sitronics.tn.taskservice.service;

import ru.sitronics.tn.taskservice.dto.CorrelationMessageDto;
import ru.sitronics.tn.taskservice.dto.MessageCorrelationResultDto;

public interface MessageService {
    MessageCorrelationResultDto correlateMessage(CorrelationMessageDto correlationMessageDto);
}
