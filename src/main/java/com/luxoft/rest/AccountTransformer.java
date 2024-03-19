package com.luxoft.rest;

import com.luxoft.rest.dto.AccountDto;
import com.luxoft.service.model.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountTransformer {

    public List<AccountDto> toDtoList(List<Account> accounts) {
        List<AccountDto> result = new ArrayList<>();

        accounts.forEach(account -> result.add(toDto(account)));
        return result;
    }

    public static Account toDomainObject(AccountDto dto) {
        return Account.builder().id(dto.getId()).holder(dto.getHolder()).balance(dto.getBalance()).build();
    }

    public static AccountDto toDto(Account account) {
        return AccountDto.builder().id(account.getId()).holder(account.getHolder()).balance(account.getBalance()).build();
    }

}
