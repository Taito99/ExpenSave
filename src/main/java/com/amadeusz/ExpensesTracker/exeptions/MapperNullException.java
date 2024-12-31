package com.amadeusz.ExpensesTracker.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MapperNullException extends RuntimeException {
    public MapperNullException(String message) {
        super(message);
    }
}
