# TEORIA: Spring Framework

---

## 1. Co to jest Spring?

### Problem który rozwiązuje
W "czystej" Javie sam zarządzasz zależnościami:
- Sam tworzysz obiekty
- Sam wiążesz je ze sobą
- Sam zarządzasz ich cyklem życia
- Sam konfigurujesz

To prowadzi do:
- Tight coupling (klasy tworzą swoje zależności)
- Trudne testowanie (nie możesz podmienić zależności)
- Boilerplate code
- Rozproszona konfiguracja

### Spring jako IoC Container
**Inversion of Control** - odwrócenie kontroli:
- Ty NIE tworzysz obiektów → Spring je tworzy
- Ty NIE zarządzasz zależnościami → Spring je wstrzykuje
- Ty DEKLARUJESZ czego potrzebujesz → Spring dostarcza

**Analogia:** Zamiast gotować sam (kontrola), zamawiasz w restauracji (odwrócona kontrola) - deklarujesz co chcesz, kuchnia dostarcza.

---

## 2. Dependency Injection (DI)

### Koncept
Zależności są **wstrzykiwane** z zewnątrz, nie tworzone wewnątrz klasy.

**Bez DI:**
```
class OrderService {
    private PaymentService payment = new PaymentService();  // Tight coupling!
    private EmailService email = new EmailService();        // Nie możesz podmienić
}
```

**Z DI:**
```
class OrderService {
    private final PaymentService payment;  // Wstrzyknięte
    private final EmailService email;      // Wstrzyknięte
    
    OrderService(PaymentService payment, EmailService email) {
        this.payment = payment;  // Możesz wstrzyknąć mock!
        this.email = email;
    }
}
```

### Typy DI

**1. Constructor Injection ✅ (rekomendowane)**
- Wymuszone zależności (final)
- Immutable
- Łatwe testowanie
- Jasne co klasa potrzebuje

**2. Setter Injection**
- Opcjonalne zależności
- Mutable (można zmienić po utworzeniu)

**3. Field Injection ❌ (unikaj)**
- Ukryte zależności
- Trudne testowanie (wymaga refleksji)
- Nie można użyć final

### Dlaczego Constructor Injection jest najlepszy?

1. **Wymuszone zależności** - bez nich obiekt się nie utworzy
2. **Immutability** - pola `final`, brak setterów
3. **Testowanie** - `new Service(mockA, mockB)` - proste
4. **Fail-fast** - brakująca zależność = błąd przy starcie
5. **Dokumentacja** - konstruktor mówi czego klasa potrzebuje

---

## 3. Spring IoC Container

### ApplicationContext
"Kontener" który:
1. Tworzy obiekty (beany)
2. Zarządza ich cyklem życia
3. Wstrzykuje zależności
4. Przechowuje konfigurację

### Bean
Obiekt zarządzany przez Spring.

**Tworzenie beana:**
1. **Annotation-based:** `@Component`, `@Service`, `@Repository`, `@Controller`
2. **Java Config:** `@Bean` w `@Configuration` klasie
3. **XML** (legacy, rzadko używane)

### Stereotypy (semantyczne @Component)

| Adnotacja | Warstwa | Semantyka |
|-----------|---------|-----------|
| `@Component` | Dowolna | Generic bean |
| `@Service` | Business | Logika biznesowa |
| `@Repository` | Data | Dostęp do danych + exception translation |
| `@Controller` | Web | HTTP endpoints |
| `@RestController` | Web | REST API (@Controller + @ResponseBody) |
| `@Configuration` | Config | Źródło definicji beanów |

**Technicznie identyczne** - różnica semantyczna (dokumentacja dla programisty).

### Component Scanning
Spring skanuje pakiety w poszukiwaniu klas z adnotacjami.

```
@SpringBootApplication  // włącza scanning od tego pakietu w dół
package com.example.app;

com.example.app/
├── controller/  ← skanowane
├── service/     ← skanowane
└── repository/  ← skanowane

com.other/       ← NIE skanowane (inny pakiet bazowy)
```

---

## 4. Bean Lifecycle

### Fazy życia beana

```
1. INSTANTIATION
   └─ Spring wywołuje konstruktor
   
2. POPULATE PROPERTIES
   └─ Wstrzykiwanie zależności (DI)
   
3. BEAN NAME AWARE
   └─ setBeanName() jeśli implementuje BeanNameAware
   
4. BEAN FACTORY AWARE
   └─ setBeanFactory() jeśli implementuje BeanFactoryAware
   
5. APPLICATION CONTEXT AWARE
   └─ setApplicationContext() jeśli implementuje ApplicationContextAware
   
6. PRE-INITIALIZATION (BeanPostProcessor)
   └─ postProcessBeforeInitialization()
   
7. INITIALIZATION
   ├─ @PostConstruct
   ├─ InitializingBean.afterPropertiesSet()
   └─ Custom init-method
   
8. POST-INITIALIZATION (BeanPostProcessor)
   └─ postProcessAfterInitialization()
   
═══════════════════════════════════════
          BEAN READY TO USE
═══════════════════════════════════════

9. DESTRUCTION (przy shutdown)
   ├─ @PreDestroy
   ├─ DisposableBean.destroy()
   └─ Custom destroy-method
```

### @PostConstruct / @PreDestroy
```java
@Service
class DataInitializer {
    
    @PostConstruct
    void init() {
        // Wywoływane PO wstrzyknięciu zależności
        // Przed udostępnieniem beana
        loadInitialData();
    }
    
    @PreDestroy
    void cleanup() {
        // Wywoływane przy shutdown aplikacji
        closeConnections();
    }
}
```

### BeanPostProcessor
Hook do modyfikacji beanów przed/po inicjalizacji. Tak działają adnotacje jak `@Transactional`, `@Async`.

---

## 5. Bean Scopes

### Singleton (default)
Jedna instancja per ApplicationContext.

- **Kiedy:** stateless services, repozytoria
- **Uwaga:** musi być thread-safe (shared between requests)

### Prototype
Nowa instancja przy każdym żądaniu beana.

- **Kiedy:** stateful objects, builders
- **Uwaga:** Spring NIE zarządza destruction

### Web Scopes
- **Request** - nowa instancja per HTTP request
- **Session** - nowa instancja per HTTP session
- **Application** - per ServletContext

### Scope mismatch problem
```
Singleton Service
    └─ injects → Prototype Bean  ← TEN SAM obiekt!
```

Singleton dostaje prototype raz przy utworzeniu. Rozwiązania:
1. `@Lookup` method injection
2. `ObjectFactory<T>` / `Provider<T>`
3. Proxy scope (`proxyMode = ScopedProxyMode.TARGET_CLASS`)

---

## 6. Spring AOP (Aspect-Oriented Programming)

### Koncept
**Cross-cutting concerns** - funkcjonalności przecinające wiele warstw:
- Logging
- Security
- Transactions
- Caching
- Error handling

Zamiast powtarzać ten sam kod w każdej metodzie, definiujesz go raz jako **aspect**.

### Terminologia

| Term | Znaczenie |
|------|-----------|
| **Aspect** | Moduł cross-cutting concern |
| **Join Point** | Punkt w wykonaniu (metoda, exception) |
| **Pointcut** | Wyrażenie wybierające join points |
| **Advice** | Akcja wykonywana w join point |
| **Weaving** | Łączenie aspektów z kodem |

### Typy Advice

- **@Before** - przed metodą
- **@After** - po metodzie (zawsze)
- **@AfterReturning** - po pomyślnym return
- **@AfterThrowing** - po exception
- **@Around** - opakowuje metodę (najpotężniejszy)

### Jak Spring AOP działa?

**Proxy Pattern:**
```
Client → Proxy → Target
         ↓
      [Advice]
```

Spring tworzy **proxy** wokół beana:
1. **JDK Dynamic Proxy** - dla interfejsów
2. **CGLIB Proxy** - dla klas (subclassing)

**Ważna konsekwencja:** Self-invocation nie przechodzi przez proxy!
```java
@Service
class MyService {
    @Transactional
    public void methodA() {
        methodB();  // ❌ NIE przechodzi przez proxy!
    }
    
    @Transactional
    public void methodB() { }
}
```

### Przykłady użycia w Spring

**@Transactional** - zarządzanie transakcjami
**@Cacheable** - cache wyników
**@Async** - asynchroniczne wykonanie
**@Secured** - sprawdzenie uprawnień

---

## 7. Spring Boot

### Co dodaje?
Spring Boot to **opinionated** konfiguracja Spring:
- **Auto-configuration** - domyślne ustawienia
- **Starter dependencies** - pakiety zależności
- **Embedded server** - Tomcat/Jetty/Undertow w JAR
- **Production-ready features** - metrics, health checks

### Auto-configuration
Spring Boot wykrywa klasy w classpath i automatycznie konfiguruje beany.

```
Masz spring-boot-starter-data-jpa w pom.xml
    ↓
Boot wykrywa DataSource
    ↓
Auto-konfiguruje EntityManagerFactory, TransactionManager
    ↓
Możesz używać @Repository bez konfiguracji
```

### @SpringBootApplication
Łączy trzy adnotacje:
1. `@Configuration` - źródło beanów
2. `@EnableAutoConfiguration` - włącza auto-config
3. `@ComponentScan` - skanuje pakiety

### application.properties / application.yml
Externalized configuration:
```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost/db
logging.level.com.example=DEBUG
```

### Profiles
Różne konfiguracje per środowisko:
- `application.properties` - default
- `application-dev.properties` - development
- `application-prod.properties` - production

```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## 8. Spring MVC

### Architektura

```
HTTP Request
     ↓
┌─────────────────────────────┐
│     DispatcherServlet       │  ← Front Controller
│     (deleguje wszystko)     │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│      Handler Mapping        │  ← Znajduje controller
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│       Controller            │  ← Twój kod
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│     ViewResolver /          │  ← JSON serialization
│   MessageConverter          │     (dla REST)
└──────────────┬──────────────┘
               ↓
         HTTP Response
```

### Request Processing

1. **DispatcherServlet** otrzymuje request
2. **HandlerMapping** mapuje URL → Controller method
3. **HandlerAdapter** wywołuje metodę z argumentami
4. **ArgumentResolvers** mapują request na parametry (@PathVariable, @RequestBody)
5. **Controller** przetwarza i zwraca
6. **ReturnValueHandler** konwertuje response
7. **MessageConverter** serializuje do JSON (Jackson)

### Content Negotiation
Spring automatycznie wybiera format response:
- `Accept: application/json` → Jackson
- `Accept: application/xml` → JAXB

---

## 9. Spring Data

### Repository Pattern
Abstrakcja dostępu do danych - definiujesz interfejs, Spring dostarcza implementację.

### Query Methods
Spring generuje SQL z nazwy metody:
```
findByLastName          → WHERE last_name = ?
findByAgeGreaterThan    → WHERE age > ?
findByActiveTrue        → WHERE active = true
countByStatus           → SELECT COUNT(*) WHERE status = ?
deleteByExpiredTrue     → DELETE WHERE expired = true
```

### Jak to działa?

1. Spring skanuje interfejsy extends `Repository`
2. Dla każdego tworzy **proxy** w runtime
3. Proxy parsuje nazwę metody → generuje query
4. Proxy wywołuje EntityManager/JdbcTemplate

---

## 10. Typowe błędy

### Circular Dependencies
```
BeanA → BeanB → BeanA  (cykl!)
```
**Rozwiązania:**
1. Refaktoruj - wydziel wspólną logikę
2. @Lazy na jednej zależności
3. Setter injection (nie rekomendowane)

### @Transactional na private
```java
@Transactional
private void save() { }  // ❌ NIE DZIAŁA!
```
AOP proxy nie widzi private metod.

### Self-invocation bypasses proxy
```java
public void methodA() {
    methodB();  // ❌ @Transactional na methodB nie zadziała
}
```
Wywołanie wewnętrzne nie przechodzi przez proxy.

### Missing @Repository exception translation
Bez `@Repository` wyjątki JDBC nie są tłumaczone na Spring hierarchy.

---

## Podsumowanie

| Koncept | Rola |
|---------|------|
| **IoC Container** | Zarządza beanami i zależnościami |
| **DI** | Wstrzykuje zależności |
| **Bean** | Obiekt zarządzany przez Spring |
| **Scope** | Lifecycle beana (singleton, prototype, request) |
| **AOP** | Cross-cutting concerns przez proxy |
| **Auto-config** | Domyślna konfiguracja w Boot |
| **MVC** | Request → Controller → Response |
| **Data** | Query methods, abstraction nad DB |
