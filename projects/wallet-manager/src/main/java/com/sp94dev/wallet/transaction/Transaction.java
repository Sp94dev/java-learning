package com.sp94dev.wallet.transaction;

public record Transaction(
        Long id,
        Long instrumentId,
        String type,
        Double quantity,
        Double price) {

}