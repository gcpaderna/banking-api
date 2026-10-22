package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.entity.Account;

import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(int theId);

    Account save(Account theAccount);
}
