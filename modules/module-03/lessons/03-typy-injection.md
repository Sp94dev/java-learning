# Lesson 03: Injection Types

> Constructor ✅, Setter ⚠️, Field ❌ — why Constructor Injection wins.

## Concept

### Three ways to inject dependencies

Spring offers 3 DI mechanisms. Only one is recommended.

### 1. Constructor Injection ✅ (RECOMMENDED)

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;  // final!

    // Spring sees a constructor with an InstrumentRepository parameter
    // → looks for a Bean of that type → injects it
    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }
}
```

**Why is this the best?**

| Advantage       | Explanation                                                  |
| --------------- | ------------------------------------------------------------ |
| **Immutable**   | Field is `final` → can't be changed after creation           |
| **Enforced**    | Can't create object WITHOUT dependencies (compiler enforces) |
| **Testability** | In tests simply: `new InstrumentService(mockRepo)`           |
| **Explicit**    | Looking at the constructor, you see ALL dependencies         |

**Angular Analogy:** Exactly what you do in every component:

```typescript
// Angular — Constructor Injection
export class InstrumentComponent {
  constructor(private service: InstrumentService) {}
  //          ↑ Angular injects — exactly like Spring
}
```

**Important:** Since Spring 4.3, if a class has **ONE constructor**, the `@Autowired` annotation
is **optional** — Spring will automatically use that constructor.

```java
// ✅ @Autowired NOT NEEDED (single constructor)
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }
}

// ⚠️ @Autowired NEEDED (multiple constructors — you must point to which one)
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    @Autowired  // ← "use THIS constructor"
    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public InstrumentService() {
        this.repository = null; // fallback
    }
}
```

---

### 2. Setter Injection ⚠️ (OPTIONAL DEPENDENCIES)

```java
@Service
public class InstrumentService {
    private InstrumentRepository repository;  // NOT final!

    @Autowired  // ← required for setter injection
    public void setRepository(InstrumentRepository repository) {
        this.repository = repository;
    }
}
```

**When to use?** Almost never. The only sensible case: the dependency is **optional**
(e.g., a cache that may not exist, an optional logger).

**Problem:** Object can exist WITHOUT a set dependency → `NullPointerException` at runtime.

---

### 3. Field Injection ❌ (AVOID)

```java
@Service
public class InstrumentService {
    @Autowired  // ← Spring injects directly into the field (via reflection)
    private InstrumentRepository repository;  // NOT final!

    // No constructor with parameters
}
```

**Why is this bad?**

| Problem              | Explanation                                              |
| -------------------- | -------------------------------------------------------- |
| **Hidden deps**      | Not visible in the constructor — you have to read fields |
| **No immutability**  | Field CANNOT be `final`                                  |
| **Hard testing**     | `new InstrumentService()` → repository = null → 💥 NPE   |
| **Reflection magic** | Spring uses reflection — breaks encapsulation            |
| **God Object**       | Easy to add 15 `@Autowired` fields without noticing      |

**Angular Analogy:** Often confused with the `inject()` function in new Angular.
⚠️ **Note:** Although syntactically similar (`private service = inject(Service)`),
in Angular this is a **modern and recommended approach** (functional, explicit).
In Java, `@Autowired private Service` is an **outdated anti-pattern** (hides dependencies, complicates testing).
Don't carry `inject()` habits over to `@Autowired` on fields!

---

### Comparison — one table

```java
// ✅ Constructor (RECOMMENDED)
public InstrumentService(InstrumentRepository repo) {
    this.repo = repo;  // final, explicit, testable
}

// ⚠️ Setter (optional dependencies)
@Autowired
public void setRepo(InstrumentRepository repo) {
    this.repo = repo;  // NOT final, can be null
}

// ❌ Field (AVOID)
@Autowired
private InstrumentRepository repo;  // hidden, NOT final, reflection
```

| Aspect        | Constructor               | Setter             | Field                  |
| ------------- | ------------------------- | ------------------ | ---------------------- |
| `final`       | ✅ Yes                    | ❌ No              | ❌ No                  |
| Explicit deps | ✅ Visible in constructor | ⚠️ Scattered       | ❌ Hidden              |
| Testability   | ✅ `new Service(mock)`    | ⚠️ Requires setter | ❌ Requires reflection |
| Required      | ✅ Compiler enforces      | ❌ Can be null     | ❌ Can be null         |
| `@Autowired`  | Optional (1 constructor)  | Required           | Required               |

### When you see many dependencies in a constructor — it's a CODE SMELL

```java
// ⚠️ Red flag — too many dependencies (Single Responsibility Principle violated)
public OrderService(
    InstrumentRepository instrumentRepo,
    TransactionRepository transactionRepo,
    UserRepository userRepo,
    NotificationService notificationService,
    PriceService priceService,
    AuditService auditService,
    CacheService cacheService
) { ... }
```

**Rule of thumb:** More than **3-4 dependencies** → consider splitting the class into smaller ones.
Constructor Injection makes this problem **visible** — that's a feature, not a bug!

## Exercise

**Task:** Review your Wallet Manager and answer the questions:

1. What type of injection are you currently using in controllers and services?
2. Are the dependency fields `final`?
3. Are you using `@Autowired`? Is it needed?
4. How many dependencies does each class have (count constructor parameters)?

**Don't change the code yet** — practical refactoring happens in Lesson 06.

## Checklist

- [x] I know that Constructor Injection is the only recommended approach
- [x] I understand why Field Injection is bad (hidden dependencies, no final)
- [x] I know when `@Autowired` is optional (single constructor)
- [x] I understand that many dependencies in a constructor = signal for refactoring
- [x] I see the analogy to Angular Constructor DI
