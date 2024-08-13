package com.joyfarm.board.exceptions;

import com.joyfarm.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CommonException {
    public BoardNotFoundException() {
        super("NotFound.board", HttpStatus.NOT_FOUND); //404
        setErrorCode(true);
    }
}
