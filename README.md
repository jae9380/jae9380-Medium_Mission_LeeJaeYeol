
- [x]  필수미션 1 : 회원기능   
    - [X] 가입
      - [x] GET /member/join : 가입 폼
      - [x] POST /member/join : 가입 폼 처리

   -[x] 로그인  
     - [x] GET /member/login : 로그인 폼
     - [x] POST /member/login : 로그인 폼 처리

   - [x] 로그아웃
     - [x] POST /member/logout : 로그아웃

>   #### 회원가입 폼  
>   - username
>   - password  
>   - passwordConfirm

> #### 로그인 폼
>   - username  
>   - password

<hr>

- [ ] 필수미션 2 : 글 CRUD  
  - [x] 홈 GET / : 홈 - 최신글 30개 노출

  - [x] 글 목록 조회 GET /post/list : 전체 글 리스트 - 공개된 글만 노출

  - [x] 내 글 목록 조회 -   GET /post/myList : 내 글 리스트

  - [x] 글 상세내용 조회 -   GET /post/1 : 1번 글 상세보기
 
  - [x] 글 작성
    - [x] GET /post/write : 글 작성 폼
    - [x] POST /post/write : 글 작성 처리

  - [x] 글 수정
    - [x] GET /post/1/modify : 1번 글 수정 폼
    - [x] PUT /post/1/modify : 1번 글 수정 폼 처리

  - [x] 글 삭제 -  DELETE /post/1/delete : 1번 글 삭제

  - [x] 특정 회원의 글 모아보기
    - [x] GET /b/user1 : 회원 user1 의 전체 글 리스트
    - [x] GET /b/user1/3 : 회원 user1 의 글 중에서 3번글 상세보기


>글 쓰기 폼
> - title
> - body
> - isPublished
>   - 체크박스
>   - value="true"

>글 수정 폼
> - title
> - body
> - isPublished
>   - 체크박스
>   - value="true"
<hr>

## 추가 작업
 - [x] 추천
 - [ ] 댓글
   - [x] 댓글 작성 
     - POST /post/{id}/comment/write
   - [x] 댓글 목록
       글 상세페이지 하단 : 5번글에 대한 댓글 작성 폼
   - [ ] 댓글 수정
     - GET /post/5/comment/6/modify : 5번글에 대한 6번 댓글 수점 폼
     - POST /post/5/comment/6/modify : 5번글에 대한 6번 댓글 수점 폼 처리

>댓글 작성 폼
> - body
