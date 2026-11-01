package com.gab.springboot.bankingapi.service;

import com.gab.springboot.bankingapi.dao.AccountRepository;
import com.gab.springboot.bankingapi.dao.TransactionRepository;
import com.gab.springboot.bankingapi.entity.Account;
import com.gab.springboot.bankingapi.entity.Transaction;
import com.gab.springboot.bankingapi.entity.TransactionType;
import com.gab.springboot.bankingapi.exception.AccountNotFoundException;
import com.gab.springboot.bankingapi.exception.InsightUnavailableException;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InsightServiceImpl implements InsightService {

    private final ChatClient chatClient;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public InsightServiceImpl(ChatClient.Builder chatClientBuilder, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.chatClient = chatClientBuilder.build();
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public String getInsights(Integer theId) {

        Optional<Account> result = accountRepository.findById(theId);
        Account account;
        if (result.isPresent()) {
            account = result.get();
        } else {
            throw new AccountNotFoundException(theId);
        }

        // get all transactions of the account
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(theId);

        // no transactions → no need to call the AI
        if (transactions.isEmpty()) {
            return "No transactions yet.";
        }

        // calculate totals
        BigDecimal totalDeposits = BigDecimal.ZERO;
        BigDecimal totalWithdrawals = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new HashMap<String, BigDecimal>();

        for(int i = 0; i < transactions.size();i++) {
            Transaction tx = transactions.get(i);

            if (tx.getType() == TransactionType.DEPOSIT) {
                // deposit
                totalDeposits = totalDeposits.add(tx.getAmount());
            } else {
                // withdraw
                totalWithdrawals = totalWithdrawals.add(tx.getAmount());

                // clean category name
                String category;
                if (tx.getCategory() == null) {
                    category = "uncategorized";
                } else {
                    category = tx.getCategory().trim().toLowerCase();
                }

                // add amount to that category's total
                if (byCategory.containsKey(category)) {
                    BigDecimal currentTotal = byCategory.get(category);
                    BigDecimal newTotal = currentTotal.add(tx.getAmount());
                    byCategory.put(category, newTotal);
                } else {
                    byCategory.put(category, tx.getAmount());
                }
            }
        }

        // build the facts for the AI
        String facts = "Total deposits: ¥" + totalDeposits + "\n"
                + "Total withdrawals: ¥" + totalWithdrawals + "\n"
                + "Spending by category: " + byCategory + "\n"
                + "Current balance: ¥" + account.getBalance();

        // ask the AI to summarize the facts
        try {
            ChatClient.ChatClientRequestSpec request = chatClient.prompt();
            request = request.system("Summarize these bank figures for a customer in 2 sentences. Use only the numbers given.");
            request = request.user(facts);

            ChatClient.CallResponseSpec response = request.call();
            String summary = response.content();

            return summary;
        } catch (Exception e) {
            throw new InsightUnavailableException();
        }
    }
}
