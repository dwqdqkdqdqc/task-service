package ru.sitronics.tn.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sitronics.tn.taskservice.dto.CorrelationMessageDto;
import ru.sitronics.tn.taskservice.dto.MessageCorrelationResultDto;
import ru.sitronics.tn.taskservice.util.CustomRestClient;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final CustomRestClient customRestClient;

    @Override
    public MessageCorrelationResultDto[] correlateMessage(CorrelationMessageDto correlationMessageDto) {
        String endPointUri = "/message";
        return customRestClient
                .postJson(endPointUri, correlationMessageDto, MessageCorrelationResultDto[].class)
                .getBody();
    }
}
