# Lesson 07: Stream API

> Functional collection processing

## RxJS/TypeScript Analogy

| Java Stream | RxJS/TS Array |
|-------------|---------------|
| `.filter()` | `.filter()` |
| `.map()` | `.map()` |
| `.flatMap()` | `.flatMap()` |
| `.reduce()` | `.reduce()` |
| `.collect(toList())` | (not needed) |
| `.findFirst()` | `.find()` |
| `.anyMatch()` | `.some()` |
| `.allMatch()` | `.every()` |

**Key difference:** Java Stream is lazy and can only be consumed once.

## Basic Operations

```java
List<Wallet> wallets = repository.findAll();

// Filtering
List<Wallet> activeWallets = wallets.stream()
    .filter(w -> w.balance().compareTo(BigDecimal.ZERO) > 0)
    .toList();  // Java 16+

// Mapping
List<String> names = wallets.stream()
    .map(Wallet::name)
    .toList();

// Filter + Map + Sort
List<WalletDto> result = wallets.stream()
    .filter(w -> w.currency().equals("PLN"))
    .map(w -> new WalletDto(w.id(), w.name(), w.balance()))
    .sorted(Comparator.comparing(WalletDto::balance).reversed())
    .toList();
```

## Finding Elements

```java
// First matching (Optional!)
Optional<Wallet> found = wallets.stream()
    .filter(w -> w.name().equals("Main"))
    .findFirst();

// Does it exist?
boolean hasEmpty = wallets.stream()
    .anyMatch(w -> w.balance().equals(BigDecimal.ZERO));

// Do all match?
boolean allPositive = wallets.stream()
    .allMatch(w -> w.balance().compareTo(BigDecimal.ZERO) >= 0);
```

## Aggregations

```java
// Sum
BigDecimal total = wallets.stream()
    .map(Wallet::balance)
    .reduce(BigDecimal.ZERO, BigDecimal::add);

// Counting
long count = wallets.stream()
    .filter(w -> w.currency().equals("EUR"))
    .count();

// Max/Min
Optional<Wallet> richest = wallets.stream()
    .max(Comparator.comparing(Wallet::balance));
```

## Grouping (Collectors)

```java
import static java.util.stream.Collectors.*;

// Group by currency
Map<String, List<Wallet>> byCurrency = wallets.stream()
    .collect(groupingBy(Wallet::currency));

// Sum per currency
Map<String, BigDecimal> totalByCurrency = wallets.stream()
    .collect(groupingBy(
        Wallet::currency,
        reducing(BigDecimal.ZERO, Wallet::balance, BigDecimal::add)
    ));
```

## Practical usage in Service

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

## Exercise

**Task:** Add methods using Stream to the `NoteService`:
- `findByAuthor(String author)` - filtering
- `searchByTitle(String keyword)` - `contains()` in filter
- `getRecentNotes(int limit)` - sorting by date + limit
- `countByAuthor()` - returns `Map<String, Long>`

**Files:** `exercises/ex07-stream-api/`

## Checklist

- [ ] I can use filter + map + collect
- [ ] I understand the difference between `findFirst()` and `filter().toList()`
- [ ] I use method references (`Wallet::name`) where possible
- [ ] I know how to group and aggregate data