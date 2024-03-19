package com.luxoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleNotEnoughFunds(NotEnoughFundsException ex) {
        String message = "Not enough funds on account `%d` to withdraw `%d`"
                .formatted(ex.getId(), ex.getAmount());
        return ResponseEntity.badRequest()
                .body(message);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        String message = "Account with id `%d` cannot be found.".formatted(ex.getId());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(message);
    }
}
