# Lekcja 03: Obsługa Danych Wejściowych

> `@PathVariable`, `@RequestParam`, `@RequestBody`

## Trzy sposoby przekazywania danych

### 1. Path Variable - część adresu URL

```java
// GET /api/users/42
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) { }

// GET /api/users/42/orders/7
@GetMapping("/users/{userId}/orders/{orderId}")
public Order getOrder(
    @PathVariable Long userId,
    @PathVariable Long orderId
) { }
```

### 2. Query Params - po znaku `?`

```java
// GET /api/users?page=1&size=10&sort=name
@GetMapping("/users")
public List<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(required = false) String sort
) { }

// GET /api/search?q=java
@GetMapping("/search")
public List<Result> search(@RequestParam String q) { }
```

### 3. Request Body - JSON w ciele żądania

```java
// POST /api/users
// Body: {"name": "John", "email": "john@example.com"}
@PostMapping("/users")
public User createUser(@RequestBody CreateUserRequest request) { }
```

## Kiedy używać czego?

| Typ | Kiedy | Przykład |
|-----|-------|----------|
| PathVariable | Identyfikator zasobu | `/users/42` |
| RequestParam | Filtrowanie, stronicowanie, opcje | `?status=active&page=2` |
| RequestBody | Tworzenie/aktualizacja złożonych obiektów | JSON payload |

## Ćwiczenie

**Zadanie:** Rozszerz `NoteController`:
- `GET /notes?author=John` - filtrowanie po autorze
- `GET /notes?search=keyword` - szukanie w tytule
- `GET /notes/{id}/comments/{commentId}` - zagnieżdżone zasoby

**Pliki:** `exercises/ex03-input-handling/`

## Checklist

- [ ] Wiem kiedy użyć PathVariable vs RequestParam
- [ ] Potrafię obsługiwać parametry opcjonalne
- [ ] Rozumiem jak Spring deserializuje JSON do obiektu