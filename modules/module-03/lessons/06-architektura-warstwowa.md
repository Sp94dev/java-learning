# Lekcja 06: Architektura Warstwowa

> Controller → Service → Repository. Thin Controller. Zależności tylko W DÓŁ.

## Koncept

### Trzy warstwy — podział odpowiedzialności

```
┌──────────────────────────────────────────────────────┐
│  HTTP REQUEST                                        │
│         ↓                                            │
│  ┌────────────────────────────────────────────────┐  │
│  │  @RestController (WARSTWA WEB)                 │  │
│  │  • Odbiera HTTP request                        │  │
│  │  • Parsuje input (path vars, body, params)     │  │
│  │  • Deleguje do Service                         │  │
│  │  • Buduje HTTP response (status code, body)    │  │
│  │  • NIE MA logiki biznesowej!                   │  │
│  └────────────────┬───────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  @Service (WARSTWA BIZNESOWA)                  │  │
│  │  • Logika, reguły, obliczenia                  │  │
│  │  • Orkiestruje operacje na danych              │  │
│  │  • Walidacja biznesowa                         │  │
│  │  • Może wołać WIELE Repository                 │  │
│  └────────────────┬───────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  @Repository (WARSTWA DANYCH)                  │  │
│  │  • CRUD (Create, Read, Update, Delete)         │  │
│  │  • Komunikacja z bazą / storage                │  │
│  │  • NIE MA logiki biznesowej!                   │  │
│  └────────────────────────────────────────────────┘  │
│                   ↓                                  │
│  ┌────────────────────────────────────────────────┐  │
│  │  DATABASE / IN-MEMORY STORE                    │  │
│  └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

### Zasada: Zależności TYLKO w dół

```
Controller  →  Service  →  Repository
    ↓              ↓            ↓
  widzi          widzi        widzi
  Service       Repository   Database

Controller NIE widzi Repository (nie importuje, nie wstrzykuje)
Repository NIE widzi Controller ani Service
```

**Analogia Angular:**

```
Component → Service → HttpClient
     ↓          ↓
   widzi      widzi
   Service   HttpClient

Component NIE woła HttpClient bezpośrednio
```

---

### Thin Controller — "Cienki Kontroler"

**Zasada:** Controller robi **3 rzeczy** i nic więcej:

1. **Przyjmij** — rozpakuj HTTP request (parametry, body, headers)
2. **Deleguj** — wywołaj Service
3. **Odpowiedz** — zwróć HTTP response (status code, body)

#### ❌ Fat Controller (Źले)

```java
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentRepository repository;  // ❌ Controller wie o Repository!

    @PostMapping
    public ResponseEntity<Instrument> create(@RequestBody Instrument instrument) {
        // ❌ Logika biznesowa W KONTROLERZE
        if (instrument.ticker() == null || instrument.ticker().isBlank()) {
            throw new IllegalArgumentException("Ticker required");
        }

        // ❌ Sprawdzanie duplikatów w kontrolerze
        if (repository.findByTicker(instrument.ticker()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // ❌ Bezpośredni dostęp do Repository
        Instrument saved = repository.save(instrument);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

#### ✅ Thin Controller (DOBRZE)

```java
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentService service;  // ✅ Zna TYLKO Service

    public InstrumentController(InstrumentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Instrument> create(@RequestBody Instrument instrument) {
        // ✅ 1. Przyjmij (Spring robi to automatycznie — @RequestBody)
        // ✅ 2. Deleguj
        Instrument saved = service.create(instrument);
        // ✅ 3. Odpowiedz
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
        // ✅ Logika biznesowa tu gdzie powinna być
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

### Dlaczego to ważne?

| Aspekt       | Fat Controller                | Thin Controller                           |
| ------------ | ----------------------------- | ----------------------------------------- |
| Testowanie   | Trudne (HTTP + logika razem)  | Łatwe (Service testujemy osobno)          |
| Reużywalność | Logika zamknięta w HTTP layer | Service można użyć z CLI, Scheduler, etc. |
| Czytelność   | Kontroler 200+ linii          | Kontroler 30 linii                        |
| Zmiana DB    | Musisz zmienić Controller     | Zmieniasz tylko Repository                |

**Analogia Angular:** Identycznie! W Angularze:

- Komponent NIE woła `HttpClient` bezpośrednio
- Komponent deleguje do Service
- Service enkapsuluje logikę i komunikację z API

---

### Kiedy Service może wołać wiele Repository?

```java
@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final InstrumentRepository instrumentRepo;  // ✅ OK — Service może wołać wiele Repo

    public TransactionService(
            TransactionRepository transactionRepo,
            InstrumentRepository instrumentRepo
    ) {
        this.transactionRepo = transactionRepo;
        this.instrumentRepo = instrumentRepo;
    }

    public Transaction create(String ticker, int qty, BigDecimal price) {
        // Sprawdź czy instrument istnieje (w innym Repo)
        Instrument instrument = instrumentRepo.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Instrument not found: " + ticker));

        // Zapisz transakcję (w swoim Repo)
        Transaction tx = new Transaction(UUID.randomUUID(), instrument, qty, price, LocalDate.now());
        return transactionRepo.save(tx);
    }
}
```

### Kiedy Service może wołać inny Service?

```java
@Service
public class PortfolioService {
    private final TransactionService transactionService;  // ✅ Service → Service jest OK
    private final InstrumentService instrumentService;

    // Orkiestruje logikę wyższego poziomu, delegując do wyspecjalizowanych serwisów
}
```

**⚠️ Unikaj cyklicznych zależności:**

```
// ❌ Service A wstrzykuje Service B, a B wstrzykuje A
// Spring rzuci: BeanCurrentlyInCreationException
ServiceA → ServiceB → ServiceA → 💥 cykl!
```

## Ćwiczenie

**Zadanie:** Refaktoryzacja Wallet Manager!

To jest **kluczowe ćwiczenie** tego modułu. Upewnij się że Twój kod spełnia te kryteria:

1. **Controller:**
   - Ma TYLKO `@RestController` + `@RequestMapping`
   - Wstrzykuje TYLKO Service (nie Repository!)
   - Metody: przyjmij → deleguj → odpowiedz (max 3-5 linii)
   - NIE ma logiki `if/else` biznesowej

2. **Service:**
   - Ma `@Service`
   - Wstrzykuje Repository (Constructor Injection, `final`)
   - Zawiera CAŁĄ logikę biznesową
   - Może wstrzykiwać wiele Repository

3. **Repository:**
   - Ma `@Repository`
   - Tylko CRUD: `findAll()`, `findById()`, `save()`, `delete()`
   - NIE ma logiki biznesowej
   - `ConcurrentHashMap` jako storage (do modułu 05)

**Sprawdź linijka po linijce:**

- Czy Controller importuje cokolwiek z warstwy Repository? → ❌ Usuń
- Czy Controller ma `new` gdziekolwiek? → ❌ Usuń
- Czy Service ma adnotację `@Service`? → ✅ Dodaj
- Czy Repository ma adnotację `@Repository`? → ✅ Dodaj

## Checklist

- [ ] Controller deleguje do Service — nie ma logiki biznesowej
- [ ] Service zawiera logikę — używa Repository
- [ ] Repository robi tylko CRUD — nie ma logiki
- [ ] Zależności idą TYLKO w dół (Controller → Service → Repository)
- [ ] Controller NIE importuje Repository
- [ ] Wszystkie zależności są `final` i wstrzyknięte przez konstruktor
