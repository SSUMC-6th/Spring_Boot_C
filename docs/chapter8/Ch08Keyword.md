# Chapter 8. API 응답 통일 & 에러 핸들러

# 1. 다양한 의존관계 주입 방법
- 생성자 주입
- 수정자 주입(setter 주입)
- 필드 주입
- 일반 메서드 주입

# 1.1 생성자 주입
- 특징
  - 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
  - 불변, 필수 의존관계에 사용
  - 생성자가 딱 1개만 존재하면 @Autowired를 생략해도 자동 주입이 된다. 물론 스프링 빈에만 해당된다.
- 예시
```java
@Component
public class OrderServiceImpl implements OrderService {
private final MemberRepository memberRepository;
private final DiscountPolicy discountPolicy;
public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
discountPolicy) {
this.memberRepository = memberRepository;
this.discountPolicy = discountPolicy;
}
}
```

# 1.2 수정자 주입(setter 주입)
> setter 필드 값을 변경하는 수정자 메서드를 통해 의존관계를 주입하는 방법
- 특징
  - 선택, 변경 가능성이 있는 의존관계에 사용
  - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.
- 예시
```java
@Component
     public class OrderServiceImpl implements OrderService {
         private MemberRepository memberRepository;
         private DiscountPolicy discountPolicy;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```
> @Autowired가 주입할 대상이 없으면 오류가 발생하는 것이 기본 동작이다. 주입할 대상이 없어도 동작하게하려면 @Autowired(requeire=False)로 지정해야한다.

# 1.3 필드 주입 (걍 쓰지 말자!)
- 특징
  - 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트하기 힘들다는 치명적인 단점이 존재한다.
  - DI프레임워크가 없으면 아무것도 할 수 없다.
  - 그니깐 사용하지말자!

# 1.4 일반 메서드 주입
> 일반 메서드를 통해서 주입 받을 수 있다.
- 특징
  - 한번에 여러 필드를 주입 받을 수 있다.
  - 일반적으로 잘 사용하진 않는다.. 왜냐면 수정자 주입(setter 주입) 선에서 다 처리하는 기능이기 때문!

---

# 2. IoC 컨테이너
> 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 제어의 역전(IoC)라 한다.
> 객체를 생성하고, 책임지고 의존성을 관리해주는 스프링 컨테이너를 IoC 컨테이너라 한다. 
- 종류
  1) BeanFactory : 자바 객체인 빈을 생성, 설정, 관리하는 인터페이스
  2) ApplicationContext : BeanFactory를 상속받고 있고, BeanFactory 외에도 다양한 클래스를 상속받고 있으므로 더 많은 기능을 제공한다고 보면 됨!

--- 

# 3. RestControllerAdvice

### 배경 지식
> - Spring은 전역적으로 예외를 처리할 수 있는 @ControllerAdvice와 @RestControllerAdvice를 제공하고 있다
> - 두 개의 차이는 @Controller, @RestController와 같다고 볼 수 있는데, @RestControllerAdvice는 @ControllerAdvice와 달리 @ResponseBody가 붙어 있어서
> 응답을 Json으로 내려준다는 차이가 존재한다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ControllerAdvice
@ResponseBody
public @interface RestControllerAdvice {
...
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {
...
}
```
#### * @ControllerAdvice를 이용함으로써 얻는 장점은 다음과 같다.
- 하나의 클래스로 모든 컨트롤러에 대해 전역적으로 예외 처리 가능
- 직접 정의한 에러 응답을 일관성있게 클라이언트에게 내려줄 수 있음
- 별도의 try-catch문이 없어서 코드의 가독성이 높아짐

> - 이러한 이유로 API에 대한 예외처리를 진행할 때는 @ControllerAdvice를 이용하면 된다.
> - 하지만 여러 ControllerAdvice가 있을 때는 @Order로 순서를 지정해주지 않는다면, Spring은 @ControllerAdvice에 대해 에러를 임의의 순서로 처리할 수 있다.

> 그럼 왜 @RestControllerAdvice를 사용하는가?
>> RESTful 웹 서비스에 더 특화되어 있다!
 
- 장점
  - 자동 JSON 응답: @RestControllerAdvice는 @ResponseBody가 자동으로 적용되어, 별도의 설정 없이 예외를 JSON 형식으로 반환할 수 있다. 이는 RESTful 웹 서비스에 매우 유용하다.
  - 간결한 코드: @RestControllerAdvice를 사용하면 별도로 @ResponseBody를 지정할 필요가 없어 코드가 더 간결해진다.
  - 일관된 응답 형식: RESTful 서비스에서 일관된 형식의 에러 응답을 제공하여 클라이언트가 예외를 쉽게 처리할 수 있도록 도와준다.
  - REST 컨트롤러 전용: RESTful 웹 서비스에 특화되어 있어, HTML 뷰를 반환하는 컨트롤러와 분리하여 관리할 수 있다. 응답 형식이 명확하게 구분되므로 유지보수에 도움이 된다.

---

# 4. lombok
##### : Boilerplate 코드를 자동 생성해주므로 편리하다. 롬복을 왜쓰냐면 가독성, 간결성, 편리성! 편하니까! 비즈니스 로직에 더 집중하자는 것!

> Boilerplate 코드? : 프로그래밍에서 상용구 코드와 같이 수정하지 않거나, 최소한의 수정만을 거쳐 여러 곳에서 필수적으로 사용되는 코드

- Lombok을 사용하는 이유
  - BoilerPlate 코드 감소 : getter/setter, toString, equals, 생성자 등의 메서드를 자동으로 생성해줌
  - 가독성 향상 : 불필요한 코드가 줄어 클래스의 핵심 비즈니스 로직이 더 잘 보임
  - 개발 생산성 향상 : 개발자가 반복적으로 작성해야하는 코드를 줄여줌으로써 개발 생산성 향상
  - 일관성 유지 : ex) 모든 클래스에서 동일한 방식으로 getter/setter를 생성하므로 코드의 일관성 보장됨

- 개인적으로 가장 많이 쓰는 사례 - 생성자 자동 생성
1) @NoArgsConstructor : 매개변수가 없는 기본 생성자 생성
```java
public User() {
}
```
2) @AllArgsConstructor : 모든 필드를 매개변수로 받는 생성자 생성
```java
public User(String name, int age) {
    this.name = name;
    this.age = age;
}
```
3) @RequiredArgsConstructor : 'final', '@NonNull' 어노테이션이 붙은 필드를 매개변수로 받는 생성자 생성
```java
public User(@NonNull String name, int age) {
    this.name = name;
    this.age = age;
}
```
- 'final' 필드 : 클래스 내에서 변경할 수 없는 필드로, 반드시 초기화되어야 한다. 이러한 어노테이션을 통해 필드를 초기화하는 생성자를 자동으로 생성하는 것이다.
- '@NonNull' 필드 : 필드 값이 null일 수 없는 필드로, 초기화 시 반드시 값을 제공해야한다. 이러한 어노테이션을 통해 필드를 초기화하는 생성자를 자동으로 생성하며, null값이 전달되면 예외를 던지는 것이다.
