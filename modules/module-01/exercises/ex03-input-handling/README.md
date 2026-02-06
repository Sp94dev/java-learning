# Exercise 03: Input Data Handling

## Task

Extend the `NoteController` to handle various types of input data.

### New Endpoints

```
GET /api/notes?author=John         → filter by author
GET /api/notes?search=keyword      → search in title
GET /api/notes?page=0&size=10      → pagination
GET /api/notes/{id}/comments/{cid} → nested resources
```

### Updated Model

```java
class Note {
    Long id;
    String title;
    String content;
    String author;        // new field
    List<Comment> comments; // new field
}

class Comment {
    Long id;
    String text;
}
```

## Hints

- Use `@RequestParam(required = false)` for optional parameters.
- Use `@RequestParam(defaultValue = "0")` for default values.
- You can use multiple `@PathVariable` in a single method.

## Testing

```bash
# Filtering
curl "http://localhost:8080/api/notes?author=John"

# Searching
curl "http://localhost:8080/api/notes?search=java"

# Pagination
curl "http://localhost:8080/api/notes?page=0&size=5"

# Nested Resources
curl http://localhost:8080/api/notes/1/comments/2
```
