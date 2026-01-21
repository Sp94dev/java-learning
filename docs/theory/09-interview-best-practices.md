# TEORIA: Interview Prep & Best Practices

---

## CZĘŚĆ 1: JAVA INTERVIEW QUESTIONS

---

## 1. Core Java

### equals() vs ==

**==** porównuje **referencje** (czy to ten sam obiekt w pamięci)
**equals()** porównuje **wartości** (logiczna równość)

```
String a = new String("hello");
String b = new String("hello");

a == b;       // false (różne obiekty)
a.equals(b);  // true (ta sama wartość)
```

**Kontrakt equals():**
1. Reflexive: `x.equals(x) == true`
2. Symmetric: `x.equals(y) == y.equals(x)`
3. Transitive: `x.equals(y) && y.equals(z) → x.equals(z)`
4. Consistent: wielokrotne wywołanie = ten sam wynik
5. `x.equals(null) == false`

**Związek z hashCode():**
Jeśli `a.equals(b)`, to `a.hashCode() == b.hashCode()`.
(Odwrotnie nie musi być prawdą - kolizje są dozwolone)

### HashMap Internals

**Struktura:**
```
HashMap = Array of buckets
Bucket = LinkedList lub Tree (gdy > 8 elementów)

put(key, value):
1. hash = key.hashCode()
2. index = hash % array.length
3. Jeśli bucket pusty → dodaj
4. Jeśli nie pusty → sprawdź equals() → update lub append
```

**Dlaczego immutable keys?**
- hashCode() musi być stały
- Jeśli key zmieni się po put() → nie znajdziesz go

**Load factor (0.75):**
Gdy `size / capacity > 0.75` → resize (2x)

### final, finally, finalize

**final:**
- Variable: nie można reassign
- Method: nie można override
- Class: nie można extend

**finally:**
- Blok w try-catch wykonywany zawsze (oprócz System.exit())

**finalize():**
- DEPRECATED (Java 9+)
- Wywoływana przez GC przed usunięciem obiektu
- Nie używaj - nieprzewidywalny timing

### Checked vs Unchecked Exceptions

**Checked (extends Exception):**
- Kompilator wymusza obsługę
- Recoverable errors
- IOException, SQLException

**Unchecked (extends RuntimeException):**
- Kompilator nie wymusza
- Programming errors
- NullPointerException, IllegalArgumentException

### Generics - Type Erasure

**Compile time:**
```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
```

**Runtime (after erasure):**
```java
List strings = new ArrayList();  // Generic info usunięte!
List integers = new ArrayList();
```

**Konsekwencje:**
- Nie możesz: `new T()`, `new T[]`
- Nie możesz: `instanceof List<String>`
- Możesz: `instanceof List<?>`

### String Immutability

**Dlaczego String jest immutable?**

1. **String Pool:** Możliwy tylko dla immutable
2. **Security:** Stringi jako hasła, URL, paths
3. **Thread Safety:** Współdzielenie bez synchronizacji
4. **Caching:** hashCode() obliczany raz

### Interface vs Abstract Class

| Interface | Abstract Class |
|-----------|----------------|
| Tylko abstrakcja | Częściowa implementacja |
| `implements` (wiele) | `extends` (jedna) |
| Brak stanu (tylko constants) | Może mieć state |
| default methods (Java 8+) | Zawsze mogła mieć |
| "can-do" capability | "is-a" relationship |

---

## 2. Spring Interview Questions

### Bean Lifecycle

```
1. Instantiation
2. Populate properties (DI)
3. BeanNameAware.setBeanName()
4. BeanFactoryAware.setBeanFactory()
5. ApplicationContextAware.setApplicationContext()
6. BeanPostProcessor.postProcessBeforeInitialization()
7. @PostConstruct
8. InitializingBean.afterPropertiesSet()
9. Custom init-method
10. BeanPostProcessor.postProcessAfterInitialization()
--- BEAN READY ---
11. @PreDestroy
12. DisposableBean.destroy()
13. Custom destroy-method
```

### @Transactional Propagation

| Type | Behavior |
|------|----------|
| REQUIRED | Użyj istniejącej lub utwórz nową |
| REQUIRES_NEW | Zawsze nowa (suspend bieżącej) |
| NESTED | Nested transaction (savepoint) |
| SUPPORTS | Jeśli jest - użyj, jeśli nie - bez |
| NOT_SUPPORTED | Wykonaj bez transakcji |
| MANDATORY | Musi istnieć, inaczej exception |
| NEVER | Nie może istnieć transakcja |

### Why @Transactional on private doesn't work?

Spring używa **AOP proxy** do obsługi `@Transactional`.
Proxy opakowuje bean i przechwytuje wywołania.

**Problem:** Proxy nie widzi private metod (nie można ich override).

**Self-invocation problem:**
```java
public void methodA() {
    methodB();  // Wywołanie WEWNĘTRZNE - pomija proxy!
}

@Transactional
public void methodB() { }
```

### Circular Dependency

**Problem:**
```
BeanA requires BeanB
BeanB requires BeanA
```

**Rozwiązania:**
1. **Refactor** - wydziel wspólną logikę (najlepsze)
2. **@Lazy** - opóźnij tworzenie jednego beana
3. **Setter injection** - nie rekomendowane
4. **@PostConstruct** - ustaw zależność po utworzeniu

### @Component vs @Bean

| @Component | @Bean |
|------------|-------|
| Na klasie | Na metodzie w @Configuration |
| Auto-detected (scanning) | Explicit declaration |
| Stereotypy (@Service, etc.) | Dowolna nazwa |
| Masz źródło klasy | Third-party library |

---

## 3. System Design Basics

### Load Balancing

**Cel:** Rozłożyć ruch na wiele serwerów.

**Algorytmy:**
- **Round Robin:** Po kolei
- **Least Connections:** Do najmniej obciążonego
- **IP Hash:** Ten sam client → ten sam server
- **Weighted:** Proporcjonalnie do capacity

**L4 vs L7:**
- L4 (Transport): TCP/UDP, szybszy
- L7 (Application): HTTP, może routować po URL/headers

### Caching Strategies

**Cache-Aside:**
1. App sprawdza cache
2. Jeśli brak → pobierz z DB, zapisz w cache

**Read-Through:**
Cache sam pobiera z DB gdy brak.

**Write-Through:**
Zapis do cache + DB synchronicznie.

**Write-Behind:**
Zapis do cache, async do DB (ryzyko utraty danych).

### Database Scaling

**Vertical:** Więcej RAM/CPU (limit)

**Horizontal - Read Replicas:**
```
Writes → Primary
Reads  → Replica 1, Replica 2, ...
```

**Horizontal - Sharding:**
```
Users A-M → Shard 1
Users N-Z → Shard 2
```

### CAP Theorem

Distributed system może mieć max 2 z 3:
- **C**onsistency: Każdy read widzi najnowszy write
- **A**vailability: Każdy request dostaje response
- **P**artition tolerance: System działa mimo network failure

**W praktyce:** P jest konieczne, wybierasz między C i A.
- **CP:** Consistency (banking)
- **AP:** Availability (social media)

### Message Queues

**Kiedy używać?**
- Asynchronous processing
- Decoupling services
- Peak handling (buffer)
- Reliability (retry)

**Patterns:**
- **Point-to-point:** 1 producer → queue → 1 consumer
- **Pub/Sub:** 1 producer → topic → N consumers

---

## CZĘŚĆ 2: BEST PRACTICES

---

## 4. Code Quality

### Naming

```
// ❌ Bad
int d;
void proc();
class Mgr;

// ✅ Good
int daysUntilExpiry;
void processPayment();
class OrderManager;
```

**Zasady:**
- Klasy: Noun (User, OrderService)
- Metody: Verb (create, calculate, validate)
- Boolean: is/has/can (isActive, hasPermission)
- Avoid abbreviations (except common: id, url, html)

### Method Design

**Małe metody:**
```
// ❌ 100 linii logiki
void processOrder() { /* wszystko */ }

// ✅ Małe, fokusowane
void processOrder() {
    validateOrder();
    calculateTotals();
    applyDiscounts();
    saveOrder();
    sendConfirmation();
}
```

**Single level of abstraction:**
Nie mieszaj high-level i low-level operacji.

**Limit parameters:**
Max 3-4. Więcej → Object Parameter.

### Defensive Programming

```java
// Validate inputs
public void transfer(Account from, Account to, BigDecimal amount) {
    Objects.requireNonNull(from, "from account required");
    Objects.requireNonNull(to, "to account required");
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("amount must be positive");
    }
    // ...
}

// Prefer Optional over null
public Optional<User> findById(Long id) {
    return Optional.ofNullable(repository.find(id));
}

// Immutability
public record Money(BigDecimal amount, String currency) { }
```

### Error Handling

```java
// ❌ Catch everything
try {
    doSomething();
} catch (Exception e) {
    // swallow
}

// ✅ Specific exceptions, proper handling
try {
    doSomething();
} catch (BusinessException e) {
    log.warn("Business error: {}", e.getMessage());
    throw e;
} catch (TechnicalException e) {
    log.error("Technical error", e);
    throw new ServiceException("Internal error", e);
}
```

---

## 5. Common Anti-patterns

### God Class
Klasa która robi wszystko. 2000+ linii.

**Symptom:** Trudno nazwać bez "and".
**Fix:** Wydziel odpowiedzialności.

### Primitive Obsession
Używanie primitives zamiast domain objects.

```java
// ❌ Bad
void transfer(String fromAccount, String toAccount, double amount);

// ✅ Good
void transfer(AccountId from, AccountId to, Money amount);
```

### Feature Envy
Metoda więcej używa innej klasy niż swojej.

```java
// ❌ Feature Envy
class OrderService {
    BigDecimal calculateTotal(Order order) {
        return order.getItems().stream()
            .map(i -> i.getPrice().multiply(i.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

// ✅ Move to Order
class Order {
    BigDecimal calculateTotal() { ... }
}
```

### Anemic Domain Model
Entity bez logiki - tylko data + getters/setters.

```java
// ❌ Anemic
class Order {
    private List<OrderItem> items;
    // only getters/setters
}

// ✅ Rich Domain
class Order {
    private List<OrderItem> items;
    
    public void addItem(Product product, int quantity) {
        validateQuantity(quantity);
        items.add(new OrderItem(product, quantity));
    }
    
    public Money calculateTotal() { ... }
}
```

---

## 6. API Design

### REST Best Practices

**Resources (nouns, not verbs):**
```
✅ GET /users
❌ GET /getUsers
```

**HTTP Methods:**
```
GET    /users       - list
POST   /users       - create
GET    /users/{id}  - read one
PUT    /users/{id}  - replace
PATCH  /users/{id}  - update partial
DELETE /users/{id}  - delete
```

**Status Codes:**
```
200 OK           - success
201 Created      - resource created (POST)
204 No Content   - success, no body (DELETE)
400 Bad Request  - invalid input
401 Unauthorized - not authenticated
403 Forbidden    - not authorized
404 Not Found    - resource doesn't exist
409 Conflict     - duplicate
500 Internal     - server error
```

**Consistent Error Format:**
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Invalid input",
  "details": ["email: must be valid"],
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Versioning

```
// URL path (rekomendowane - jasne)
/api/v1/users

// Header
X-API-Version: 1

// Query param
/api/users?version=1
```

---

## 7. Interview Tips

### Technical Interview

**1. Clarify requirements**
Nie zakładaj. Pytaj o edge cases.

**2. Think out loud**
Mów co robisz i dlaczego.

**3. Start simple**
Brute force → optymalizuj.

**4. Write tests first (jeśli czas)**
Pokazuje TDD mindset.

**5. Analyze complexity**
Time & space. Big O.

### Behavioral (STAR method)

**S**ituation - Kontekst, projekt
**T**ask - Co miałeś zrobić
**A**ction - Co zrobiłeś (TY, nie "my")
**R**esult - Efekt, liczby jeśli możliwe

**Przykład:**
```
"Tell me about a time you disagreed with a teammate"

S: Pracowaliśmy nad migracją do microservices
T: Musiałem przekonać zespół do innego podejścia
A: Przygotowałem spike z obu rozwiązań, pokazałem dane
R: Zespół wybrał moje podejście, migracja szybsza o 30%
```

### Questions to Ask

```
- Jak wygląda typowy dzień w zespole?
- Jaki jest proces code review?
- Jak wygląda onboarding?
- Jakie są biggest challenges?
- Jak wygląda tech stack i czy planujecie zmiany?
- Jak mierzycie sukces w tej roli?
```

---

## Podsumowanie

| Topic | Key Points |
|-------|------------|
| **equals/==** | == referencje, equals() wartości |
| **HashMap** | hashCode() → bucket, equals() → find |
| **Generics** | Type erasure - info usunięte w runtime |
| **Transactions** | Propagation, proxy limitations |
| **System Design** | Load balancing, caching, CAP |
| **Code Quality** | Names, small methods, defensive |
| **REST** | Resources, HTTP methods, status codes |
| **Interview** | Clarify, think aloud, STAR |
