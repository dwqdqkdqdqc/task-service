package ru.sitronics.tn.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sitronics.tn.taskservice.dto.CorrelationMessageDto;
import ru.sitronics.tn.taskservice.dto.MessageCorrelationResultDto;
import ru.sitronics.tn.taskservice.service.MessageService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/message", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private static final String CORRELATING_MESSAGE_LOG = "Correlating message: {}";
    private final MessageService messageService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageCorrelationResultDto[]> correlateMessage(@RequestBody CorrelationMessageDto correlateMessageDto) {
        logger.info(CORRELATING_MESSAGE_LOG, correlateMessageDto);
        return ResponseEntity.ok(messageService.correlateMessage(correlateMessageDto));
    }
}
