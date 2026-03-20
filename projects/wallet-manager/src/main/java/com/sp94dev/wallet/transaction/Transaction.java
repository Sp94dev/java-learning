package com.sp94dev.wallet.transaction;

import java.time.LocalDate;

public record Transaction(
        Long id,
        Long instrumentId,
        TransactionType type,
        Double quantity,
        Double price,
        LocalDate date) {
}
