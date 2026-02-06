# Exercise 02: CRUD Controller

## Task

Create a `NoteController` with full CRUD operations for notes.

### Model
```java
class Note {
    Long id;
    String title;
    String content;
}
```

### Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/notes` | List all notes |
| GET | `/api/notes/{id}` | Single note details |
| POST | `/api/notes` | Create a new note |
| PUT | `/api/notes/{id}` | Update an existing note |
| DELETE | `/api/notes/{id}` | Delete a note |

## Hints

- For now, keep the data in a `List<Note>` inside the controller.
- Use `AtomicLong` for generating IDs.
- This is intentionally "messy" code - we will refactor it in Lesson 05.

## Test

```bash
# Create
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"First","content":"Hello"}'

# Get all
curl http://localhost:8080/api/notes

# Get one
curl http://localhost:8080/api/notes/1

# Update
curl -X PUT http://localhost:8080/api/notes/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Updated","content":"World"}'

# Delete
curl -X DELETE http://localhost:8080/api/notes/1
```