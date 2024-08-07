package com.joyfarm.global.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CommonException {
    public UnAuthorizedException(String code) {
        super(code, HttpStatus.UNAUTHORIZED);
        setErrorCode(true); //에러코드 형태
    }
    public UnAuthorizedException() {
        this("UNAUTHORIZED");
    }
}
