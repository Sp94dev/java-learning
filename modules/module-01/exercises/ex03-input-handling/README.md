# Exercise 03: Input Handling

## Task

Extend `NoteController` to handle different types of input.

### New endpoints

```
GET /api/notes?author=John         → filter by author
GET /api/notes?search=keyword      → search in title
GET /api/notes?page=0&size=10      → pagination
GET /api/notes/{id}/comments/{cid} → nested resources
```

### Updated model

```java
class Note {
    Long id;
    String title;
    String content;
    String author;        // new
    List<Comment> comments; // new
}

class Comment {
    Long id;
    String text;
}
```

## Hints

- `@RequestParam(required = false)` for optional
- `@RequestParam(defaultValue = "0")` for default values
- You can use multiple `@PathVariable` in one method

## Test

```bash
# Filtering
curl "http://localhost:8080/api/notes?author=John"

# Searching
curl "http://localhost:8080/api/notes?search=java"

# Pagination
curl "http://localhost:8080/api/notes?page=0&size=5"

# Nested
curl http://localhost:8080/api/notes/1/comments/2
```
