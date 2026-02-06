package com.sp94dev.wallet.instrument.dto;

import com.sp94dev.wallet.instrument.Instrument;

import io.swagger.v3.oas.annotations.media.Schema;

public record InstrumentResponse(
        @Schema(description = "Unique identifier", example = "1") Long id,
        @Schema(description = "Ticker symbol", example = "AAPL") String ticker,
        @Schema(description = "Currency of the instrument", example = "USD") String currency,
        @Schema(description = "Market where instrument is traded", example = "NASDAQ") String market,
        @Schema(description = "Type of the instrument", example = "STOCK") String type) {
    public static InstrumentResponse from(Instrument instrument) {
        return new InstrumentResponse(
                instrument.id(),
                instrument.ticker(),
                instrument.currency(),
                instrument.market(),
                instrument.type());
    }
}
