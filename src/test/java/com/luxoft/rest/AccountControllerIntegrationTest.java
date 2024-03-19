package com.luxoft.rest;

import com.luxoft.rest.dto.AccountDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.LinkedHashMap;
import java.util.List;

import static com.luxoft.rest.AccountController.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIntegrationTest {

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.schema-locations", () -> "classpath:schema-test.sql");
        registry.add("spring.sql.init.data-locations", () -> "classpath:data-test.sql");
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void findAll() {
        List<LinkedHashMap> response = restTemplate.getForObject(PATH, List.class);

        assertEquals(2, response.size());
        assertEquals("Test Mike", response.get(0).get("holder"));
        assertEquals("Test Don", response.get(1).get("holder"));

    }

    @Test
    void create() {
        var accountDto = AccountDto.builder().id(3).holder("Test 1").balance(300).build();
        var accountId = restTemplate.postForObject(PATH, accountDto, String.class);

        assertEquals("3", accountId);
    }

    @Test
    void get() {
        var accountId = "/1";
        var accountDto = restTemplate.getForObject(PATH + accountId, AccountDto.class);
        assertEquals(1, accountDto.getId());
        assertEquals("Test Mike", accountDto.getHolder());
        assertEquals(1300, accountDto.getBalance());
    }

    @Test
    void get_accountNotFound() {
        var response = restTemplate.getForObject(PATH + "/10", String.class);
        assertEquals("Account with id `10` cannot be found.", response);
    }

    @Test
    @Order(Integer.MAX_VALUE - 1)
    void delete() {
        var response = restTemplate.getForObject(PATH, List.class);
        var initialNumberOfAccounts = response.size();

        var accountId = "/2";
        restTemplate.delete(PATH + accountId);

        var finalResponse = restTemplate.getForObject(PATH, List.class);
        var finalNumberOfAccounts = finalResponse.size();
        assertEquals(1, initialNumberOfAccounts - finalNumberOfAccounts);
    }

    @Test
    void deposit() {
        var accountId = "/1";
        var accountDto = restTemplate.getForObject(PATH + accountId, AccountDto.class);
        var initialBalance = accountDto.getBalance();

        var amount = 300;

        restTemplate.put(PATH + accountId + DEPOSIT + "?amount=" + amount, null);

        var response = restTemplate.getForObject(PATH + accountId, AccountDto.class);
        var finalBalance = response.getBalance();

        assertEquals(amount, finalBalance - initialBalance);
    }

    @Test
    void deposit_accountNotFound() {
        var response = restTemplate.exchange(PATH + "/10" + DEPOSIT + "?amount=300", HttpMethod.PUT, null, String.class);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("Account with id `10` cannot be found.", response.getBody());
    }

    @Test
    @Order(Integer.MAX_VALUE - 3)
    void withdraw() {
        var accountId = "/1";
        var accountDto = restTemplate.getForObject(PATH + accountId, AccountDto.class);
        var initialBalance = accountDto.getBalance();

        var amount = 300;

        restTemplate.put(PATH + accountId + WITHDRAW + "?amount=" + amount, null);

        var result = restTemplate.getForObject(PATH + accountId, AccountDto.class);
        var finalBalance = result.getBalance();

        assertEquals(amount, initialBalance - finalBalance);
    }

    @Test
    void withdraw_accountNotFound() {
        var response = restTemplate.exchange(PATH + "/10" + WITHDRAW + "?amount=300", HttpMethod.PUT, null, String.class);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("Account with id `10` cannot be found.", response.getBody());
    }

    @Test
    void withdraw_notEnoughFunds() {
        var accountId = "/1";
        var response = restTemplate.exchange(PATH + accountId + WITHDRAW + "?amount=30000", HttpMethod.PUT, null, String.class);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        var body = response.getBody();
        assertEquals("Not enough funds on account `1` to withdraw `30000`", body);
    }

    @Test
    @Order(Integer.MAX_VALUE - 2)
    void changeHolder() {
        var accountId = "/1";
        var name = "Stephan";

        restTemplate.put(PATH + accountId + HOLDER + "?name=" + name, null);

        var accountDto = restTemplate.getForObject(PATH + accountId, AccountDto.class);

        assertEquals("Stephan", accountDto.getHolder());
    }

    @Test
    void changeHolder_accountNotFound() {
        var response = restTemplate.exchange(PATH + "/10" + HOLDER + "?name=Stephan", HttpMethod.PUT, null, String.class);

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("Account with id `10` cannot be found.", response.getBody());
    }
}