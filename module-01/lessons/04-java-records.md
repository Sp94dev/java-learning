# Lekcja 04: Java Records

> Niemutowalne DTO zamiast POJO

## Problem z klasycznym POJO

```java
// 30+ linii boilerplate
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
    // ... kolejne gettery i settery
    // ... equals(), hashCode(), toString()
}
```

## Record - Java 16+ (produkcyjny od Java 17)

```java
// 1 linia - wszystko wygenerowane
public record WalletDto(
    Long id,
    String name,
    BigDecimal balance
) {}
```

**Dostajesz automatycznie:**
- Konstruktor
- Gettery (bez prefixu `get` - `wallet.name()` nie `wallet.getName()`)
- `equals()`, `hashCode()`, `toString()`
- Niemutowalność (wszystkie pola `final`)

## Customizacja Records

```java
public record CreateWalletRequest(
    String name,
    String currency
) {
    // Compact constructor - walidacja
    public CreateWalletRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name required");
        }
        currency = currency != null ? currency.toUpperCase() : "PLN";
    }

    // Dodatkowe metody
    public boolean isDefaultCurrency() {
        return "PLN".equals(currency);
    }
}
```

## Pattern: Request vs Response DTO

```java
// Wejście - bez ID (serwer generuje)
public record CreateWalletRequest(String name, String currency) {}

// Wyjście - pełne dane
public record WalletResponse(Long id, String name, BigDecimal balance, String currency) {}

// Aktualizacja - tylko modyfikowalne pola
public record UpdateWalletRequest(String name) {}
```

## Dlaczego DTO a nie Entity?

```
Controller → DTO → Service → Entity → Repository
           ↑                        ↓
        granica API            granica bazy
```

- **Bezpieczeństwo**: nie eksponujesz wewnętrznej struktury
- **Elastyczność**: API i baza mogą ewoluować niezależnie
- **Kontrola**: wybierasz co zwracasz

## Ćwiczenie

**Zadanie:** Przepisz `NoteController` na Records:
- `CreateNoteRequest` - tytuł, treść
- `UpdateNoteRequest` - tylko treść
- `NoteResponse` - id, tytuł, treść, createdAt

**Pliki:** `exercises/ex04-records/`

## Checklist

- [ ] Rozumiem różnicę między Record a klasą
- [ ] Wiem jak dodać walidację w compact constructor
- [ ] Stosuję pattern Request/Response DTO
