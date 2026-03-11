package com.sp94dev.wallet.transaction.dto;

import java.util.Map;

import com.sp94dev.wallet.transaction.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionStats(
        @Schema(description = "Total number of transactions recorded", example = "50") int totalTransactions,
        @Schema(description = "Count of transactions grouped by type (e.g., BUY, SELL)") Map<TransactionType, Long> countByType,
        @Schema(description = "Total monetary value of transactions grouped by instrument ID") Map<Long, Double> totalValueByInstrument) {
}
