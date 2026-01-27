# Ćwiczenie 05: Service Layer

## Zadanie

Refaktoruj `InstrumentController` w projekcie `projects/wallet-manager` do architektury warstwowej.
Celem jest oddzielenie logiki biznesowej od warstwy HTTP.

### Docelowa struktura (w pakiecie `com.sp94dev.wallet`)

```
instrument/
├── InstrumentController.java      # Tylko routing HTTP
├── InstrumentService.java         # Logika biznesowa
├── InMemoryInstrumentRepository.java  # Przechowywanie danych
└── Instrument.java                # Model domeny (Record)
```

(Opcjonalnie możesz stworzyć podpakiet `dto` dla Request/Response, jeśli chcesz oddzielić model API od modelu domeny).

### 1. Stwórz `InMemoryInstrumentRepository`

To klasa, która przejmie odpowiedzialność za `List<Instrument>` (lub `Map`) oraz generowanie ID.

```java
@Repository
public class InMemoryInstrumentRepository {
    
    private final Map<Long, Instrument> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Przenieś tutaj logikę inicjalizacji danych (AAPL, GOOGL...) z kontrolera
    
    public Instrument save(Instrument instrument) {
        // Logika nadawania ID jeśli null
        // Zapis do mapy
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
    
    // ... inne metody potrzebne do filtrowania
}
```

### 2. Stwórz `InstrumentService`

Service pośredniczy między Controllerem a Repository. Tutaj trafi logika filtrowania.

```java
@Service
public class InstrumentService {
    
    private final InMemoryInstrumentRepository repository;
    
    public InstrumentService(InMemoryInstrumentRepository repository) {
        this.repository = repository;
    }
    
    public List<Instrument> findAll(String type, String currency, String ticker, String market) {
        // Przenieś tutaj logikę stream().filter(...) z kontrolera
        // Pobierz wszystkie z repository i filtruj
    }
    
    public Instrument create(Instrument instrument) {
        // Walidacja? (później)
        return repository.save(instrument);
    }
    
    // ... getById, update, delete
}
```

### 3. Odchudź `InstrumentController`

Kontroler powinien tylko delegować zadania do serwisu.

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
    
    // ... reszta metod deleguje do service
}
```

## Zadanie dodatkowe (Transaction)

Wykonaj analogiczną refaktoryzację dla `TransactionController`.

### Checklist

- [ ] `InstrumentController` nie ma pola `List` ani `AtomicLong`.
- [ ] Logika filtrowania jest w `InstrumentService`.
- [ ] Dane są przechowywane w `InMemoryInstrumentRepository`.
- [ ] Aplikacja nadal działa tak samo (sprawdź `rest/instrument.rest`).