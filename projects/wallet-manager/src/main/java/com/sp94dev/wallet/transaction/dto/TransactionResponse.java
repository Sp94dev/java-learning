package com.sp94dev.wallet.transaction.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

import com.sp94dev.wallet.transaction.Transaction;

public record TransactionResponse(
        @Schema(description = "Unique identifier", example = "101") Long id,
        @Schema(description = "ID of the related financial instrument", example = "1") Long instrumentId,
        @Schema(description = "Transaction type", example = "BUY") String type,
        @Schema(description = "Number of units", example = "10.0") Double quantity,
        @Schema(description = "Price per unit", example = "150.25") Double price,
        @Schema(description = "Transaction date", example = "2024-02-06") LocalDate date) {

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
