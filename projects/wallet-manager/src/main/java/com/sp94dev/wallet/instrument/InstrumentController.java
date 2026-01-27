package com.sp94dev.wallet.instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private List<Instrument> instruments = new ArrayList<>(List.of(
            new Instrument(1L, "AAPL", "USD", "NASDAQ", "STOCK"),
            new Instrument(2L, "GOOGL", "USD", "NASDAQ", "ETF"),
            new Instrument(3L, "TSLA", "USD", "NASDAQ", "STOCK"),
            new Instrument(4L, "AMZN", "USD", "NASDAQ", "STOCK"),
            new Instrument(5L, "MSFT", "USD", "NASDAQ", "STOCK")));
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping()
    List<Instrument> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String market) {
        return this.instruments.stream()
                .filter(instrument -> type == null || instrument.type().equals(type))
                .filter(instrument -> currency == null || instrument.currency().equals(currency))
                .filter(instrument -> ticker == null
                        || instrument.ticker().toLowerCase().contains(ticker.toLowerCase()))
                .filter(instrument -> market == null || instrument.market().equals(market))
                .toList();
    }

    @GetMapping("/{id}")
    public Instrument getInstrument(@PathVariable Long id) {
        return this.instruments.stream()
                .filter(instrument -> instrument.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Instrument not found" + id));

    };

    @PostMapping()
    public List<Instrument> createInstrument(@RequestBody Instrument instrumentBody) {
        Instrument newInstrument = new Instrument(
                idCounter.getAndIncrement(),
                instrumentBody.ticker(),
                instrumentBody.currency(),
                instrumentBody.market(),
                instrumentBody.type());
        this.instruments.add(newInstrument);
        return this.instruments;
    };

    @PutMapping("/{id}")
    public Instrument updateInstrument(@PathVariable Long id, @RequestBody Instrument instrument) {
        return this.instruments.stream()
                .filter(oldInstrument -> oldInstrument.id().equals(id))
                .findFirst()
                .map(oldInstrument -> {
                    Instrument newInstrument = new Instrument(
                            id,
                            instrument.ticker(),
                            instrument.currency(),
                            instrument.market(),
                            instrument.type());
                    int index = instruments.indexOf(oldInstrument);
                    instruments.set(index, newInstrument);
                    return newInstrument;
                }).orElseThrow(() -> new NoSuchElementException("Instrument not found " + id));
    };

    @DeleteMapping("/{id}")
    public void deleteInstrument(@PathVariable Long id) {
        boolean removed = this.instruments.removeIf(Instrument -> Instrument.id().equals(id));
        if (!removed) {
            throw new NoSuchElementException("Instrument not found " + id);
        }
    }
}
