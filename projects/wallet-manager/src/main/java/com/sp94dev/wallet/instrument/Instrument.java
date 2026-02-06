package com.sp94dev.wallet.instrument;

import io.swagger.v3.oas.annotations.media.Schema;

public record Instrument(
        @Schema(description = "Unique identifier", accessMode = Schema.AccessMode.READ_ONLY) Long id,
        @Schema(description = "Ticker symbol", example = "AAPL") String ticker,
        @Schema(description = "Currency", example = "USD") String currency,
        @Schema(description = "Market", example = "NASDAQ") String market,
        @Schema(description = "Instrument type", example = "STOCK") String type) {

}
