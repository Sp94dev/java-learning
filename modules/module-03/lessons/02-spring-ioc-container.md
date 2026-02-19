# Lesson 02: Spring IoC Container

> ApplicationContext, Bean, Component Scanning — the DI engine in Spring Boot.

## Concept

### What is the Spring IoC Container?

In Angular you have the **Injector** — an object that knows how to create services.
In Spring you have **ApplicationContext** — it's your Injector on steroids.

```
Angular:    Injector → creates @Injectable() services → injects
Spring:     ApplicationContext → creates @Component beans → injects
```

### ApplicationContext — "the registry of all beans"

When you run `./mvnw spring-boot:run`, Spring Boot:

1. **Scans** packages looking for annotations (`@Component`, `@Service`, etc.)
2. **Creates** instances of found classes (Beans)
3. **Injects** dependencies between them
4. **Stores** them in the `ApplicationContext`
5. **Serves** them when someone needs them

```
Application start:
  @SpringBootApplication
        ↓
  Component Scan (scans packages)
        ↓
  Found: InstrumentController, InstrumentService, InMemoryRepository
        ↓
  Spring creates: Repository → Service(repository) → Controller(service)
        ↓
  Everything sits in ApplicationContext — ready to use
```

### What is a Bean?

**Bean = an object managed by Spring.** You don't create it with `new` — Spring does.

| Aspect        | Regular object (`new`)  | Spring Bean                   |
| ------------- | ----------------------- | ----------------------------- |
| Who creates?  | You (`new MyService()`) | Spring (automatically)        |
| Who manages?  | You (lifecycle)         | Spring (init → use → destroy) |
| How many?     | As many as you `new`    | Default: 1 (Singleton)        |
| Dependencies? | Manually passed         | Spring injects automatically  |

**Angular Analogy:**

```typescript
// Angular: service registered as a Bean
@Injectable({
  providedIn: "root", // ≈ Singleton Bean in Spring
})
export class InstrumentService {}
```

```java
// Spring: service registered as a Bean
@Service  // ≈ @Injectable({ providedIn: 'root' })
public class InstrumentService { }
```

### Component Scanning — how Spring finds Beans

Spring scans the **main package** (where `@SpringBootApplication` is) and **all sub-packages**.

```
com.sp94dev.wallet/
├── WalletApplication.java       ← @SpringBootApplication (scans from here)
├── instrument/
│   ├── InstrumentController.java  ← @RestController → found! ✅
│   ├── InstrumentService.java     ← @Service → found! ✅
│   └── InMemoryInstrumentRepository.java ← @Repository → found! ✅
└── transaction/
    ├── TransactionController.java ← @RestController → found! ✅
    └── ...
```

**⚠️ Gotcha:** If a class is OUTSIDE the `com.sp94dev.wallet` package, Spring **won't find it**.

```
com.sp94dev.wallet/          ← scanned ✅
com.sp94dev.wallet.instrument/ ← scanned ✅ (sub-package)
com.sp94dev.other/           ← NOT scanned ❌ (different package!)
```

**Angular Analogy:** It's like Angular Module — if you don't declare a component
in `declarations` (or it doesn't have `providedIn`), Angular won't see it.

### @SpringBootApplication — what it does under the hood

```java
@SpringBootApplication  // = 3 annotations in one:
// @Configuration      → this class is a configuration
// @EnableAutoConfiguration → Spring Boot auto-configures (Tomcat, Jackson, etc.)
// @ComponentScan      → scan this package and sub-packages
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
        // ↑ here Spring creates ApplicationContext, scans, creates beans
    }
}
```

### How does Spring build the dependency graph?

Spring analyzes Bean constructors and automatically resolves dependencies:

```
Analysis:
  InstrumentController(InstrumentService) → needs InstrumentService
  InstrumentService(InstrumentRepository) → needs InstrumentRepository
  InMemoryInstrumentRepository() → needs nothing

Creation order (from "leaves" to "root"):
  1. InMemoryInstrumentRepository  (no dependencies)
  2. InstrumentService(repository) (injects repository)
  3. InstrumentController(service) (injects service)
```

**What if Spring can't resolve a dependency?**

```
***************************
APPLICATION FAILED TO START
***************************
Parameter 0 of constructor in InstrumentService required a bean
of type 'InstrumentRepository' that could not be found.
```

You'll see this error often — it means: "I need a Bean of type X, but I didn't find it
in the ApplicationContext." Solution? Add the appropriate annotation (`@Service`, `@Repository`, etc.).

## Exercise

**Task:** Explore your application's ApplicationContext.

1. Run Wallet Manager: `./mvnw spring-boot:run`
2. Look for this line in the logs: `Tomcat started on port 8080`
3. Temporarily add to `WalletApplication.java`:

```java
@SpringBootApplication
public class WalletApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(WalletApplication.class, args);

        // Print all beans
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("=== Loaded beans: " + beanNames.length + " ===");
        for (String name : beanNames) {
            System.out.println("  → " + name);
        }
    }
}
```

4. Find your beans on the list (instrument, transaction).
5. Notice how many beans Spring creates automatically (auto-configuration).

## Checklist

- [x] I know that ApplicationContext = container for all Beans
- [x] I understand Component Scanning (scans @SpringBootApplication package + sub-packages)
- [x] I know that Bean = object managed by Spring (not created via `new`)
- [x] I understand what @SpringBootApplication does under the hood (3 annotations)
- [x] I can diagnose a "bean not found" error (missing stereotype annotation)
