
# 2022 캡스톤 디자인: BERT 기반 맛집 리뷰 필터링 서비스 - 찐맛집

BERT 기반 모델으로 네이버 맛집 리뷰 중 광고일 가능성이 높은 게시글을 필터링하는 서비스 입니다. Java Spring Boot 기반 서버를 위한 저장소입니다.


## Tech Stack

**Client:** React

**Server:** Java Spring Boot


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

![App Screenshot](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/cfc23af0-12a9-46bb-b694-4946feffc65e/Picture1.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220526%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220526T112100Z&X-Amz-Expires=86400&X-Amz-Signature=d088ba34943dd6a3cce29d51428159b775b611ef74dfdae0044f1fb3fd3e6261&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Picture1.png%22&x-id=GetObject)

