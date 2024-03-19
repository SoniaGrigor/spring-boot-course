package com.luxoft.service.model;

import com.luxoft.exception.NotEnoughFundsException;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class Account {

    private long id;
    private String holder;
    private long balance;

    public void deposit(long amount) {
        balance += amount;
    }

    public void withdraw(long amount) throws NotEnoughFundsException {
        if (amount > balance) {
            throw new NotEnoughFundsException(id, amount);
        }
        balance -= amount;
    }
}
