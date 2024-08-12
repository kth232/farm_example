package com.joyfarm.global.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CommonException {
    public UnAuthorizedException(String code) {
        super(code, HttpStatus.UNAUTHORIZED);

    }
    public UnAuthorizedException() {
        this("UNAUTHORIZED");
        setErrorCode(true); //에러코드 형태
    }
}
