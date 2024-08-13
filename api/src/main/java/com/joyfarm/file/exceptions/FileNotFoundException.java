package com.joyfarm.file.exceptions;

import com.joyfarm.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends AlertBackException {
    public FileNotFoundException() {
        super("NotFound.file", HttpStatus.NOT_FOUND);
        setErrorCode(true);
    }
}
