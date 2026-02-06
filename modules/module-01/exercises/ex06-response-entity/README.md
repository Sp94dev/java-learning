# Exercise 06: ResponseEntity

## Task

Update the `InstrumentController` in the `wallet-manager` project to return precise HTTP status codes and headers.

### Required Changes in `InstrumentController`

Change the return types of methods from `Instrument` / `List<Instrument>` to `ResponseEntity<Instrument>` / `ResponseEntity<List<Instrument>>`.

| Endpoint | Before | After |
|----------|-------|-----|
| POST /api/instruments | 200 OK | 201 Created + `Location` Header |
| GET /api/instruments/{id} (found) | 200 OK | 200 OK |
| GET /api/instruments/{id} (not found) | 500/Exception | 404 Not Found |
| DELETE /api/instruments/{id} | 200 OK | 204 No Content |

### Implementation Hints

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

#### 2. GET (Single) - OK (200) or Not Found (404)

The Service should return `Optional<Instrument>`.

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

### Extra Task

Apply the same principles to the `TransactionController`.

## Verification

Use the `rest/instrument.rest` file and check the response headers (e.g., in VS Code REST Client or curl with the `-i` flag).

```bash
curl -i -X POST http://localhost:8080/api/instruments ...
# Expect: HTTP/1.1 201 
# Expect: Location: http://localhost:8080/api/instruments/6
```
