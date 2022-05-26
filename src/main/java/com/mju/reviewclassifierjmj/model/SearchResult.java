package com.mju.reviewclassifierjmj.model;

import com.mju.reviewclassifierjmj.model.vo.BlogTextResponseVo;
import com.mju.reviewclassifierjmj.model.vo.ClassifierResponseVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResult {
    private String lastBuildDate;

    private Long total;

    private Long start;

    private Long display;

    private List<Item> items;

    private String errorMessage;

    private String errorCode;

    public void assignClassificationResult(ClassifierResponseVo classifierResponseVo) {
        for (int i = 0; i < this.items.size(); i++) {
            Item item = this.items.get(i);
            BlogTextResponseVo vo = classifierResponseVo.getBlogTextResponseVos().get(i);
            item.assignClassificationResult(vo);
        }
    }
}
