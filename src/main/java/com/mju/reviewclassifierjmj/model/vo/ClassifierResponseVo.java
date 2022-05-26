package com.mju.reviewclassifierjmj.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ClassifierResponseVo {
    private List<BlogTextResponseVo> blogTextResponseVos = new ArrayList<>();
}