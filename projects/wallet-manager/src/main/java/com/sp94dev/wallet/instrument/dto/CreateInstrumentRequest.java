package com.sp94dev.wallet.instrument.dto;

public record CreateInstrumentRequest(
        String ticker,
        String currency,
        String market,
        String type) {

}
