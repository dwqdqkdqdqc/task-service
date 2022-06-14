package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public abstract class PageDto {
    private String filter;
    private long totalAmount;
    private int elementsOnPage;
    private int pages;
    private String sort;
    private int page;
}
