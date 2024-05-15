-- 1.1 진행 중 쿼리

SELECT mm.*, m.reward, s.name, m.mission_spec
FROM member_mission mm
    JOIN mission m ON mm.mission_id = m.id
    JOIN store s ON m.store_id = s.id
WHERE mm.status = 'in_progress'
  AND mm.created_at < '2024-05-15 23:00:00'  -- 이전 페이지의 마지막 created_at 값
ORDER BY mm.created_at DESC
    LIMIT 10 -- OFFSET 10 <- 페이지 2

-- 1.2 진행 완료 쿼리

SELECT mm.*, m.reward, s.name, m.mission_spec
FROM member_mission mm
        JOIN mission m ON mm.mission_id = m.id
        JOIN store s ON m.store_id = s.id
WHERE  mm.status = 'completed'
  AND mm.created_at < '2024-05-15 23:00:00'  -- 이전 페이지의 마지막 created_at 값
ORDER BY mm.created_at DESC
    LIMIT 10


-- 2. 리뷰를 작성하는 쿼리

INSERT INTO review (member_id, store_id, body, score)
VALUES (?, ?, ?, ?);
-- ?는 prepared statement로 실제 값으로 대체

-- 예시)
INSERT INTO review (member_id, store_id, body, score)
VALUES (1, 2, '마라탕 맛있습니다~', 5);


-- 3. 홈 화면 쿼리

SELECT m.id, s.name, s.address, m.mission_spec, m.reward, m.deadline, mm.status, mm.created_at
FROM member_mission mm
   JOIN mission m ON mm.mission_id = m.id
   JOIN store s ON m.store_id = s.id
   JOIN region r ON s.region_id = r.id
WHERE r.id = ?  -- 현재 선택된 지역 ID
   AND mm.status = 'in_progress'  -- 진행 중인 미션
   AND mm.created_at < '2024-05-15 23:00:00'  -- 이전 페이지의 마지막 created_at 값
ORDER BY
    mm.created_at DESC
    LIMIT 10;


-- 4. 마이 페이지 화면 쿼리

SELECT m.id, m.name, m.email, m.point, r.id, s.name, r.body, r.score
FROM member m
    LEFT JOIN review r ON m.id = r.member_id
    LEFT JOIN store s ON r.store_id = s.id
WHERE m.id = ?  -- 특정 사용자 ID
ORDER BY
    r.created_at DESC;





