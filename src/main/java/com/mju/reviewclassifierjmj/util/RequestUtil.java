package com.mju.reviewclassifierjmj.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class RequestUtil {

    public static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public static String post(String apiUrl, String requestBody) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;");
            con.setDoOutput(true);

            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(con.getOutputStream())
            );
            bufferedWriter.write(requestBody);
            bufferedWriter.flush();
            bufferedWriter.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    public static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    public static String createNaverSearchUrl(String query, Long display, Long start) throws UnsupportedEncodingException {
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", encodedQuery);
        if (!Objects.isNull(display)) {
            params.add("display", String.valueOf(display));
        }
        if (!Objects.isNull(start)) {
            params.add("start", String.valueOf(start));
        }
        return UriComponentsBuilder.fromUriString("https://openapi.naver.com/v1/search/blog.json")
                .queryParams(params)
                .build().toUriString();
    }
}
