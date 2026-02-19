# Lesson 05: Bean Scopes + Lifecycle

> Singleton, Prototype, @PostConstruct, @PreDestroy — the lifecycle of a Bean.

## Concept

### Bean Scopes — how many instances does Spring create?

Scope determines **how many instances** of a given Bean Spring will create.

### 1. Singleton (DEFAULT) — one instance for the entire application

```java
@Service  // Singleton by default — no need to add anything
public class InstrumentService {
    // Spring creates ONE instance
    // Everyone who needs InstrumentService → gets THE SAME instance
}
```

```
Controller A ──▶ InstrumentService (0x001)
Controller B ──▶ InstrumentService (0x001)  ← same instance!
ScheduledTask ──▶ InstrumentService (0x001)  ← same instance!
```

**Angular Analogy:**

```typescript
@Injectable({
  providedIn: "root", // ← Singleton! One for the entire application
})
export class InstrumentService {}
```

**⚠️ Note:** A Singleton Bean is **shared between threads** (HTTP requests).
That's why you should **NOT store state** in Service fields (unless thread-safe like `ConcurrentHashMap`).

```java
@Service
public class InstrumentService {
    // ❌ BAD — mutable state in Singleton Bean (thread problems!)
    private int requestCount = 0;  // race condition!

    // ✅ GOOD — bean is stateless
    private final InstrumentRepository repository;  // final, immutable
}
```

### 2. Prototype — new instance EVERY time

```java
@Component
@Scope("prototype")  // new instance every time
public class ReportGenerator {
    private final List<String> data = new ArrayList<>();

    public void addLine(String line) {
        data.add(line);
    }
}
```

```
Someone asks for ReportGenerator → new instance (0x001)
Someone asks for ReportGenerator → new instance (0x002)  ← DIFFERENT instance!
```

**When:** When the Bean holds state specific to a single use
(e.g., report builder, form handler).

**Angular Analogy:** `providedIn` on a component instead of `'root'` →
each component gets its own instance.

### 3. Request — one instance per HTTP Request (web only)

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestContext {
    private String userId;
    // lives only within a single HTTP request
}
```

**When:** Data specific to a single request (e.g., user context, audit log).

### 4. Session — one instance per HTTP Session (web only)

```java
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserPreferences {
    private String theme;
    private String currency;
    // lives as long as the user session
}
```

**When:** Per-session data (e.g., user preferences). Rare in REST APIs (REST is stateless).

### Scopes Summary

| Scope         | How many instances | When to use                             | Frequency    |
| ------------- | ------------------ | --------------------------------------- | ------------ |
| **Singleton** | 1 per application  | Services, Repositories, Controllers     | 99% of cases |
| **Prototype** | new one every time | Stateful builders, temporary processors | Rarely       |
| **Request**   | 1 per HTTP request | Request-scoped context                  | Rarely       |
| **Session**   | 1 per HTTP session | User preferences (not in REST)          | Almost never |

**Rule:** If you don't know which scope — **Singleton** (default). In 99% of cases that's enough.

---

### Bean Lifecycle

```
 Spring creates Bean
       ↓
 Injects dependencies (Constructor Injection)
       ↓
 @PostConstruct ← method called AFTER creation (initialization)
       ↓
 Bean ready for use
       ↓
 ... application runs ...
       ↓
 @PreDestroy ← method called BEFORE destruction (cleanup)
       ↓
 Bean removed from memory
```

### @PostConstruct — initialization after injection

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    @PostConstruct  // called ONCE, after Constructor Injection
    public void init() {
        System.out.println("InstrumentService ready!");
        // Here you can: load startup data, check configuration, etc.
    }
}
```

**When:** You need initialization logic that requires injected dependencies
(in the constructor, dependencies might not be fully ready in complex scenarios).

**Angular Analogy:** It's like `ngOnInit()`:

```typescript
export class InstrumentComponent implements OnInit {
  constructor(private service: InstrumentService) {}

  ngOnInit() {
    // ≈ @PostConstruct — called AFTER dependency injection
    this.loadData();
  }
}
```

### @PreDestroy — cleanup before shutdown

```java
@Service
public class CacheService {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @PreDestroy  // called BEFORE application shutdown
    public void cleanup() {
        System.out.println("Clearing cache (" + cache.size() + " items)...");
        cache.clear();
    }
}
```

**When:** You need to release resources: close connections, stop schedulers, clear caches.

**Angular Analogy:** It's like `ngOnDestroy()`:

```typescript
export class InstrumentComponent implements OnDestroy {
  ngOnDestroy() {
    // ≈ @PreDestroy — cleanup, unsubscribe, teardown
    this.subscription.unsubscribe();
  }
}
```

### Important: @PostConstruct and @PreDestroy DON'T work with Prototype!

Spring creates a Prototype Bean and **forgets about it** — it doesn't manage its lifecycle.
`@PreDestroy` will NOT be called. You have to clean up yourself.

## Exercise

**Task 1:** Temporarily add `@PostConstruct` to one of your Services:

```java
@PostConstruct
public void init() {
    System.out.println(">>> " + getClass().getSimpleName() + " initialized!");
}
```

Run the application and check the logs — when exactly does it print?

**Task 2:** Answer the questions:

- Why should Controller, Service, and Repository be Singletons?
- What would happen if InstrumentService was Prototype? (hint: every request → new instance → empty state)

## Checklist

- [x] I know the default scope is Singleton (one instance)
- [x] I understand when to use Prototype (stateful, temporary)
- [x] I know what @PostConstruct is (init after injection ≈ ngOnInit)
- [x] I know what @PreDestroy is (cleanup before shutdown ≈ ngOnDestroy)
- [x] I understand why a Singleton Service should NOT have mutable state
