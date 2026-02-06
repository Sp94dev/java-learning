# Lesson 05: Service Layer

> Logic separation, `@Service`, Constructor Injection

## Angular Analogy

| Spring | Angular |
|--------|---------|
| `@Service` | `@Injectable()` |
| `@RestController` | Component |
| Constructor Injection | Constructor Injection |
| `@Autowired` (old style) | `inject()` (older versions) |

## Why Service Layer?

**Fat Controller (Bad):**
```java
@RestController
public class WalletController {
    private final List<Wallet> wallets = new ArrayList<>();

    @PostMapping
    public Wallet create(@RequestBody CreateRequest req) {
        // validation
        // business logic
        // mapping
        // saving
        // notifications
        // logging
        // return
    }
}
```

**Thin Controller + Service (Good):**
```java
@RestController
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public WalletResponse create(@RequestBody CreateRequest req) {
        return walletService.create(req);
    }
}
```

## Constructor Injection (Recommended)

```java
@Service
public class WalletService {
    
    private final WalletRepository repository;
    private final NotificationService notifications;

    // Spring automatically injects - no @Autowired needed
    public WalletService(
        WalletRepository repository,
        NotificationService notifications
    ) {
        this.repository = repository;
        this.notifications = notifications;
    }
}
```

**Why constructor injection?**
- Explicit dependencies
- Easier testing (you can pass a mock)
- Immutability (`final` fields)
- Fail-fast if dependencies are missing

## Application Layers

```
┌─────────────────────────────────────┐
│  Controller (HTTP)                  │  ← thin, only routing
├─────────────────────────────────────┤
│  Service (Business Logic)           │  ← logic, validation
├─────────────────────────────────────┤
│  Repository (Data Access)           │  ← data access
└─────────────────────────────────────┘
```

## In-Memory Repository (For Now)

```java
@Repository
public class InMemoryWalletRepository {
    
    private final Map<Long, Wallet> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Wallet save(Wallet wallet) {
        if (wallet.id() == null) {
            wallet = new Wallet(idGenerator.getAndIncrement(), wallet.name());
        }
        storage.put(wallet.id(), wallet);
        return wallet;
    }

    public Optional<Wallet> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Wallet> findAll() {
        return List.copyOf(storage.values());
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}
```

## Exercise

**Task:** Refactor `NoteController`:
1. Extract `NoteService` with the logic
2. Extract `InMemoryNoteRepository` for storage
3. The controller should only call the service

**Files:** `exercises/ex05-service-layer/`

## Checklist

- [ ] Controller has no business logic
- [ ] Service operates on domain objects
- [ ] Repository isolates the data storage method
- [ ] I'm using constructor injection without `@Autowired`