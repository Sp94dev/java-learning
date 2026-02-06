# Exercise 04: Java Records

## Task

Refactor the note model to use Records with Request/Response separation.

### DTOs to Create

```java
// Request - creating (no id, no createdAt)
public record CreateNoteRequest(
    String title,
    String content,
    String author
) {}

// Request - updating (only editable fields)
public record UpdateNoteRequest(
    String title,
    String content
) {}

// Response - full data
public record NoteResponse(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt
) {}
```

### Internal Model (not a DTO)

```java
// Domain object - can be a class or a record
public record Note(
    Long id,
    String title,
    String content,
    String author,
    LocalDateTime createdAt
) {}
```

## Hints

- Record with validation in a compact constructor:
  ```java
  public record CreateNoteRequest(String title, String content, String author) {
      public CreateNoteRequest {
          if (title == null || title.isBlank()) {
              throw new IllegalArgumentException("Title required");
          }
      }
  }
  ```

- Mapping in the controller:
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

Ensure that JSON serialization works correctly:

```bash
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","content":"Body","author":"Me"}'

# Should return NoteResponse with id and createdAt
```