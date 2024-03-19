package com.luxoft.dao;

import com.luxoft.service.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Repository
public class AccountDaoImpl implements AccountDao {

    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts";
    private static final String SELECT_ACCOUNT = "SELECT * FROM accounts WHERE accounts.id=:id";
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET holder=:holder, balance=:balance WHERE id=:id";
    private static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id=:id";
    private static final String ACCOUNTS_TABLE = "accounts";
    private static final String ID_COLUMN = "id";
    private static final String HOLDER_COLUMN = "holder";
    private static final String BALANCE_COLUMN = "balance";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedJdbcTemplate;

    @Autowired
    public AccountDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcOperations namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Account get(long id) {
        return namedJdbcTemplate.query(
                SELECT_ACCOUNT, singletonMap(ID_COLUMN, id),
                (resultSet, i) -> buildAccount(resultSet)
        ).stream().findFirst().orElse(null);
    }

    private static Account buildAccount(ResultSet resultSet) throws SQLException {
        var id1 = resultSet.getLong(ID_COLUMN);
        var holder = resultSet.getString(HOLDER_COLUMN);
        var balance = resultSet.getLong(BALANCE_COLUMN);
        return Account.builder().id(id1).holder(holder).balance(balance).build();
    }

    @Override
    public Account insert(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put(HOLDER_COLUMN, account.getHolder());
        params.put(BALANCE_COLUMN, account.getBalance());
        long newId = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(ACCOUNTS_TABLE)
                .usingGeneratedKeyColumns(ID_COLUMN)
                .executeAndReturnKey(params).longValue();
        account.setId(newId);
        return account;
    }

    @Override
    public void update(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put(ID_COLUMN, account.getId());
        params.put(HOLDER_COLUMN, account.getHolder());
        params.put(BALANCE_COLUMN, account.getBalance());
        namedJdbcTemplate.update(UPDATE_ACCOUNT, params);
    }

    @Override
    public void delete(long id) {
        namedJdbcTemplate.update(DELETE_ACCOUNT, singletonMap(ID_COLUMN, id));
    }

    public List<Account> findAll() {
        return namedJdbcTemplate.query(
                SELECT_ALL_ACCOUNTS,
                (resultSet, i) -> buildAccount(resultSet)
        );
    }

}
