# Moduł 04: Architektura Aplikacji

> `opt` = optional

## Cel

Poznać popularne wzorce architektoniczne i kiedy je stosować.

---

## Lekcje

- [Lekcja 01: Po co architektura?](lessons/01-why-architecture.md)
- [Lekcja 02: Layered Architecture (Architektura Warstwowa)](lessons/02-layered-architecture.md)
- [Lekcja 03: Struktura Pakietów - Pakiety wg Funkcjonalności](lessons/03-package-structure.md)
- [Lekcja 04: Architektura Heksagonalna a systemy AI](lessons/04-hexagonal-architecture.md)
- [Lekcja 05: Clean Architecture](lessons/05-clean-architecture.md)
- [Lekcja 06: Modelowanie Domeny (DDD Lite)](lessons/06-domain-modeling.md)
- [Lekcja 07: Wzorce Projektowe W Architekturze Aplikacji](lessons/07-design-patterns.md)
- [Lekcja 08: Monolith vs Microservices](lessons/08-monolith-vs-microservices.md)
- [Lekcja 09: Spring Modulith 🆕](lessons/09-spring-modulith.md)
- [Lekcja 10: Enterprise Integration Patterns (EIP)](lessons/10-enterprise-integration-patterns.md)
- [Ćwiczenie Refaktoryzacji Wallet Manager (Krok po Kroku)](lessons/11-refactoring-wallet.md)

---

## Tematy do opanowania

### 1. Po co architektura?

- [x] Separation of Concerns
- [x] Low Coupling, High Cohesion
- [x] Testability
- [x] Changeability

### 2. Layered Architecture

- [x] Presentation → Business → Persistence → Database
- [x] Zależności tylko w dół
- [x] Zalety: prosta, jasna separacja
- [x] Wady: tight coupling z DB, anemic domain

### 3. Package by Layer vs Package by Feature

- [x] **By Layer:** `controller/`, `service/`, `repository/`
- [x] **By Feature:** `user/`, `order/`, `product/`
- [x] Zalety Package by Feature:
  - [x] Wysoka kohezja
  - [x] Łatwa nawigacja
  - [x] Przygotowanie do microservices

### 4. Hexagonal Architecture (Ports & Adapters)

- [ ] Domena w centrum, niezależna od infrastruktury
- [ ] **Port IN** - co domena oferuje (Use Cases)
- [ ] **Port OUT** - czego domena potrzebuje (Repository interface)
- [ ] **Adapter IN** - implementacja portu IN (Controller)
- [ ] **Adapter OUT** - implementacja portu OUT (JPA Repository)
- [ ] Dependency Rule: zależności wskazują DO ŚRODKA

### 5. Clean Architecture

- [ ] Podobna do Hexagonal
- [ ] Warstwy: Frameworks → Adapters → Use Cases → Entities
- [ ] Enterprise Business Rules (Entities) w centrum

### 6. Domain-Driven Design - koncepty

- [ ] **Bounded Context** - granica modelu
- [ ] **Ubiquitous Language** - wspólny język z biznesem
- [ ] **Entity** - ma tożsamość (ID)
- [ ] **Value Object** - bez tożsamości, immutable
- [ ] **Aggregate** - klaster z root entity

### 7. Wzorce projektowe

- [x] **Repository Pattern** - abstrakcja dostępu do danych
- [x] **DTO Pattern** - oddzielenie reprezentacji od domeny
- [x] **Factory Pattern** - tworzenie złożonych obiektów
- [x] **Builder Pattern** - step-by-step construction

### 8. Monolith vs Microservices

- [x] **Monolith:** prostszy, jeden deployment
- [x] **Microservices:** niezależne skalowanie, złożoność operacyjna
- [x] **Modular Monolith:** kompromis - moduły gotowe do wydzielenia
- [x] "Monolith first" - nie zaczynaj od microservices

### 9. Spring Modulith 🆕

- [ ] Modularność w Spring — podział na moduły
- [ ] Event-based integration (ApplicationEventPublisher)
- [ ] @Externalized — publikacja eventów między JVM
- [ ] Documenter — UML diagram zależności
- [ ] Testing modułów

### 10. Enterprise Integration Patterns 🆕 `opt`

- [ ] Cztery style integracji: file, shared DB, RPC, messaging
- [ ] Kiedy który pattern
- [ ] Rekomendowana lektura (EIP book)

---

## Powiązana teoria

- `docs/theory/06-architecture.md` → Cały plik
- `docs/theory/03-oop-solid.md` → SOLID principles

---

## Struktura dla Wallet Manager (rekomendowana po Module 04)

> Bazowane na: [`projects/PROJECT.md`](../../projects/PROJECT.md), sekcja "Aktualna Struktura Pakietów"

```
com.sp94dev.wallet/
├── WalletApplication.java
├── instrument/
│   ├── InstrumentController.java          # Adapter IN
│   ├── InstrumentService.java             # Domena (Use Case)
│   ├── InstrumentRepository.java          # 🆕 Port OUT (interfejs)
│   ├── InMemoryInstrumentRepository.java  # Adapter OUT (impl)
│   ├── Instrument.java                    # Entity
│   └── dto/
│       ├── InstrumentResponse.java        # Response DTO
│       └── CreateInstrumentRequest.java   # 🆕 Request DTO
├── transaction/
│   ├── TransactionController.java
│   ├── TransactionService.java
│   ├── TransactionRepository.java         # 🆕 Port OUT (interfejs)
│   ├── InMemoryTransactionRepository.java
│   ├── Transaction.java
│   ├── TransactionType.java               # 🆕 Value Object (enum)
│   └── dto/
│       ├── TransactionResponse.java
│       ├── TransactionStats.java
│       └── CreateTransactionRequest.java  # 🆕 Request DTO
└── common/
    └── config/
        └── OpenApiConfig.java
```

---

## Ćwiczenia (z lekcji)

> Pełny checklist z postępem: [Ćwiczenie 11](lessons/11-refactoring-wallet.md)

1. ✏️ Audyt coupling w `InstrumentService` (Lekcja 01)
2. ✏️ Mapowanie przepływu żądania przez warstwy (Lekcja 02)
3. ✏️ Weryfikacja cross-module imports (Lekcja 03)
4. 🔨 **Wydzielenie interfejsów `InstrumentRepository` / `TransactionRepository`** (Lekcja 04) ← najważniejsze
5. ✏️ Mapowanie klas na pierścienie Clean Architecture (Lekcja 05)
6. 🔨 **Stworzenie `TransactionType` enum** (Lekcja 06)
7. 🔨 **Wydzielenie Request DTO** (`CreateInstrumentRequest`, `CreateTransactionRequest`) (Lekcja 07)
8. ✏️ Analiza gotowości do wydzielenia mikroserwisu (Lekcja 08)

> ✏️ = analiza/notatki, 🔨 = zmiana w kodzie

---

## Sprawdzian gotowości

- [ ] Rozumiem różnicę Package by Layer vs Package by Feature
- [ ] Wiem kiedy stosować Hexagonal Architecture (złożona domena)
- [x] Znam podstawowe wzorce: Repository, DTO, Factory
- [x] Rozumiem trade-offs Monolith vs Microservices
- [ ] Potrafię wybrać architekturę dla projektu
- [ ] **Wydzieliłem** interfejsy Repository (Port OUT) w wallet-manager
- [x] **Stworzyłem** Request DTO i odseparowałem od domain models
