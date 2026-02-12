# Moduł 04: Architektura Aplikacji

> `opt` = optional

## Cel

Poznać popularne wzorce architektoniczne i kiedy je stosować.

---

## Tematy do opanowania

### 1. Po co architektura?

- [ ] Separation of Concerns
- [ ] Low Coupling, High Cohesion
- [ ] Testability
- [ ] Changeability

### 2. Layered Architecture

- [ ] Presentation → Business → Persistence → Database
- [ ] Zależności tylko w dół
- [ ] Zalety: prosta, jasna separacja
- [ ] Wady: tight coupling z DB, anemic domain

### 3. Package by Layer vs Package by Feature

- [ ] **By Layer:** `controller/`, `service/`, `repository/`
- [ ] **By Feature:** `user/`, `order/`, `product/`
- [ ] Zalety Package by Feature:
  - [ ] Wysoka kohezja
  - [ ] Łatwa nawigacja
  - [ ] Przygotowanie do microservices

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

- [ ] **Repository Pattern** - abstrakcja dostępu do danych
- [ ] **DTO Pattern** - oddzielenie reprezentacji od domeny
- [ ] **Factory Pattern** - tworzenie złożonych obiektów
- [ ] **Builder Pattern** - step-by-step construction

### 8. Monolith vs Microservices

- [ ] **Monolith:** prostszy, jeden deployment
- [ ] **Microservices:** niezależne skalowanie, złożoność operacyjna
- [ ] **Modular Monolith:** kompromis - moduły gotowe do wydzielenia
- [ ] "Monolith first" - nie zaczynaj od microservices

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

## Struktura dla Wallet Manager (rekomendowana)

```
com.sp94dev.wallet/
├── WalletApplication.java
├── instrument/
│   ├── InstrumentController.java
│   ├── InstrumentService.java
│   ├── InstrumentRepository.java      # Interface
│   ├── InMemoryInstrumentRepository.java  # Impl
│   ├── Instrument.java
│   └── dto/
│       ├── CreateInstrumentRequest.java
│       └── InstrumentResponse.java
├── transaction/
│   └── ... (analogicznie)
├── portfolio/
│   └── ...
└── common/
    ├── exception/
    └── config/
```

---

## Ćwiczenia

1. Przeorganizuj kod do Package by Feature
2. Wydziel DTO (Request/Response) od domenowego modelu
3. Stwórz interface Repository i implementację InMemory

---

## Sprawdzian gotowości

- [ ] Rozumiem różnicę Package by Layer vs Package by Feature
- [ ] Wiem kiedy stosować Hexagonal Architecture (złożona domena)
- [ ] Znam podstawowe wzorce: Repository, DTO, Factory
- [ ] Rozumiem trade-offs Monolith vs Microservices
- [ ] Potrafię wybrać architekturę dla projektu
