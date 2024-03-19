package com.luxoft.rest.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Data Transfer Object that represents Account.
 */
@Builder
@Getter
public class AccountDto {

    private long id;
    private String holder;
    private long balance;

}
