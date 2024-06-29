package com.example.umc.web.controller;

import com.example.umc.common.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @Value("${server.env}")
    private String env;
    @Value("${server.port}")
    private String port;
    @Value("${server.address}")
    private String address;


    @GetMapping
    public BaseResponse<?> healthCheck(){
        Map<String, String> responseData = new TreeMap<>();
        responseData.put("env", env);
        responseData.put("port", port);
        responseData.put("address", address);

        return BaseResponse.onSuccess(responseData);
    }

    @GetMapping("/env")
    public BaseResponse<String> getEnv(){

        return BaseResponse.onSuccess(env);
    }
}
