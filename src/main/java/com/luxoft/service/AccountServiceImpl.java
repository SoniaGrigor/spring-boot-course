package com.luxoft.service;

import com.luxoft.dao.AccountDao;
import com.luxoft.service.model.Account;
import com.luxoft.exception.AccountNotFoundException;
import com.luxoft.exception.NotEnoughFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDao dao;

    @Autowired
    public AccountServiceImpl(AccountDao dao) {
        this.dao = dao;
    }

    @Override
    public Account get(long id) throws AccountNotFoundException {
        Account account = dao.get(id);
        if (account == null) {
            throw new AccountNotFoundException(id);
        }
        return account;
    }

    @Override
    public List<Account> findAll() {
        return dao.findAll();
    }

    @Override
    public Account create(Account account) {
        return dao.insert(account);
    }

    @Override
    public void delete(long accountId) {
        dao.delete(accountId);
    }

    @Override
    public void deposit(long accountId, long amount) throws AccountNotFoundException {
        Account account = dao.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        account.deposit(amount);
        dao.update(account);
    }

    @Override
    public void withdraw(long accountId, long amount) throws NotEnoughFundsException, AccountNotFoundException {
        Account account = dao.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        account.withdraw(amount);
        dao.update(account);
    }

    @Override
    public void changeHolder(long accountId, String newHolder) throws AccountNotFoundException {
        Account account = dao.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        account.setHolder(newHolder);
        dao.update(account);
    }
}
