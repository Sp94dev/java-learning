# TODO: Wallet Manager

## Phase 1: REST + Java Basics (Module 01) ✅

### Instrument Controller (`/api/instruments`)

- [x] **Get single instrument**: Implemented `GET /api/instruments/{id}` with `Optional` handling and `404 Not Found`.
- [x] **Filter instruments**: Implemented `GET /api/instruments` with multiple `@RequestParam`, sorting, and limiting (Stream API).
- [x] **Create instrument**: Implemented `POST /api/instruments` returning `201 Created` and `Location` header.
- [x] **Full CRUD**: Added `PUT` (update) and `DELETE` (204 No Content) support.

### Transaction Controller (`/api/transactions`)

- [x] **Create controller**: Implemented `TransactionController`.
- [x] **Add transaction**: Handling `POST /api/transactions` with linking to instrument by ID.
- [x] **Statistics**: Added `GET /api/transactions/stats` (Grouping and summing using `Collectors.groupingBy`).

### Architecture & Best Practices

- [x] **Layering**: Full separation of Controller -> Service -> Repository.
- [x] **Modeling**: Usage of `Java Records` for models and DTOs.
- [x] **ResponseEntity**: Correct HTTP status codes throughout the API.
- [x] **OpenAPI / Swagger**: Added interactive documentation (`springdoc-openapi`) with English descriptions.

## Phase 2: DI Refactor (Module 03) 🟡

- [ ] **Interface extraction**: Create `InstrumentRepository` / `TransactionRepository` interfaces.
- [ ] **Constructor Injection**: Replace `new` with Spring IoC (`@Service`, `@Repository`).
- [ ] **Request DTOs**: Separate `CreateInstrumentRequest` / `CreateTransactionRequest` from domain models.

## Phase 3: Validation + Error Handling (Module 09) 🆕

- [ ] **Bean Validation**: Add `@Valid` + constraint annotations on Request DTOs.
- [ ] **Error Handling**: `@ControllerAdvice` + `Problem Details` standard (RFC 7807).

## Phase 4: Java Internals & Quality (Module 02 + 10) 🆕

- [ ] **Profiling**: Memory usage analysis in `ConcurrentHashMap`.
- [ ] **Unit Tests**: JUnit 5 + Mockito for service logic.

## Phase 5: Database + Vector Storage (Module 05-06) 🆕

- [ ] **PostgreSQL**: Replace `ConcurrentHashMap` with PostgreSQL (Docker Compose).
- [ ] **Spring Data JPA**: Entity mapping, Repository interfaces, `@Transactional`.
- [ ] **Flyway Migrations**: Versioned schema management.
- [ ] **Transaction fee + platformId**: Add missing PRD fields (`fee`, `platform_id`) to Transaction entity.
- [ ] **Import XTB**: CSV parser — zamknięta pozycja = BUY + SELL (PRD MVP #3).
- [ ] **Dashboard**: `GET /api/dashboard` — wartość portfela, koszt, zysk (PRD MVP #4).
- [ ] **pgvector Extension**: Install `pgvector` in PostgreSQL, create vector columns.
  - Cel: Przechowywanie embeddings transakcji obok danych relacyjnych (jedno źródło prawdy).

## Phase 6: Caching (Module 07) 🆕

- [ ] **Prices table/cache**: Implement `prices` model (instrument_id, price, updated_at) per PRD.
- [ ] **Caffeine**: Local in-memory cache for hot data.
- [ ] **Redis**: Distributed cache for instrument prices (Spring Data Redis).

## Phase 7: Architecture Refactor (Module 04) 🆕

- [ ] **Package by Feature**: Verify current structure follows the pattern.
- [ ] **Hexagonal / Clean Architecture**: Isolate domain logic from infrastructure (Ports & Adapters).

## Phase 8: Testing (Module 10) 🆕

- [ ] **Unit Tests**: Full coverage for Services (Mockito).
- [ ] **Integration Tests**: `@WebMvcTest` for Controllers.
- [ ] **Testcontainers**: PostgreSQL + Redis in tests.

## Phase 9: Containerization + CI/CD (Module 12-13) 🆕

- [ ] **Dockerfile**: Multi-stage build for production.
- [ ] **Docker Compose**: app + PostgreSQL + Redis.
- [ ] **GitHub Actions**: Build → Test → Push image pipeline.

## Phase 10: AI Financial Advisor 🧠 (Module 20)

- [ ] **Spring AI Setup**: Add `spring-ai-azure-openai-spring-boot-starter` dependency.
- [ ] **Azure OpenAI Integration**: Configure connection to Azure OpenAI Service (GPT model).
- [ ] **AdvisorService**: Create service that:
  1. Pobiera ostatnie 10 transakcji użytkownika z bazy.
  2. Buduje prompt z kontekstem finansowym (RAG pattern).
  3. Wysyła do Azure OpenAI i zwraca jednozdaniową, spersonalizowaną poradę.
- [ ] **Endpoint**: `POST /api/advisor` — returns AI-generated financial advice.
- [ ] **pgvector + Semantic Search**: Embed transaction descriptions, enable similarity queries ("pokaż transakcje podobne do...").
- [ ] `opt` **LangChain4j**: Alternative implementation for comparison.

## Phase 11: Azure Cloud Deployment ☁️ (Module 14)

- [ ] **Terraform**: IaC for Azure resources (Resource Group, App Service, PostgreSQL, Key Vault).
- [ ] **Azure App Service**: Deploy Spring Boot app from GitHub Actions.
- [ ] **Azure Database for PostgreSQL**: Provision Flexible Server (with pgvector).
- [ ] **Azure Key Vault**: Manage secrets (DB credentials, OpenAI API keys).
- [ ] **CI/CD**: GitHub Actions pipeline — build → test → deploy to Azure.
