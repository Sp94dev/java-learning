# Ćwiczenie 07: Stream API

## Zadanie

Rozbuduj warstwę serwisową w `wallet-manager` o bardziej zaawansowane operacje na danych z użyciem Java Stream API.

### 1. Rozbudowa `InstrumentService`

Dodaj metody pozwalające na sortowanie i limitowanie wyników.

**Nowe parametry w kontrolerze:**
`GET /api/instruments?sort=ticker&limit=5`

**Implementacja w serwisie:**
Wykorzystaj `sorted(Comparator...)` i `limit(long)`.

### 2. Statystyki Transakcji (`TransactionService`)

Stwórz nowy endpoint `GET /api/transactions/stats`, który zwróci podsumowanie portfela.

**Stwórz rekord DTO `TransactionStats`:**
```java
public record TransactionStats(
    int totalTransactions,
    Map<String, Long> countByType, // BUY: 5, SELL: 2
    Map<Long, Double> totalValueByInstrument // InstrumentID: Suma (price * quantity)
) {}
```

**Zaimplementuj logikę w `TransactionService`:**

```java
public TransactionStats getStats() {
    List<Transaction> all = repository.findAll();

    // 1. Liczba wszystkich
    int total = all.size();

    // 2. Grupowanie po typie (BUY/SELL)
    Map<String, Long> byType = all.stream()
        .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

    // 3. Wartość per instrument (zakładając uproszczenie: suma price * quantity)
    Map<Long, Double> valueByInstrument = all.stream()
        .collect(Collectors.groupingBy(
            Transaction::instrumentId,
            Collectors.summingDouble(t -> t.price() * t.quantity())
        ));

    return new TransactionStats(total, byType, valueByInstrument);
}
```

### 3. Wyszukiwanie instrumentów "full text" (uproszczone)

Dodaj endpoint `GET /api/instruments/search?query=app`

Przeszukaj listę instrumentów sprawdzając czy `query` występuje w `ticker`, `market` LUB `type` (case insensitive).

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

- [ ] Endpoint `/api/transactions/stats` zwraca zagregowane dane.
- [ ] Endpoint `/api/instruments` obsługuje proste sortowanie.
- [ ] Kod wykorzystuje `Collectors.groupingBy`, `summingDouble`, etc.
- [ ] Logika jest testowalna (jest w serwisie, nie w kontrolerze).