package ru.sitronics.tn.taskservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskPageDto extends PageDto {
    private List<TaskDto> entity;
}
