package ru.sitronics.tn.taskservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskStatusEnum {
    PENDING("Ожидает исполнения"),
    IN_PROGRESS("В работе"),
    COMPLETED("Выполнена");

    private final String displayValue;
}