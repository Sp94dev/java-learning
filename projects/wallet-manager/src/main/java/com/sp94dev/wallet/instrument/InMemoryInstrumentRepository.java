package com.sp94dev.wallet.instrument;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryInstrumentRepository {
    private final Map<Long, Instrument> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public InMemoryInstrumentRepository() {
        storage.put(1L, new Instrument(1L, "AAPL", "USD", "NASDAQ", "STOCK"));
        storage.put(2L, new Instrument(2L, "GOOGL", "USD", "NASDAQ", "ETF"));
        storage.put(3L, new Instrument(3L, "TSLA", "USD", "NASDAQ", "STOCK"));
        storage.put(4L, new Instrument(4L, "AMZN", "USD", "NASDAQ", "STOCK"));
        storage.put(5L, new Instrument(5L, "MSFT", "USD", "NASDAQ", "STOCK"));
    }

    public Instrument save(Instrument instrument) {
        Long id = idCounter.getAndIncrement();
        Instrument newInstrument = new Instrument(
                id,
                instrument.ticker(),
                instrument.currency(),
                instrument.market(),
                instrument.type());
        storage.put(id, newInstrument);
        return newInstrument;
    }

    public Optional<Instrument> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Instrument> findAll() {
        return List.copyOf(storage.values());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public Instrument update(Long id, Instrument instrument) {
        Instrument updatedInstrument = new Instrument(
                id,
                instrument.ticker(),
                instrument.currency(),
                instrument.market(),
                instrument.type());
        storage.put(id, updatedInstrument);
        return updatedInstrument;
    }

    public List<Instrument> findByCriteria(
            String type,
            String currency,
            String ticker,
            String market) {
        return storage.values().stream()
                .filter(instrument -> type == null || instrument.type().equals(type))
                .filter(instrument -> currency == null || instrument.currency().equals(currency))
                .filter(instrument -> ticker == null
                        || instrument.ticker().toLowerCase().contains(ticker.toLowerCase()))
                .filter(instrument -> market == null || instrument.market().equals(market))
                .toList();
    }

}