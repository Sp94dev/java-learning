# Lekcja 06: Exceptions

> Checked vs Unchecked. Dlaczego `try-catch` to nie wszystko.

## Koncept

### Hierarchia wyjątków — "drzewo genealogiczne"

```
                    Throwable
                   /         \
              Error           Exception
             (NIE łap!)       /         \
            /      \    (Checked)    RuntimeException
     OutOfMemory  StackOverflow       (Unchecked)
     Error        Error              /      |        \
                               NullPointer  IllegalArgument  IndexOutOfBounds
                               Exception    Exception        Exception
```

| Typ                                        | Znaczenie                                             | Obsługa                                       |
| ------------------------------------------ | ----------------------------------------------------- | --------------------------------------------- |
| **Error**                                  | Krytyczny błąd JVM (brak pamięci, Stack overflow)     | ❌ NIE łap — nie masz co z nim zrobić         |
| **Checked Exception**                      | Przewidywalny problem (plik nie istnieje, błąd sieci) | ✅ MUSISZ obsłużyć (`try-catch` lub `throws`) |
| **Unchecked Exception** (RuntimeException) | Błąd programisty (null, zły index)                    | ⚠️ Możesz łapać, ale lepiej naprawić kod      |

### Checked Exceptions — kompilator Cię zmusza

```java
// FileNotFoundException jest Checked — kompilator WYMAGA obsługi

// ❌ nie kompiluje się!
public String readConfig() {
    BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
    // Error: Unhandled exception: java.io.FileNotFoundException
}

// ✅ Opcja 1: try-catch
public String readConfig() {
    try {
        BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
        return reader.readLine();
    } catch (FileNotFoundException e) {
        return "default-value";
    } catch (IOException e) {
        throw new RuntimeException("Nie mogę odczytać konfiguracji", e);
    }
}

// ✅ Opcja 2: deklaracja throws (= "nie ja się tym zajmuję, niech wywołujący ogarnie")
public String readConfig() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
    return reader.readLine();
}
```

**Analogia Angular/TS:** TypeScript **nie ma** checked exceptions.
Wszystkie wyjątki w JS/TS są "unchecked" — nigdy nie jesteś zmuszony do obsługi.
Java zmusza Cię — i to jest intencjonalne.

### Unchecked Exceptions (RuntimeException) — błędy programisty

```java
// Te NIE wymagają try-catch ani throws
String name = null;
name.length();               // 💥 NullPointerException

List<String> list = List.of("a", "b");
list.get(5);                 // 💥 IndexOutOfBoundsException

int x = Integer.parseInt("abc"); // 💥 NumberFormatException
```

**Filozofia:**

- Checked = "może się zdarzyć i musisz mieć plan B" (plik nie istnieje, sieć pada)
- Unchecked = "to jest bug w Twoim kodzie, napraw go" (null, zły index)

### try-catch-finally — pełna składnia

```java
try {
    // kod który może rzucić wyjątek
    String data = readFromNetwork();
} catch (SocketTimeoutException e) {
    // obsługa KONKRETNEGO wyjątku (najpierw bardziej specyficzny!)
    log.warn("Timeout: " + e.getMessage());
    return cachedData;
} catch (IOException e) {
    // obsługa OGÓLNIEJSZEGO wyjątku (po bardziej specyficznym!)
    log.error("IO błąd", e);
    throw new ServiceException("Nie mogę pobrać danych", e);
} finally {
    // ZAWSZE się wykona — niezależnie czy był wyjątek czy nie
    cleanup();
}
```

**Kolejność `catch` ma znaczenie!**

```java
// ❌ ŹLE — IOException łapie wszystko, SocketTimeoutException nigdy nie złapany
catch (IOException e) { ... }
catch (SocketTimeoutException e) { ... }  // unreachable code!

// ✅ DOBRZE — od najwęższego do najszerszego
catch (SocketTimeoutException e) { ... }
catch (IOException e) { ... }
```

### try-with-resources (powtórka z Lekcji 05)

```java
// AutoCloseable — automatyczne zamknięcie zasobów
try (var reader = new BufferedReader(new FileReader("data.txt"))) {
    return reader.readLine();
}  // reader.close() wywołane automatycznie, nawet przy wyjątku!
```

### Multi-catch (Java 7+)

```java
// Zamiast duplikować obsługę:
try {
    processData();
} catch (ParseException | ValidationException e) {
    log.error("Błąd przetwarzania: " + e.getMessage());
}
```

### Tworzenie własnych wyjątków

#### Unchecked (najczęstszy wybór w Spring Boot)

```java
// Własny wyjątek biznesowy — extends RuntimeException (unchecked)
public class InstrumentNotFoundException extends RuntimeException {
    private final String ticker;

    public InstrumentNotFoundException(String ticker) {
        super("Nie znaleziono instrumentu: " + ticker);
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }
}

// Użycie w Service:
public Instrument findByTicker(String ticker) {
    return repository.findByTicker(ticker)
            .orElseThrow(() -> new InstrumentNotFoundException(ticker));
}
```

#### Checked (rzadsze, ale ważne)

```java
// Gdy chcesz WYMUSIĆ obsługę przez wywołującego
public class InsufficientFundsException extends Exception {
    private final BigDecimal required;
    private final BigDecimal available;

    public InsufficientFundsException(BigDecimal required, BigDecimal available) {
        super("Brakuje środków. Wymagane: " + required + ", dostępne: " + available);
        this.required = required;
        this.available = available;
    }
}

// Wywołujący MUSI obsłużyć:
public void buyInstrument(String ticker, int qty) throws InsufficientFundsException {
    // ...
}
```

### Kiedy Checked, kiedy Unchecked?

| Scenariusz                   | Typ         | Dlaczego                                   |
| ---------------------------- | ----------- | ------------------------------------------ |
| Plik nie istnieje            | Checked     | Przewidywalny, wywołujący musi mieć plan B |
| Błąd sieci                   | Checked     | Przewidywalny, retry/fallback potrzebny    |
| Null argument                | Unchecked   | Bug — napraw kod                           |
| Nie znaleziono rekordu       | Unchecked   | Logika biznesowa, Spring obsłuży globalnie |
| Naruszenie reguły biznesowej | Unchecked\* | Spring @ControllerAdvice złapie globalnie  |

_\* Nowoczesny Spring Boot preferuje Unchecked + `@ControllerAdvice` (Moduł 09)_

### Anti-patterns — czego NIE robić

```java
// ❌ 1. Łapanie wszystkiego (Pokemon Exception Handling)
try {
    doSomething();
} catch (Exception e) {
    // "gotta catch 'em all" — ukrywa prawdziwy problem
}

// ❌ 2. Połykanie wyjątku (silent catch)
try {
    doSomething();
} catch (IOException e) {
    // nic — wyjątek zniknął, nikt się nie dowie
}

// ❌ 3. Rzucanie Exception zamiast konkretnego typu
public void process() throws Exception {  // zbyt ogólne!
    // ...
}

// ❌ 4. Łapanie Error
try {
    doSomething();
} catch (OutOfMemoryError e) {  // NIE łap Error!
    // co tu zrobisz? I tak nie masz pamięci 😄
}

// ✅ Dobre praktyki:
// - Łap KONKRETNE wyjątki
// - Loguj LUB rzucaj dalej, nie oba
// - Dodawaj kontekst (wrapping): new RuntimeException("kontekst", przyczyna)
// - Twórz własne wyjątki dla domeny biznesowej
```

### Pattern: Exception Wrapping (przyczyna + kontekst)

```java
try {
    return objectMapper.readValue(json, Instrument.class);
} catch (JsonProcessingException e) {
    // ✅ Dodajesz kontekst biznesowy + zachowujesz oryginalny wyjątek jako "cause"
    throw new InvalidInstrumentDataException(
        "Nie mogę sparsować instrumentu z JSON: " + json, e
    );
}
// Stack trace pokaże OBA: Twój wyjątek + oryginalny JsonProcessingException
```

## Ćwiczenie

**Zadanie 1:** Stwórz hierarchię wyjątków dla Wallet Manager:

```
WalletException (abstract, extends RuntimeException)
├── InstrumentNotFoundException
├── DuplicateInstrumentException
└── TransactionException
    ├── InsufficientFundsException
    └── InvalidTransactionException
```

**Zadanie 2:** Napisz metodę `parsePrice(String input)` która:

- Przyjmuje string np. `"150.50"`, `"abc"`, `null`
- Zwraca `BigDecimal`
- Rzuca `IllegalArgumentException` dla null
- Rzuca własny `PriceParseException` dla nieprawidłowego formatu
- Łapie `NumberFormatException` i opakowuje go

**Zadanie 3:** Napisz program który demonstruje:

1. `try-catch-finally` — pokaż że `finally` się zawsze wykona
2. Multi-catch — złap 2 różne wyjątki jednym blokiem
3. Exception wrapping — złap, dodaj kontekst, rzuć dalej

## Checklist

- [ ] Znam hierarchię: `Throwable` → `Error` / `Exception` → `RuntimeException`
- [ ] Rozumiem różnicę Checked vs Unchecked i kiedy który używać
- [ ] Wiem dlaczego NIE łapać `Error`
- [ ] Stosuję try-with-resources dla `AutoCloseable`
- [ ] Potrafię stworzyć własny wyjątek (checked i unchecked)
- [ ] Znam anti-patterns: Pokemon catching, silent catch, `throws Exception`
- [ ] Rozumiem pattern Exception Wrapping (przyczyna + kontekst)
