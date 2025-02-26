package com.amadeusz.ExpensesTracker.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidExpenseException extends RuntimeException {
    public InvalidExpenseException(String message) {
        super(message);
    }
}
