# Module 01 Cheatsheet

## Controller Annotations

```java
@RestController                 // REST API (zwraca JSON)
@RequestMapping("/api/v1/x")    // Prefix dla wszystkich endpointów
@GetMapping                     // GET request
@PostMapping                    // POST request  
@PutMapping("/{id}")            // PUT request z path variable
@DeleteMapping("/{id}")         // DELETE request
```

## Input Handling

```java
@PathVariable Long id           // /items/123 → id = 123
@RequestParam String name       // ?name=John → name = "John"
@RequestParam(defaultValue = "0") int page
@RequestParam(required = false) String filter
@RequestBody CreateRequest req  // JSON body → obiekt
```

## ResponseEntity

```java
ResponseEntity.ok(body)                    // 200 + body
ResponseEntity.ok().build()                // 200 bez body
ResponseEntity.created(uri).body(body)     // 201 + Location + body
ResponseEntity.noContent().build()         // 204
ResponseEntity.notFound().build()          // 404
ResponseEntity.badRequest().body(error)    // 400 + body
```

## Records

```java
// Definicja
public record WalletDto(Long id, String name, BigDecimal balance) {}

// Użycie
WalletDto dto = new WalletDto(1L, "Main", BigDecimal.ZERO);
dto.id()      // getter (bez "get" prefix!)
dto.name()

// Z walidacją
public record CreateRequest(String name) {
    public CreateRequest {
        Objects.requireNonNull(name, "Name required");
    }
}
```

## Stream API

```java
// Filter + Map + Collect
list.stream()
    .filter(x -> x.active())
    .map(x -> new Dto(x.id(), x.name()))
    .toList();

// Find
list.stream()
    .filter(x -> x.id().equals(id))
    .findFirst();  // Optional<T>

// Check
list.stream().anyMatch(x -> x.name().equals("test"));
list.stream().allMatch(x -> x.balance() > 0);

// Aggregate
list.stream()
    .map(Wallet::balance)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Group
list.stream()
    .collect(Collectors.groupingBy(Wallet::currency));
```

## DI Pattern

```java
@Service
public class MyService {
    private final MyRepository repo;  // final!
    
    public MyService(MyRepository repo) {  // constructor injection
        this.repo = repo;
    }
}
```

## REST Conventions

```
✅ GET    /wallets        - list
✅ GET    /wallets/123    - single
✅ POST   /wallets        - create
✅ PUT    /wallets/123    - update
✅ DELETE /wallets/123    - delete

❌ GET /getWallet
❌ POST /createWallet  
❌ DELETE /deleteWallet/123
```