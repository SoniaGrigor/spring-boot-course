package com.luxoft.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
public class NotEnoughFundsException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    private final long id;
    private final long amount;

}
