package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.dao.AccountRepository;
import com.gab.springboot.bankingapi.entity.Account;
import com.gab.springboot.bankingapi.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl (AccountRepository theAccountRepository) {
        accountRepository = theAccountRepository;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(int theId) {
        return accountRepository.findById(theId)
                .orElseThrow(() -> new AccountNotFoundException(theId));
    }

    @Override
    public Account save(Account theAccount) {

        theAccount.setId(null);
        theAccount.setBalance(BigDecimal.ZERO);

        return accountRepository.save(theAccount);
    }
}
