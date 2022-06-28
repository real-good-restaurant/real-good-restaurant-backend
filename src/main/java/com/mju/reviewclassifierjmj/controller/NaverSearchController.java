package com.mju.reviewclassifierjmj.controller;

import com.mju.reviewclassifierjmj.model.SearchResult;
import com.mju.reviewclassifierjmj.model.vo.ClassifierResponseVo;
import com.mju.reviewclassifierjmj.service.NaverSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NaverSearchController {
    private final NaverSearchService naverSearchService;

    @GetMapping("/search/blog.json")
    @ResponseBody
    public ResponseEntity<SearchResult> test(@RequestParam String query,
                                             @RequestParam(required = false) Long display, @RequestParam(required = false) Long start) {
        SearchResult searchResult = new SearchResult();
        try {
            searchResult = naverSearchService.getNaverBlogSearchResults(query, display, start);
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            searchResult.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(searchResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/classification/blog.json")
    @ResponseBody
    public ResponseEntity<SearchResult> classify(@RequestBody String requestBody) {
        SearchResult searchResult = new SearchResult();
        try {
            searchResult = naverSearchService.jsonToSearchResultObj(requestBody);
            ClassifierResponseVo classifierResponseVo = naverSearchService.classifyBlogTests(searchResult);
            searchResult.assignClassificationResult(classifierResponseVo);
            return new ResponseEntity<>(searchResult, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            searchResult.setErrorMessage(e.getMessage());
            searchResult.getItems().clear();
            return new ResponseEntity<>(searchResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
