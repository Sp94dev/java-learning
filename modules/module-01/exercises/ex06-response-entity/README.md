# Ćwiczenie 06: ResponseEntity

## Zadanie

Zaktualizuj `InstrumentController` w projekcie `wallet-manager`, aby zwracał precyzyjne kody HTTP i nagłówki.

### Wymagane zmiany w `InstrumentController`

Zmień typy zwracane metod z `Instrument` / `List<Instrument>` na `ResponseEntity<Instrument>` / `ResponseEntity<List<Instrument>>`.

| Endpoint | Przed | Po |
|----------|-------|-----|
| POST /api/instruments | 200 OK | 201 Created + Header `Location` |
| GET /api/instruments/{id} (znaleziono) | 200 OK | 200 OK |
| GET /api/instruments/{id} (nie znaleziono) | 500/Exception | 404 Not Found |
| DELETE /api/instruments/{id} | 200 OK | 204 No Content |

### Wskazówki implementacyjne

#### 1. POST - Created (201)

```java
@PostMapping
public ResponseEntity<Instrument> createInstrument(@RequestBody Instrument instrument) {
    Instrument created = instrumentService.create(instrument);
    
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
            
    return ResponseEntity.created(location).body(created);
}
```

#### 2. GET (Single) - OK (200) lub Not Found (404)

Service może zwracać `Optional<Instrument>`.

```java
@GetMapping("/{id}")
public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
    return instrumentService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

#### 3. DELETE - No Content (204)

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
    if (!instrumentService.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    instrumentService.deleteById(id);
    return ResponseEntity.noContent().build(); // 204
}
```

### Zadanie dodatkowe

Zastosuj te same zasady dla `TransactionController`.

## Weryfikacja

Użyj pliku `rest/instrument.rest` i sprawdzaj nagłówki odpowiedzi (np. w VS Code REST Client lub curl z flagą `-i`).

```bash
curl -i -X POST http://localhost:8080/api/instruments ...
# Oczekuj: HTTP/1.1 201 
# Oczekuj: Location: http://localhost:8080/api/instruments/6
```