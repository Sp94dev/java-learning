package com.sp94dev.wallet.instrument;

import java.util.List;
import java.util.Optional;

public interface InstrumentRepository {
    Instrument save(Instrument instrument);

    Optional<Instrument> findById(Long id);

    List<Instrument> findAll();

    void deleteById(Long id);

    Instrument update(Long id, Instrument instrument);

    List<Instrument> findByCriteria(String type, String currency, String ticker, String market);
}
