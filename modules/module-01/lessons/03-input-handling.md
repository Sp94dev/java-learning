# Lesson 03: Input Handling

> `@PathVariable`, `@RequestParam`, `@RequestBody`

## Three ways to pass data

### 1. Path Variable - part of URL

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

### 2. Query Params - after `?` sign

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

### 3. Request Body - JSON in body

```java
// POST /api/users
// Body: {"name": "John", "email": "john@example.com"}
@PostMapping("/users")
public User createUser(@RequestBody CreateUserRequest request) { }
```

## When to use what?

| Type | When | Example |
|-----|-------|----------|
| PathVariable | Resource Identifier | `/users/42` |
| RequestParam | Filtering, pagination, options | `?status=active&page=2` |
| RequestBody | Creating/updating complex objects | JSON payload |

## Exercise

**Task:** Extend `NoteController`:
- `GET /notes?author=John` - filtering by author
- `GET /notes?search=keyword` - searching in title
- `GET /notes/{id}/comments/{commentId}` - nested resources

**Files:** `exercises/ex03-input-handling/`

## Checklist

- [ ] I know when to use PathVariable vs RequestParam
- [ ] I can handle optional parameters
- [ ] I understand how Spring deserializes JSON to object
