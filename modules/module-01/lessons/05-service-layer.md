# Lekcja 05: Service Layer

> Separacja logiki, `@Service`, Constructor Injection

## Analogia Angular

| Spring | Angular |
|--------|---------|
| `@Service` | `@Injectable()` |
| `@RestController` | Component |
| Constructor Injection | Constructor Injection |
| `@Autowired` (stare) | `inject()` (stare) |

## Dlaczego Service Layer?

**Fat Controller (źle):**
```java
@RestController
public class WalletController {
    private final List<Wallet> wallets = new ArrayList<>();

    @PostMapping
    public Wallet create(@RequestBody CreateRequest req) {
        // walidacja
        // logika biznesowa
        // mapowanie
        // zapis
        // notyfikacje
        // logowanie
        // zwrot
    }
}
```

**Thin Controller + Service (dobrze):**
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

## Constructor Injection (rekomendowane)

```java
@Service
public class WalletService {
    
    private final WalletRepository repository;
    private final NotificationService notifications;

    // Spring automatycznie wstrzykuje - bez @Autowired
    public WalletService(
        WalletRepository repository,
        NotificationService notifications
    ) {
        this.repository = repository;
        this.notifications = notifications;
    }
}
```

**Dlaczego constructor injection?**
- Jasne zależności
- Łatwiejsze testowanie (możesz przekazać mock)
- Niemutowalność (`final` pola)
- Fail-fast przy brakujących zależnościach

## Warstwy aplikacji

```
┌─────────────────────────────────────┐
│  Controller (HTTP)                  │  ← cienki, tylko routing
├─────────────────────────────────────┤
│  Service (Business Logic)           │  ← logika, walidacja
├─────────────────────────────────────┤
│  Repository (Data Access)           │  ← dostęp do danych
└─────────────────────────────────────┘
```

## In-Memory Repository (na razie)

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

## Ćwiczenie

**Zadanie:** Refaktoruj `NoteController`:
1. Wydziel `NoteService` z logiką
2. Wydziel `InMemoryNoteRepository` z przechowywaniem
3. Kontroler ma tylko wywoływać serwis

**Pliki:** `exercises/ex05-service-layer/`

## Checklist

- [ ] Controller nie ma logiki biznesowej
- [ ] Service operuje na domenowych obiektach
- [ ] Repository izoluje sposób przechowywania danych
- [ ] Używam constructor injection bez `@Autowired`
