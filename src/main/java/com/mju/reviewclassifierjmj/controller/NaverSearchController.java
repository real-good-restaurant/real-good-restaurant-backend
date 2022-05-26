package com.mju.reviewclassifierjmj.controller;

import com.mju.reviewclassifierjmj.model.SearchResult;
import com.mju.reviewclassifierjmj.model.vo.ClassifierResponseVo;
import com.mju.reviewclassifierjmj.service.NaverSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NaverSearchController {
    private final NaverSearchService naverSearchService;

    @GetMapping("/search/blog.json")
    @ResponseBody
    public ResponseEntity<SearchResult> test(@RequestParam String query,
                                             @RequestParam(required = false) Long display, @RequestParam(required = false) Long start) {
        SearchResult searchResult = new SearchResult();
        try {
            searchResult = naverSearchService.getNaverBlogSearchResults(query, display, start);
            ClassifierResponseVo classifierResponseVo = naverSearchService.classifyBlogTests(searchResult);
            searchResult.assignClassificationResult(classifierResponseVo);
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            searchResult.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(searchResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
