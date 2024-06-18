package com.example.umc.common.exception.handler;


import com.example.umc.common.error.BaseErrorCode;
import com.example.umc.common.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
