package com.gab.springboot.bankingapi.rest;

import com.gab.springboot.bankingapi.dto.TransactionRequest;
import com.gab.springboot.bankingapi.entity.Transaction;
import com.gab.springboot.bankingapi.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionRestController {

    private TransactionService transactionService;

    @Autowired
    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<Transaction> getTransactionsByAccountId(@PathVariable int accountId) { return transactionService.findByAccountId(accountId);}

    @PostMapping("/accounts/{accountId}/transactions")
    public Transaction addTransaction(@PathVariable int accountId, @Valid @RequestBody TransactionRequest request) {
        return transactionService.record(accountId, request);
    }
}
