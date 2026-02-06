# Lesson 06: ResponseEntity

> HTTP codes, headers, response control

## Default Behavior

```java
@GetMapping("/{id}")
public Wallet getById(@PathVariable Long id) {
    return walletService.findById(id);  // always 200 OK
}
```

Problem: What if the wallet doesn't exist? It returns `null` and 200 OK.

## ResponseEntity - Full Control

```java
@GetMapping("/{id}")
public ResponseEntity<WalletResponse> getById(@PathVariable Long id) {
    return walletService.findById(id)
        .map(ResponseEntity::ok)                    // 200 OK
        .orElse(ResponseEntity.notFound().build()); // 404 Not Found
}
```

## Popular HTTP Status Codes

| Code | Method | When |
|-----|-------|-------|
| 200 | `ok()` | Success with data |
| 201 | `created(uri)` | Resource created |
| 204 | `noContent()` | Success without data (DELETE) |
| 400 | `badRequest()` | Invalid input data |
| 404 | `notFound()` | Resource not found |
| 409 | `status(CONFLICT)` | Conflict (duplicate) |

## Patterns

### POST - return 201 + Location header

```java
@PostMapping
public ResponseEntity<WalletResponse> create(@RequestBody CreateWalletRequest req) {
    WalletResponse created = walletService.create(req);
    
    URI location = URI.create("/api/v1/wallets/" + created.id());
    
    return ResponseEntity
        .created(location)     // 201 + Location header
        .body(created);
}
```

### GET single - 200 or 404

```java
@GetMapping("/{id}")
public ResponseEntity<WalletResponse> getById(@PathVariable Long id) {
    return walletService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

### DELETE - 204 No Content

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!walletService.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    walletService.deleteById(id);
    return ResponseEntity.noContent().build();
}
```

### PUT - 200 or 404

```java
@PutMapping("/{id}")
public ResponseEntity<WalletResponse> update(
    @PathVariable Long id,
    @RequestBody UpdateWalletRequest req
) {
    return walletService.update(id, req)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
}
```

## Custom Headers

```java
@GetMapping
public ResponseEntity<List<WalletResponse>> getAll() {
    List<WalletResponse> wallets = walletService.findAll();
    
    return ResponseEntity.ok()
        .header("X-Total-Count", String.valueOf(wallets.size()))
        .body(wallets);
}
```

## Exercise

**Task:** Add proper HTTP codes to the `NoteController`:
- POST → 201 + Location
- GET by id → 200 or 404
- DELETE → 204 or 404
- PUT → 200 or 404

**Files:** `exercises/ex06-response-entity/`

## Checklist

- [ ] I know when to use 200, 201, 204, 404
- [ ] I can return a Location header on POST
- [ ] I use `Optional` + `map` instead of if-else