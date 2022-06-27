package ru.sitronics.tn.taskservice.dto;

import lombok.Data;

@Data
public abstract class PageDto {
    protected String filter;
    protected long totalAmount;
    protected int elementsOnPage;
    protected int pages;
    protected String sort;
    protected int page;
}
