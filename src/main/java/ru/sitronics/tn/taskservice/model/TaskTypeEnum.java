package ru.sitronics.tn.taskservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TaskTypeEnum {
    APPROVAL("Согласование"),
    FAMILIARIZATION("Ознакомление");

    private final String displayValue;
}
