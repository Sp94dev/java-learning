# Ćwiczenie 02: CRUD Controller

## Zadanie

Stwórz `NoteController` z pełnym CRUD dla notatek.

### Model
```java
class Note {
    Long id;
    String title;
    String content;
}
```

### Endpointy

| Method | Path | Opis |
|--------|------|------|
| GET | `/api/notes` | Lista wszystkich |
| GET | `/api/notes/{id}` | Pojedyncza notatka |
| POST | `/api/notes` | Utwórz |
| PUT | `/api/notes/{id}` | Aktualizuj |
| DELETE | `/api/notes/{id}` | Usuń |

## Hints

- Na razie trzymaj dane w `List<Note>` wewnątrz kontrolera
- Użyj `AtomicLong` do generowania ID
- To celowo "brzydki" kod - będziemy refaktorować w lekcji 05

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
