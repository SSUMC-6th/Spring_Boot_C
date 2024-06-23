package com.example.umc.web.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TestResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempTestDTO{
        String testString;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempExceptionDTO{
        Integer flag;
    }

    public static TempTestDTO toTestTempDto(String testString){
        return TempTestDTO.builder()
                .testString(testString)
                .build();
    }

    public static TempExceptionDTO toExceptionTempDto(Integer flag) {
        return TempExceptionDTO.builder()
                .flag(flag)
                .build();
    }


}
