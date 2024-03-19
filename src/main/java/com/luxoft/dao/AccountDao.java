package com.luxoft.dao;

import com.luxoft.service.model.Account;

import java.util.List;

public interface AccountDao {

    Account get(long id);

    Account insert(Account account);

    void update(Account account);

    void delete(long id);

    List<Account> findAll();
}
