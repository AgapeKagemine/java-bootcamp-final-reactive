package com.bootcamp.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentsException extends RuntimeException {

    public PaymentsException(String message) {
        super(message);
    }

}
