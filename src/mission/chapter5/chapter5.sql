-- 1. 내가 진행중, 진행 완료한 미션 모아서 보는 쿼리(페이징 포함)
SELECT * FROM user_mission as um
WHERE user_id = 1  --(사용자 아이디)
AND mission_status in ("in_progress", "completed")
ORDER BY um.updated_at DESC
LIMIT 10 offset (n-1)*10;

-- 2. 리뷰 작성하는 쿼리, 사진의 경우는 일단 배제
store_id := SELECT store_id FROM store WHERE "가게명"
user_id := SELECT user_id FROM user WHERE 
INSERT INTO review values(1, store_id, user_id, 5.0, "맛있다ㅏ");

-- 3. 홈 화면 쿼리 (현재 선택 된 지역에서 도전이 가능한 미션 목록, 페이징 포함)
SELECT M.mission_id, M.point, DATEDIFF(M.due_date, NOW())
FROM mission AS M
JOIN store S ON S.store_id = M.store_id
WHERE S.region_id = (SELECT region_id R FROM region where R.name = "지역명")
AND M.due_date < (SELECT due_date FROM mission WHERE mission_id = 4 -- 4: 마지막으로 조회한 mission id)
ORDER BY M.due_date LIMIT 15;

-- 4. 마이 페이지 화면 쿼리
SELECT nickname, email, phone_number, points FROM user
WHERE user_id = 1; --사용의 아이디