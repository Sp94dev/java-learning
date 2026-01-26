# Ćwiczenie 03: Obsługa Danych Wejściowych

## Zadanie

Rozszerz `NoteController` o obsługę różnych typów danych wejściowych.

### Nowe endpointy

```
GET /api/notes?author=John         → filtrowanie po autorze
GET /api/notes?search=keyword      → szukanie w tytule
GET /api/notes?page=0&size=10      → stronicowanie
GET /api/notes/{id}/comments/{cid} → zagnieżdżone zasoby
```

### Zaktualizowany model

```java
class Note {
    Long id;
    String title;
    String content;
    String author;        // nowe pole
    List<Comment> comments; // nowe pole
}

class Comment {
    Long id;
    String text;
}
```

## Wskazówki

- `@RequestParam(required = false)` dla opcjonalnych
- `@RequestParam(defaultValue = "0")` dla wartości domyślnych
- Możesz użyć wielu `@PathVariable` w jednej metodzie

## Testowanie

```bash
# Filtrowanie
curl "http://localhost:8080/api/notes?author=John"

# Wyszukiwanie
curl "http://localhost:8080/api/notes?search=java"

# Stronicowanie
curl "http://localhost:8080/api/notes?page=0&size=5"

# Zagnieżdżone
curl http://localhost:8080/api/notes/1/comments/2
```