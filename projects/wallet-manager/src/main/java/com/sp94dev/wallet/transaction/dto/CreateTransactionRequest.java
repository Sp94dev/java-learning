package com.sp94dev.wallet.transaction.dto;

import java.time.LocalDate;

import com.sp94dev.wallet.transaction.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTransactionRequest(
        @Schema(description = "ID of the financial instrument", example = "1") Long instrumentId,
        @Schema(description = "Transaction type (BUY/SELL)", example = "BUY") TransactionType type,
        @Schema(description = "Quantity", example = "10.5") Double quantity,
        @Schema(description = "Price per unit", example = "150.0") Double price,
        @Schema(description = "Transaction date", example = "2024-02-06") LocalDate date) {
}
