# Ćwiczenie 06: ResponseEntity

## Zadanie

Dodaj właściwe kody HTTP do wszystkich endpointów.

### Wymagane zmiany

| Endpoint | Przed | Po |
|----------|-------|-----|
| POST /notes | 200 | 201 + Location |
| GET /notes/{id} (found) | 200 | 200 |
| GET /notes/{id} (not found) | 200 + null | 404 |
| PUT /notes/{id} (found) | 200 | 200 |
| PUT /notes/{id} (not found) | 200 + null | 404 |
| DELETE /notes/{id} (found) | 200 | 204 |
| DELETE /notes/{id} (not found) | 200 | 404 |

### Zaktualizowany NoteService

Service zwraca `Optional` dla pojedynczych wyników:

```java
public Optional<NoteResponse> findById(Long id) {
    return repository.findById(id)
        .map(this::toResponse);
}

public Optional<NoteResponse> update(Long id, UpdateNoteRequest request) {
    return repository.findById(id)
        .map(existing -> {
            Note updated = new Note(
                existing.id(),
                request.title(),
                request.content(),
                existing.author(),
                existing.createdAt()
            );
            return toResponse(repository.save(updated));
        });
}
```

### Zaktualizowany NoteController

```java
@PostMapping
public ResponseEntity<NoteResponse> create(@RequestBody CreateNoteRequest request) {
    NoteResponse created = noteService.create(request);
    URI location = URI.create("/api/notes/" + created.id());
    return ResponseEntity.created(location).body(created);
}

@GetMapping("/{id}")
public ResponseEntity<NoteResponse> getById(@PathVariable Long id) {
    return noteService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!noteService.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    noteService.deleteById(id);
    return ResponseEntity.noContent().build();
}
```

## Test

```bash
# POST → 201
curl -i -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","content":"Body","author":"Me"}'
# Sprawdź: HTTP 201, Location header

# GET existing → 200
curl -i http://localhost:8080/api/notes/1
# Sprawdź: HTTP 200

# GET missing → 404
curl -i http://localhost:8080/api/notes/999
# Sprawdź: HTTP 404

# DELETE existing → 204
curl -i -X DELETE http://localhost:8080/api/notes/1
# Sprawdź: HTTP 204, brak body
```
