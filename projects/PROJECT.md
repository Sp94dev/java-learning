# Projekt Końcowy: WalletManager API

> Import transakcji z XTB → Zobacz wartość portfela.
>
> To jest Twój "Capstone Project". Aplikacja ewoluuje wraz z każdym ukończonym modułem — od prostego CRUD do produkcyjnego systemu z AI na Azure.
> Źródło prawdy biznesowej: [`docs/prd.md`](../docs/prd.md)

---

## 📊 Model Danych

> Bazowany na PRD. Pola oznaczone ✅ są zaimplementowane w kodzie, 🔜 czekają na kolejne moduły.

### Instrument

```java
public record Instrument(
    Long id,            // ✅ auto-generated
    String ticker,      // ✅ e.g. "AAPL"
    String currency,    // ✅ e.g. "USD"
    String market,      // ✅ e.g. "NASDAQ"
    String type         // ✅ e.g. "STOCK", "ETF"
) {}
```

### Transaction

```java
public record Transaction(
    Long id,            // ✅ auto-generated
    Long instrumentId,  // ✅ FK → Instrument
    String type,        // ✅ "BUY" / "SELL"
    Double quantity,    // ✅
    Double price,       // ✅ price per unit
    LocalDate date,     // ✅
    Double fee,         // 🔜 prowizja (PRD)
    String platformId   // 🔜 identyfikator platformy (PRD)
) {}
```

### Prices (cache) — 🔜 Module 07

```
prices: instrument_id, price, updated_at
```

> Osobna tabela/cache na aktualne ceny instrumentów. Docelowo: Redis lub Caffeine.

---

## 🗺️ Mapa Rozwoju (Implementation Stages)

Każdy punkt mapuje się na moduł z [`roadmap.md`](../roadmap.md). Listowane są **tylko moduły zmieniające wallet-manager** — moduły teoretyczne (02, 11, 15-16) rozwijają wiedzę, ale nie dodają features.

| #   | Moduł                  | Co zmienia się w wallet-manager                                          | Status |
| --- | ---------------------- | ------------------------------------------------------------------------ | ------ |
| 00  | Setup + Tooling        | Init projektu Spring Boot, Git, Maven Wrapper                            | ✅     |
| 01  | REST + Java Basics     | CRUD Instruments & Transactions, Stream API, OpenAPI                     | ✅     |
| 03  | DI + Warstwy           | Constructor Injection, interfejsy Repository, rozdzielenie DTO vs domain | 🟡     |
| 04  | Architektura Aplikacji | Package by Feature, Hexagonal / Clean Architecture refactor              | 🔜     |
| 05  | JPA + PostgreSQL       | Zamiana `ConcurrentHashMap` → PostgreSQL + **pgvector** (embeddings)     | 🔜     |
| 06  | DB Deep Dive           | Flyway migrations, relacje encji, N+1, paginacja                         | 🔜     |
| 07  | Caching                | Redis / Caffeine cache dla tabeli `prices`                               | 🔜     |
| 09  | Walidacja              | @Valid, @ControllerAdvice, Problem Details (RFC 7807)                    | 🔜     |
| 10  | Testy                  | JUnit 5, Mockito, @WebMvcTest, Testcontainers                            | 🔜     |
| 12  | Konteneryzacja         | Dockerfile multi-stage, Docker Compose (app + db + redis)                | 🔜     |
| 13  | CI/CD                  | GitHub Actions — build → test → push image                               | 🔜     |
| 14  | Azure + Terraform      | Deploy na Azure App Service, Azure DB for PostgreSQL, IaC                | 🔜     |
| 20  | Spring AI              | 🧠 AI Financial Advisor — Azure OpenAI, pgvector, RAG                    | 🔜     |

---

## 🎯 AKTUALNY CEL: Module 03 (DI + Warstwy)

Refaktoryzacja wallet-managera pod kątem Dependency Injection:

- [ ] Wydzielenie interfejsów `InstrumentRepository` / `TransactionRepository`
- [ ] Przejście z `new` na Constructor Injection (Spring IoC)
- [ ] Rozdzielenie modeli domenowych od Request DTO

### Zrealizowane (Module 01) ✅

| Method | Path                      | Opis                                                |
| ------ | ------------------------- | --------------------------------------------------- |
| GET    | `/api/instruments`        | Lista instrumentów (filtrowanie, sortowanie, limit) |
| GET    | `/api/instruments/{id}`   | Pojedynczy instrument (404 jeśli brak)              |
| POST   | `/api/instruments`        | Utwórz instrument (201 Created + Location)          |
| PUT    | `/api/instruments/{id}`   | Aktualizuj instrument                               |
| DELETE | `/api/instruments/{id}`   | Usuń instrument (204 No Content)                    |
| GET    | `/api/transactions`       | Lista transakcji                                    |
| POST   | `/api/transactions`       | Dodaj transakcję (z instrument ID)                  |
| GET    | `/api/transactions/stats` | Statystyki (groupingBy, summing)                    |

### Planowane endpointy (przyszłe moduły)

| Method | Path                      | Moduł | Opis                                   |
| ------ | ------------------------- | ----- | -------------------------------------- |
| POST   | `/api/instruments/import` | 05    | Import CSV z XTB                       |
| GET    | `/api/dashboard`          | 05+   | Wartość portfela, koszt, zysk          |
| POST   | `/api/advisor`            | 20    | 🧠 AI Financial Advisor (Azure OpenAI) |

---

## 📂 Aktualna Struktura Pakietów

```
src/main/java/com/sp94dev/wallet/
├── config/
│   └── OpenApiConfig.java
├── instrument/
│   ├── Instrument.java                 # Record (domain model)
│   ├── InstrumentController.java       # REST endpoints
│   ├── InstrumentService.java          # Business logic
│   ├── InMemoryInstrumentRepository.java  # ConcurrentHashMap (→ JPA w Module 05)
│   └── dto/
│       └── InstrumentResponse.java     # Response DTO
├── transaction/
│   ├── Transaction.java                # Record (domain model)
│   ├── TransactionController.java      # REST endpoints
│   ├── TransactionService.java         # Business logic
│   ├── InMemoryTransactionRepository.java # ConcurrentHashMap (→ JPA w Module 05)
│   └── dto/
│       ├── TransactionResponse.java    # Response DTO
│       └── TransactionStats.java       # Stats DTO (grouping)
└── WalletApplication.java
```

---

## ☁️ Docelowa Infrastruktura (Azure)

```
┌──────────────────────────────────────────────────────┐
│                   Azure Cloud                        │
│                  (Terraform IaC)                     │
├──────────────────────────────────────────────────────┤
│  Azure App Service / Azure Spring Apps               │
│  ├── WalletManager API (Spring Boot)                 │
│  └── Spring AI → Azure OpenAI Service                │
├──────────────────────────────────────────────────────┤
│  Azure Database for PostgreSQL (+ pgvector)           │
│  Azure Cache for Redis (prices)                      │
│  Azure Key Vault (secrets)                           │
└──────────────────────────────────────────────────────┘
```

---

## 🚫 Ograniczenia (z PRD)

Świadomie **NIE** implementujemy:

- ~~Multi-user / Auth~~ — single-user app
- ~~Multi-currency~~ — wszystko w oryginalnej walucie instrumentu
- ~~FIFO / Podatki~~ — brak kalkulacji podatkowych
- ~~Realtime~~ — brak WebSocket / live updates

> **Uwaga:** Moduł 08 (Security) jest w roadmapie jako **nauka konceptu**, ale wallet-manager per PRD nie dostaje auth layer.
