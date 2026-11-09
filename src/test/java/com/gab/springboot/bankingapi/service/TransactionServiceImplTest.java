package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.dao.AccountRepository;
import com.gab.springboot.bankingapi.dao.TransactionRepository;
import com.gab.springboot.bankingapi.dto.TransactionRequest;
import com.gab.springboot.bankingapi.entity.Account;
import com.gab.springboot.bankingapi.entity.TransactionType;
import com.gab.springboot.bankingapi.exception.AccountNotFoundException;
import com.gab.springboot.bankingapi.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        // Runs before every test: a fresh account with 10,000
        account = new Account();
        account.setBalance(new BigDecimal("10000"));
    }

    @Test
    void deposit_increasesBalance(){
        // 1. When the service asks for account 1, give it the test account
        Optional<Account> foundAccount = Optional.of(account);
        when(accountRepository.findById(1)).thenReturn(foundAccount);

        // 2. Deposit 5,000
        TransactionRequest request = new TransactionRequest(
                TransactionType.DEPOSIT, new BigDecimal("5000"), "Salary", "income");
        transactionService.record(1, request);

        // 3. Check the balance: 10,000 + 5,000 = 15,000
        BigDecimal expected = new BigDecimal("15000");
        int comparison = expected.compareTo(account.getBalance());
        assertEquals(0, comparison);
    }

    @Test
    void withdrawal_decreasesBalance(){
        // 1. When the service asks for account 1, give it the test account
        Optional<Account> foundAccount = Optional.of(account);
        when(accountRepository.findById(1)).thenReturn(foundAccount);

        // 2. Withdraw 3,000
        TransactionRequest request = new TransactionRequest(
                TransactionType.WITHDRAWAL, new BigDecimal("3000"), "Grocery", "grocery");
        transactionService.record(1, request);

        // 3. Check the balance: 10,000 - 3,000 = 7,000
        BigDecimal expected = new BigDecimal("7000");
        int comparison = expected.compareTo(account.getBalance());
        assertEquals(0, comparison);
    }

    @Test
    void withdrawal_moreThanBalance_throwsInsufficientFunds(){
        // 1. When the service asks for account 1, give it the test account
        Optional<Account> foundAccount = Optional.of(account);
        when(accountRepository.findById(1)).thenReturn(foundAccount);

        // 2. Withdraw an amount more than the balance
        TransactionRequest request = new TransactionRequest(
                TransactionType.WITHDRAWAL, new BigDecimal("11000"), "Grocery", "grocery");

        // 3. Throw InsufficientFundsException
        assertThrows(InsufficientFundsException.class,
                () -> {
                    transactionService.record(1, request);
                });
    }

    @Test
    void record_unknownAccount_throwsNotFound(){
        // 1. When the service asks for account 1, return an empty box (no account)
        when(accountRepository.findById(999)).thenReturn(Optional.empty());

        // 2. Random request
        TransactionRequest request = new TransactionRequest(
                TransactionType.WITHDRAWAL, new BigDecimal("5000"), "Grocery", "grocery");

        // 3. Throw AccountNotFoundException
        assertThrows(AccountNotFoundException.class,
                () -> {
                    transactionService.record(999, request);
                });
    }
}