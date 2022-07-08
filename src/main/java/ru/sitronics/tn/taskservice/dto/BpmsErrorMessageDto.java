package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public class BpmsErrorMessageDto {
    private String status;
    private String errorMessage;
}
