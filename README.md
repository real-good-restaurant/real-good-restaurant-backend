
# 2022 캡스톤 디자인: BERT 기반 맛집 리뷰 필터링 서비스 - 찐맛집

BERT 기반 모델으로 네이버 맛집 리뷰 중 광고일 가능성이 높은 게시글을 필터링하는 서비스 입니다. Java Spring Boot 기반 서버를 위한 저장소입니다.


## Tech Stack

**Client:** React

**Server:** Java Spring Boot


## Architecture
<img src="./imgs/jmj_architecture.png" width="600">

## API Reference

#### 광고 여부, 광고 확률과 함께 블로그 포스팅 목록을 반환

```http
  GET /search/blog.json
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `query` | `string` | **필수** 검색하고자 하는 키워드  |
| `display` | `integer` | 검색 결과 출력 건수 지정 |
| `start` | `integer` | 검색 시작 위치로 최대 1000까지 가능 |


## Related

[NAVER Developers - 블로그 검색 API](https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%EB%B8%94%EB%A1%9C%EA%B7%B8)


## Screenshots

![App Screenshot](./imgs/jmj_screenshot.png)

