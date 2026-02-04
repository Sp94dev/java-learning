package com.sp94dev.wallet.transaction.dto;

import java.time.LocalDate;

import com.sp94dev.wallet.transaction.Transaction;

public record TransactionResponse(
        Long id,
        Long instrumentId,
        String type,
        Double quantity,
        Double price,
        LocalDate date) {

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.id(),
                transaction.instrumentId(),
                transaction.type(),
                transaction.quantity(),
                transaction.price(),
                transaction.date());
    }
}
