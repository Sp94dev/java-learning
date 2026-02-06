# Exercise 05: Service Layer

## Task

Refactor the `InstrumentController` in the `projects/wallet-manager` project into a layered architecture.
The goal is to separate business logic from the HTTP layer.

### Target Structure (in `com.sp94dev.wallet` package)

```
instrument/
├── InstrumentController.java      # HTTP routing only
├── InstrumentService.java         # Business logic
├── InMemoryInstrumentRepository.java  # Data storage
└── Instrument.java                # Domain model (Record)
```

(Optionally, you can create a `dto` sub-package for Request/Response if you want to separate the API model from the domain model).

### 1. Create `InMemoryInstrumentRepository`

This class will take over the responsibility for the `List<Instrument>` (or `Map`) and ID generation.

```java
@Repository
public class InMemoryInstrumentRepository {
    
    private final Map<Long, Instrument> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Move data initialization logic (AAPL, GOOGL...) here from the controller
    
    public Instrument save(Instrument instrument) {
        // Logic for assigning ID if null
        // Save to map
    }
    
    public Optional<Instrument> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    public List<Instrument> findAll() {
        return List.copyOf(storage.values());
    }
    
    public void deleteById(Long id) {
        storage.remove(id);
    }
    
    // ... other methods needed for filtering
}
```

### 2. Create `InstrumentService`

The service acts as an intermediary between the Controller and the Repository. This is where the filtering logic goes.

```java
@Service
public class InstrumentService {
    
    private final InMemoryInstrumentRepository repository;
    
    public InstrumentService(InMemoryInstrumentRepository repository) {
        this.repository = repository;
    }
    
    public List<Instrument> findAll(String type, String currency, String ticker, String market) {
        // Move the stream().filter(...) logic here from the controller
        // Fetch all from repository and filter
    }
    
    public Instrument create(Instrument instrument) {
        // Validation? (later)
        return repository.save(instrument);
    }
    
    // ... getById, update, delete
}
```

### 3. Slim down `InstrumentController`

The controller should only delegate tasks to the service.

```java
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    
    private final InstrumentService instrumentService;
    
    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }
    
    @GetMapping
    public List<Instrument> getAll(
        @RequestParam(required = false) String type,
        // ...
    ) {
        return instrumentService.findAll(type, currency, ticker, market);
    }
    
    // ... remaining methods delegate to the service
}
```

## Extra Task (Transaction)

Perform a similar refactoring for the `TransactionController`.

### Checklist

- [ ] `InstrumentController` no longer has a `List` or `AtomicLong` field.
- [ ] Filtering logic is in `InstrumentService`.
- [ ] Data is stored in `InMemoryInstrumentRepository`.
- [ ] The application still works the same (check `rest/instrument.rest`).
