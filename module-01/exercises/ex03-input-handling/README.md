# Ćwiczenie 03: Input Handling

## Zadanie

Rozbuduj `NoteController` o obsługę różnych typów inputu.

### Nowe endpointy

```
GET /api/notes?author=John         → filtruj po autorze
GET /api/notes?search=keyword      → szukaj w tytule
GET /api/notes?page=0&size=10      → paginacja
GET /api/notes/{id}/comments/{cid} → zagnieżdżone zasoby
```

### Zaktualizowany model

```java
class Note {
    Long id;
    String title;
    String content;
    String author;        // nowe
    List<Comment> comments; // nowe
}

class Comment {
    Long id;
    String text;
}
```

## Hints

- `@RequestParam(required = false)` dla opcjonalnych
- `@RequestParam(defaultValue = "0")` dla wartości domyślnych
- Możesz użyć wielu `@PathVariable` w jednej metodzie

## Test

```bash
# Filtrowanie
curl "http://localhost:8080/api/notes?author=John"

# Szukanie
curl "http://localhost:8080/api/notes?search=java"

# Paginacja
curl "http://localhost:8080/api/notes?page=0&size=5"

# Zagnieżdżone
curl http://localhost:8080/api/notes/1/comments/2
```
