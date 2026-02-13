# Lekcja 05: Bean Scopes + Lifecycle

> Singleton, Prototype, @PostConstruct, @PreDestroy — cykl życia Beana.

## Koncept

### Bean Scopes — ile instancji Spring tworzy?

Scope (zakres) decyduje, **ile instancji** danego Beana Spring utworzy.

### 1. Singleton (DOMYŚLNY) — jedna instancja na całą aplikację

```java
@Service  // domyślnie Singleton — nie musisz nic dodawać
public class InstrumentService {
    // Spring tworzy JEDNĄ instancję
    // Każdy kto potrzebuje InstrumentService → dostaje TĘ SAMĄ instancję
}
```

```
Controller A ──▶ InstrumentService (0x001)
Controller B ──▶ InstrumentService (0x001)  ← ta sama instancja!
ScheduledTask ──▶ InstrumentService (0x001)  ← ta sama instancja!
```

**Analogia Angular:**

```typescript
@Injectable({
  providedIn: "root", // ← Singleton! Jeden na całą aplikację
})
export class InstrumentService {}
```

**⚠️ Uwaga:** Singleton Bean jest **współdzielony między wątkami** (HTTP requesty).
Dlatego **NIE przechowuj stanu** w polach Service'u (chyba że thread-safe jak `ConcurrentHashMap`).

```java
@Service
public class InstrumentService {
    // ❌ ŹLE — stan mutable w Singleton Bean (problemy z wątkami!)
    private int requestCount = 0;  // wyścig wątków!

    // ✅ DOBRZE — bean jest bezstanowy (stateless)
    private final InstrumentRepository repository;  // final, immutable
}
```

### 2. Prototype — nowa instancja za KAŻDYM razem

```java
@Component
@Scope("prototype")  // za każdym razem nowa instancja
public class ReportGenerator {
    private final List<String> data = new ArrayList<>();

    public void addLine(String line) {
        data.add(line);
    }
}
```

```
Ktoś prosi o ReportGenerator → nowa instancja (0x001)
Ktoś prosi o ReportGenerator → nowa instancja (0x002)  ← INNA instancja!
```

**Kiedy:** Gdy Bean przechowuje stan specyficzny dla jednego użycia
(np. builder raportów, form handler).

**Analogia Angular:** `providedIn` w komponencie zamiast w `'root'` →
każdy komponent dostaje swoją instancję.

### 3. Request — jedna instancja per HTTP Request (web only)

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext {
    private String userId;
    // żyje tylko w ramach jednego HTTP requestu
}
```

**Kiedy:** Dane specyficzne dla jednego requestu (np. kontekst użytkownika, audit log).

### 4. Session — jedna instancja per HTTP Session (web only)

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserPreferences {
    private String theme;
    private String currency;
    // żyje tak długo jak sesja użytkownika
}
```

**Kiedy:** Dane per sesja (np. preferencje użytkownika). Rzadkie w REST API (REST jest stateless).

### Podsumowanie Scopes

| Scope         | Ile instancji        | Kiedy użyć                              | Częstość       |
| ------------- | -------------------- | --------------------------------------- | -------------- |
| **Singleton** | 1 na aplikację       | Services, Repositories, Controllers     | 99% przypadków |
| **Prototype** | nowa za każdym razem | Stateful builders, temporary processors | Rzadko         |
| **Request**   | 1 na HTTP request    | Request-scoped context                  | Rzadko         |
| **Session**   | 1 na HTTP session    | User preferences (nie w REST)           | Prawie nigdy   |

**Zasada:** Jeśli nie wiesz jaki scope — **Singleton** (domyślny). W 99% przypadków to wystarczy.

---

### Bean Lifecycle — cykl życia Beana

```
 Spring tworzy Bean
       ↓
 Wstrzykuje zależności (Constructor Injection)
       ↓
 @PostConstruct ← metoda wywoływana PO utworzeniu (inicjalizacja)
       ↓
 Bean gotowy do użycia
       ↓
 ... aplikacja działa ...
       ↓
 @PreDestroy ← metoda wywoływana PRZED zniszczeniem (cleanup)
       ↓
 Bean usuwany z pamięci
```

### @PostConstruct — inicjalizacja po wstrzyknięciu

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    @PostConstruct  // wywoływane RAZ, po Constructor Injection
    public void init() {
        System.out.println("InstrumentService gotowy!");
        // Tu możesz: załadować dane startowe, sprawdzić konfigurację, itp.
    }
}
```

**Kiedy:** Potrzebujesz logiki inicjalizacyjnej, która wymaga wstrzykniętych zależności
(w konstruktorze zależności jeszcze mogą nie być w pełni gotowe w złożonych scenariuszach).

**Analogia Angular:** To jak `ngOnInit()`:

```typescript
export class InstrumentComponent implements OnInit {
  constructor(private service: InstrumentService) {}

  ngOnInit() {
    // ≈ @PostConstruct — wywoływane PO wstrzyknięciu zależności
    this.loadData();
  }
}
```

### @PreDestroy — sprzątanie przed zamknięciem

```java
@Service
public class CacheService {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @PreDestroy  // wywoływane PRZED zamknięciem aplikacji
    public void cleanup() {
        System.out.println("Czyszczę cache (" + cache.size() + " elementów)...");
        cache.clear();
    }
}
```

**Kiedy:** Musisz zwolnić zasoby: zamknąć połączenia, zatrzymać scheduler, wyczyścić cache.

**Analogia Angular:** To jak `ngOnDestroy()`:

```typescript
export class InstrumentComponent implements OnDestroy {
  ngOnDestroy() {
    // ≈ @PreDestroy — sprzątanie, odsubskrybowanie, cleanup
    this.subscription.unsubscribe();
  }
}
```

### Ważne: @PostConstruct i @PreDestroy NIE działają z Prototype!

Spring tworzy Prototype Bean i **zapomina o nim** — nie zarządza jego lifecycle.
`@PreDestroy` się NIE wywoła. Musisz sam posprzątać.

## Ćwiczenie

**Zadanie 1:** Dodaj tymczasowo `@PostConstruct` do jednego ze swoich Service'ów:

```java
@PostConstruct
public void init() {
    System.out.println(">>> " + getClass().getSimpleName() + " zainicjalizowany!");
}
```

Uruchom aplikację i sprawdź logi — kiedy dokładnie się wypisuje?

**Zadanie 2:** Odpowiedz na pytania:

- Dlaczego Controller, Service i Repository powinny być Singletonami?
- Co by się stało, gdyby InstrumentService był Prototype? (wskazówka: każdy request → nowa instancja → pusty stan)

## Checklist

- [ ] Wiem że domyślny scope to Singleton (jedna instancja)
- [ ] Rozumiem kiedy użyć Prototype (stateful, temporary)
- [ ] Wiem czym jest @PostConstruct (init po wstrzyknięciu ≈ ngOnInit)
- [ ] Wiem czym jest @PreDestroy (cleanup przed zamknięciem ≈ ngOnDestroy)
- [ ] Rozumiem dlaczego Singleton Service NIE powinien mieć mutable state
