package com.sp94dev.wallet.transaction;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public record Transaction(
        @Schema(description = "Unique identifier", accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "ID of the financial instrument", example = "1") Long instrumentId,
        @Schema(description = "Transaction type (BUY/SELL)", example = "BUY") String type,
        @Schema(description = "Quantity", example = "10.5") Double quantity,
        @Schema(description = "Price per unit", example = "150.0") Double price,
        @Schema(description = "Transaction date", example = "2024-02-06") LocalDate date) {

}
