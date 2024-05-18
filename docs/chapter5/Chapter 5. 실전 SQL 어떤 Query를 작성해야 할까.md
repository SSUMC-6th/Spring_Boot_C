
>[!궁금한 점 : ORM 을 쓰는데 굳이 Query를 써야 할까? ]


>
# 들어가기 전에 

## Join
- inner join
	- 두 테이블에서 데이터가 있는 값들만 가지고 온다. 
```
SELECT <열 목록> 
FROM <첫 번째 테이블>     
	INNER JOIN <두 번째 테이블>     
	ON <조인 조건> 
[WHERE 검색 조건]  
#INNER JOIN을 JOIN이라고만 써도 INNER JOIN으로 인식합니다.`
```
- Outer join
	- 보통 left를 많이 쓴다. 
```
SELECT <열 목록> 
FROM <첫 번째 테이블(LEFT 테이블)>     
	<LEFT | RIGHT | FULL> OUTER JOIN <두 번째 테이블(RIGHT 테이블)>      ON <조인 조건> 
[WHERE 검색 조건]`
```

- select, from, where가 큰 틀이고 내가 가져올 테이블을 조인 하는 것이기 때문에 From에 조인 테이블을 때려 넣는다. 

ex) 
모든 사진의 "파일명", 게시자가 있다면 "닉네임"도 함께 조회
```sql
SELECT photos.filename, users.nickname
FROM photos
LEFT JOIN users ON users.id = photos.user_id
```

## Subquery

- 서브 쿼리: 하나의 SQL 문 안에 포함되어 있는 또 다른 SQL 문을 말한다. 서브쿼리는 메인 쿼리가 서브쿼리를 포함하는 종속적인 관계이다. 
- 종류
	- 단일행 서브쿼리(Single Row Subquery)
```sql
	SELECT *
FROM Player
WHERE Team_ID = (
    SELECT Team_ID
    FROM Player
    WHERE Player_name = 'yonglae'
)
ORDER BY Team_name;

```
			- 'yonglae'라는 이름을 가진 플레이어의 팀 ID를 하나만 반환합니다. - 만약 서브쿼리가 여러 행을 반환하면 오류가 발생합니다. 이 경우 'yonglae'라는 이름을 가진 플레이어가 단 한 명임을 가정합니다.
	
	- 다중행 서브쿼리(Multiple Row Subquery)
```sql
SELECT *
FROM Team
WHERE Team_ID IN (
    SELECT Team_ID
    FROM Player
    WHERE Player_name = 'yonglae'
)
ORDER BY Team_name;


```
	- 서브쿼리가 여러 행을 반환해도 오류가 발생하지 않으며, 그 팀 ID들 중 하나라도 일치하는 팀을 선택합니다.
		
	
	- 다중컬럼 서브쿼리(Multi Column Subquery)
```sql
SELECT *
FROM Player
WHERE (Team_ID, Height) IN (
    SELECT Team_ID, MIN(Height)
    FROM Player
    GROUP BY Team_ID
)
ORDER BY Team_ID, Player_name;

```
각 팀 별로 가장 작은 키를 가진 사람들을 보여주게 된다. 

#### 위치에 따라서 사용되는 서브 쿼리
1. select 절 

```sql
SELECT Player_name, Height, (
    SELECT AVG(Height)
    FROM Player p
    WHERE p.Team_ID = x.Team_ID
) as AVG_Height
FROM Player x;

```

2. from 절
```sql
SELECT t.Team_name, p.Player_name, p.Height
FROM Team t, (
    SELECT Team_ID, Player_name, Back_no
    FROM Player
    WHERE Position = 'MF'
) p
WHERE p.Team_ID = t.Team_ID;

```

# 쿼리 작성

>[!note]
>쿼리를 작성할 때 N : M 관계로 인해 가운데 매핑 테이블이 추가 된 경우는 쉬운 쿼리로 데이터를 가져오기가 힘들다. 

ex) UMC라는 이름을 가진 해시태그가 붙은 책을 찾도록 해봅시다.
``` sql
select * from book where id 
	in (select book_id from book_hash_tag 
	where hash_tag_id = (select id from hash_tag where name = 'UMC' ));
```

in에 매핑 테이블이 들어간다. (매핑 테이블에서 찾는 것)

## 페이징
Database 자체에서 끊어서 가져와야 한다. 이것을 Paging이라고 한다. 

#### Offset based 페이징
(paging 쿼리는 sql 마다 상이하다 mysql 기준으로 보게 된다면)
```sql
select* from book order by likes desc limit 10 offset 0;
```
`limit`: 한 페이지에서 보여줄 데이터의 개수
`Offset` : 몇 개를 건너뛸 것인가?(시작 점으로 부터)

```sql
select * from book 
order by created_at 
desc limit 15 offset (n - 1) * 15;
```

(n - 1) * 15 : 이 부분은 숫자를 계산해서 넣어 한다. 

**장단점**
페이지가 뒤로 갈 수록 넘어가야 하는 데이터양이 많아짐 -> 성능상의 이슈
**사용자가 1 페이지에서 2 페이지로 넘어가려는 찰나, 게시글 6개가 추가가 되었다.** ->분명 2 페이지로 왔는데 1 페이지에서 봤던 게 또 보이네??

#### Cursor based 페이징
커서 : 가리키기(포인터 느낌으로 보면 될 것 같다. )
**Cursor** : 사용자에게 응답해준 마지막의 데이터의 식별자 값이 Cursor가 된다. 
==비교==
- **오프셋 기반 방식**
    - 1억번~1억+10번 데이터 주세요. 라고 한다면 → 1억+10번개의 데이터를 읽음
- **커서 기반 방식**
    - 마지막으로 읽은 데이터(1억번)의 다음 데이터(1억+1번) 부터 10개의 데이터 주세요  
        → 10개의 데이터만 읽음

select * from book as b 
join (select count(*) as like_count from book_likes group by book_id) as likes on b.id = likes.book_id where likes.like_count < (select count(*) from book_likes where book_id = 3) order by likes.like_count desc, b.created_at desc limit 15;

얘 그래서 정렬만 된건데 뭐가 해결된건가?

#### 한줄 평
sql 복잡하게 쓰면 정말 어렵다...