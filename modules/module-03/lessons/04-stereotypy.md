# Lesson 04: Stereotypes (Annotations)

> @Component, @Service, @Repository, @Controller — semantics matter.

## Concept

### What are stereotypes?

Stereotype = annotation that tells Spring: **"This is a Bean, manage it."**

Technically **all stereotypes inherit from `@Component`** — they are functionally identical.
The difference is **semantic** — they inform the developer about the class's ROLE in the architecture.

```
                    @Component (generic)
                   /     |     \       \
          @Service  @Repository  @Controller  @Configuration
          (logic)   (data)       (HTTP)        (configuration)
```

### Mapping to layers

```
┌─────────────────────────────────────────┐
│  @RestController / @Controller          │  ← WEB LAYER (HTTP)
│  Handles requests, returns response     │
├─────────────────────────────────────────┤
│  @Service                               │  ← BUSINESS LAYER
│  Logic, rules, calculations             │
├─────────────────────────────────────────┤
│  @Repository                            │  ← DATA LAYER
│  CRUD, database queries, IO             │
├─────────────────────────────────────────┤
│  @Configuration                         │  ← CONFIGURATION
│  Bean definitions, external settings    │
└─────────────────────────────────────────┘
```

---

### Each stereotype in detail

#### 1. `@Component` — generic bean

```java
@Component
public class PriceCalculator {
    public BigDecimal calculateTotal(int qty, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
```

**When:** The class doesn't fit any other category.
Utility, helper, converter — something that's neither Service nor Repository.

#### 2. `@Service` — business layer

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

**When:** The class contains **business logic**. Orchestrates data operations.

**Angular Analogy:** `@Injectable({ providedIn: 'root' })` on a business service.

#### 3. `@Repository` — data layer

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

**When:** The class communicates with a database or simulates data storage.

**Bonus `@Repository`:** Spring automatically translates database exceptions
into a unified `DataAccessException` hierarchy (useful from Module 05 with JPA).

#### 4. `@Controller` / `@RestController` — web layer

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

**Difference:**

- `@Controller` → returns views (HTML, Thymeleaf) — typical for MVC
- `@RestController` → returns data (JSON) — what you're doing in a REST API

**Angular Analogy:** `@RestController` is like `@Component` with routing in Angular.

#### 5. `@Configuration` — bean definitions

```java
@Configuration
public class AppConfig {

    @Bean  // ← manual Bean definition (instead of @Component on the class)
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
```

**When:** You can't add `@Component` to a class (because it comes from an external library)
or you want full control over Bean creation.

**Angular Analogy:** It's like a module with `providers:` — you manually register services.

```typescript
// Angular — manual registration
@NgModule({
  providers: [
    { provide: API_URL, useValue: 'http://localhost:8080' }
  ]
})
```

---

### Technically identical, semantically different

You can replace `@Service` with `@Component` and the code **will work**.
But it's like naming an Angular file `instrument.component.ts` instead of `instrument.service.ts` — it works, but confuses developers.

```java
// ✅ Correct semantics
@Service         // → business logic
@Repository      // → data access
@RestController  // → HTTP handling

// ❌ Technically works, but semantically WRONG
@Component       // → on everything (unclear what the class does)
```

### Quick quiz — what to use?

| Class                                       | Stereotype                 |
| ------------------------------------------- | -------------------------- |
| Handles `GET /api/instruments`              | `@RestController`          |
| Calculates portfolio value                  | `@Service`                 |
| Stores instruments in a `ConcurrentHashMap` | `@Repository`              |
| Configures `ObjectMapper`                   | `@Configuration` + `@Bean` |
| Converts `Transaction` → `TransactionDTO`   | `@Component`               |

## Exercise

**Task:** Check your classes in Wallet Manager.

1. Open each class in the `instrument/` and `transaction/` packages.
2. Check if it has the correct stereotype:
   - Controller → `@RestController`
   - Service → `@Service`
   - Repository → `@Repository`
3. Is any annotation missing? If so — add it.

## Checklist

- [x] I know the 5 stereotypes: @Component, @Service, @Repository, @Controller, @Configuration
- [x] I know they all inherit from @Component (technically identical)
- [x] I understand the semantic difference (role in architecture)
- [x] I know when to use @Bean in @Configuration (classes from external libraries)
- [x] I can assign a stereotype to every class in the project
