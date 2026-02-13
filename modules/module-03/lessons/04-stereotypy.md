# Lekcja 04: Stereotypy (Adnotacje)

> @Component, @Service, @Repository, @Controller — semantyka ma znaczenie.

## Koncept

### Czym są stereotypy?

Stereotyp = adnotacja, która mówi Springowi: **"To jest Bean, zarządzaj nim."**

Technicznie **wszystkie stereotypy dziedziczą po `@Component`** — są funkcjonalnie identyczne.
Różnica jest **semantyczna** — informują programistę o ROLI klasy w architekturze.

```
                    @Component (generic)
                   /     |     \       \
          @Service  @Repository  @Controller  @Configuration
          (logika)  (dane)       (HTTP)        (konfiguracja)
```

### Mapowanie na warstwy

```
┌─────────────────────────────────────────┐
│  @RestController / @Controller          │  ← WARSTWA WEB (HTTP)
│  Obsługuje requesty, zwraca response    │
├─────────────────────────────────────────┤
│  @Service                               │  ← WARSTWA BIZNESOWA
│  Logika, reguły, obliczenia             │
├─────────────────────────────────────────┤
│  @Repository                            │  ← WARSTWA DANYCH
│  CRUD, zapytania do bazy, IO            │
├─────────────────────────────────────────┤
│  @Configuration                         │  ← KONFIGURACJA
│  Definicje beanów, ustawieniaexternal   │
└─────────────────────────────────────────┘
```

---

### Każdy stereotyp z osobna

#### 1. `@Component` — generic bean

```java
@Component
public class PriceCalculator {
    public BigDecimal calculateTotal(int qty, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
```

**Kiedy:** Klasa nie pasuje do żadnej z innych kategorii.
Utility, helper, converter — coś co nie jest ani Service, ani Repository.

#### 2. `@Service` — warstwa biznesowa

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public List<Instrument> findAll() {
        return repository.findAll();
    }

    public Instrument findByTicker(String ticker) {
        return repository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("Not found: " + ticker));
    }
}
```

**Kiedy:** Klasa zawiera **logikę biznesową**. Orkiestruje operacje na danych.

**Analogia Angular:** `@Injectable({ providedIn: 'root' })` na serwisie biznesowym.

#### 3. `@Repository` — warstwa danych

```java
@Repository
public class InMemoryInstrumentRepository {
    private final Map<String, Instrument> store = new ConcurrentHashMap<>();

    public List<Instrument> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Instrument> findByTicker(String ticker) {
        return Optional.ofNullable(store.get(ticker));
    }

    public Instrument save(Instrument instrument) {
        store.put(instrument.ticker(), instrument);
        return instrument;
    }
}
```

**Kiedy:** Klasa komunikuje się z bazą danych lub symuluje przechowywanie danych.

**Bonus `@Repository`:** Spring automatycznie tłumaczy wyjątki bazodanowe
na ujednoliconą hierarchię `DataAccessException` (przydatne od Module 05 z JPA).

#### 4. `@Controller` / `@RestController` — warstwa web

```java
@RestController  // = @Controller + @ResponseBody
@RequestMapping("/api/instruments")
public class InstrumentController {
    private final InstrumentService service;

    public InstrumentController(InstrumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Instrument> getAll() {
        return service.findAll();
    }
}
```

**Różnica:**

- `@Controller` → zwraca widoki (HTML, Thymeleaf) — typowe dla MVC
- `@RestController` → zwraca dane (JSON) — to co robisz w REST API

**Analogia Angular:** `@RestController` to jak `@Component` z routingiem w Angular.

#### 5. `@Configuration` — definicje beanów

```java
@Configuration
public class AppConfig {

    @Bean  // ← ręczna definicja Beana (zamiast @Component na klasie)
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
```

**Kiedy:** Kiedy nie możesz dodać `@Component` do klasy (bo pochodzi z zewnętrznej biblioteki)
lub chcesz mieć pełną kontrolę nad tworzeniem Beana.

**Analogia Angular:** To jak moduł z `providers:` — ręcznie rejestrujesz serwisy.

```typescript
// Angular — ręczna rejestracja
@NgModule({
  providers: [
    { provide: API_URL, useValue: 'http://localhost:8080' }
  ]
})
```

---

### Technicznie identyczne, semantycznie różne

Możesz zamienić `@Service` na `@Component` i kod **zadziała**.
Ale to jak nazwanie pliku Angular `instrument.component.ts` zamiast `instrument.service.ts` — działa, ale myli programistów.

```java
// ✅ Poprawna semantyka
@Service         // → logika biznesowa
@Repository      // → dostęp do danych
@RestController  // → obsługa HTTP

// ❌ Technicznie działa, ale semantycznie BŁĘDNE
@Component       // → na wszystkim (nie wiadomo co klasa robi)
```

### Szybki quiz — co użyć?

| Klasa                                         | Stereotyp                  |
| --------------------------------------------- | -------------------------- |
| Obsługuje `GET /api/instruments`              | `@RestController`          |
| Oblicza wartość portfela                      | `@Service`                 |
| Przechowuje instrumenty w `ConcurrentHashMap` | `@Repository`              |
| Konfiguruje `ObjectMapper`                    | `@Configuration` + `@Bean` |
| Konwertuje `Transaction` → `TransactionDTO`   | `@Component`               |

## Ćwiczenie

**Zadanie:** Sprawdź swoje klasy w Wallet Manager.

1. Otwórz każdą klasę w pakietach `instrument/` i `transaction/`.
2. Sprawdź czy ma poprawny stereotyp:
   - Kontroler → `@RestController`
   - Serwis → `@Service`
   - Repozytorium → `@Repository`
3. Czy brakuje jakiejś adnotacji? Jeśli tak — dodaj ją.

## Checklist

- [ ] Znam 5 stereotypów: @Component, @Service, @Repository, @Controller, @Configuration
- [ ] Wiem że wszystkie dziedziczą po @Component (technicznie identyczne)
- [ ] Rozumiem różnicę semantyczną (rola w architekturze)
- [ ] Wiem kiedy użyć @Bean w @Configuration (klasy z zewnętrznych bibliotek)
- [ ] Potrafię przypisać stereotyp do każdej klasy w projekcie
