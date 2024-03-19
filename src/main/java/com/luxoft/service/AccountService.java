package com.luxoft.service;

import com.luxoft.service.model.Account;
import com.luxoft.exception.AccountNotFoundException;
import com.luxoft.exception.NotEnoughFundsException;

import java.util.List;

public interface AccountService {

    Account get(long id) throws AccountNotFoundException;

    List<Account> findAll();

    Account create(Account account);

    void deposit(long accountId, long amount) throws AccountNotFoundException;

    void withdraw(long accountId, long amount) throws NotEnoughFundsException, AccountNotFoundException;

    void changeHolder(long accountId, String newHolder) throws AccountNotFoundException;

    void delete (long accountId);
}
