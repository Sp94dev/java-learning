# Projekt końcowy: WalletManager API v1

> Integracja wszystkich lekcji z Module 01

## Cel

Zbuduj pierwszą wersję WalletManager API łączącą wszystkie poznane koncepty.

## Wymagania funkcjonalne

### Endpoints

| Method | Path | Opis |
|--------|------|------|
| GET | `/api/v1/wallets` | Lista wszystkich portfeli |
| GET | `/api/v1/wallets/{id}` | Pojedynczy portfel |
| POST | `/api/v1/wallets` | Utwórz portfel |
| PUT | `/api/v1/wallets/{id}` | Aktualizuj portfel |
| DELETE | `/api/v1/wallets/{id}` | Usuń portfel |
| GET | `/api/v1/wallets?currency=PLN` | Filtrowanie |
| GET | `/api/v1/wallets/stats` | Statystyki (suma, count) |

### Model danych

```
Wallet:
- id: Long (auto-generated)
- name: String
- currency: String (PLN, EUR, USD)
- balance: BigDecimal
- createdAt: LocalDateTime
```

## Wymagania techniczne

- [ ] Struktura: Controller → Service → Repository
- [ ] Records jako DTO (Request/Response)
- [ ] Właściwe kody HTTP (201, 204, 404)
- [ ] Stream API do filtrowania i statystyk
- [ ] Swagger dokumentacja
- [ ] In-memory storage (ConcurrentHashMap)

## Struktura projektu

```
projects/wallet-manager/
├── src/main/java/com/example/walletmanager/
│   ├── WalletManagerApplication.java
│   ├── config/
│   │   └── OpenApiConfig.java
│   ├── wallet/
│   │   ├── WalletController.java
│   │   ├── WalletService.java
│   │   ├── InMemoryWalletRepository.java
│   │   ├── Wallet.java                    # domena
│   │   └── dto/
│   │       ├── CreateWalletRequest.java
│   │       ├── UpdateWalletRequest.java
│   │       ├── WalletResponse.java
│   │       └── WalletStatsResponse.java
│   └── common/
│       └── ... (później)
└── pom.xml
```

## Kryterium zaliczenia

1. Wszystkie endpointy działają
2. Swagger UI pokazuje dokumentację
3. Kod jest czysty (thin controller, logika w service)
4. Używasz Java Records i Stream API

## Następny krok

Po ukończeniu → Module 02: Walidacja, obsługa błędów, testy jednostkowe.
