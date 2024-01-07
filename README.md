
- [x]  필수미션 1 :  Member 클래스에 private boolean isPaid 필드를 추가
   - 해당 필드가 true 인 사람이 로그인할 때, ROLE_PAID 권한도 가지도록(스프링 시큐리티)
   - 해당 필드가 true 이면 유료 멤버십 회원 입니다.
<hr>

- [x] 필수미션 2  
  - [x] Post 클래스에 private boolean isPaid 필드를 추가
     - 해당 필드가 true 인 글은 유료회원이 아닌사람에게는 상세보기(GET /post/1)에서 본문(content)이 나올 자리에 이 글은 유료멤버십전용 입니다. 라는 문구가 나온다.
  - [x] 엔드 포인트
    - [x] GET /post/list
      - 멤버십 회원이 아니라도 글 리스트에서는 멤버십 전용글을 볼 수 있습니다.
    - [x] GET /post/1
      - 유료 멤버십 회원이고 1번 글이 멤버십전용글 이라면 볼 수 있다.
      - 그 외에는 이 글은 유료멤버십전용 입니다. 노출
<hr>

- [x] 필수미션 3 : NotProd 에서 유료멤버십 회원(샘플 데이터)과 유료글(샘플 데이터)을 각각 100개 이상 생성
<hr>

- [x] 선택미션 1 : 검색 필터링, 정렬
  - GET /post/list?sortCode=idDesc&kwType=title&kw=검색어
    - 정렬 : id 최신순, 검색범위 : 제목
   
  - GET /post/list?sortCode=hitAsc&kwType=body&kw=검색어
    - 정렬 : id 오래된순, 검색범위 : 내용
  
  - GET /post/list?sortCode=hitDesc&kwType=title,body&kw=검색어
    - 정렬 : 조회수 높은순, 검색범위 : 제목과 내용

  - GET /post/list?sortCode=likeCountAsc&kwType=title,body,author&kw=검색어
    - 정렬 : 추천수 낮은순, 검색범위 : 제목과 내용과 작성자 아이디
<hr>
    
- [ ] 선택미션 2 : 글 본문에 마크다운 에디터 적용
  - 토스트 UI 에디터 적용