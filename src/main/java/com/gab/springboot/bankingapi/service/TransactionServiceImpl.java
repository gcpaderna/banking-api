package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.dao.AccountRepository;
import com.gab.springboot.bankingapi.dao.TransactionRepository;
import com.gab.springboot.bankingapi.dto.TransactionRequest;
import com.gab.springboot.bankingapi.entity.Account;
import com.gab.springboot.bankingapi.entity.Transaction;
import com.gab.springboot.bankingapi.entity.TransactionType;
import com.gab.springboot.bankingapi.exception.AccountNotFoundException;
import com.gab.springboot.bankingapi.exception.InsufficientFundsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> findByAccountId(Integer theId) {
        if (!accountRepository.existsById(theId)) {
            throw new AccountNotFoundException(theId);
        }
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(theId);
    }

    @Override
    @Transactional
    public Transaction record(Integer accountId, TransactionRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        BigDecimal newBalance;
        if (request.type() == TransactionType.DEPOSIT) {
            // deposit
            newBalance = account.getBalance().add(request.amount());
        } else {
            // withdraw
            if (account.getBalance().compareTo(request.amount()) < 0) {
                throw new InsufficientFundsException(accountId);
            }
            newBalance = account.getBalance().subtract(request.amount());
        }

        // set new balance after transaction
        account.setBalance(newBalance);

        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setType(request.type());
        tx.setAmount(request.amount());
        tx.setDescription(request.description());
        tx.setCategory(request.category());
        tx.setBalanceAfter(newBalance);
        return transactionRepository.save(tx);
    }


}
