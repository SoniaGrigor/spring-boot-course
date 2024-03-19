package com.luxoft.rest;

import com.luxoft.rest.dto.AccountDto;
import com.luxoft.service.AccountService;
import com.luxoft.service.model.Account;
import com.luxoft.exception.AccountNotFoundException;
import com.luxoft.exception.NotEnoughFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.luxoft.rest.AccountTransformer.toDomainObject;
import static com.luxoft.rest.AccountTransformer.toDto;

@RestController
@RequestMapping(AccountController.PATH)
public class AccountController {

    public static final String PATH = "/accounts";
    public static final String BY_ID = "/{id}";
    public static final String DEPOSIT = "/deposit";
    public static final String WITHDRAW = "/withdraw";
    public static final String HOLDER = "/holder";
    private final AccountService service;
    private final AccountTransformer transformer;

    @Autowired
    public AccountController(AccountService service, AccountTransformer transformer) {
        this.service = service;
        this.transformer = transformer;
    }

    @GetMapping
    public List<AccountDto> findAll() {
        List<Account> accounts = service.findAll();
        return transformer.toDtoList(accounts);
    }

    @PostMapping
    public String create(@RequestBody AccountDto accountDTO) {
        Account createdAccount = service.create(toDomainObject(accountDTO));
        long createdAccountId = createdAccount.getId();
        return String.valueOf(createdAccountId);
    }

    @RequestMapping(path = BY_ID, method = RequestMethod.GET)
    public AccountDto get(@PathVariable @NonNull String id) throws AccountNotFoundException {
        long idLong = Long.parseLong(id);
        Account account = service.get(idLong);
        return toDto(account);
    }

    @DeleteMapping(BY_ID)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PutMapping(BY_ID + DEPOSIT)
    public void deposit(@PathVariable @NonNull long id,
                        @RequestParam("amount") @NonNull long amount) throws AccountNotFoundException {
        service.deposit(id, amount);
    }

    @PutMapping(BY_ID + WITHDRAW)
    public void withdraw(@PathVariable @NonNull long id,
                         @RequestParam("amount") @NonNull long amount) throws NotEnoughFundsException, AccountNotFoundException {
        service.withdraw(id, amount);
    }

    @PutMapping(BY_ID + HOLDER)
    public void changeHolder(@PathVariable @NonNull long id,
                             @RequestParam("name") @NonNull String name) throws AccountNotFoundException {
        service.changeHolder(id, name);
    }


}
