## java의 Exception 종류들
    
Exception은 컴파일 시에 발생하는 예외와 런타임에 발생하는 예외로 나뉜다. 
    
**컴파일 시에 발생하는 예외**
    
- IOException
        
    입출력을 다루는 메서드에 예외처리가 없는 경우 발생
        
```java
    public class boo{
        public static void main(String[] args){
        	byte[] list = {'a', 'b', 'c'};
        	System.out.write(list);
        }
    }
```    
    위의 경우, wirte()에서 발생할 수 있는 IOException에 대한 예외 처리를 하지 않았기 때문에 컴파일 시 오류가 발생한다. 흔히 사용하는 print, println의 경우는 자체적으로 컴파일 예외 처리를 미리 해놓기 때문에 에러가 발생하지 않는다.
        
- FileNotFoundException
        
    파일에 접근하려고 할 때 파일을 찾지 못한 경우 발생
        
- DataFormatException
        
    데이터 형식이 잘못된 경우 발생
        
    주로 데이터 처리, 변환 과정에서 발생한다.
        
    
**런타임 시에 발생하는 예외**
    
- NullPointerException
        
    객체가 null인 상태에서 접근하여 메서드를 호출하는 경우 발생, 객체가 없는 상태에서 그 객체를 사용하려고 했기 때문에.
        
- ArrayIndexOutOfBoundsException
        
    배열에 할당된 인덱스 범위를 초과하여 접근한 경우 발생
        
- ArithmeticException
        
    정수를 0으로 나누는 것처럼 비정상 계산이 실행되는 경우 발생
        
- IllegalArgumentException
        
    메소드의 전달 인자값이 잘못된 경우 발생
        
- ClassCastException
        
    상속 관계, 구현 클래스-인터페이스 간 관계가 아님에도 클래스 간 형 변환을 시도한 경우 발생
        
- NumberFormatException
        
    문자열로 되어 있는 데이터를 숫자 타임으로 변경하는 경우에 숫자 타입으로 변경할 수 없는 문자를 치환하는 경우 발생
        
    ``` java
    String strNum = “3.141592”
        
    ex) Integer.parseInt(”strNum”)
    ```


## @Valid

- request Body를 검증할 때 주로 사용
- 기본적으로 컨트롤러에서 동작
- Request DTO에 @NotNull처럼 유효성 검사 어노테이션을 적용한 경우, 컨트롤러 내의 파라미터 부분에 @Valid 어노테이션을 적어야 작동
- 유효성 검증에 실패한 경우(검증 과정에서 예외가 발생한 경우) MethodArgumentNotValidException 예외를 발생시킨다. 이는 @ExceptionHandler로 핸들링 할 수 있다.
- @Valid에 의한 반환값은 400 BadRequest
    

**@Validated**
    
- 스프링에서 제공하는 어노테이션
- 검증 과정에서 예외가 발생한다면 ConstraintViolationException 예외를 발생시킨다. 이는 @ExceptionHandler로 핸들링 할 수 있다.
    


**쿼리 스트링이나 파라미터를 검증해야 하는 경우**
    
@Validated 을 클래스 레벨에 선언하고, 유효성 검사하는 어노테이션(@Min)을 추가하면 된다.
    
![출처: [https://medium.com/sjk5766/valid-vs-validated-정리-5665043cd64b](https://medium.com/sjk5766/valid-vs-validated-%EC%A0%95%EB%A6%AC-5665043cd64b)](./1.png)