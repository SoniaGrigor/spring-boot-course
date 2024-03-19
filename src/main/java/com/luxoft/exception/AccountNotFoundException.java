package com.luxoft.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountNotFoundException extends Exception {

    private final long id;
}
