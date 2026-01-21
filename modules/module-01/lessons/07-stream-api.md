# Lekcja 07: Stream API

> Funkcyjne przetwarzanie kolekcji

## Analogia do RxJS/TypeScript

| Java Stream | RxJS/TS Array |
|-------------|---------------|
| `.filter()` | `.filter()` |
| `.map()` | `.map()` |
| `.flatMap()` | `.flatMap()` |
| `.reduce()` | `.reduce()` |
| `.collect(toList())` | (nie potrzebne) |
| `.findFirst()` | `.find()` |
| `.anyMatch()` | `.some()` |
| `.allMatch()` | `.every()` |

**Kluczowa różnica:** Java Stream jest lazy i jednorazowy.

## Podstawowe operacje

```java
List<Wallet> wallets = repository.findAll();

// Filtrowanie
List<Wallet> activeWallets = wallets.stream()
    .filter(w -> w.balance().compareTo(BigDecimal.ZERO) > 0)
    .toList();  // Java 16+

// Mapowanie
List<String> names = wallets.stream()
    .map(Wallet::name)
    .toList();

// Filtr + Map + Sort
List<WalletDto> result = wallets.stream()
    .filter(w -> w.currency().equals("PLN"))
    .map(w -> new WalletDto(w.id(), w.name(), w.balance()))
    .sorted(Comparator.comparing(WalletDto::balance).reversed())
    .toList();
```

## Znajdowanie elementów

```java
// Pierwszy pasujący (Optional!)
Optional<Wallet> found = wallets.stream()
    .filter(w -> w.name().equals("Main"))
    .findFirst();

// Czy istnieje?
boolean hasEmpty = wallets.stream()
    .anyMatch(w -> w.balance().equals(BigDecimal.ZERO));

// Czy wszystkie spełniają?
boolean allPositive = wallets.stream()
    .allMatch(w -> w.balance().compareTo(BigDecimal.ZERO) >= 0);
```

## Agregacje

```java
// Suma
BigDecimal total = wallets.stream()
    .map(Wallet::balance)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Zliczanie
long count = wallets.stream()
    .filter(w -> w.currency().equals("EUR"))
    .count();

// Max/Min
Optional<Wallet> richest = wallets.stream()
    .max(Comparator.comparing(Wallet::balance));
```

## Grupowanie (Collectors)

```java
import static java.util.stream.Collectors.*;

// Grupuj po walucie
Map<String, List<Wallet>> byCurrency = wallets.stream()
    .collect(groupingBy(Wallet::currency));

// Suma per waluta
Map<String, BigDecimal> totalByCurrency = wallets.stream()
    .collect(groupingBy(
        Wallet::currency,
        reducing(BigDecimal.ZERO, Wallet::balance, BigDecimal::add)
    ));
```

## Praktyczne użycie w Service

```java
@Service
public class WalletService {
    
    public List<WalletResponse> findByMinBalance(BigDecimal minBalance) {
        return repository.findAll().stream()
            .filter(w -> w.balance().compareTo(minBalance) >= 0)
            .map(this::toResponse)
            .sorted(Comparator.comparing(WalletResponse::balance).reversed())
            .toList();
    }

    public Optional<WalletResponse> findByName(String name) {
        return repository.findAll().stream()
            .filter(w -> w.name().equalsIgnoreCase(name))
            .findFirst()
            .map(this::toResponse);
    }

    public BigDecimal getTotalBalance() {
        return repository.findAll().stream()
            .map(Wallet::balance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private WalletResponse toResponse(Wallet w) {
        return new WalletResponse(w.id(), w.name(), w.balance());
    }
}
```

## Ćwiczenie

**Zadanie:** Dodaj do `NoteService` metody używające Stream:
- `findByAuthor(String author)` - filtrowanie
- `searchByTitle(String keyword)` - `contains()` w filter
- `getRecentNotes(int limit)` - sortowanie po dacie + limit
- `countByAuthor()` - zwraca `Map<String, Long>`

**Pliki:** `exercises/ex07-stream-api/`

## Checklist

- [ ] Potrafię użyć filter + map + collect
- [ ] Rozumiem różnicę między `findFirst()` a `filter().toList()`
- [ ] Używam method references (`Wallet::name`) gdzie możliwe
- [ ] Wiem jak grupować i agregować dane
