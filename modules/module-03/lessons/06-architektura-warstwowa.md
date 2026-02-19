# Lesson 06: Layered Architecture

> Controller → Service → Repository. Thin Controller. Dependencies only go DOWN.

## Concept

### Three layers — separation of responsibilities

```
┌──────────────────────────────────────────────────────┐
│  HTTP REQUEST                                        │
│         ↓                                            │
│  ┌────────────────────────────────────────────────┐  │
│  │  @RestController (WEB LAYER)                   │  │
│  │  • Receives HTTP request                       │  │
│  │  • Parses input (path vars, body, params)      │  │
│  │  • Delegates to Service                        │  │
│  │  • Builds HTTP response (status code, body)    │  │
│  │  • NO business logic!                          │  │
│  └────────────────┬───────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  @Service (BUSINESS LAYER)                     │  │
│  │  • Logic, rules, calculations                  │  │
│  │  • Orchestrates data operations                │  │
│  │  • Business validation                         │  │
│  │  • Can call MULTIPLE Repositories              │  │
│  └────────────────┬───────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  @Repository (DATA LAYER)                      │  │
│  │  • CRUD (Create, Read, Update, Delete)         │  │
│  │  • Communication with database / storage       │  │
│  │  • NO business logic!                          │  │
│  └────────────────────────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  DATABASE / IN-MEMORY STORE                    │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

### Rule: Dependencies ONLY go down

```
Controller  →  Service  →  Repository
    ↓              ↓            ↓
  sees           sees         sees
  Service       Repository   Database

Controller does NOT see Repository (doesn't import, doesn't inject)
Repository does NOT see Controller or Service
```

**Angular Analogy:**

```
Component → Service → HttpClient
     ↓          ↓
   sees       sees
   Service   HttpClient

Component does NOT call HttpClient directly
```

---

### Thin Controller

**Rule:** Controller does **3 things** and nothing more:

1. **Accept** — unpack the HTTP request (parameters, body, headers)
2. **Delegate** — call the Service
3. **Respond** — return the HTTP response (status code, body)

#### ❌ Fat Controller (BAD)

```java
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentRepository repository;  // ❌ Controller knows about Repository!

    @PostMapping
    public ResponseEntity<Instrument> create(@RequestBody Instrument instrument) {
        // ❌ Business logic IN THE CONTROLLER
        if (instrument.ticker() == null || instrument.ticker().isBlank()) {
            throw new IllegalArgumentException("Ticker required");
        }

        // ❌ Duplicate checking in the controller
        if (repository.findByTicker(instrument.ticker()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // ❌ Direct Repository access
        Instrument saved = repository.save(instrument);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

#### ✅ Thin Controller (GOOD)

```java
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentService service;  // ✅ Knows ONLY the Service

    public InstrumentController(InstrumentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Instrument> create(@RequestBody Instrument instrument) {
        // ✅ 1. Accept (Spring does this automatically — @RequestBody)
        // ✅ 2. Delegate
        Instrument saved = service.create(instrument);
        // ✅ 3. Respond
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public Instrument create(Instrument instrument) {
        // ✅ Business logic where it belongs
        if (instrument.ticker() == null || instrument.ticker().isBlank()) {
            throw new IllegalArgumentException("Ticker required");
        }

        repository.findByTicker(instrument.ticker())
                .ifPresent(existing -> {
                    throw new RuntimeException("Instrument already exists: " + existing.ticker());
                });

        return repository.save(instrument);
    }
}
```

### Why does this matter?

| Aspect      | Fat Controller               | Thin Controller                          |
| ----------- | ---------------------------- | ---------------------------------------- |
| Testing     | Hard (HTTP + logic together) | Easy (Service tested separately)         |
| Reusability | Logic locked in HTTP layer   | Service usable from CLI, Scheduler, etc. |
| Readability | Controller 200+ lines        | Controller 30 lines                      |
| DB change   | Must change Controller       | Only change Repository                   |

**Angular Analogy:** Identical! In Angular:

- Component does NOT call `HttpClient` directly
- Component delegates to Service
- Service encapsulates logic and API communication

---

### When can a Service call multiple Repositories?

```java
@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final InstrumentRepository instrumentRepo;  // ✅ OK — Service can call multiple Repos

    public TransactionService(
            TransactionRepository transactionRepo,
            InstrumentRepository instrumentRepo
    ) {
        this.transactionRepo = transactionRepo;
        this.instrumentRepo = instrumentRepo;
    }

    public Transaction create(String ticker, int qty, BigDecimal price) {
        // Check if instrument exists (in another Repo)
        Instrument instrument = instrumentRepo.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Instrument not found: " + ticker));

        // Save transaction (in its own Repo)
        Transaction tx = new Transaction(UUID.randomUUID(), instrument, qty, price, LocalDate.now());
        return transactionRepo.save(tx);
    }
}
```

### When can a Service call another Service?

```java
@Service
public class PortfolioService {
    private final TransactionService transactionService;  // ✅ Service → Service is OK
    private final InstrumentService instrumentService;

    // Orchestrates higher-level logic, delegating to specialized services
}
```

**⚠️ Avoid circular dependencies:**

```
// ❌ Service A injects Service B, and B injects A
// Spring will throw: BeanCurrentlyInCreationException
ServiceA → ServiceB → ServiceA → 💥 cycle!
```

## Exercise

**Task:** Wallet Manager Refactoring!

This is the **key exercise** of this module. Make sure your code meets these criteria:

1. **Controller:**
   - Has ONLY `@RestController` + `@RequestMapping`
   - Injects ONLY Service (not Repository!)
   - Methods: accept → delegate → respond (max 3-5 lines)
   - Has NO business `if/else` logic

2. **Service:**
   - Has `@Service`
   - Injects Repository (Constructor Injection, `final`)
   - Contains ALL business logic
   - Can inject multiple Repositories

3. **Repository:**
   - Has `@Repository`
   - Only CRUD: `findAll()`, `findById()`, `save()`, `delete()`
   - Has NO business logic
   - `ConcurrentHashMap` as storage (until Module 05)

**Check line by line:**

- Does Controller import anything from the Repository layer? → ❌ Remove
- Does Controller have `new` anywhere? → ❌ Remove
- Does Service have the `@Service` annotation? → ✅ Add
- Does Repository have the `@Repository` annotation? → ✅ Add

## Checklist

- [x] Controller delegates to Service — no business logic
- [x] Service contains logic — uses Repository
- [x] Repository does only CRUD — no logic
- [x] Dependencies go ONLY downward (Controller → Service → Repository)
- [x] Controller does NOT import Repository
- [x] All dependencies are `final` and injected via constructor
