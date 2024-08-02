package com.joyfarm.global.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class BadRequestException extends CommonException {
    //예외 코드를 400으로 고정

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(Map<String, List<String>> errorMessages) {
        super("", HttpStatus.BAD_REQUEST); //응답 코드 400
        setErrorMessages(errorMessages);
    }

}
