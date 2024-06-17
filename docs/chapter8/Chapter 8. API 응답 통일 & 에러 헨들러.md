
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




# 예외 처리

## 1. 스프링의 기본적인 예외 처리 방법

spring에서 예외 처리를 위한 BasicErrorController를 구현해두었고 
에러 발생 => /error 로 에러 요청을 다시 전달 하도록 WAS 설정이 되어 있다. 

일반적인 요청 흐름
WAS(톰캣) -> 필터 -> 서블릿(디스패처 서블릿) -> 인터셉터 -> 컨트롤러

예외가 발생했을 때 
예외처리를 하지 않으면 WAS까지 에러가 전달 된다. 

"아 내가 대응 못하는 에러구나! 바로 error page"
컨트롤러(예외 발생) -> 인터셉터 -> 서블릿(dispatcher 서블릿) -> 필터 -> WAS(톰캣)
--여기까지 WAS까지 에러가 올라가는 것이고--

이제 에러 페이지를 호출하기 위해서 아까 처음에 말했던 BasicErrorController를 호출한다. (이름 어렵지 안잖아~)

> 그럼 요청이 2번 가는 것인가요?

1번의 요청이 2번 전달 되는 것입니다~

```java
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;
    ...

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ...
    }

    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        ...
        return new ResponseEntity<>(body, status);
    }
    
    ...
}

```
BasicErrorController는 accept 헤더에 따라 에러 페이지를 반환하거나 에러 메시지를 반환한다. 
에러 경로는 기본적으로 /error로 정의되어 있으며 properties에서 server.error.path로 변경할 수 있다. 


errorHtml()과 error()는 모두 getErrorAttributeOptions를 호출해 반환할 에러 속성을 얻는데, 기본적으로 DefaultErrorAttributes로부터 반환할 정보를 가져온다. DefaultErrorAttributes는 전체 항목들에서 설정에 맞게 불필요한 속성들을 제거한다.

![[Pasted image 20240615082957.png]]

# 2. 스프링이 제공하는 다양한 예외 처리 방법

try-catch를 모든 코드에 붙이는 것은 너어무 비효율적이다. 
스프링에서는 이 문제를 어떻게 해결을 하였나? (error는 메인 로직과 떼어서 보자)
"Cross-cutting-concerns" 
=> 예외 처리 전략을 추상화한 HandlerExceptionResolver 인터페이스

발생한 Exception을 catch하고 HTTP 상태나 응답 메시지를 설정한다. 그래서 WAS에서는 해당 요청이 정상적인 응답인 것으로 인식이 되고 복잡한 WAS의 에러 전달이 진행되지 않는다. 


![[Pasted image 20240617151948.png]]
Object 타입인 handler는 뭘까? -> 예외가 발생한 컨트롤러 객체이다. 
예외가 던져지면 dispatcher servlet까지 전달이 되는데 적합한 예외 처리를 위해서 HandlerExceptionResolver 구현체들을 빈으로 등록해서 관리한다. 
아래 우선순위대로 4가지 구현체들이 빈으로 등록이 되어있다. 

- DefaultErrorAttributes : 에러 속성을 저장하며 직접 예외를 처리하지는 않는다. 
- ExceptionHandlerExceptionResolver : 에러 응답을 위한 Controller나 ControllerAdvice에 있는 ExceptionHandler를 처리한다. 
- ResponseStatusExceptionResolver  : HTTP 상태 코드를 지정하는 @ResponseStatus 또는 ResponseStatusException를 처리한다. 
- DefaultHandlerExceptionResolver : 스프링 내부의 기본 예외들을 처리한다. 

spring은 ExceptionResolver를 동작시켜서 에러를 처리한다
방식
- ResponseStatus
- ResponseStatusException
- ExceptionHandler
- ControllerAdvice, RestControllerAdvice

### ResponseStatus
에러 HTTP 상태를 변경하도록 도와주는 어노테이션이다. 
- Exception 클래스 자체
- 메소드에 @ExceptionHandler와 함께
- 클래스에 @RestControllerAdvice와 함께

![[Pasted image 20240617154203.png]]
이렇게 만든 클래스에 @ResponseStatus로 응답 상태를 지정해줄 수 있다. 

이렇게 되면 ResponseStatusExceptionResolver가 지정해준 상태로 에러 응답이 내려가도록 처리한다. 

![[Pasted image 20240617154336.png]]

이는 BasicErrorController에 의한 응답이다. 
@ResponseStatus를 처리하는 ResponseStatusExceptionResolver는 WAS까지 예외를 전달하고 복잡한 WAS의 에러 요청 전달이 진행되는 것이다. 

**한계점**
- 에러 응답의 내용(Payload)를 수정할 수 없다. (DefaultErrorAttribure를 수정하면 가능하기는 하다. )
- 예외 클래스와 강하게 결합되어 같은 예외는 같은 상태, 같은 에러메시지를 반환한다. 
- 별도의 응답이 필요하다면 예외 클래스를 추가해야한다. (어노테이션을 붙이는 형식이니까)
- WAS까지 예외가 전달이 되고, WAS의 에러 요청 전달이 진행된다. 
- 외부에서 정의한 Exception 클래스에는 @ResponseStatus를 붙여줄 수 없다. 


### ResponseStatusException
외부 라이브러리에서 정의한 코드는 우리가 수정할 수 없으므로 @ResponseStatus를 붙여줄 수 없다. Spring5에는 @ResponseStatus의 프로그래밍적 대안으로써 손쉽게 에러를 반환할 수 있는 ResponseStatusException이 추가 되었다. 

ResponseStatusException는 HttpStatus와 함께 선택적으로 reason과 cause를 추가할 수 있고, 언체크 예외을 상속받고 있어 명시적으로 에러를 처리해주지 않아도 된다. 이러한 ResponseStatusException은 다음과 같이 사용할 수 있다.

![[Pasted image 20240617155847.png]]

언체크 예외 : `RuntimeException`을 상속받는다. 즉, 컴파일 타임에 예외 처리를 강제하지 않는다. 예를 들어 try-catch블록으로 명시적으로 예외를 처리하지 않아도 된다. 

@ResponseStatus와 동일하게 예외가 발생하면 ResponseStatusExceptionResolver가 에러를 처리한다. ResponseStatusException를 사용하면 다음과 같은 이점을 누릴 수 있다.

- 기본적인 예외 처리를 빠르게 적용할 수 있으므로 손쉽게 프로토타이핑할 수 있음
- HttpStatus를 직접 설정하여 예외 클래스와의 결합도를 낮출 수 있음
- 불필요하게 많은 별도의 예외 클래스를 만들지 않아도 됨
- 프로그래밍 방식으로 예외를 직접 생성하므로 예외를 더욱 잘 제어할 수 있음

하지만 그럼에도 불구하고 ResponseStatusException는 다음과 같은 한계점들을 가지고 있다. 이러한 이유로 API 에러 처리를 위해서는 @ExceptionHandler를 사용하는 방식이 더 많이 사용된다.

- 직접 예외 처리를 프로그래밍하므로 일관된 예외 처리가 어려움
- 예외 처리 코드가 중복될 수 있음
- Spring 내부의 예외를 처리하는 것이 어려움
- 예외가 WAS까지 전달되고, WAS의 에러 요청 전달이 진행됨

ExceptionHandler와 RestControllerAdvice는 맨 아라애 정리해두었다. 

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

@ExceptionHandler는 다음에 어노테이션을 추가함으로써 에러를 손쉽게 처리할 수 있다. 
- 컨트롤러의 메서드
- @ControllerAdvice나 @RestControllerAdvice 있는 메서드 
얘는 ExceptionHandlerExceptionResolver에 의해 처리된다. 

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

@ExceptionHandler는 Exception 클래스들을 속성으로 받아 처리할 예외를 지정할 수 있다. 만약 ExceptionHandler 어노테이션에 예외 클래스를 지정하지 않는다면, 파라미터에 설정된 에러 클래스를 처리하게 된다. 또한 @ResponseStatus와도 결합가능한데,  만약 ResponseEntity에서도 status를 지정하고 @ResponseStatus도 있다면 ResponseEntity가 우선순위를 갖는다.

ExceptionHandler는 @ResponseStatus와 달리 에러 응답(payload)을 자유롭게 다룰 수 있다는 점에서 유연하다. 예를 들어 응답을 다음과 같이 정의해서 내려준다면 좋을 것이다.

- code: 어떠한 종류의 에러가 발생하는지에 대한 에러 코드
- message: 왜 에러가 발생했는지에 대한 설명
- erros: 어느 값이 잘못되어 @Valid에 의한 검증이 실패한 것인지를 위한 에러 목록

--위의 설명 추가--
- **Exception 클래스 지정**: `@ExceptionHandler`는 속성으로 예외 클래스를 받아 처리할 예외를 지정할 수 있습니다. 예를 들어, `@ExceptionHandler(NullPointerException.class)`는 `NullPointerException`이 발생했을 때 해당 메서드를 호출합니다.
    
- **파라미터에 설정된 예외 처리**: 만약 `@ExceptionHandler` 어노테이션에 예외 클래스를 지정하지 않으면, 메서드의 파라미터로 설정된 예외 클래스를 처리하게 됩니다. 즉, 파라미터에 어떤 예외 클래스가 설정되었느냐에 따라 처리할 예외가 결정됩니다.
    
- **@ResponseStatus와 결합**: `@ResponseStatus`는 특정 예외가 발생했을 때 반환할 HTTP 상태 코드를 지정할 수 있습니다. 이를 통해 예외가 발생했을 때 클라이언트에게 반환할 상태 코드를 설정할 수 있습니다.

```java
@RestController
public class MyController {

    @GetMapping("/example")
    public String example() {
        if (someCondition) {
            throw new NullPointerException("Example NullPointerException");
        }
        return "Success";
    }

    // 특정 예외를 처리하는 메서드
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Handled NullPointerException: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 모든 예외를 처리하는 메서드
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>("Handled Exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

```

Spring은 예외가 발생하면 가장 구체적인 예외 헨들러를 먼저 찾고 없으면 부모 예외의 핸들러를 찾는다. 
ex) NullPointerException이 발생했다면, 위에서는 NullPointerException 처리기가 없으므로 Exception에 대한 처리기가 찾아진다.

==주의점==
@ExceptionHandler에 등록된 예외 클래스와 파라미터로 받는 예외 클래스가 동일해야 한다는 것이다. 만약 값이 다르다면 스프링은 컴파일 시점에 에러를 내지 않다가 런타임 시점에 에러를 발생시킨다.


**장점**
HttpServletRequest나 WebRequest 등을 얻을 수 있으며 반환 타입으로는 ResponseEntity, String, void 등 자유롭게 활용할 수 있다.

**한계**
- `@ExceptionHandler`는 특정 컨트롤러에서만 발생하는 예외만 처리하기 때문에 여러 Controller에서 발생하는 **에러 처리 코드가 중복**될 수 있으며, 
- 사용자의 요청과 응답을 처리하는 Controller의 기능에 **예외처리 코드가 섞이며 단일 책임 원칙(SRP)가 위배**되게 됩니다.

### @ControllerAdvice와 @RestControllerAdvice

==전역적으로== @ExceptionHandler를 적용할 수 있는 방법이다. 

두개의 차이는 @Controller와 @RestController와 같이 @ResponseBoy가 붙어 있는 응답을 Json으로 내려준다는 점에서 다르다. 

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

ControllerAdvice는 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용해준다. 위에서 보이듯 ControllerAdvice 어노테이션에는 @Component 어노테이션이 있어서 ControllerAdvice가 선언된 클래스는 스프링 빈으로 등록된다. 

그러므로 우리는 다음과 같이 전역적으로 에러를 핸들링하는 클래스를 만들어 어노테이션을 붙여주면 에러 처리를 위임할 수 있다.

@interface는 사용자 정의 어노테이션을 정의할 때 사용된다. 
어노테이션은 메타 데이터를 코드에 첨부하는 방법으로, 주로 컴파일러에게 정보를 제공하거나 런타임 시 특정 동작을 수행하기 위해 사용된다. 

ControllerAdvice는 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용해준다. 위에서 보이듯 ControllerAdvice 어노테이션에는 @Component 어노테이션이 있어서 ControllerAdvice가 선언된 클래스는 스프링 빈으로 등록된다. 그러므로 우리는 다음과 같이 전역적으로 에러를 핸들링하는 클래스를 만들어 어노테이션을 붙여주면 에러 처리를 위임할 수 있다.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementFoundException.class)
    protected ResponseEntity<?> handleNoSuchElementFoundException(NoSuchElementFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .code("Item Not Found")
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

```

### 스프링의 예외처리 흐름

앞서 설명하였듯 다음과 같은 예외 처리기들은 스프링의 빈으로 등록되어 있고, 예외가 발생하면 순차적으로 다음의 Resolver들이 처리가능한지 판별한 후에 예외가 처리된다.

1. ExceptionHandlerExceptionResolver: 에러 응답을 위한 Controller나 ControllerAdvice에 있는 ExceptionHandler를 처리함
2. ResponseStatusExceptionResolver: Http 상태 코드를 지정하는 @ResponseStatus 또는 ResponseStatusException를 처리함
3. DefaultHandlerExceptionResolver:  스프링 내부의 기본 예외들을 처리한다.

![[Pasted image 20240617162932.png]]

[추가 참고](https://mangkyu.tistory.com/205)
