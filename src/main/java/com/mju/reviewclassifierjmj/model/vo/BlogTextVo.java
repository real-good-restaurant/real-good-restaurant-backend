package com.mju.reviewclassifierjmj.model.vo;

import lombok.Data;

@Data
public class BlogTextVo {
    private Long id;

    private String text;

    private String ad;

    private float probability;
}
