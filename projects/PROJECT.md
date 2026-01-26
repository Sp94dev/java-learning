# Projekt Końcowy: WalletManager API

> To jest Twój "Capstone Project". Aplikacja będzie ewoluować i zmieniać się wraz z każdym ukończonym modułem, przechodząc od prostego prototypu do produkcyjnego systemu.

## 🗺️ Mapa Rozwoju (Implementation Stages)

Realizuj kolejne kroki zgodnie z postępami w nauce:

*   **Module 00 (Setup):** Zainicjalizuj czysty projekt Spring Boot, skonfiguruj Git, Maven Wrapper i strukturę katalogów.
*   **Module 01 (REST Basics):** Zbuduj w pełni funkcjonalne API (CRUD) z walidacją in-memory, używając `RestController`, `Records` i `Stream API`.
*   **Module 02 (Internals):** Zweryfikuj zarządzanie pamięcią (String Pool, Immutable Objects) i zoptymalizuj struktury danych pod kątem GC.
*   **Module 03 (OOP & SOLID):** Wprowadź interfejsy dla warstwy Repository (Strategy Pattern) i rozdziel modele domenowe od DTO.
*   **Module 04 (Spring Core):** Skonfiguruj profile (dev/prod), własne Beany i zrozum cykl życia komponentów (Scopes).
*   **Module 05 (Databases):** Zastąp `ConcurrentHashMap` bazą PostgreSQL, wykorzystując Spring Data JPA i migracje Liquibase/Flyway.
*   **Module 06 (Architecture):** Przebuduj aplikację na Architekturę Heksagonalną (Ports & Adapters), całkowicie izolując domenę biznesową.
*   **Module 07 (Security):** Wdróż uwierzytelnianie (Login) i autoryzację (Role) przy użyciu Spring Security i JWT (OAuth2).
*   **Module 08 (Testing & DevOps):** Dodaj pełne pokrycie testami (Unit, Integration z Testcontainers) i zbuduj pipeline CI/CD (Docker).

---

## 🎯 AKTUALNY CEL: Wersja v1 (Scope: Module 01)

Implementujemy MVP oparte o pamięć RAM.

### Wymagania funkcjonalne

| Method | Path | Opis |
|--------|------|------|
| GET | `/api/v1/wallets` | Lista wszystkich portfeli |
| GET | `/api/v1/wallets/{id}` | Pojedynczy portfel |
| POST | `/api/v1/wallets` | Utwórz portfel |
| PUT | `/api/v1/wallets/{id}` | Aktualizuj portfel (nazwa, balans) |
| DELETE | `/api/v1/wallets/{id}` | Usuń portfel |
| GET | `/api/v1/wallets?currency=PLN` | Filtrowanie po walucie |
| GET | `/api/v1/wallets/stats` | Statystyki (np. suma środków w PLN) |

### Model danych (In-Memory)

```java
public record Wallet(
    Long id,
    String name,
    String currency,
    BigDecimal balance,
    LocalDateTime createdAt
) {}
```

### Wymagania techniczne

- [ ] Architektura warstwowa: `Controller` → `Service` → `Repository` (In-Memory)
- [ ] **Java Records** jako DTO (oddziel `CreateWalletRequest` od domeny `Wallet`)
- [ ] Poprawne kody HTTP (`201 Created`, `204 No Content`, `404 Not Found`)
- [ ] **Stream API** wykorzystane do filtrowania list i obliczania statystyk
- [ ] Dokumentacja **OpenAPI (Swagger)** dostępna pod `/swagger-ui.html`

### Sugerowana struktura pakietów (v1)

```
src/main/java/com/example/walletmanager/
├── config/
│   └── OpenApiConfig.java
├── wallet/
│   ├── WalletController.java      # REST Endpoints
│   ├── WalletService.java         # Logika biznesowa
│   ├── InMemoryWalletRepository.java # HashMap storage
│   ├── domain/
│   │   └── Wallet.java            # Główny model
│   └── dto/
│       ├── CreateWalletRequest.java
│       ├── WalletResponse.java
│       └── WalletStats.java
└── WalletManagerApplication.java
```