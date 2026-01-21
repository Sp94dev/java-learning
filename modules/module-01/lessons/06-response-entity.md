# Lekcja 06: ResponseEntity

> Kody HTTP, nagłówki, kontrola odpowiedzi

## Domyślne zachowanie

```java
@GetMapping("/{id}")
public Wallet getById(@PathVariable Long id) {
    return walletService.findById(id);  // zawsze 200 OK
}
```

Problem: Co gdy wallet nie istnieje? Zwróci `null` i 200 OK.

## ResponseEntity - pełna kontrola

```java
@GetMapping("/{id}")
public ResponseEntity<WalletResponse> getById(@PathVariable Long id) {
    return walletService.findById(id)
        .map(ResponseEntity::ok)                    // 200 OK
        .orElse(ResponseEntity.notFound().build()); // 404 Not Found
}
```

## Popularne kody HTTP

| Kod | Stała | Kiedy |
|-----|-------|-------|
| 200 | `ok()` | Sukces z danymi |
| 201 | `created(uri)` | Zasób utworzony |
| 204 | `noContent()` | Sukces bez danych (DELETE) |
| 400 | `badRequest()` | Błędne dane wejściowe |
| 404 | `notFound()` | Zasób nie istnieje |
| 409 | `status(CONFLICT)` | Konflikt (duplikat) |

## Patterns

### POST - zwróć 201 + Location header

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

### GET single - 200 lub 404

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

### PUT - 200 lub 404

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

## Ćwiczenie

**Zadanie:** Dodaj właściwe kody HTTP do `NoteController`:
- POST → 201 + Location
- GET by id → 200 lub 404
- DELETE → 204 lub 404
- PUT → 200 lub 404

**Pliki:** `exercises/ex06-response-entity/`

## Checklist

- [ ] Wiem kiedy użyć 200, 201, 204, 404
- [ ] Potrafię zwrócić Location header przy POST
- [ ] Używam `Optional` + `map` zamiast if-else
