package com.gab.springboot.bankingapi.dto;

import com.gab.springboot.bankingapi.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull TransactionType type,
        @NotNull @Positive BigDecimal amount,
        String description,
        String category
) {}
