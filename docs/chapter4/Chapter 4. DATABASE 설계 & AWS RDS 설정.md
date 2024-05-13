
>[!학습 목표]
>데이터베이스를 어떻게 설계하는 것이 좋을까?

# 들어가기 전에 

### MYSQL
- 관계형 데이터베이스이다. 
- 데이터를 관계(Table)로 표현한다. 

### MySQL 기초 문법
- [ ] 데이터베이스와 테이블의 생성
- [ ] 데이터의 CRUD

**MySQL 설치**
mysql을 실행하고 (설치 후 mysql 환경 변수 설정을 해준다. )
```
%mysql -uroot -p
```
비밀번호를 입력한다. 
-uroot는 유저로 루트에 접근하겠다는 것이고 -p는 패스워드를 입력하겠다는 의미이다. 

**데이터베이스와 테이블 생성**
```
CREATE DATABASE [데이터베이스 명]; // 데이터베이스 생성 
DROP DATABASE [데이터베이스 명]; // 데이터베이스 제거 
SHOW DATABASES; // 데이터베이스 목록 
USE [데이터베이스 명]; 데이터베이스 선택
```

```
USE mydb01; 
CREATE TABLE topic( 
	id INT(11) NOT NULL AUTO_INCREMENT, 
	title VARCHAR(100) NOT NULL, 
	description TEXT NULL, 
	created DATETIME NOT NULL, 
	author VARCHAR(30) NULL, 
	profile VARCHAR(100) NULL, PRIMARY KEY(id) 
)
```
INT 타입 : 기본적으로 정수를 담는다. 
VATCHAR : 변경 가능한 문자를 담는다. 

**데이터의 CRUD**
```
USE mydb01 
DESC topic // 테이블 구조 정보 (describe)
```
DESC 문으로 테이블의 구조를 확인할 수 있다. 

Create
```
INSERT INTO topic (title,description,created,author,profile) VALUES('MySQL','MySQL is ...',NOW(),'egoing','developer');
```

Read
```
SELECT * FROM topic; // topic 테이블의 모든 칼럼의 모든 데이터를 표시
```

Update, Delete
```
UPDATE topic SET description='ORACLE is ...', title='Oracle' WHERE ID = 2; 
DELETE FROM topic WHERE id = 2;
```

### ERD

-  ERD란 `Entity Relationship Diagram`의 약어로, 데이터베이스 구조를 한눈에 알아보기 위해서 쓰인다.
- 핵심 3요소
	- Entity
	- Relationship
	- Attribute

**Entity**
- 테이블이 Entity로 정의 될 수 있다. 
- 모든 Entity는 하나의 UID(식별자)를 가져야 한다. 
- Weak Entity
	- 개체가 가진 속성으로는 개체를 고유하게 정의할 수 없는 개체를 의미한다. 
	- 1203 이라는 강의가 존재하고, 001, 002, 003..분반이 있다고 했을때, 이 분반 이름은 따로 특징이 없고 다른 강의와도 겹칠 수 있다. 이를 단독으로 존재할 수 없고, 다른 개체에 의존해야 하는 Weak Entity라고 한다.

**Attribute**
- Attribute는 Entity를 구성하고 있는 구성 요소이다. 
- 데이터 타입을 같이 명시 해주어야 한다. 
- `Key Attribute 
	- 다른 객체들과 중복되지 않는 고유한 값을 가진 Attribute로, 객체를 식별하는데 사용된다. 
- `Composite Attribute
    - 독립적인 Attribute들이 모여서 생성된 Attribute를 의미한다.
    - 예를 들어 OO시,OO동,OO아파트 등의 독립적인 Attribute 네개가 모여서 생성된 주소 Attribute는 복합 attribute라고 할 수 있다.
- `Multi-Valued Attribute  
    - 하나의 Attribute가 여러개의 값을 가지는 Attribute를 의미한다.
    - 예를 들어 하나의 영상물에 로맨스, SF, 호러 등의 여러가지 장르가 공통적으로 존재할 수 있다.
- `Derived Attribute
    - 이는 다른 Attribute가 갖고 있는 값으로부터 유도된 속성을 의미한다.
    - 예를 들어 모든 상품의 총 가격을 나타내는 total이라는 속성은 상품의 가격 attribute, 상품의 개수 attribute를 곱해서 계산된 값이다. 이는 Derived Attribute이 된다.

**Relationship**
- Entity 간의 관계를 의미한다. 
- 두 Entity간에 선을 긋고, 관계 명칭을 기록하게 된다.
- 선택 사항을 표시한다.
    - 점선은 선택적인 사항을 의미한다.
        - 예를 들어, 사원과 부서 Entity가 있을때 부서 입장에서는 사원을 배치 받을수도, 받지 않을 수도 있다.
    - 실선은 필수적인 사항을 의미한다.
        - 사원 입장에서는 부서가 필수적으로 배정받아야 한다.
- 관계 형태를 표시한다.
    - 삼지창 모양은 하나 이상을 의미한다.
    - 단선은 하나를 의미한다.
        - 부서는 여러명의 사원을 가질 수 있으나, 사원은 하나의 부서에만 배치된다.

**Cardinality**
- 1:1 관계
	- 양쪽 모두 단 하나씩 존재하는 경우이다. 
- 1:N 관계
	- 일대다/다대일 관계는 하나의 원소가 두개 이상의 원소와 관계를 맺는 것을 의미한다. 한 부서에는 여러명의 사원이 소속되어 있다.
		-> 이를 관계 모델로 바꿀 때 따로 relationship의 table을 만들지 않고, =="Many"쪽에 있는 entity에 "one"쪽의 primary key를 Attribute로 추가하게 된다==. 쉽게는 사원의 정보에 부서 번호를 추가한다고 생각하면 된다.
- N:M 관계
	- 다대다 관계라고 하며, 양쪽 모두 하나 이상과 연관될 수 있다. 예를 들어 하나의 수업에는 여러명의 학생이 있을 수 있고, 한 명의 학생이 여러 개의 수업을 들을 수 있다.
		-> ==이러한 경우에는, M:N 관계를 M:1 관계로 분할한다==. 관계를 맺는 두 entity의 primary key를 가져와 하나의 relation을 생성한다. 즉, 수강신청이라는 테이블을 하나 더 만들어 학생은 자신이 수강신청하는 수업에만 관계를 맺고 있으면 된다.

# ERD 설계

- 테이블 이름과 칼럼 이름은 모두 소문자, 그리고 단어 구분은 대소문자가 아닌, 밑줄로 구분을 해주는 것이 좋다. 
- 기본 키를 위해 각 엔티티 정보 중 유일한 값을 기본 키로 설정하기 보다는 index를 따로 두는 것이 편하다 (중복되는 일이 추후에 생길 수 있다.)
- 기본 키 타입은 int가 아닌 추후 서비스 확장을 고려해서 bigint로

추가)
**member 테이블에서 gender의 경우...!**
- 0이면 남자, 1이면 여자 이렇게 하는 경우도 있고
- varchar로 설정하여, 문자로 두고 enum으로 관리 해도 된다. 
**member 테이블에서 status와 inactive_date를 두는 이유**
그냥 delete해버리는 수가 있지만 회원탈퇴하고 돌아오는 경우가 있기 때문이다. 
- 따라서 사용자 같이 곧바로 지워버리는 것이 위험한 엔티티는 바로 Delete를 하는 것이 아니라 ==일단 비활성 상태로 두고, 일정 기간동안 비활성인 경우 자동 삭제가 되도록 설계==하는 것이 좋다. 

**create_at, update_at을 두는 이유**
datetime(6)에서 (6)은 밀리초 소수점 6자리까지 구분한다는 의미이다. 
최신 순 정렬 기능을 위해서 고려한 설계이다. 

자동으로 지우는 방법
`batch` : 정해진 시간에 자동으로 실행되는 프로세스 
=> soft delete : Http Method 중에 Patch로 구현을 한다. 

### 연관 관계

N : M 관계일 때는 가운데에 매핑 테이블을 따로 둬야 한다.
Mapping table은 양쪽의 기본 키를 외래 키로 가진다. 

