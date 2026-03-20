# Roadmapa - Full Version

> `opt` = optional

## PHASE 1: FUNDAMENTALS

### Module 00: Setup + Tooling ✅

**Goal:** Working environment.

- JDK 25, Maven, Docker, VS Code
- Spring Boot 4.0.1 project
- SDKMAN — zarządzanie wersjami JDK

### Module 01: REST + Java Basics ✅

**Goal:** First API, Records, Streams.

- [x] Controller, HTTP methods
- [x] Java Records as DTOs
- [x] Stream API (Advanced: sorting, grouping)
- [x] In-memory storage
- [x] Service layer, ResponseEntity
- [x] OpenAPI Documentation

### Module 02: Java Internals ✅

**Goal:** Understand how Java works under the hood + core language features.

#### JVM & Memory

- [ ] JVM Architecture — Class Loader, Runtime Data Areas, Execution Engine
- [ ] JIT Compilation — Interpreter vs JIT, hot spots, warm-up
- [ ] Memory Model — Stack vs Heap, primitives vs objects, stack frames
- [ ] Pass by Value — reference copying
- [ ] `opt` String Pool — interning, immutability, StringBuilder
- [ ] Garbage Collection — generational hypothesis, GC algorithms
  - 💡 _AI Context: Alokacja pamięci przy przetwarzaniu dużych wektorów (embeddings) na potrzeby Semantic Search — wpływ na Heap, large object allocation, GC pressure._
- [ ] Memory Leaks — static references, unclosed resources, listener leaks

#### Core Language Features

- [ ] Generics + Type Erasure — bounded types, wildcards, erasure na runtime
- [ ] Collections Framework — List, Set, Map, Queue, implementacje, kiedy co
- [ ] Enums jako klasy — metody, pola, implementacja interfejsów
- [ ] Optional + Null Handling — Optional<T>, Objects.requireNonNull()
- [ ] Functional Interfaces + Lambdy — Predicate, Function, Consumer, Supplier, ::
- [ ] Date/Time API — LocalDate, ZonedDateTime, Instant, Duration, Period
- [ ] Sealed Classes + Pattern Matching — restricted hierarchies, instanceof patterns
- [ ] Switch Expressions — arrow syntax, pattern matching w switch
- [ ] `var` — local variable type inference, kiedy (nie) używać
- [ ] `final` keyword — klasy, metody, zmienne

#### Exceptions & Wrappers

- [ ] Exceptions — Checked vs Unchecked, try-with-resources, hierarchy
- [ ] Primitives vs Wrappers — autoboxing, Integer Cache, identity vs equality

#### Context

- [ ] Java EE → Jakarta EE → Spring — historia i most do Springa

---

## FAZA 2: ARCHITEKTURA

### Moduł 03: DI + Warstwy ✅

**Cel:** Oddzielić logikę od HTTP.

- [x] Spring IoC Container
- [x] Constructor Injection
- [x] @Service, @Repository, @Component
- [x] Lombok
- 💡 _AI Context: Zrozumienie DI/IoC jako fundamentu do łatwego wstrzykiwania komponentów LLM (ChatClient, EmbeddingModel) i baz wektorowych (VectorStore) za pomocą Spring AI. Ten sam mechanizm, który wstrzykuje Repository, wstrzyknie Ci klienta do Azure OpenAI._

### Moduł 04: Architektura Aplikacji 🟡

**Cel:** Poznać popularne wzorce architektoniczne.

- [x] Layered Architecture — Controller → Service → Repository → Database
- [x] Package by Feature
- [x] Repository / DTO / Factory / Builder patterns
- Clean Architecture / Hexagonal — Ports & Adapters, Dependency Rule
- Spring Modulith — modularność, eventy, @Externalized, UML zależności
- `opt` Enterprise Integration Patterns — overview (file, DB, RPC, messaging)
- Monolith vs Microservices — trade-offs, "monolith first"

---

## FAZA 3: DANE

### Moduł 05: JPA + PostgreSQL

**Cel:** Trwałość danych.

- Docker Compose + PostgreSQL (docelowo: **Azure Database for PostgreSQL**)
- Entity, Repository
- Spring Data JPA
- @Transactional
- **pgvector** — rozszerzenie PostgreSQL do przechowywania wektorów (embeddings) obok danych transakcyjnych
  - 💡 _Knowledge Engineering: Jedno źródło prawdy — dane relacyjne + wektory w jednej bazie. Kluczowe dla architektury RAG._
- MongoDB — Spring Data Mongo, teoria + porównanie z SQL
- Spring Batch — ETL, import CSV → DB

### Moduł 06: Bazy Danych Deep Dive 🆕

**Cel:** Efektywna praca z bazą.

#### SQL & Performance

- JOINs (INNER, LEFT, RIGHT)
- Indexy — kiedy i jak
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
- Cache invalidation patterns

---

## FAZA 4: SECURITY

### Moduł 08: Autentykacja & Autoryzacja 🆕

**Cel:** Zabezpieczyć API.

#### Fundamentals

- Authentication vs Authorization
- Stateless vs Stateful

#### Spring Security

- Security Filter Chain
- UserDetailsService
- Password encoding (BCrypt)

#### JWT

- Token structure (Header, Payload, Signature)
- Access Token + Refresh Token
- Token storage (gdzie NIE przechowywać)

#### OAuth2

- Flow types + integracja z Google/GitHub
- OAuth Authorization Server — implementacja

#### Modern Auth

- Passkeys / WebAuthn — nowoczesna autentykacja

#### Best Practices

- CORS configuration
- HTTPS everywhere
- Rate limiting

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
- `opt` Testing for Modularity — Spring Modulith tests

### Moduł 11: Debugowanie & Profiling 🆕

**Cel:** Znajdować i rozwiązywać problemy.

#### Debugging

- Debugger (breakpoints, conditional, exception)
- `opt` Remote debugging
- Logging best practices (SLF4J, Logback)

#### Monitoring

- Spring Actuator — health, metrics, endpointy produkcyjne
- Stack trace reading

#### Profiling

- `opt` JVisualVM / JConsole — memory/CPU profiling
- `opt` GraalVM native image — kompilacja do natywnego binarnego

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

### Moduł 14: Cloud Deployment — Microsoft Azure ☁️

**Cel:** Wdrożenie produkcyjne w ekosystemie Azure.

| Platform                | Complexity  | Cost        | Approach   |
| ----------------------- | ----------- | ----------- | ---------- |
| Azure App Service       | Low         | Free tier   | Startup    |
| Azure Spring Apps / AKS | Medium-High | Pay-per-use | Enterprise |

#### Startup: Azure App Service

- Deploy from GitHub (GitHub Actions → Azure)
- Environment config (App Settings, Key Vault)
- Azure Database for PostgreSQL — Flexible Server provisioning

#### Enterprise: Azure Spring Apps

- Managed Spring Boot hosting
- Azure API Management — gateway, rate limiting
- Azure Kubernetes Service (AKS) — basics
- Azure Container Registry (ACR)

#### Azure Basics (awareness)

- Azure Resource Groups, VNets, Azure Monitor
- Azure Key Vault — secrets management
- `opt` Azure Service Bus — messaging

#### Infrastructure as Code (IaC)

- **Terraform** — deklaratywne zarządzanie infrastrukturą Azure (Resource Group, App Service, PostgreSQL, Key Vault)
  - HCL syntax, `plan` → `apply` workflow, state management
  - 💡 _Najbardziej pożądany IaC tool na rynku — cross-cloud, ogromna społeczność._

---

## FAZA 7: PRAKTYKA

### Moduł 15: Dobre i Złe Praktyki 🆕

**Cel:** Pisać kod jak Senior.

- Code quality, anti-patterns
- Spring-specific pitfalls
- REST API best practices

### Moduł 16: Interview Prep 🆕

**Cel:** Przejść rozmowę rekrutacyjną.

- Java Core Questions
- Spring Questions
- System Design Basics
- Behavioral (STAR method)

### Moduł 17: Frontend Integration 🆕

**Cel:** Połączyć Java backend z Angular.

- Static Files (Monolith)
- Proxy Setup (Angular + Spring Boot)
- Docker Compose (Nginx + Java)

---

## FAZA 8: ZAAWANSOWANE

### 🆕 Moduł 18: Web Beyond REST

**Cel:** Alternatywne protokoły komunikacji.

- GraphQL w Spring — @QueryMapping, @SchemaMapping, @BatchMapping
- `opt` gRPC + Protocol Buffers — high-performance RPC
- Deklaratywny HTTP Client — @HttpExchange

### 🆕 Moduł 19: Messaging

**Cel:** Event-driven architecture.

- Kafka — producer/consumer, serializacja JSON
- `opt` Spring Integration — flows, channels, adaptery
- Event-driven architecture + Outbox pattern

### 🆕 Moduł 20: Spring AI + Azure OpenAI 🧠

**Cel:** Budowa enterprise-grade AI features w Java.

- Spring AI — ChatClient, EmbeddingModel, VectorStore
- **Azure OpenAI Service** — wdrożenie modeli GPT w środowisku korporacyjnym (compliance, data residency)
- pgvector jako VectorStore — similarity search na danych transakcyjnych
- RAG (Retrieval-Augmented Generation) — wzbogacanie promptów danymi z bazy
- Tool Calling + MCP — agenci AI w Spring (function calling)
- `opt` LangChain4j — alternatywa dla Spring AI
- 💡 _Enterprise AI: Ten moduł łączy Twoje wykształcenie w Inżynierii Wiedzy z praktycznym wdrożeniem RAG w bezpiecznym środowisku Azure._

---

## PROJEKT KOŃCOWY

### Wallet Manager - Full Implementation

Aplikacja łącząca wszystkie moduły:

**Features (per [PRD](docs/prd.md)):**

- [ ] CRUD Instruments & Transactions (z `fee` i `platform_id`)
- [ ] Import CSV (XTB format — zamknięta pozycja = BUY + SELL)
- [ ] Dashboard (wartość portfela, koszt, zysk)
- [ ] Prices cache (Redis / Caffeine)
- [ ] **🧠 AI Financial Advisor** — przesyłanie ostatnich N transakcji do Azure OpenAI w celu uzyskania spersonalizowanej porady finansowej (Spring AI + RAG)
- [ ] **pgvector** — embeddings transakcji do semantic search ("pokaż transakcje podobne do...")
- [ ] Full test coverage (Testcontainers)
- [ ] Dockerized (multi-stage)
- [ ] Deployed to **Azure** (App Service + Terraform IaC)
- [ ] CI/CD pipeline (GitHub Actions → Azure)

> ⚠️ Per PRD: brak multi-user auth, multi-currency, FIFO/podatków, realtime.

**Architecture:**

```
┌─────────────────────────────────────────────────┐
│   InstrumentController   TransactionController  │
│                AdvisorController                 │
├─────────────────────────────────────────────────┤
│   InstrumentService      TransactionService     │
│                AdvisorService                    │
│          ↓                    ↓         ↓       │
│     DashboardCalculator     Spring AI (RAG)     │
├─────────────────────────────────────────────────┤
│     JPA Repositories     Redis Cache (prices)   │
├─────────────────────────────────────────────────┤
│   PostgreSQL (+ pgvector)       Redis           │
├─────────────────────────────────────────────────┤
│              Azure Cloud (Terraform)            │
│   App Service  ·  Azure DB  ·  Azure OpenAI     │
└─────────────────────────────────────────────────┘
```

---

## Timeline (12 miesięcy)

| Faza            | Moduły         | Czas  |
| --------------- | -------------- | ----- |
| 1. Fundamenty   | 00-02          | 6 tyg |
| 2. Architektura | 03-04          | 4 tyg |
| 3. Dane         | 05-07          | 7 tyg |
| 4. Security     | 08             | 3 tyg |
| 5. Quality      | 09-11          | 5 tyg |
| 6. DevOps       | 12-14          | 5 tyg |
| 7. Praktyka     | 15-17          | 4 tyg |
| 8. Zaawansowane | 18-20          | 6 tyg |
| Projekt         | Wallet Manager | 6 tyg |

**Total: ~46 tygodni + buffer**

---

## 🎯 CERTYFIKACJA

### Cel długoterminowy

- [ ] **Microsoft Certified: Azure AI Engineer Associate (AI-102)**
  - Zakres: Azure Cognitive Services, Azure OpenAI, Knowledge Mining, Document Intelligence
  - Dlaczego: Formalne potwierdzenie kompetencji w budowaniu rozwiązań AI na Azure — kluczowe dla pozycji "Enterprise AI Solutions Architect"
  - Kiedy: Po ukończeniu Modułu 14 (Azure) i Modułu 20 (Spring AI)
- [ ] `opt` **Microsoft Certified: Azure Developer Associate (AZ-204)**
  - Zakres: Azure App Service, Functions, Cosmos DB, Storage, Security
  - Wzmocnienie profilu cloud-native
