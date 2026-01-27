package com.sp94dev.wallet.instrument;

public record Instrument(Long id,
        String ticker,
        String currency,
        String market,
        String type) {

}
