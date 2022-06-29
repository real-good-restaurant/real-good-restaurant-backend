package com.mju.reviewclassifierjmj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mju.reviewclassifierjmj.model.Item;
import com.mju.reviewclassifierjmj.model.NaverSearchResult;
import com.mju.reviewclassifierjmj.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NaverSearchService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.naver.client.id}")
    private String clientId;

    @Value("${app.naver.client.secret}")
    private String clientSecret;

    @Value("${app.classifier.url}")
    private String classifierUrl;

    private final ItemService itemService;

    @Transactional
    public NaverSearchResult classify(NaverSearchResult naverSearchResult) throws IOException, RuntimeException {
        List<String> itemLinkList = itemService.extractItemsLink(naverSearchResult.getItems());
        List<Item> cachedItemList = itemService.findAllByLinkList(itemLinkList);
        List<Item> newItemList = this.filterCachedItems(naverSearchResult.getItems(), cachedItemList);
        if (newItemList.size() > 0) {
            newItemList = this.fillBlogText(newItemList);
            String requestBody = mapper.writeValueAsString(newItemList);
            String responseBody = RequestUtil.post(this.classifierUrl, requestBody);
            List<Item> classifiedItemList = Arrays.asList(mapper.readValue(responseBody, Item[].class));
            itemService.saveAll(classifiedItemList);
            cachedItemList.addAll(classifiedItemList);
        }
        naverSearchResult.setItems(cachedItemList);
        return naverSearchResult;
    }

    public List<Item> filterCachedItems(List<Item> itemList, List<Item> cachedItemList) {
        List<Item> newItemList = new ArrayList<>();
        for (Item item: itemList) {
            boolean cached = false;
            boolean needUpdate = false;
            for (Item cachedItem: cachedItemList) {
                if (cachedItem.getLink().equals(item.getLink())) {
                    cached = true;
                    if (cachedItem.isOlderThanMonth()) {
                        needUpdate = true;
                        newItemList.add(cachedItem);
                    }
                    break;
                }
            }
            if (!cached && !needUpdate) {
                newItemList.add(item);
            }
        }
        return newItemList;
    }

    public List<Item> fillBlogText(List<Item> itemList) throws IOException {
        for (Item item: itemList) {
            String blogLinkForCrawling = this.findBlogLinkForCrawling(item.getLink());
            String blogText = this.extractNaverBlogText(blogLinkForCrawling);
            item.setFullText(blogText);
        }
        return itemList;
    }

    private String findBlogLinkForCrawling(String link) throws IOException {
        Document iframeDocument = Jsoup.connect(link).get();
        Element iframe = iframeDocument.getElementById("mainFrame");
        return "http://blog.naver.com" + iframe.attr("src");
    }

    private String extractNaverBlogText(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        try {
            Elements elements = this.findTagIncludingText(document);
            return elements.text();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "";
        }
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

    public NaverSearchResult getNaverBlogSearchResults(String query, Long display, Long start) throws UnsupportedEncodingException, JsonProcessingException {
        String naverSearchUrl = RequestUtil.createNaverSearchUrl(query, display, start);
        Map<String, String> requestHeaders = this.putNaverClientInfos(new HashMap<>());
        String responseBody = RequestUtil.get(naverSearchUrl, requestHeaders);
        return mapper.readValue(responseBody, NaverSearchResult.class);
    }

    public NaverSearchResult jsonToSearchResultObj(String requestBody) throws JsonProcessingException {
        return mapper.readValue(requestBody, NaverSearchResult.class);
    }
}
