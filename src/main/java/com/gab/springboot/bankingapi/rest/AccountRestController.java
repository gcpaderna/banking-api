package com.gab.springboot.bankingapi.rest;

import com.gab.springboot.bankingapi.entity.Account;
import com.gab.springboot.bankingapi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountRestController {

    private AccountService accountService;

    @Autowired
    public AccountRestController(AccountService theAccountService) {
        accountService = theAccountService;
    }

    @GetMapping("/accounts")
    public List<Account> findAll(){
        return accountService.findAll();
    }

    @GetMapping("/accounts/{accountId}")
    public Account getAccount(@PathVariable int accountId) {
        return accountService.findById(accountId);
    }

    @PostMapping("/accounts")
    public Account addAccount(@RequestBody Account theAccount) {

        Account dbAccount = accountService.save(theAccount);

        return dbAccount;
    }
}
