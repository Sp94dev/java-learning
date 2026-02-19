# Lesson 07: Lombok

> @RequiredArgsConstructor, @Data, @Builder, @Slf4j — less boilerplate.

## Concept

### Problem: Java is verbose

Compare Java with TypeScript:

```typescript
// TypeScript — 3 lines
export class Instrument {
  constructor(
    public readonly ticker: string,
    public readonly name: string,
  ) {}
}
```

```java
// Java (without Record, without Lombok) — 25+ lines
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

**Lombok** generates this boilerplate for you **at compile time**.
It's not part of Spring — it's a separate library, but Spring Boot uses it extensively.

**Note:** For simple **DTOs/models** prefer **Java Records** (Module 01).
Lombok is most useful for **Spring Beans** (Service, Repository) and mutable classes.

---

### Main Annotations

### 1. `@RequiredArgsConstructor` — Constructor Injection without writing a constructor

**This is the most important Lombok annotation for Spring DI.**

```java
// ❌ Without Lombok — manual constructor
@Service
public class InstrumentService {
    private final InstrumentRepository repository;
    private final PriceCalculator calculator;

    public InstrumentService(InstrumentRepository repository, PriceCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }
}

// ✅ With Lombok — constructor auto-generated
@Service
@RequiredArgsConstructor  // generates constructor for ALL `final` fields
public class InstrumentService {
    private final InstrumentRepository repository;
    private final PriceCalculator calculator;
    // Lombok auto-generates: public InstrumentService(repo, calculator) { ... }
}
```

**How does it work?**

- Lombok looks for `final` fields (and `@NonNull`)
- Generates a constructor with those fields as parameters
- Spring sees one constructor → automatically injects dependencies

**Angular/TS Analogy:**

```typescript
// TypeScript does this natively — parameter properties
export class InstrumentService {
  constructor(
    private readonly repository: InstrumentRepository,
    private readonly calculator: PriceCalculator,
  ) {}
  // ↑ TypeScript automatically creates and assigns fields
  //   Lombok does the same for Java
}
```

---

### 2. `@Data` — everything in one (for MUTABLE classes)

```java
@Data  // = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
public class UserPreferences {
    private String theme;
    private String currency;
    private int pageSize;
}
// Lombok generates: getters, setters, toString(), equals(), hashCode(), constructor
```

**⚠️ When NOT to use `@Data`:**

- On **JPA Entities** (Module 05) — `equals`/`hashCode` can cause problems with lazy loading
- On **DTOs** — use `record` instead
- On **immutable** classes — use `@Value` (immutable version of `@Data`)

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

// Usage:
Transaction tx = Transaction.builder()
        .ticker("AAPL")
        .quantity(10)
        .price(new BigDecimal("150.50"))
        .date(LocalDate.now())
        .build();
```

**When:** Class has many fields (>3-4) and you want to avoid a constructor with many parameters.

**Angular/TS Analogy:** Like creating an object with an interface/type:

```typescript
const tx: Transaction = {
  ticker: "AAPL",
  quantity: 10,
  price: 150.5,
  date: new Date(),
};
// Java doesn't have object literals, so Builder is the closest equivalent
```

---

### 4. `@Slf4j` — Logger without boilerplate

```java
// ❌ Without Lombok
@Service
public class InstrumentService {
    private static final Logger log = LoggerFactory.getLogger(InstrumentService.class);

    public Instrument findByTicker(String ticker) {
        log.info("Looking for instrument: {}", ticker);
        // ...
    }
}

// ✅ With Lombok
@Slf4j  // generates the `log` field automatically
@Service
public class InstrumentService {

    public Instrument findByTicker(String ticker) {
        log.info("Looking for instrument: {}", ticker);  // `log` is available!
        // ...
    }
}
```

**Note:** `{}` in the log is a **placeholder** — SLF4J inserts the variable's value.
Don't use concatenation (`"text" + var`) — it's slower and less safe.

---

### 5. Other useful annotations (quick overview)

| Annotation            | What it does                           | When to use                                 |
| --------------------- | -------------------------------------- | ------------------------------------------- |
| `@Getter`             | Generates getters                      | When you need ONLY getters                  |
| `@Setter`             | Generates setters                      | Rarely — prefer immutability                |
| `@ToString`           | Generates `toString()`                 | Debug, logging                              |
| `@NoArgsConstructor`  | No-args constructor                    | JPA Entity (required)                       |
| `@AllArgsConstructor` | Constructor with all fields            | Rarely — usually `@RequiredArgsConstructor` |
| `@Value`              | Immutable `@Data` (everything `final`) | Value Objects                               |
| `@NonNull`            | Null check in constructor/setter       | Guard clause                                |

---

### Setup — how to add Lombok to the project

In `pom.xml` (Maven):

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Spring Boot has Lombok in its BOM (Bill of Materials), so **you don't need to specify a version**.

**IDE:** VS Code with Extension Pack for Java should automatically support Lombok.
If it doesn't see generated methods → install the "Lombok Annotations Support" extension.

---

### Practical pattern — Spring Service with Lombok

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentService {
    private final InstrumentRepository repository;

    public List<Instrument> findAll() {
        log.debug("Fetching all instruments");
        return repository.findAll();
    }

    public Instrument create(Instrument instrument) {
        log.info("Creating instrument: {}", instrument.ticker());

        repository.findByTicker(instrument.ticker())
                .ifPresent(existing -> {
                    throw new RuntimeException("Already exists: " + existing.ticker());
                });

        return repository.save(instrument);
    }
}
```

This is the **standard pattern** you'll be writing every day:

- `@Slf4j` → logger
- `@Service` → stereotype
- `@RequiredArgsConstructor` → DI without writing a constructor
- `private final` → immutable dependencies

## Exercise

**Task 1:** Add Lombok to Wallet Manager (`pom.xml`).

**Task 2:** Refactor your Services and Repositories:

1. Replace manual constructors with `@RequiredArgsConstructor`
2. Add `@Slf4j` and replace `System.out.println` with `log.info()`
3. Make sure all dependencies are `private final`

**Task 3:** Check if the application still works after refactoring:

- `./mvnw clean install` → compilation OK?
- `./mvnw spring-boot:run` → startup OK?
- Test endpoints manually (`.rest` files)

## Checklist

- [x] I know what Lombok is and why we use it (boilerplate reduction)
- [x] I use `@RequiredArgsConstructor` instead of manual constructors
- [x] I use `@Slf4j` instead of `System.out.println`
- [x] I know when to use `@Data` vs `record` (mutable vs immutable)
- [x] I know that `@Builder` is the equivalent of object literals from JS/TS
- [x] I have Lombok added in `pom.xml`
