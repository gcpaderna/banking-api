package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.dto.TransactionRequest;
import com.gab.springboot.bankingapi.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> findByAccountId(Integer theId);

    Transaction record(Integer accountId, TransactionRequest request);
}
