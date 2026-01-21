# Ćwiczenie 07: Stream API

## Zadanie

Dodaj do `NoteService` metody wykorzystujące Stream API.

### Nowe endpointy

```
GET /api/notes?author=John          → filtruj po autorze
GET /api/notes?search=java          → szukaj w tytule/content
GET /api/notes/recent?limit=5       → ostatnie N notatek
GET /api/notes/stats                → statystyki
```

### NoteService - nowe metody

```java
public List<NoteResponse> findByAuthor(String author) {
    return repository.findAll().stream()
        .filter(note -> note.author().equalsIgnoreCase(author))
        .map(this::toResponse)
        .toList();
}

public List<NoteResponse> search(String keyword) {
    String lowerKeyword = keyword.toLowerCase();
    return repository.findAll().stream()
        .filter(note -> 
            note.title().toLowerCase().contains(lowerKeyword) ||
            note.content().toLowerCase().contains(lowerKeyword))
        .map(this::toResponse)
        .toList();
}

public List<NoteResponse> findRecent(int limit) {
    return repository.findAll().stream()
        .sorted(Comparator.comparing(Note::createdAt).reversed())
        .limit(limit)
        .map(this::toResponse)
        .toList();
}

public NoteStatsResponse getStats() {
    List<Note> all = repository.findAll();
    
    Map<String, Long> countByAuthor = all.stream()
        .collect(Collectors.groupingBy(Note::author, Collectors.counting()));
    
    return new NoteStatsResponse(
        all.size(),
        countByAuthor
    );
}
```

### Nowe DTO

```java
public record NoteStatsResponse(
    int totalNotes,
    Map<String, Long> notesByAuthor
) {}
```

### NoteController - rozbudowany GET

```java
@GetMapping
public List<NoteResponse> getAll(
    @RequestParam(required = false) String author,
    @RequestParam(required = false) String search
) {
    if (author != null) {
        return noteService.findByAuthor(author);
    }
    if (search != null) {
        return noteService.search(search);
    }
    return noteService.findAll();
}

@GetMapping("/recent")
public List<NoteResponse> getRecent(
    @RequestParam(defaultValue = "10") int limit
) {
    return noteService.findRecent(limit);
}

@GetMapping("/stats")
public NoteStatsResponse getStats() {
    return noteService.getStats();
}
```

## Test

```bash
# Filtrowanie
curl "http://localhost:8080/api/notes?author=John"

# Szukanie
curl "http://localhost:8080/api/notes?search=java"

# Ostatnie
curl "http://localhost:8080/api/notes/recent?limit=3"

# Statystyki
curl http://localhost:8080/api/notes/stats
```

## Bonus: Kombinowanie filtrów

```java
public List<NoteResponse> findAll(String author, String search) {
    Stream<Note> stream = repository.findAll().stream();
    
    if (author != null) {
        stream = stream.filter(n -> n.author().equalsIgnoreCase(author));
    }
    if (search != null) {
        String kw = search.toLowerCase();
        stream = stream.filter(n -> 
            n.title().toLowerCase().contains(kw) ||
            n.content().toLowerCase().contains(kw));
    }
    
    return stream.map(this::toResponse).toList();
}
```
