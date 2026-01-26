# Roadmapa - Full Version

## FAZA 1: FUNDAMENTY

### Moduł 00: Setup + Tooling ✅

**Cel:** Działające środowisko.

- JDK 25, Maven, Docker, VS Code
- Spring Boot 4.0.1 project

### Moduł 01: REST + Java Basics 🟡

**Cel:** Pierwsze API, Records, Streams.

- [x] Controller, HTTP methods
- [ ] Java Records jako DTO
- [x] Stream API (Basic usage)
- [x] In-memory storage

### Moduł 02: Java Internals 🆕

**Cel:** Zrozumieć jak Java działa pod spodem.

#### 1. JVM Architecture
- [ ] Class Loader subsystem
- [ ] Runtime Data Areas (Stack, Heap, Method Area)
- [ ] Execution Engine

#### 2. JIT Compilation
- [ ] Interpreter vs JIT
- [ ] Hot spots & Warm-up
- [ ] Native code generation

#### 3. Memory Model
- [ ] Stack vs Heap details
- [ ] Primitives vs Objects storage
- [ ] Stack frames

#### 4. Pass by Value
- [ ] Zrozumienie mechanizmu przekazywania
- [ ] Reference copying

#### 5. String Pool
- [ ] Interning stringów
- [ ] Immutability & Security
- [ ] StringBuilder

#### 6. Garbage Collection
- [ ] Reachability analysis
- [ ] Generational Hypothesis (Eden, Survivor, Old)
- [ ] GC Algorithms types

#### 7. Memory Leaks
- [ ] Static references
- [ ] Unclosed resources
- [ ] Listener leaks

#### 8. Primitives vs Wrappers
- [ ] Autoboxing/Unboxing cost
- [ ] Integer Cache
- [ ] Identity vs Equality

#### 9. Exceptions
- [ ] Hierarchy (Error vs Exception)
- [ ] Checked vs Unchecked philosophy
- [ ] Try-with-resources

---

## FAZA 2: ARCHITEKTURA

### Moduł 03: DI + Warstwy

**Cel:** Oddzielić logikę od HTTP.

- Spring IoC Container
- Constructor Injection
- @Service, @Repository, @Component
- Lombok

### Moduł 04: Architektura Aplikacji 🆕

**Cel:** Poznać popularne wzorce architektoniczne.

#### Layered Architecture (domyślna)

```
Controller → Service → Repository → Database
```

- Kiedy wystarczy
- Problemy przy większych projektach

#### Clean Architecture / Hexagonal

```
        [Adapters]
            ↓
[Ports] ← Domain → [Ports]
            ↑
        [Adapters]
```

- Domain-centric design
- Ports & Adapters
- Dependency Rule

#### Package Structure

```
# By layer (proste projekty)
com.example/
├── controller/
├── service/
└── repository/

# By feature (większe projekty)
com.example/
├── wallet/
│   ├── WalletController
│   ├── WalletService
│   └── WalletRepository
└── transaction/
    └── ...

# Hexagonal
com.example/
├── domain/
├── application/
├── infrastructure/
└── api/
```

#### Wzorce

- Repository Pattern
- DTO Pattern
- Factory Pattern
- Builder Pattern (Lombok @Builder)

---

## FAZA 3: DANE

### Moduł 05: JPA + PostgreSQL

**Cel:** Trwałość danych.

- Docker Compose + PostgreSQL
- Entity, Repository
- Spring Data JPA
- @Transactional

### Moduł 06: Bazy Danych Deep Dive 🆕

**Cel:** Efektywna praca z bazą.

#### SQL Fundamentals

- JOINs (INNER, LEFT, RIGHT)
- Indexy - kiedy i jak
- EXPLAIN ANALYZE
- N+1 problem (i jak go rozwiązać)

#### JPA Advanced

- Lazy vs Eager loading
- Entity relationships (@OneToMany, @ManyToOne)
- Cascade types
- Entity lifecycle (Transient, Managed, Detached, Removed)

#### Migrations

- Flyway setup
- Versioned migrations
- Rollback strategies

#### Query Optimization

- @Query custom queries
- Projections (interface-based, class-based)
- Pagination (Pageable, Slice)
- Batch operations

### Moduł 07: Caching 🆕

**Cel:** Przyspieszenie aplikacji.

#### In-Memory Cache

- Spring @Cacheable, @CacheEvict
- Caffeine (local cache)
- Cache strategies (TTL, LRU)

#### Distributed Cache

- Redis basics
- Spring Data Redis
- Session storage
- Cache invalidation patterns

---

## FAZA 4: SECURITY

### Moduł 08: Autentykacja & Autoryzacja 🆕

**Cel:** Zabezpieczyć API.

#### Fundamentals

- Authentication vs Authorization
- Stateless vs Stateful
- Session-based vs Token-based

#### Spring Security

- Security Filter Chain
- UserDetailsService
- Password encoding (BCrypt)

#### JWT

- Token structure (Header, Payload, Signature)
- Access Token + Refresh Token
- Token storage (gdzie NIE przechowywać)

#### OAuth2 Basics

- Flow types
- Integration z Google/GitHub

#### Best Practices

- CORS configuration
- HTTPS everywhere
- Rate limiting
- Input validation (security perspective)

---

## FAZA 5: QUALITY

### Moduł 09: Walidacja + Error Handling

**Cel:** Czytelne błędy.

- @Valid, Bean Validation
- @ControllerAdvice
- Problem Details (RFC 7807)

### Moduł 10: Testy

**Cel:** Pewność że kod działa.

- JUnit 5 + AssertJ
- Mockito
- @WebMvcTest, @DataJpaTest
- Testcontainers

### Moduł 11: Debugowanie & Profiling 🆕

**Cel:** Znajdować i rozwiązywać problemy.

#### Debugging

- IntelliJ/VS Code debugger
- Breakpoints (conditional, exception)
- Remote debugging
- Logging best practices (SLF4J, Logback)

#### Profiling

- JVisualVM / JConsole
- Memory dumps analysis
- CPU profiling
- Flame graphs

#### Troubleshooting

- Common exceptions i co oznaczają
- Stack trace reading
- Thread dumps
- OutOfMemoryError hunting

---

## FAZA 6: DEVOPS

### Moduł 12: Konteneryzacja

**Cel:** Aplikacja w Dockerze.

- Dockerfile (multi-stage build)
- Docker Compose (app + db + redis)
- Environment variables
- Health checks

### Moduł 13: CI/CD 🆕

**Cel:** Automatyzacja.

#### GitHub Actions

- Build & Test pipeline
- Docker image build & push
- Environment secrets

#### Quality Gates

- SonarQube basics
- Code coverage requirements
- Dependency scanning

### Moduł 14: Cloud Deployment 🆕

**Cel:** Wdrożenie produkcyjne.

#### Options Overview

| Platform       | Complexity | Cost        |
| -------------- | ---------- | ----------- |
| Railway/Render | Low        | Free tier   |
| Heroku         | Low        | Paid        |
| AWS ECS        | Medium     | Pay-per-use |
| Kubernetes     | High       | Varies      |

#### Practical: Railway/Render

- Deploy from GitHub
- Environment config
- Database provisioning
- Custom domain

#### AWS Basics (optional)

- EC2 vs ECS vs Lambda
- RDS for PostgreSQL
- S3 for files
- Basic networking (VPC, Security Groups)

---

## FAZA 7: PRAKTYKA

### Moduł 15: Dobre i Złe Praktyki 🆕

**Cel:** Pisać kod jak Senior.

#### Code Quality

```java
// ❌ Bad
public void process(String s) {
    if (s != null) {
        if (!s.isEmpty()) {
            // logic
        }
    }
}

// ✅ Good
public void process(String input) {
    if (input == null || input.isBlank()) {
        return;
    }
    // logic
}
```

#### Common Anti-patterns

- God class
- Primitive obsession
- Feature envy
- Anemic domain model

#### Spring-specific

- Field injection (❌) vs Constructor injection (✅)
- @Transactional na private method (nie działa!)
- Circular dependencies
- N+1 queries

#### REST API

- Proper HTTP status codes
- Consistent error format
- Versioning strategies
- HATEOAS (kiedy warto)

### Moduł 16: Interview Prep 🆕

**Cel:** Przejść rozmowę rekrutacyjną.

#### Java Core Questions

- equals() vs ==
- HashMap internals
- Immutability
- Exception handling (checked vs unchecked)
- Generics (Type erasure)

#### Spring Questions

- Bean lifecycle
- @Transactional propagation
- Circular dependency resolution
- Profiles & conditional beans

#### System Design Basics

- Load balancing
- Database scaling (read replicas, sharding)
- Message queues (kiedy używać)
- Microservices vs Monolith

#### Coding Challenges

- LeetCode Easy/Medium (Arrays, Strings, HashMaps)
- Live coding tips
- Thinking out loud

#### Behavioral

- STAR method
- "Tell me about a time..."
- Questions to ask interviewer

---

## PROJEKT KOŃCOWY

### Wallet Manager - Full Implementation

Aplikacja łącząca wszystkie moduły:

**Features:**

- [ ] User registration & JWT auth
- [ ] CRUD Instruments & Transactions
- [ ] Import CSV (XTB format)
- [ ] Dashboard (wartość, koszt, zysk)
- [ ] Redis cache dla cen
- [ ] Full test coverage
- [ ] Dockerized
- [ ] Deployed to cloud
- [ ] CI/CD pipeline

**Architecture:**

```
┌─────────────────────────────────────────┐
│              API Gateway                │
├─────────────────────────────────────────┤
│           Spring Security               │
├─────────────────────────────────────────┤
│  InstrumentController  TransactionController
├─────────────────────────────────────────┤
│  InstrumentService     TransactionService
│          ↓                    ↓
│     PortfolioCalculator (domain logic)
├─────────────────────────────────────────┤
│     JPA Repositories    Redis Cache
├─────────────────────────────────────────┤
│        PostgreSQL         Redis
└─────────────────────────────────────────┘
```

---

## Timeline (12 miesięcy)

| Faza            | Moduły         | Czas  |
| --------------- | -------------- | ----- |
| 1. Fundamenty   | 00-02          | 6 tyg |
| 2. Architektura | 03-04          | 4 tyg |
| 3. Dane         | 05-07          | 6 tyg |
| 4. Security     | 08             | 3 tyg |
| 5. Quality      | 09-11          | 5 tyg |
| 6. DevOps       | 12-14          | 4 tyg |
| 7. Praktyka     | 15-16          | 4 tyg |
| Projekt         | Wallet Manager | 6 tyg |

**Total: ~38 tygodni + buffer**
