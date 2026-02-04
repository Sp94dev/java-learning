package com.sp94dev.wallet.instrument;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sp94dev.wallet.instrument.dto.InstrumentResponse;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping()
    ResponseEntity<List<InstrumentResponse>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String market) {
        return ResponseEntity.ok(this.instrumentService.getAllInstruments(type, currency, ticker, market)
                .stream()
                .map(InstrumentResponse::from)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentResponse> getInstrument(@PathVariable Long id) {
        return instrumentService.getInstrumentById(id)
                .map(InstrumentResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    };

    @PostMapping()
    public ResponseEntity<InstrumentResponse> createInstrument(@RequestBody Instrument instrumentBody) {
        Instrument savedInstrument = this.instrumentService.createInstrument(instrumentBody);
        InstrumentResponse instrumentResponse = InstrumentResponse.from(savedInstrument);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(instrumentResponse.id())
                .toUri();

        return ResponseEntity.created(location).body(instrumentResponse);
    };

    @PutMapping("/{id}")
    public ResponseEntity<InstrumentResponse> updateInstrument(@PathVariable Long id,
            @RequestBody Instrument instrument) {
        Instrument updatedInstrument = this.instrumentService.updateInstrument(id, instrument);
        return Optional.ofNullable(updatedInstrument)
                .map(InstrumentResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    };

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        this.instrumentService.deleteInstrument(id);
        return ResponseEntity.noContent().build();
    }
}
