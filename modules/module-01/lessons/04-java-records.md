# Lesson 04: Java Records

> Immutable DTOs instead of POJOs

## The problem with classic POJO

```java
// 30+ lines of boilerplate
public class WalletDto {
    private Long id;
    private String name;
    private BigDecimal balance;

    public WalletDto() {}
    
    public WalletDto(Long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... more getters and setters
    // ... equals(), hashCode(), toString()
}
```

## Record - Java 16+ (standard in Java 17)

```java
// 1 line - everything generated
public record WalletDto(
    Long id,
    String name,
    BigDecimal balance
) {}
```

**You get automatically:**
- Constructor
- Getters (no `get` prefix - `wallet.name()` not `wallet.getName()`)
- `equals()`, `hashCode()`, `toString()`
- Immutability (all fields are `final`)

## Customizing Records

```java
public record CreateWalletRequest(
    String name,
    String currency
) {
    // Compact constructor - validation
    public CreateWalletRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        currency = currency != null ? currency.toUpperCase() : "PLN";
    }

    // Additional methods
    public boolean isDefaultCurrency() {
        return "PLN".equals(currency);
    }
}
```

## Pattern: Request vs Response DTO

```java
// Input - without ID (server generates it)
public record CreateWalletRequest(String name, String currency) {}

// Output - full data
public record WalletResponse(Long id, String name, BigDecimal balance, String currency) {}

// Update - only modifiable fields
public record UpdateWalletRequest(String name) {}
```

## Why DTO instead of Entity?

```
Controller → DTO → Service → Entity → Repository
           ↑                        ↓
        API boundary            DB boundary
```

- **Security**: you don't expose your internal data structure
- **Flexibility**: API and database can evolve independently
- **Control**: you choose exactly what you return

## Exercise

**Task:** Refactor `NoteController` to use Records:
- `CreateNoteRequest` - title, content
- `UpdateNoteRequest` - content only
- `NoteResponse` - id, title, content, createdAt

**Files:** `exercises/ex04-records/`

## Checklist

- [ ] I understand the difference between a Record and a class
- [ ] I know how to add validation in a compact constructor
- [ ] I apply the Request/Response DTO pattern