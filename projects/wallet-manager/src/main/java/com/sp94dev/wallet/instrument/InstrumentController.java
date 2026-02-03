package com.sp94dev.wallet.instrument;

import java.util.List;

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
    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping()
    List<Instrument> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String market) {
        return this.instrumentService.getAllInstruments(type, currency, ticker, market);
    }

    @GetMapping("/{id}")
    public Instrument getInstrument(@PathVariable Long id) {
        return instrumentService.getInstrumentById(id);

    };

    @PostMapping()
    public void createInstrument(@RequestBody Instrument instrumentBody) {
        this.instrumentService.createInstrument(instrumentBody);
    };

    @PutMapping("/{id}")
    public Instrument updateInstrument(@PathVariable Long id, @RequestBody Instrument instrument) {
        return this.instrumentService.updateInstrument(id, instrument);
    };

    @DeleteMapping("/{id}")
    public void deleteInstrument(@PathVariable Long id) {
        this.instrumentService.deleteInstrument(id);
    }
}
