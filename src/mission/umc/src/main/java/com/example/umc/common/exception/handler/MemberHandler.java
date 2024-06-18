package com.example.umc.common.exception.handler;


import com.example.umc.domain.maaping.error.BaseErrorCode;
import com.example.umc.common.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
