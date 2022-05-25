package com.mju.reviewclassifierjmj.model;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private String lastBuildDate;

    private Long total;

    private Long start;

    private Long display;

    private List<Item> items;

    private String errorMessage;

    private String errorCode;
}
