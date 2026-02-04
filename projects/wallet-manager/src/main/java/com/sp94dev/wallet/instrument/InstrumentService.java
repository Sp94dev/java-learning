package com.sp94dev.wallet.instrument;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class InstrumentService {
    private final InMemoryInstrumentRepository inMemoryInstrumentRepository;

    public InstrumentService(InMemoryInstrumentRepository inMemoryInstrumentRepository) {
        this.inMemoryInstrumentRepository = inMemoryInstrumentRepository;
    }

    public Optional<Instrument> getInstrumentById(Long id) {
        return this.inMemoryInstrumentRepository.findById(id);
    }

    public List<Instrument> getAllInstruments(String type, String currency, String ticker, String market) {
        return this.inMemoryInstrumentRepository.findByCriteria(type, currency, ticker, market);
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
