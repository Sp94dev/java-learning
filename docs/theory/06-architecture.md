# TEORIA: Architektura Aplikacji

---

## 1. Po co architektura?

### Problemy bez architektury

- **Spaghetti code** - wszystko połączone ze wszystkim
- **Niemożliwe testowanie** - tight coupling
- **Trudna zmiana** - dotknięcie jednego miejsca psuje inne
- **Brak skalowalności** - nie można wydzielić modułów

### Cel architektury

1. **Separation of Concerns** - każdy moduł ma jedną odpowiedzialność
2. **Low Coupling** - minimalne zależności między modułami
3. **High Cohesion** - powiązane rzeczy razem
4. **Testability** - można testować w izolacji
5. **Changeability** - łatwa adaptacja do zmian

---

## 2. Layered Architecture

### Struktura

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│         (Controllers, Views)            │
│         - obsługa HTTP                  │
│         - walidacja wejścia             │
│         - mapowanie DTO                 │
├─────────────────────────────────────────┤
│         BUSINESS LAYER                  │
│         (Services)                      │
│         - logika biznesowa              │
│         - orchestracja                  │
│         - transakcje                    │
├─────────────────────────────────────────┤
│         PERSISTENCE LAYER               │
│         (Repositories, DAO)             │
│         - dostęp do danych              │
│         - queries                       │
├─────────────────────────────────────────┤
│         DATABASE LAYER                  │
│         (Entities, Tables)              │
└─────────────────────────────────────────┘
```

### Zasady

1. **Zależności tylko w dół** - Controller może używać Service, nie odwrotnie
2. **Każda warstwa ma własną odpowiedzialność**
3. **Komunikacja przez interfejsy** - ułatwia testowanie

### Zalety

- Prosta do zrozumienia
- Jasna separacja
- Łatwy start

### Wady

- **Tight coupling z DB** - cała aplikacja zależy od modelu danych
- **Anemic Domain Model** - logika w serwisach, entity to tylko dane
- **Monolith tendency** - trudno wydzielić moduły

---

## 3. Package by Feature

### Idea

Organizuj kod wokół **funkcjonalności biznesowych**, nie warstw technicznych.

### Package by Layer (tradycyjne)

```
com.example/
├── controller/
│   ├── UserController.java
│   ├── OrderController.java
│   └── ProductController.java
├── service/
│   ├── UserService.java
│   ├── OrderService.java
│   └── ProductService.java
└── repository/
    ├── UserRepository.java
    ├── OrderRepository.java
    └── ProductRepository.java
```

### Package by Feature

```
com.example/
├── user/
│   ├── UserController.java
│   ├── UserService.java
│   ├── UserRepository.java
│   └── User.java
├── order/
│   ├── OrderController.java
│   ├── OrderService.java
│   ├── OrderRepository.java
│   └── Order.java
└── product/
    └── ...
```

### Zalety Package by Feature

1. **Wysoka kohezja** - wszystko związane z feature razem
2. **Łatwa nawigacja** - szukasz order? → pakiet `order/`
3. **Modularity** - każdy feature to potencjalny microservice
4. **Łatwiejsze usuwanie** - usuń cały pakiet
5. **Ograniczenie widoczności** - `package-private` działa sensownie

---

## 4. Hexagonal Architecture (Ports & Adapters)

### Problem z Layered

W layered architecture domena **zależy od infrastruktury** (DB, HTTP).
Zmiana bazy = zmiana w całej aplikacji.

### Idea Hexagonal

**Domena w centrum**, niezależna od wszystkiego.
Infrastruktura "podłącza się" przez **porty**.

### Struktura

```
                    ┌─────────────────────────────────────┐
                    │          ADAPTERS (IN)              │
                    │   REST Controller, CLI, GraphQL    │
                    └──────────────┬──────────────────────┘
                                   │ calls
                                   ▼
                    ┌─────────────────────────────────────┐
                    │          PORTS (IN)                 │
                    │   Use Case Interfaces               │
                    │   (CreateOrderUseCase, etc.)       │
                    └──────────────┬──────────────────────┘
                                   │ implements
                                   ▼
         ┌─────────────────────────────────────────────────────────┐
         │                       DOMAIN                            │
         │                                                         │
         │   ┌─────────────┐     ┌─────────────┐     ┌──────────┐ │
         │   │   Entities  │     │  Services   │     │  Value   │ │
         │   │   (Order)   │     │ (OrderSvc)  │     │ Objects  │ │
         │   └─────────────┘     └─────────────┘     └──────────┘ │
         │                                                         │
         └────────────────────────┬────────────────────────────────┘
                                  │ uses
                                  ▼
                    ┌─────────────────────────────────────┐
                    │          PORTS (OUT)                │
                    │   Repository Interfaces             │
                    │   External Service Interfaces       │
                    └──────────────┬──────────────────────┘
                                   │ implements
                                   ▼
                    ┌─────────────────────────────────────┐
                    │          ADAPTERS (OUT)             │
                    │   JPA Repository, REST Client,     │
                    │   Message Queue Publisher          │
                    └─────────────────────────────────────┘
```

### Port

**Interfejs** definiowany przez domenę.

- **Driving Port (IN)** - co domena oferuje światu (Use Cases)
- **Driven Port (OUT)** - czego domena potrzebuje (Repository, External Services)

### Adapter

**Implementacja** portu dla konkretnej technologii.

- **Driving Adapter (IN)** - REST Controller, CLI, Message Consumer
- **Driven Adapter (OUT)** - JPA Repository, HTTP Client, Kafka Producer

### Dependency Rule

**Zależności wskazują do środka** - domena nie wie o infrastrukturze.

```
Controller → UseCase Interface ← Domain Service → Repository Interface ← JPA Repo
          (calls)              (implements)      (calls)               (implements)
```

### Package Structure

```
com.example/
├── domain/
│   ├── model/
│   │   ├── Order.java
│   │   └── OrderItem.java
│   ├── port/
│   │   ├── in/
│   │   │   └── CreateOrderUseCase.java
│   │   └── out/
│   │       └── OrderRepository.java
│   └── service/
│       └── OrderService.java
├── application/
│   └── CreateOrderService.java  (implements UseCase)
├── infrastructure/
│   ├── persistence/
│   │   ├── JpaOrderRepository.java
│   │   └── OrderEntity.java
│   └── web/
│       └── OrderController.java
```

### Zalety

1. **Domain isolation** - czysta Java, bez frameworków
2. **Testability** - testujesz domenę bez DB, HTTP
3. **Flexibility** - łatwa podmiana adaptera (MySQL → Postgres)
4. **Postponed decisions** - możesz zacząć bez wyboru DB

### Wady

1. **Complexity** - więcej interfejsów, mapowanie
2. **Over-engineering** - dla CRUD to overkill
3. **Learning curve** - zespół musi rozumieć

### Kiedy używać

✅ Złożona logika biznesowa
✅ Długoterminowy projekt
✅ Wiele źródeł danych / interfejsów
✅ Potrzeba wysokiej testowalności

❌ Proste CRUD
❌ Prototyp / MVP
❌ Mały zespół bez doświadczenia

---

## 5. Clean Architecture

### Podobna do Hexagonal

```
┌───────────────────────────────────────────────────────────────┐
│                    FRAMEWORKS & DRIVERS                       │
│                    (Web, UI, DB, External)                    │
├───────────────────────────────────────────────────────────────┤
│                    INTERFACE ADAPTERS                         │
│                    (Controllers, Presenters, Gateways)        │
├───────────────────────────────────────────────────────────────┤
│                    APPLICATION BUSINESS RULES                 │
│                    (Use Cases)                                │
├───────────────────────────────────────────────────────────────┤
│                    ENTERPRISE BUSINESS RULES                  │
│                    (Entities)                                 │
└───────────────────────────────────────────────────────────────┘
```

**Dependency Rule:** Zależności tylko do środka (inner circles).

### Różnice od Hexagonal

- Hexagonal: focus na **ports & adapters**
- Clean: focus na **warstwy** i ich odpowiedzialności
- W praktyce: bardzo podobne, różne słownictwo

---

## 6. Domain-Driven Design (DDD) - koncepty

### Strategic DDD

**Bounded Context**
Granica wokół części domeny z własnym modelem i językiem.

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   SALES         │     │   INVENTORY     │     │   SHIPPING      │
│   Context       │────►│   Context       │────►│   Context       │
│                 │     │                 │     │                 │
│ Customer        │     │ Product         │     │ Shipment        │
│ Order           │     │ Stock           │     │ Address         │
│ Product (ref)   │     │ Warehouse       │     │ Package         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

"Product" w Sales ≠ "Product" w Inventory - różne atrybuty, różne zachowania.

**Ubiquitous Language**
Jeden język między developerami a ekspertami domenowymi w ramach kontekstu.
Kod używa tych samych terminów co biznes.

### Tactical DDD

**Entity**

- Ma tożsamość (ID)
- Mutowalny stan
- Równość przez ID

**Value Object**

- Brak tożsamości
- Immutable
- Równość przez wartość
- Przykłady: Money, Address, DateRange

**Aggregate**

- Klaster encji z jednym "root"
- Invarianty chronione przez root
- Transakcje na poziomie aggregate

**Repository**

- Abstrakcja kolekcji aggregates
- Zwykle jeden per aggregate root

**Domain Service**

- Logika która nie należy do żadnej encji
- Stateless

**Domain Event**

- Coś co się wydarzyło w domenie
- "OrderPlaced", "PaymentReceived"

---

## 7. Microservices vs Monolith

### Monolith

```
┌─────────────────────────────────────────┐
│              MONOLITH                   │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐   │
│  │ Users   │ │ Orders  │ │ Products│   │
│  └─────────┘ └─────────┘ └─────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │         Shared Database          │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

**Zalety:**

- Prostsze na start
- Łatwiejszy deployment (jeden artefakt)
- Brak network latency między modułami
- Łatwe transakcje (ACID)
- Prostsze debugging

**Wady:**

- Skalowanie całości, nie części
- Deployment = całość (ryzyko)
- Tight coupling między modułami
- Duży = wolny build

### Microservices

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Users      │     │   Orders     │     │   Products   │
│   Service    │────►│   Service    │────►│   Service    │
│              │     │              │     │              │
│   [User DB]  │     │   [Order DB] │     │   [Prod DB]  │
└──────────────┘     └──────────────┘     └──────────────┘
```

**Zalety:**

- Niezależne skalowanie
- Niezależne deployment
- Różne technologie per serwis
- Fault isolation
- Małe zespoły (ownership)

**Wady:**

- **Złożoność operacyjna** - wiele serwisów do monitorowania
- **Network latency** - komunikacja przez sieć
- **Distributed transactions** - saga pattern, eventual consistency
- **Data consistency** - brak JOIN między serwisami
- **Testing** - integracja trudna

### Kiedy microservices?

**NIE zaczynaj od microservices!**

"Monolith first" - zacznij od dobrze zmodularyzowanego monolitu.
Wydzielaj serwisy gdy:

1. Masz konkretny powód (skalowanie, niezależny deploy)
2. Granice modułów są jasne
3. Zespół ma doświadczenie z distributed systems

### Modular Monolith

Kompromis: monolith z jasną modularyzacją.

```
┌─────────────────────────────────────────────────────────┐
│                    MODULAR MONOLITH                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐    │
│  │   Users     │  │   Orders    │  │  Products   │    │
│  │   Module    │──│   Module    │──│   Module    │    │
│  │             │  │             │  │             │    │
│  │ API         │  │ API         │  │ API         │    │
│  │ Domain      │  │ Domain      │  │ Domain      │    │
│  │ Persistence │  │ Persistence │  │ Persistence │    │
│  └─────────────┘  └─────────────┘  └─────────────┘    │
│                                                         │
│  Komunikacja: przez API modułu (nie bezpośrednio DB)   │
│  Gotowy do wydzielenia jako microservice               │
└─────────────────────────────────────────────────────────┘
```

---

## 8. Wybór architektury - decision tree

```
Jak złożona jest logika biznesowa?
│
├─► Prosta (CRUD) → Layered Architecture, Package by Feature
│
└─► Złożona →
    │
    ├─► Czy potrzebujesz izolacji domeny?
    │   │
    │   ├─► Tak → Hexagonal / Clean Architecture
    │   │
    │   └─► Nie → Layered + rich domain model
    │
    └─► Czy potrzebujesz niezależnego skalowania/deployu?
        │
        ├─► Tak, teraz → Microservices (jeśli masz doświadczenie)
        │
        └─► Może w przyszłości → Modular Monolith
```

---

## Podsumowanie

| Architektura           | Kiedy                                  |
| ---------------------- | -------------------------------------- |
| **Layered**            | Proste projekty, szybki start          |
| **Package by Feature** | Średnie projekty, modularity           |
| **Hexagonal**          | Złożona domena, izolacja, testowalność |
| **Modular Monolith**   | Przygotowanie do microservices         |
| **Microservices**      | Skalowanie, niezależne zespoły         |

**Zasada:** Start simple, evolve as needed. Nie over-engineer.
