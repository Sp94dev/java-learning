# Lesson 01: The Problem Without DI + Dependency Injection Concept

> Why `new` is a problem and how Inversion of Control changes the game.

## Concept

### Problem: Tight Coupling (without DI)

Imagine that in Angular you don't have DI and have to create everything manually:

```typescript
// ❌ Angular WITHOUT Dependency Injection (if it didn't exist)
export class InstrumentComponent {
  private service: InstrumentService;

  constructor() {
    const http = new HttpClient(); // you create HttpClient yourself
    const cache = new CacheService(); // you create CacheService yourself
    this.service = new InstrumentService(http, cache); // you wire it up yourself
  }
}
```

**What's wrong?**

1. **Tight coupling** — the component DEPENDS on concrete implementations.
2. **Hard to test** — you can't swap in a mock.
3. **Hidden dependencies** — it's unclear what the class actually needs.
4. **Cascade of changes** — changing the `CacheService` constructor breaks EVERYTHING that creates it.

### The same problem in Java

```java
// ❌ Java WITHOUT Dependency Injection
public class InstrumentController {
    private final InstrumentService service;

    public InstrumentController() {
        // Controller CREATES Service → Service CREATES Repository → ...
        InMemoryInstrumentRepository repo = new InMemoryInstrumentRepository();
        this.service = new InstrumentService(repo);  // tight coupling!
    }
}
```

**Problems:**

- Controller **knows** which Repository implementation the Service uses → that's not its concern!
- Want to switch from `InMemory` to `JPA`? You have to change the Controller → absurd.
- Want to test the Controller without a real Service? Not possible.

---

### Solution: Dependency Injection (DI)

**DI = dependencies provided FROM THE OUTSIDE, not created internally.**

```java
// ✅ Java WITH Dependency Injection
public class InstrumentController {
    private final InstrumentService service;

    // Someone FROM THE OUTSIDE gives me the Service — I don't care where it comes from
    public InstrumentController(InstrumentService service) {
        this.service = service;
    }
}
```

**Angular Analogy:** Exactly what you do every day!

```typescript
// ✅ Angular — DI via constructor (same thing as above!)
export class InstrumentComponent {
  constructor(private service: InstrumentService) {}
  // Angular Injector creates InstrumentService FOR YOU
}
```

### Inversion of Control (IoC)

| Without IoC                    | With IoC                              |
| ------------------------------ | ------------------------------------- |
| **You** create objects (`new`) | **Container** creates objects for you |
| **You** manage dependencies    | **Container** injects dependencies    |
| **You** decide the order       | **Container** decides the order       |

```
Without IoC:  Your code → creates dependencies → uses them
With IoC:     Container → creates dependencies → injects into your code → your code uses them
```

**Angular Analogy:**

```
Angular Injector  =  Spring IoC Container
providedIn: 'root' =  @Service (Singleton Bean)
@Injectable()      =  @Component / @Service
```

### Three benefits of DI — remember these

1. **Loose Coupling**
   - Class depends on an **interface**, not an implementation.
   - Switching `InMemoryRepo` → `JpaRepo`? Change in ONE place.

2. **Testability**
   - In tests you inject a **mock** instead of the real implementation.
   - No DI = no mocks = no unit tests.

3. **Separation of Concerns**
   - Controller doesn't know HOW the Service works.
   - Service doesn't know HOW the Repository stores data.
   - Everyone does THEIR OWN thing.

---

### Visualization: Before and After DI

```
❌ WITHOUT DI (tight coupling):

Controller ──new──▶ Service ──new──▶ Repository ──new──▶ DataSource
     ↑                ↑                  ↑
  knows everything   knows about repo  knows about DS

✅ WITH DI (loose coupling):

Container creates:  DataSource → Repository → Service → Controller
                                                              ↓
Controller only knows about Service (doesn't care about the rest)
```

## Exercise

**Task:** Analyze your current Wallet Manager code.

1. Open `InstrumentController.java` and `InstrumentService.java`.
2. Check: **Does the Controller create the Service via `new`?**
3. Check: **Does the Service create the Repository via `new`?**
4. If yes — identify where tight coupling exists.
5. Think: what would you need to change if you wanted to swap `InMemoryRepository` for `JpaRepository`?

**Don't change the code yet** — we'll do that in Lessons 03 and 06.

## Checklist

- [x] I understand the tight coupling problem (class creates its own dependencies)
- [x] I know what Dependency Injection is (dependencies from outside)
- [x] I understand Inversion of Control (container manages, not me)
- [x] I see the analogy to Angular DI (`@Injectable()` ≈ `@Service`)
- [x] I can identify tight coupling in code (look for `new` in the constructor)
