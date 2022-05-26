package com.mju.reviewclassifierjmj.model;

import com.mju.reviewclassifierjmj.model.vo.BlogTextResponseVo;
import lombok.Data;

@Data
public class Item {
    private String title;

    private String link;

    private String description;

    private String bloggername;

    private String bloggerlink;

    private String postdate;

    private String text;

    private String ad;

    private float probability;

    public void assignClassificationResult(BlogTextResponseVo vo) {
        this.text = vo.getText();
        this.ad = vo.getAd();
        this.probability = vo.getProbability();
    }
}
