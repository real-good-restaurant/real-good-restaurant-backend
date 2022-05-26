package com.mju.reviewclassifierjmj.model.vo;

import lombok.Data;

@Data
public class BlogTextResponseVo {
    private int id;

    private String text;

    private String ad;

    private float probability;
}
