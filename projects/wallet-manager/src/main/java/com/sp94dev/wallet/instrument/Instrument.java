package com.sp94dev.wallet.instrument;

public class Instrument {
    public Long id;
    public String ticker;
    public String currency;
    public String market;
    public String type;

    public Instrument() {
    }

    public Instrument(
            Long id,
            String ticker,
            String currency,
            String market,
            String type) {
        this.id = id;
        this.ticker = ticker;
        this.currency = currency;
        this.market = market;
        this.type = type;
    }

    void setId(Long id) {
        this.id = id;
    };

    void setTicker(String ticker) {
        this.ticker = ticker;
    };

    void setCurrency(String currency) {
        this.currency = currency;
    };

    void setMarket(String market) {
        this.market = market;
    };

    void setType(String type) {
        this.type = type;
    };

    Long getId() {
        return this.id;
    };

    String getTicker() {
        return this.ticker;
    };

    String getCurrency() {
        return this.currency;
    };

    String getMarket() {
        return this.market;
    };

    String getType() {
        return this.type;
    };

}
