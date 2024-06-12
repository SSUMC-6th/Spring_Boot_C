
### 들어가기 전에

- 프론트 입장에서 API 응답을 통일 해주어야 편하다 
	- 통일 안해주면 개빡칩니다^^

# API 응답 통일

- Code라는 enum으로 관리를 합니다. 
	- 성공, 실패를 하나의 enum으로 관리할 수 있고 분리할 수도 있다. 


### HTTP 상태코드

1. 200번대 
	1. 200 : 성공
	2. 201 : 잘 만들었다. 
2. 400번 대
	1. 400 : Bad Request : 요청 이상하게 함, 필요정보 누락됨
	2. 401 : Unauthorized : 인증 안됨 (로그인 되어있어야 하는데 안된 상황)
	3. 403 : Forbidden : 권한 없음 (로그인은 되었으나 접근이 안된다, 관리자 페이지)
	4. 404 : NotFound : 요청한 정보가 그냥 없다. 
3. 500번대 
	1. 500 : 서버 터짐
	2. 504 : 서버가 응답을 안준다. 

>[!note]
>http 응답 코드로 반환을 해주는 것보다 좀 더 자세하게 알려주기 위해서 code와 message를 사용한다. 



# Spring의 의존성 주입

## 의존성 주입이 필요한 이유

```java
public class PetOwner{
	private AnimalType animal;
    
    public PetOwner(){
    	this.animal = new Dog();
    }
}

interface AnimalType{}

class Dog implements AnimalType{}

class Cat implements AnimalType{}
```
이 코드의 문제점 : animal 필드를 cat으로 생성하고자 한다면? 
==코드 자체를 수정== 해야한다. 
=> PetOwner 클래스가 AnimalType에 의존적이다. 

> spring에서는 의존성 주입을 통해서 객체 간 의존성을 줄여준다. 


## 의존성 주입이란?

"스프링 컨테이너에서 객체 Bean을 먼저 생성해두고 생성한 객체를 지정한 객체에 주입하는 방식을 의존성 주입이라고 한다."

> 스프링 컨테이너에서 객체의 생명주기를 관리하게 된다는 사실 잊지 말자!!

### 그럼 어케 주입할건데? 

#### 필드주입

클래스에 선언된 필드에 생성된 객체를 주입해주는 방식이다. 
필드에 주입할 떄는 어노테이션을 사용한다. @Autowired

#### 수정자 주입
```java
@Controller
public class PetController{

    private PetService petService;

	@Autowired
    public void setPetService(PetService petService){
    	this.petService = petService;
    }
}
```

#### 생성자 주입
```java
@Controller
public class PetController{

    private final PetService petService;

	@Autowired
    public PetController(PetService petService){
    	this.petService = petService;
    }
}
```
생성자를 통해서 의존성을 주입해주는 방식이다. 
생성자 주입은 인스턴스가 생성될 때 1회 호출되는 것이 보장된다. 
==final 키워드를 사용할 수 있다. ==

```java
@Controller
@RequiredArgsConstructor
public class PetController{
    private final PetService petService;
}
```
이렇게 가능 

#### 뭐가 좋은데?

> 생성자 주입이 좋다. 

1. 객체의 불변성
	1. 객체가 생성되는 시점에 생성자를 호출하여 최초 1회만 주입한다. 
	2. 불변 객체를 보장하는 것이다. 
	3. 객체를 포함한 클래스가 생성되는 시점에 포함된 객체들도 반드시 생성되기 때문에 개체가 비어있을 가능성도 배제한다. 
2. 순환 참조 문제 방지 기능
	1. 순환 참조란 두 객체가 서로를 필드에 포함하여 참조하고 있는 상태를 말한다. 
	2. 이거 큰일 날 수도 있다. 
	3. 생성자 주입은 런타임이 아니라 컴파일 타임에 터치기 때문에 좀 더 안전하다. 
3. 테스트 용이
	1. 순수 자바 코드로 테스트가 가능하다 

# IoC 컨테이너 

Inversion Of Control의 약자이다. 제어의 역전이라고도 한다 
- 객체의 생성, 생명주기, 관리까지 모든 객체에 대한 주도권을 프레임워크가 가진 것이다. 
- spring container가 관리한다. 
	- IoC 컨테이너라고도 한다. 
- Application을 구성하는 객체 간의 낮은 결합도를 유지할 수 있다. 

### 그럼 spring container가 뭘까?

**스프링 컨테이너는 Spring Application 내에서 자바 객체를 관리하는 공간을 뜻한다.**
**자바 객체를 Bean(빈) 이라고 Spring 에서는 부른다.**

> 컨테이너의 역할은 의존성 주입(DI, Dependency Injection)을 통하여 Application을 구성하는 빈(Bean)들의 생명주기(Life Cycle)을 개발자 대신 관리해준다. (Bean의 생성, 소멸 관리)

##### Spring 컨테이너 종류
- BeanFactory
- ApplicationContext

 **BeanFactory(인터페이스, 최상위) <- ApplicationContext(인터페이스) <- ApplicationContext(구현체) 의 구조**

"**BeanFactory의 모든 기능을 ApplicationContext가 포함하고 있고, 추가기능까지 있다.**"


# RestContollerAdvice

>예외 처리 방법이다. 

@RestControllerAdvice(@ControllerAdvice)

### @ExceptionHandler란

```java
@Controller
public class IndexController {
	...
    @GetMapping("/posts")
    public String posts(){
        int exception = 4/0;
        return "posts";
    }
}
```
예외 코드(0으로 나눈 ArithmeticException)

![[Pasted image 20240611133013.png]]
이렇게 보내주면 되겠냐? 사용자가 사용하는데 불편하잖아 

==try-catch 구문을 작성하는 것이 아닌== @ExceptionHandler를 사용한다. 

```java
@GetMapping("/posts")
public String posts(){
	int exception = 4/0;
	return "posts";
}

@ExceptionHandler(ArithmeticException.class)
public ResponseEntity<String> handleNoSuchElementFoundException(ArithmeticException exception) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
}
```
위와같이 @ExceptionHandler 어노테이션이 붙은 메서드를 추가해서 에러를 처리하고 발생한 예외는 ExceptionHandlerExceptionResolver에 의해 처리가 된다.

**장점**
HttpServletRequest나 WebRequest 등을 얻을 수 있으며 반환 타입으로는 ResponseEntity, String, void 등 자유롭게 활용할 수 있다.

**한계**
- `@ExceptionHandler`는 특정 컨트롤러에서만 발생하는 예외만 처리하기 때문에 여러 Controller에서 발생하는 **에러 처리 코드가 중복**될 수 있으며, 
- 사용자의 요청과 응답을 처리하는 Controller의 기능에 **예외처리 코드가 섞이며 단일 책임 원칙(SRP)가 위배**되게 됩니다.

