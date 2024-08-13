package com.joyfarm.board.exceptions;

import com.joyfarm.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class BoardDataNotFoundException extends CommonException { //우리는 commonException으로
    public BoardDataNotFoundException() {
        super("NotFound.boardData", HttpStatus.NOT_FOUND); //404
        setErrorCode(true);
    }
}
