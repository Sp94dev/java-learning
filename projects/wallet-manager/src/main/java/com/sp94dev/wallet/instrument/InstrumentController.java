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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/instruments")
@Tag(name = "Instruments", description = "Financial instruments management (stocks, ETFs)")
public class InstrumentController {
        private final InstrumentService instrumentService;

        @GetMapping()
        @Operation(summary = "Get list of instruments", description = "Returns a list of all instruments with filtering and sorting options")
        ResponseEntity<List<InstrumentResponse>> getAll(
                        @RequestParam(required = false) String type,
                        @RequestParam(required = false) String currency,
                        @RequestParam(required = false) String ticker,
                        @RequestParam(required = false) String market,
                        @RequestParam(required = false) String sort,
                        @RequestParam(required = false) Number limit) {
                log.info("Get all instruments");
                return ResponseEntity.ok(
                                this.instrumentService.getAllInstruments(type, currency, ticker, market, sort, limit)
                                                .stream()
                                                .map(InstrumentResponse::from)
                                                .toList());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get instrument by ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Instrument found"),
                        @ApiResponse(responseCode = "404", description = "Instrument not found")
        })
        public ResponseEntity<InstrumentResponse> getInstrument(@PathVariable Long id) {
                log.info("Get instrument {}", id);
                return instrumentService.getInstrumentById(id)
                                .map(InstrumentResponse::from)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());

        };

        @PostMapping()
        @Operation(summary = "Add new instrument")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Instrument created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data")
        })
        public ResponseEntity<InstrumentResponse> createInstrument(@RequestBody Instrument instrumentBody) {
                log.info("Create instrument");
                Instrument savedInstrument = this.instrumentService.createInstrument(instrumentBody);
                InstrumentResponse instrumentResponse = InstrumentResponse.from(savedInstrument);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(instrumentResponse.id())
                                .toUri();
                log.info("Create instrument {}", instrumentResponse.id());
                return ResponseEntity.created(location).body(instrumentResponse);
        };

        @PutMapping("/{id}")
        @Operation(summary = "Update existing instrument")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Instrument updated successfully"),
                        @ApiResponse(responseCode = "404", description = "Instrument not found")
        })
        public ResponseEntity<InstrumentResponse> updateInstrument(@PathVariable Long id,
                        @RequestBody Instrument instrument) {
                Instrument updatedInstrument = this.instrumentService.updateInstrument(id, instrument);
                log.info("Update instrument {}", updatedInstrument.id());
                return Optional.ofNullable(updatedInstrument)
                                .map(InstrumentResponse::from)
                                .map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        };

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete instrument")
        @ApiResponse(responseCode = "204", description = "Instrument deleted successfully")
        public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
                this.instrumentService.deleteInstrument(id);
                log.info("Delete instrument {}", id);
                return ResponseEntity.noContent().build();
        }
}
