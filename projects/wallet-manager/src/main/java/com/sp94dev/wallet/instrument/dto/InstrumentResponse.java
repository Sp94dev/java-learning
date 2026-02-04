package com.sp94dev.wallet.instrument.dto;

import com.sp94dev.wallet.instrument.Instrument;

public record InstrumentResponse(Long id,
        String ticker,
        String currency,
        String market,
        String type) {
    public static InstrumentResponse from(Instrument instrument) {
        return new InstrumentResponse(
                instrument.id(),
                instrument.ticker(),
                instrument.currency(),
                instrument.market(),
                instrument.type());
    }
}
