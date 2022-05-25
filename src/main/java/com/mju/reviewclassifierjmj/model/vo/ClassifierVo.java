package com.mju.reviewclassifierjmj.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassifierVo {
    private List<BlogTextVo> blogTextVos = new ArrayList<>();
}
