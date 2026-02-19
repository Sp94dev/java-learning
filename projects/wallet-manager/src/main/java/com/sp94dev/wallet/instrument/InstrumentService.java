package com.sp94dev.wallet.instrument;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstrumentService {
    private final InMemoryInstrumentRepository inMemoryInstrumentRepository;

    private static final Map<String, Function<Instrument, String>> SORT_FIELDS = Map.of(
            "ticker", Instrument::ticker,
            "market", Instrument::market,
            "type", Instrument::type,
            "currency", Instrument::currency);

    public Optional<Instrument> getInstrumentById(Long id) {
        return this.inMemoryInstrumentRepository.findById(id);
    }

    public List<Instrument> getAllInstruments(String type, String currency, String ticker, String market, String sort,
            Number limit) {
        return this.inMemoryInstrumentRepository.findByCriteria(type, currency, ticker, market).stream()
                .sorted(Comparator.comparing(SORT_FIELDS.get(sort)))
                .limit(limit != null ? limit.longValue() : Long.MAX_VALUE)
                .toList();
    }

    public Instrument createInstrument(Instrument instrument) {
        return this.inMemoryInstrumentRepository.save(instrument);
    }

    public Instrument updateInstrument(Long id, Instrument instrument) {
        return this.inMemoryInstrumentRepository.update(id, instrument);
    }

    public void deleteInstrument(Long id) {
        this.inMemoryInstrumentRepository.deleteById(id);
    }
}
