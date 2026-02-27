# Ćwiczenie: Checklist Refaktoryzacji Wallet Manager (Moduł 04)

> Źródło wymagań: [`projects/PROJECT.md`](../../../projects/PROJECT.md) + [`todo.md`](../../../projects/wallet-manager/todo.md)

Ten plik to **zbiorczy checklist** zmian z lekcji 01-08, do oznaczania postęu.

---

## Pochodzenie zadań

| Zadanie                   | Źródło w dokumentacji                        |
| ------------------------- | -------------------------------------------- |
| Interfejsy Repository     | `PROJECT.md` → Aktualny Cel Module 03, pkt 1 |
| Request DTO               | `PROJECT.md` → Aktualny Cel Module 03, pkt 3 |
| Package by Feature verify | `todo.md` → Phase 7, pkt 1                   |
| Hexagonal refactor        | `todo.md` → Phase 7, pkt 2                   |
| TransactionType enum      | `prd.md` → Transactions: type BUY/SELL       |

---

## ✅ Checklist

### 1. Weryfikacja Package by Feature (Lekcja 03)

- [ ] Sprawdzone: `TransactionService` nie importuje klas z `instrument/`
- [ ] Sprawdzone: `InstrumentService` nie importuje klas z `transaction/`
- [ ] Przeniesiony: `config/OpenApiConfig.java` → `common/config/` (lub zostawiony z uzasadnieniem)
- [ ] Sprawdzone modyfikatory dostępu: klasy nieużywane poza pakietem → package-private

### 2. Wydzielenie Port OUT — Repository Interfaces (Lekcja 04)

- [ ] Stworzony: `instrument/InstrumentRepository.java` (interfejs)
- [ ] Zaimplementowany: `InMemoryInstrumentRepository implements InstrumentRepository`
- [ ] Zmieniony: `InstrumentService` → wstrzykuje `InstrumentRepository` (interfejs)
- [ ] Stworzony: `transaction/TransactionRepository.java` (interfejs)
- [ ] Zaimplementowany: `InMemoryTransactionRepository implements TransactionRepository`
- [ ] Zmieniony: `TransactionService` → wstrzykuje `TransactionRepository` (interfejs)

### 3. Mapowanie Clean Architecture (Lekcja 05)

- [ ] Wypełniona tabela mapowania klas → pierścieni
- [ ] Zidentyfikowane: adnotacje frameworkowe (`@Schema`) w modelach domenowych

### 4. Modelowanie DDD (Lekcja 06)

- [ ] Stworzony: `TransactionType` enum (`BUY`, `SELL`)
- [ ] Zmieniony: `Transaction.java` → `TransactionType type` zamiast `String type`
- [ ] Zaktualizowane: `InMemoryTransactionRepository`, `TransactionController`, pliki `.rest`
- [ ] Zidentyfikowane: kandydaci na Value Object (`Money` → Module 05+)

### 5. Request DTO (Lekcja 07)

- [ ] Stworzony: `instrument/dto/CreateInstrumentRequest.java` (record bez `id`)
- [ ] Stworzony: `transaction/dto/CreateTransactionRequest.java` (record bez `id`)
- [ ] Zmienione: kontrolery używają Request DTO zamiast modeli domenowych w `@RequestBody`
- [ ] Przetestowane: endpointy POST działają z plikami `.rest`

### 6. Analiza Modularności (Lekcja 08)

- [ ] Wylistowane: cross-module imports (jeśli istnieją)
- [ ] Potwierdzone: `Transaction` odwołuje się do Instrument przez `instrumentId` (Long), nie przez obiekt

---

## 🏗️ Build & Verify

Po wykonaniu wszystkich zmian:

```bash
./mvnw clean install
```

Jeśli build przechodzi ✅ → zaktualizuj:

- `PROJECT.md` → sekcja "Aktualny Cel" (zaznacz jako zrealizowane)
- `todo.md` → Phase 2 i Phase 7 (zaznacz checkboxy)


## Sprawdzian wiedzy

- [ ] Przeanalizowałem architekturę całego modułu 04 na przykładzie projektu Wallet Manager
- [ ] Potrafię samodzielnie wydzielać interfejsy i chronić domenę biznesową
- [ ] Zaktualizowałem `PROJECT.md` oraz `todo.md` odznaczając zrealizowane kroki
- [ ] Mam pewność, że kod po refaktoryzacji pomyślnie się buduje i przechodzi testy
