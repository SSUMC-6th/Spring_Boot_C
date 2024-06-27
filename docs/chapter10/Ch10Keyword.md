# Chapter 9. API & Paging

## 1. Spring Data JPA의 Paging
> Page와 Slice는 페이징 처리를 위한 인터페이스. 
> 큰 규모의 데이터 집합을 여러 페이지로 나누어 처리할 때 사용한다.

### 1.1. Page
#### : 페이징 처리된 결과를 나타내는 인터페이스로 페이지 번호, 페이지 크기, 총 페이지 수, 총 요소 수 등의 페이징 관련 meta data를 포함

### <메서드 종류>
- getContent(): 현재 페이지에 포함된 데이터 목록을 반환
- getTotalElements(): 전체 요소 수를 반환
- getTotalPages(): 전체 페이지 수를 반환 
- getNumber(): 현재 페이지 번호를 반환
- getSize(): 페이지 크기를 반환
- hasNext(): 다음 페이지가 있는지 여부를 반환
- hasPrevious(): 이전 페이지가 있는지 여부를 반환
- isFirst(): 첫 번째 페이지인지 여부를 반환
- isLast(): 마지막 페이지인지 여부를 반환

> 'Page' 객체는 주로 검색 결과의 전체 크기와 페이지 정보를 알고 싶을 때 사용한다.

- 예시 코드
```java
Pageable pageable = PageRequest.of(0, 10); // 첫 번째 페이지, 페이지 크기 10
Page<MyEntity> page = myEntityRepository.findAll(pageable);

List<MyEntity> content = page.getContent(); // 현재 페이지의 데이터
long totalElements = page.getTotalElements(); // 전체 요소 수
int totalPages = page.getTotalPages(); // 전체 페이지 수
boolean hasNext = page.hasNext(); // 다음 페이지가 있는지 여부
```

### 1.2. Slice
#### : Page와 유사하지만 전체 데이터 집합의 크기를 계산하지 않는다. 따라서 상대적으로 가벼운 페이징 처리가 가능하다.

> 'Slice' 객체는 전체 페이지 수나 전체 요소 수를 알 필요가 없고, 다음 슬라이스가 있는지 여부만 필요한 경우에 사용한다.
> 더불어 DB에서 불필요한 전체 카운트 쿼리를 수행하지 않으므로 Page에 비해 상대적으로 성능이 좋다.


## 2. 객체 그래프 탐색
> 객체와 그 객체가 참조하는 다른 객체 간의 관계를 통해 데이터를 조회하는 것을 의미한다.
> ex) JPA엔티티를 통해 DB의 테이블 간의 관계를 매핑하고, 이러한 관계를 통해 데이터를 탐색

- 예시 코드
```java
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // getters and setters
}

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private int score;

    private LocalDateTime createdAt;

    private String body;

    // getters and setters
}
```

```java
public static StoreResponseDTO.ReviewPreViewDTO reviewPreViewDTO(Review review) {
return StoreResponseDTO.ReviewPreViewDTO.builder()
.ownerNickname(review.getMember().getName())
.score(review.getScore())
.createdAt(review.getCreatedAt().toLocalDate())
.body(review.getBody())
.build();
}
```

```java
public interface ReviewRepository extends JpaRepository<Review, Long> {
List<Review> findByMemberName(String name);
}
```

> Review 엔티티에서 Member 엔티티의 데이터를 쉽게 가져오는 것을 볼 수 있다!

- 고려 사항
> N+1 문제 : 예를 들어 Review 엔티티를 가져오는 과정에서 각 Review가 참조하는 Member를 별도의 쿼리로 가져오게 되면 쿼리 수가 급격하게 늘어나는 문제가 발생할 수 있다.
> 따라서 적절한 fetch전략이 필요! (EAGER vs LAZY)

## 3. JPQL
> JPA에서 사용하는 쿼리로, 객체지향 모델을 기반으로 DB에 질의하는 언어
> SQL과 유사하지만 DB테이블이 아닌 엔티티 객체를 대상으로 한다는 차이가 있음

ex) SELECT, FROM, WHERE, JOIN, ORDER BY, GROUP BY..


