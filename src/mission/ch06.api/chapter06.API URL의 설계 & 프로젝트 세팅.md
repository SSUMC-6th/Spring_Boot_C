
# API 설계 
## 1. 홈 화면 
### 엔드포인트: `GET /api/home` 
**요청 헤더:** 
- `Authorization: Bearer <token>` 
**쿼리 스트링:** - `member_id: bigint` 
**응답:** 
```json 
{
  "missions": [
    {
      "mission_id": bigint,
      "store_name": "string",
      "reward": int,
      "deadline": "datetime",
      "status": "string"
    }
  ]
}


```

## 2. 마이 페이지

### a. 내 포인트

### 엔드포인트: `GET /api/mypage/points`

**요청 헤더:**

- `Authorization: Bearer <token>`

**쿼리 스트링:**

- `member_id: bigint`

**응답:**
```json
{
  "points": int
}

```
### b. 정보 수정

### 엔드포인트: `PUT /api/mypage/update`

**요청 헤더:**

- `Authorization: Bearer <token>`

**요청 본문:**
```json
{
  "member_id": bigint,
  "name": "string",
  "age": int,
  "address": "string",
  "email": "string"
}

```
응답 : 
```json
{
  "message": "User info updated successfully"
}

```

### c. 로그아웃

### 엔드포인트: `POST /api/logout`

**요청 헤더:**

- `Authorization: Bearer <token>`

**요청 본문:**
```json
{
  "member_id": bigint
}

```
응답 : 
```json
{
  "message": "Logout successful"
}

```

## 3. 리뷰 작성

### 엔드포인트: `POST /api/review`

**요청 헤더:**

- `Authorization: Bearer <token>`

**요청 본문:**
```json
{
  "member_id": bigint,
  "store_id": bigint,
  "body": "text",
  "score": float,
  "images": ["image_url1", "image_url2"]
}

```
응답 : 
```json
{
  "message": "Review submitted successfully"
}

```
## 4. 미션 목록 조회

### a. 진행중인 미션

### 엔드포인트: `GET /api/missions/in-progress`

**요청 헤더:**

- `Authorization: Bearer <token>`

**쿼리 스트링:**

- `member_id: bigint`

**응답:**
```json
{
  "missions": [
    {
      "mission_id": bigint,
      "store_name": "string",
      "reward": int,
      "deadline": "datetime",
      "status": "string"
    }
  ]
}

```

### b. 완료한 미션

### 엔드포인트: `GET /api/missions/completed`

**요청 헤더:**

- `Authorization: Bearer <token>`

**쿼리 스트링:**

- `member_id: bigint`

**응답:**
```json
{
  "missions": [
    {
      "mission_id": bigint,
      "store_name": "string",
      "reward": int,
      "deadline": "datetime",
      "status": "string"
    }
  ]
}

```

## 5. 미션 성공 처리

### 엔드포인트: `POST /api/mission/success`

**요청 헤더:**

- `Authorization: Bearer <token>`

**요청 본문:**
```json
{
  "member_id": bigint,
  "mission_id": bigint
}

```
응답 : 
```json
{
  "message": "Mission marked as successful"
}

```

## 6. 회원 가입

### 엔드포인트: `POST /api/register`

**요청 헤더:**

- `Content-Type: application/json`

**요청 본문:**
```json
{
  "name": "string",
  "gender": "string",
  "age": int,
  "address": "string",
  "email": "string",
  "password": "string"
}

```

응답 : 
```json
{
  "message": "User registered successfully"
}

```

