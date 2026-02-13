# Lekcja 07: Lombok

> @RequiredArgsConstructor, @Data, @Builder, @Slf4j — mniej boilerplate'u.

## Koncept

### Problem: Java jest gadatliwa

Porównaj Javę z TypeScriptem:

```typescript
// TypeScript — 3 linie
export class Instrument {
  constructor(
    public readonly ticker: string,
    public readonly name: string,
  ) {}
}
```

```java
// Java (bez Record, bez Lombok) — 25+ linii
public class Instrument {
    private final String ticker;
    private final String name;

    public Instrument(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    public String getTicker() { return ticker; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) { /* ... */ }

    @Override
    public int hashCode() { /* ... */ }

    @Override
    public String toString() { /* ... */ }
}
```

**Lombok** generuje ten boilerplate za Ciebie **w czasie kompilacji**.
Nie jest częścią Springa — to osobna biblioteka, ale Spring Boot jej intensywnie używa.

**Uwaga:** Dla prostych **DTO/modeli** preferuj **Java Records** (Module 01).
Lombok jest najbardziej przydatny dla **Beanów Spring** (Service, Repository) i klas mutowalnych.

---

### Główne adnotacje

### 1. `@RequiredArgsConstructor` — Constructor Injection bez pisania konstruktora

**To jest najważniejsza adnotacja Lomboka dla Spring DI.**

```java
// ❌ Bez Lomboka — ręczny konstruktor
@Service
public class InstrumentService {
    private final InstrumentRepository repository;
    private final PriceCalculator calculator;

    public InstrumentService(InstrumentRepository repository, PriceCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }
}

// ✅ Z Lombokiem — konstruktor wygenerowany automatycznie
@Service
@RequiredArgsConstructor  // generuje konstruktor dla WSZYSTKICH pól `final`
public class InstrumentService {
    private final InstrumentRepository repository;
    private final PriceCalculator calculator;
    // Lombok auto-generuje: public InstrumentService(repo, calculator) { ... }
}
```

**Jak to działa?**

- Lombok szuka pól `final` (i `@NonNull`)
- Generuje konstruktor z tymi polami jako parametrami
- Spring widzi jeden konstruktor → automatycznie wstrzykuje zależności

**Analogia Angular/TS:**

```typescript
// TypeScript robi to natywnie — parameter properties
export class InstrumentService {
  constructor(
    private readonly repository: InstrumentRepository,
    private readonly calculator: PriceCalculator,
  ) {}
  // ↑ TypeScript automatycznie tworzy i przypisuje pola
  //   Lombok robi to samo dla Javy
}
```

---

### 2. `@Data` — wszystko w jednym (dla MUTOWALNYCH klas)

```java
@Data  // = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
public class UserPreferences {
    private String theme;
    private String currency;
    private int pageSize;
}
// Lombok generuje: gettery, settery, toString(), equals(), hashCode(), konstruktor
```

**⚠️ Kiedy NIE używać `@Data`:**

- Na **Entity JPA** (Module 05) — `equals`/`hashCode` mogą powodować problemy z lazy loading
- Na **DTO** — użyj `record` zamiast tego
- Na **immutable** klasach — użyj `@Value` (immutable wersja `@Data`)

---

### 3. `@Builder` — Builder Pattern

```java
@Builder
public class Transaction {
    private final String ticker;
    private final int quantity;
    private final BigDecimal price;
    private final LocalDate date;
}

// Użycie:
Transaction tx = Transaction.builder()
        .ticker("AAPL")
        .quantity(10)
        .price(new BigDecimal("150.50"))
        .date(LocalDate.now())
        .build();
```

**Kiedy:** Klasa ma wiele pól (>3-4) i chcesz uniknąć konstruktora z wieloma parametrami.

**Analogia Angular/TS:** Jak tworzenie obiektu z interfejsem/typem:

```typescript
const tx: Transaction = {
  ticker: "AAPL",
  quantity: 10,
  price: 150.5,
  date: new Date(),
};
// Java nie ma object literals, więc Builder jest najbliższym odpowiednikiem
```

---

### 4. `@Slf4j` — Logger bez boilerplate'u

```java
// ❌ Bez Lomboka
@Service
public class InstrumentService {
    private static final Logger log = LoggerFactory.getLogger(InstrumentService.class);

    public Instrument findByTicker(String ticker) {
        log.info("Szukam instrumentu: {}", ticker);
        // ...
    }
}

// ✅ Z Lombokiem
@Slf4j  // generuje pole `log` automatycznie
@Service
public class InstrumentService {

    public Instrument findByTicker(String ticker) {
        log.info("Szukam instrumentu: {}", ticker);  // `log` jest dostępne!
        // ...
    }
}
```

**Uwaga:** `{}` w logu to **placeholder** — SLF4J wstawia wartość zmiennej.
Nie używaj konkatenacji (`"text" + var`) — jest wolniejsza i mniej bezpieczna.

---

### 5. Inne przydatne adnotacje (szybki przegląd)

| Adnotacja             | Co robi                              | Kiedy użyć                                 |
| --------------------- | ------------------------------------ | ------------------------------------------ |
| `@Getter`             | Generuje gettery                     | Gdy potrzebujesz TYLKO getterów            |
| `@Setter`             | Generuje settery                     | Rzadko — preferuj immutability             |
| `@ToString`           | Generuje `toString()`                | Debug, logowanie                           |
| `@NoArgsConstructor`  | Konstruktor bez argumentów           | JPA Entity (wymagane)                      |
| `@AllArgsConstructor` | Konstruktor ze wszystkimi polami     | Rzadko — zwykle `@RequiredArgsConstructor` |
| `@Value`              | Immutable `@Data` (wszystko `final`) | Value Objects                              |
| `@NonNull`            | Null check w konstruktorze/setterze  | Guard clause                               |

---

### Setup — jak dodać Lombok do projektu

W `pom.xml` (Maven):

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Spring Boot ma Lomboka w swoim BOM (Bill of Materials), więc **nie musisz podawać wersji**.

**IDE:** VS Code z Extension Pack for Java powinien automatycznie obsługiwać Lomboka.
Jeśli nie widzi wygenerowanych metod → zainstaluj rozszerzenie "Lombok Annotations Support".

---

### Praktyczny wzorzec — Spring Service z Lombokiem

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentService {
    private final InstrumentRepository repository;

    public List<Instrument> findAll() {
        log.debug("Pobieranie wszystkich instrumentów");
        return repository.findAll();
    }

    public Instrument create(Instrument instrument) {
        log.info("Tworzenie instrumentu: {}", instrument.ticker());

        repository.findByTicker(instrument.ticker())
                .ifPresent(existing -> {
                    throw new RuntimeException("Already exists: " + existing.ticker());
                });

        return repository.save(instrument);
    }
}
```

To jest **standardowy wzorzec** który będziesz pisać codziennie:

- `@Slf4j` → logger
- `@Service` → stereotyp
- `@RequiredArgsConstructor` → DI bez pisania konstruktora
- `private final` → immutable zależności

## Ćwiczenie

**Zadanie 1:** Dodaj Lomboka do Wallet Manager (`pom.xml`).

**Zadanie 2:** Zrefaktoryzuj swoje Service i Repository:

1. Zamień ręczne konstruktory na `@RequiredArgsConstructor`
2. Dodaj `@Slf4j` i zamień `System.out.println` na `log.info()`
3. Upewnij się że wszystkie zależności są `private final`

**Zadanie 3:** Sprawdź czy aplikacja nadal działa po refaktoryzacji:

- `./mvnw clean install` → kompilacja OK?
- `./mvnw spring-boot:run` → start OK?
- Przetestuj endpointy ręcznie (pliki `.rest`)

## Checklist

- [ ] Wiem co to Lombok i czemu go używamy (redukcja boilerplate'u)
- [ ] Używam `@RequiredArgsConstructor` zamiast ręcznych konstruktorów
- [ ] Używam `@Slf4j` zamiast `System.out.println`
- [ ] Wiem kiedy użyć `@Data` vs `record` (mutable vs immutable)
- [ ] Wiem że `@Builder` to odpowiednik object literals z JS/TS
- [ ] Mam Lomboka dodanego w `pom.xml`
