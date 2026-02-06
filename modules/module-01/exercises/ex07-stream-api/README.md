# Exercise 07: Stream API

## Task

Expand the service layer in `wallet-manager` with more advanced data operations using the Java Stream API.

### 1. Expanding `InstrumentService`

Add methods that allow for sorting and limiting results.

**New parameters in the controller:**
`GET /api/instruments?sort=ticker&limit=5`

**Implementation in the service:**
Use `sorted(Comparator...)` and `limit(long)`.

### 2. Transaction Statistics (`TransactionService`)

Create a new endpoint `GET /api/transactions/stats` that returns a portfolio summary.

**Create a DTO record `TransactionStats`:**
```java
public record TransactionStats(
    int totalTransactions,
    Map<String, Long> countByType, // e.g., BUY: 5, SELL: 2
    Map<Long, Double> totalValueByInstrument // e.g., InstrumentID: Sum (price * quantity)
) {}
```

**Implement the logic in `TransactionService`:**

```java
public TransactionStats getStats() {
    List<Transaction> all = repository.findAll();

    // 1. Total count
    int total = all.size();

    // 2. Grouping by type (BUY/SELL)
    Map<String, Long> byType = all.stream()
        .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

    // 3. Value per instrument (simplified: sum of price * quantity)
    Map<Long, Double> valueByInstrument = all.stream()
        .collect(Collectors.groupingBy(
            Transaction::instrumentId,
            Collectors.summingDouble(t -> t.price() * t.quantity())
        ));

    return new TransactionStats(total, byType, valueByInstrument);
}
```

### 3. "Full Text" Instrument Search (Simplified)

Add endpoint `GET /api/instruments/search?query=app`

Search the list of instruments by checking if `query` appears in `ticker`, `market`, OR `type` (case-insensitive).

```java
public List<Instrument> search(String query) {
    String lowerQuery = query.toLowerCase();
    return repository.findAll().stream()
        .filter(i -> 
            i.ticker().toLowerCase().contains(lowerQuery) ||
            i.market().toLowerCase().contains(lowerQuery) ||
            i.type().toLowerCase().contains(lowerQuery)
        )
        .toList();
}
```

## Checklist

- [ ] The `/api/transactions/stats` endpoint returns aggregated data.
- [ ] The `/api/instruments` endpoint supports simple sorting.
- [ ] The code uses `Collectors.groupingBy`, `summingDouble`, etc.
- [ ] The logic is testable (it's in the service, not in the controller).
