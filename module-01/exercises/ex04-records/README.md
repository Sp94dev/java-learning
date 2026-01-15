# Ćwiczenie 04: Java Records

## Zadanie

Przepisz model notatek na Records z separacją Request/Response.

### DTOs do stworzenia

```java
// Request - tworzenie (bez id, bez createdAt)
public record CreateNoteRequest(
    String title,
    String content,
    String author
) {}

// Request - aktualizacja (tylko edytowalne pola)
public record UpdateNoteRequest(
    String title,
    String content
) {}

// Response - pełne dane
public record NoteResponse(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt
) {}
```

### Model wewnętrzny (nie DTO)

```java
// Domenowy obiekt - może być klasą lub record
public record Note(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt
) {}
```

## Hints

- Record z walidacją w compact constructor:
  ```java
  public record CreateNoteRequest(String title, String content, String author) {
      public CreateNoteRequest {
          if (title == null || title.isBlank()) {
              throw new IllegalArgumentException("Title required");
          }
      }
  }
  ```

- Mapowanie w kontrolerze:
  ```java
  Note note = new Note(
      generateId(),
      request.title(),
      request.content(),
      request.author(),
      LocalDateTime.now()
  );
  ```

## Test

Upewnij się, że JSON serializacja działa poprawnie:

```bash
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","content":"Body","author":"Me"}'

# Powinno zwrócić NoteResponse z id i createdAt
```
