# Lekcja 05: Clean Architecture

> 📖 Diagram pierścieni i porównanie z Hexagonal: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 5.

Clean Architecture (Bob Martin) to ta sama obietnica co Hexagonal — izolacja domeny od infrastruktury — ale przedstawiona jako **koncentryczne pierścienie**:

```mermaid
flowchart TD

    subgraph Ring4 [Pierścień 4: Frameworks & Drivers]
        direction LR
        SpringBoot([Spring Boot / Jackson])
        Docker([Docker / PostgreSQL Driver])
        Swagger([OpenAPI / Swagger])
    end

    subgraph Ring3 [Pierścień 3: Interface Adapters]
        direction LR
        Controller([InstrumentController <br> TransactionController])
        DTOs([InstrumentResponse <br> TransactionResponse])
        RepoImpl([InMemoryInstrumentRepository <br> InMemoryTransactionRepository])
    end

    subgraph Ring2 [Pierścień 2: Use Cases]
        direction LR
        Service{{InstrumentService <br> TransactionService}}
        PortOUT(InstrumentRepository <br> TransactionRepository <br> «interfejs»)
    end

    subgraph Ring1 [Pierścień 1: Entities — Czysta Domena]
        direction LR
        Instrument[[record Instrument]]
        Transaction[[record Transaction]]
    end

    %% Dependency Rule — strzałki idą DO ŚRODKA
    SpringBoot --->|konfiguruje| Controller
    Swagger --->|generuje docs dla| Controller
    Controller --->|woła| Service
    DTOs --->|mapuje z| Instrument
    RepoImpl --->|implements| PortOUT
    Service --->|orchestruje| Instrument
    Service --->|orchestruje| Transaction
    Service --->|woła przez| PortOUT

    %% Stylizacja
    classDef ring1 fill:#bae6fd,stroke:#0284c7,stroke-width:3px;
    classDef ring2 fill:#bbf7d0,stroke:#16a34a,stroke-width:2px;
    classDef ring3 fill:#fef08a,stroke:#ca8a04,stroke-width:2px;
    classDef ring4 fill:#fecaca,stroke:#dc2626,stroke-width:2px;

    class Ring1 ring1;
    class Ring2 ring2;
    class Ring3 ring3;
    class Ring4 ring4;
```

## 4 Pierścienie (od środka)

1. **Entities** 🔵 — czyste zasady biznesowe. `Instrument`, `Transaction`. Nie zależą od Springa, bazy, niczego.
2. **Use Cases** 🟢 — orchestracja. `InstrumentService.createInstrument()`. Woła Entities i Porty.
3. **Interface Adapters** 🟡 — konwersja formatów. Kontrolery REST, DTO, implementacje repozytoriów.
4. **Frameworks & Drivers** 🔴 — Spring Boot, Jackson JSON, drivery SQL, Docker, Swagger.

**Dependency Rule:** Zależności idą tylko DO ŚRODKA. Warstwa 4 zna 3, ale 1 nie wie nic o istnieniu 2, 3, 4.

### Różnica od Hexagonal

W praktyce — minimalna. Hexagonal mówi językiem "Portów i Adapterów", Clean mówi "Pierścieniami". Koncepcja ta sama: **domena w centrum, infrastruktura na zewnątrz.**

---

## 🏋️ Zadanie: Mapowanie klas Wallet Manager na pierścienie

Wypełnij tabelę — do którego pierścienia należy każda klasa z Twojego projektu?

| Klasa                               | Pierścień (1-4) | Uzasadnienie |
| ----------------------------------- | --------------- | ------------ |
| `Instrument.java` (record)          | 1               |              |
| `Transaction.java` (record)         | 1               |              |
| `InstrumentService.java`            | 2               |              |
| `TransactionService.java`           | 2               |              |
| `InstrumentController.java`         | 3               |              |
| `InMemoryInstrumentRepository.java` | 3               |              |
| `InstrumentResponse.java` (DTO)     | 3               |              |
| `OpenApiConfig.java`                | 4               |              |
| `WalletApplication.java`            | 4               |              |

Pytania pomocnicze:

1. Czy `Instrument.java` importuje cokolwiek ze Springa? Jeśli tak — to **nie jest** czysta Entity pierścienia 1. (Sprawdź adnotację `@Schema` od Swagger — czy to framework czy domena?)
2. Gdzie umieścisz interfejs `InstrumentRepository` (stworzony w Lekcji 04)?

## Sprawdzian wiedzy

- [x] Znam 4 pierścienie Clean Architecture
- [x] Rozumiem, że zależności mogą iść tylko do środka (Dependency Rule)
- [x] Potrafię przypisać klasy z Wallet Manager do odpowiednich pierścieni
- [x] Zidentyfikowałem miejsca, w których model domenowy może być "skażony" frameworkiem (np. `@Schema`)
