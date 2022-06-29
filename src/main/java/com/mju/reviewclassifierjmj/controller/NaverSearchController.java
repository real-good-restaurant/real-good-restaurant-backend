package com.mju.reviewclassifierjmj.controller;

import com.mju.reviewclassifierjmj.model.NaverSearchResult;
import com.mju.reviewclassifierjmj.service.NaverSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class NaverSearchController {
    private final NaverSearchService naverSearchService;

    @GetMapping("/search/blog.json")
    @ResponseBody
    public NaverSearchResult search(
            @RequestParam String query,
            @RequestParam(required = false) Long display,
            @RequestParam(required = false) Long start) throws Exception{
        return naverSearchService.getNaverBlogSearchResults(query, display, start);
    }

    @PostMapping("/classification/blog.json")
    @ResponseBody
    public NaverSearchResult classify(@RequestBody NaverSearchResult naverSearchResult) throws Exception{
        return naverSearchService.classify(naverSearchResult);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public NaverSearchResult handleException(Exception e) {
        e.printStackTrace();
        NaverSearchResult naverSearchResult = new NaverSearchResult();
        naverSearchResult.setErrorMessage(e.getMessage());
        return naverSearchResult;
    }
}
