package com.example.umc.service;

import com.example.umc.domain.maaping.error.status.ErrorStatus;
import com.example.umc.common.exception.handler.MemberHandler;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public void CheckFlag(Integer flag) {
        if(flag == 1) {
            throw new MemberHandler(ErrorStatus.FRONTEND_ERROR);
        }
    }

}
