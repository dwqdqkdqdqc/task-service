package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public class BpmErrorMessageDto {
    private String status;
    private String errorMessage;
}
