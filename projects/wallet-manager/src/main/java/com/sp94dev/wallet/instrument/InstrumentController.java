package com.sp94dev.wallet.instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sp94dev.wallet.instrument.Instrument;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private List<Instrument> instruments = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @GetMapping()
    List<Instrument> getAll() {
        return this.instruments;
    }

    @GetMapping("/{id}")
    public Instrument getInstrument(@PathVariable Long id) {
        return this.instruments.stream()
                .filter(instrument -> instrument.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Instrument not found" + id));

    };

    @PostMapping()
    public List<Instrument> createInstrument(@RequestBody Instrument instrumentBody) {
        Instrument newInstrument = new Instrument(
                idCounter.getAndIncrement(),
                instrumentBody.ticker,
                instrumentBody.currency,
                instrumentBody.market,
                instrumentBody.type);
        this.instruments.add(newInstrument);
        return this.instruments;
    };

    @PutMapping("/{id}")
    public Instrument updateInstrument(@PathVariable Long id, @RequestBody Instrument instrument) {
        return this.instruments.stream()
                .filter(oldInstrument -> oldInstrument.id.equals(id))
                .findFirst()
                .map(oldInstrument -> {
                    Instrument newInstrument = new Instrument(
                            id,
                            instrument.ticker,
                            instrument.currency,
                            instrument.market,
                            instrument.type);
                    int index = instruments.indexOf(oldInstrument);
                    instruments.set(index, newInstrument);
                    return newInstrument;
                }).orElseThrow(() -> new NoSuchElementException("Instrument not found " + id));
    };

    @DeleteMapping("/{id}")
    public void deleteInstrument(@PathVariable Long id) {
        boolean removed = this.instruments.removeIf(Instrument -> Instrument.id.equals(id));
        if (!removed) {
            throw new NoSuchElementException("Instrument not found " + id);
        }
    }
}
