package com.mju.reviewclassifierjmj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mju.reviewclassifierjmj.model.Item;
import com.mju.reviewclassifierjmj.model.SearchResult;
import com.mju.reviewclassifierjmj.model.vo.BlogTextRequestVo;
import com.mju.reviewclassifierjmj.model.vo.BlogTextResponseVo;
import com.mju.reviewclassifierjmj.model.vo.ClassifierRequestVo;
import com.mju.reviewclassifierjmj.model.vo.ClassifierResponseVo;
import com.mju.reviewclassifierjmj.util.RequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class NaverSearchService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.naver.client.id}")
    private String clientId;

    @Value("${app.naver.client.secret}")
    private String clientSecret;

    @Value("${app.classifier.url}")
    private String classifierUrl;

    public ClassifierResponseVo classifyBlogTests(SearchResult searchResult) throws IOException {
        ClassifierRequestVo vo = this.createClassifierRequestObj(searchResult);
        String requestBody = mapper.writeValueAsString(vo.getBlogTextRequestVos());
        String responseBody = RequestUtil.post(this.classifierUrl, requestBody);
        List<BlogTextResponseVo> blogTextResponseVos =
                Arrays.asList(mapper.readValue(responseBody, BlogTextResponseVo[].class));
        return new ClassifierResponseVo(blogTextResponseVos);
    }

    public ClassifierRequestVo createClassifierRequestObj(SearchResult searchResult) throws IOException {
        List<BlogTextRequestVo> blogTextRequestVos = new ArrayList<>();
        for (int i = 0; i < searchResult.getItems().size(); i++) {
            Item item = searchResult.getItems().get(i);
            String blogLinkForCrawling = this.findBlogLinkForCrawling(item.getLink());
            String blogText = this.extractNaverBlogText(blogLinkForCrawling);
            blogTextRequestVos.add(new BlogTextRequestVo(i, blogText));
        }
        return new ClassifierRequestVo(blogTextRequestVos);
    }

    private String findBlogLinkForCrawling(String link) throws IOException {
        Document iframeDocument = Jsoup.connect(link).get();
        Element iframe = iframeDocument.getElementById("mainFrame");
        return "http://blog.naver.com" + iframe.attr("src");
    }

    private String extractNaverBlogText(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Elements elements = this.findTagIncludingText(document);
        return elements.text();
    }

    private Elements findTagIncludingText(Document document) {
        if (this.hasBlogText(document, "div.se-main-container")) {
            return document.select("div.se-main-container");
        } else if (this.hasBlogText(document, "div.se_component_wrap.sect_dsc")) {
            return document.select("div.se_component_wrap.sect_dsc");
        } else if (this.hasBlogText(document, "div#postViewArea")) {
            return document.select("div#postViewArea");
        } else {
            throw new RuntimeException("Blog Text 크롤링 실패");
        }
    }

    private boolean hasBlogText(Document document, String cssQuery) {
        return document.select(cssQuery).size() > 0;
    }

    public Map<String, String> putNaverClientInfos(HashMap<String, String> requestHeaders) {
        requestHeaders.put("X-Naver-Client-Id", this.clientId);
        requestHeaders.put("X-Naver-Client-Secret", this.clientSecret);
        return requestHeaders;
    }

    public SearchResult getNaverBlogSearchResults(String query, Long display, Long start) throws UnsupportedEncodingException, JsonProcessingException {
        String naverSearchUrl = RequestUtil.createNaverSearchUrl(query, display, start);
        Map<String, String> requestHeaders = this.putNaverClientInfos(new HashMap<>());
        String responseBody = RequestUtil.get(naverSearchUrl, requestHeaders);
        return mapper.readValue(responseBody, SearchResult.class);
    }
}
