package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskCountDto {

    private Integer count;
}
