package com.mju.reviewclassifierjmj.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NaverSearchResult {
    private String lastBuildDate;

    private Long total;

    private Long start;

    private Long display;

    private List<Item> items;

    private String errorMessage;

    private String errorCode;
}
