package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import com.example.umc.service.TestService;
import com.example.umc.web.dto.reponse.TestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/hello")
    public BaseResponse<TestResponse.TempTestDTO> testApi(){

        //정적 메서드는 클래스 이름을 통해서 직접 호출한다.
        return BaseResponse.onSuccess(TestResponse.toTestTempDto("hello"));

    }

    @GetMapping("/exception")
    public BaseResponse<TestResponse.TempExceptionDTO> exceptionApi(){

        //여기서 무조건 에러가 터지지
        testService.CheckFlag(1);
        //정적 메서드는 클래스 이름을 통해서 직접 호출한다.
        return BaseResponse.onSuccess(TestResponse.toExceptionTempDto(1));
    }

}
