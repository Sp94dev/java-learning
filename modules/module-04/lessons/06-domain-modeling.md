# Lekcja 06: Modelowanie Domeny (DDD Lite)

> 📖 Pełne omówienie konceptów DDD: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 6.

Domain-Driven Design (DDD) to zbiór praktyk modelowania kodu wokół problemu biznesowego. Poznaj 5 kluczowych konceptów.

---

## 1. Bounded Context (Kontekst Ograniczony)

**Granica modelu** — region kodu, w którym dany termin ma jedno znaczenie.

**Analogia Angular:** Lazy-loaded Feature Module z własnym routingiem i modelami. `TransactionsModule` ma swój model `User` (kto kupił), `AuthModule` ma swój `User` (login). Dwa różne `User` w dwóch kontekstach — OK!

W wallet-manager: pakiet `instrument/` i `transaction/` to dwa Bounded Contexty. Każdy ma własne klasy, komunikacja powinna iść przez publiczne API.

## 2. Ubiquitous Language (Język Wszechobecny)

Kod mówi językiem biznesu. Jeśli PRD mówi _"Transakcja kupna Instrumentu"_, to w kodzie:

- `Transaction` (nie `TradeRecord`, nie `OrderEntry`)
- `Instrument` (nie `Asset`, nie `Stock`)
- `BUY` / `SELL` (nie `PURCHASE` / `DISPOSE`)

Sprawdź: czy Twój `prd.md` i kod używają **tych samych nazw**?

## 3. Entity (Encja)

Ma ID, jest mutowalna. Dwa obiekty z tym samym ID = ten sam byt, nawet jeśli inne pola się zmienią.

- Przykład: `Instrument(id=1, ticker="AAPL")` — zmień ticker, nadal to instrument #1.

## 4. Value Object (Obiekt Wartości)

Nie ma ID, jest **niemutowalny**, definiowany przez wartość. Dwa VO z tymi samymi wartościami = identyczne.

- Przykład: `Money(100.0, "USD")` — nie ma ID, to po prostu "100 dolarów".
- Java `record` idealnie nadaje się na VO (immutable, auto-equals, auto-hashCode).

## 5. Aggregate (Agregat)

Klaster Encji i VO z jednym **Aggregate Root** (korzeniem). Komunikacja z klastrem WYŁĄCZNIE przez Root.

**Analogia Angular:** Komponent-rodzic (`WalletComponent`) z dziećmi (`TransactionListComponent`). Świat zewnętrzny komunikuje się tylko przez rodzica (`@Input/@Output`).

Przykład: `Wallet` (root) zawiera `Transaction` (encje wewnętrzne) i `Money` (VO — saldo).

---

## 🏋️ Zadanie: Identyfikacja bytów domenowych w Wallet Manager

### Część A: Klasyfikacja

Wypełnij tabelę:

| Klasa/pole                                | Entity czy Value Object? | Uzasadnienie                                       |
| ----------------------------------------- | ------------------------ | -------------------------------------------------- |
| `Instrument`                              | Entity                   |                                                    |
| `Transaction`                             | Entity                   |                                                    |
| `Transaction.type` (String: "BUY"/"SELL") | VO                       | Powinien być `String` czy coś innego?              |
| `Transaction.price * quantity`            | VO,                      | tak przeciez nie ma sensu trzymac wartosci w bazie |
| `InstrumentResponse` (DTO)                | None                     |                                                    |

### Część B: Stwórz `TransactionType` enum

Aktualnie `Transaction.type` to `String`. PRD definiuje dokładnie dwa typy: `BUY` i `SELL`. Literówka `"BU"` przejdzie bez błędu — zero walidacji.

Stwórz Value Object jako enum:

```java
public enum TransactionType {
    BUY, SELL
}
```

Zmień `Transaction.java` żeby używał `TransactionType` zamiast `String`. Sprawdź co się psuje i napraw.

### Część C: (Opcjonalne) Pomyśl o `Money`

PRD ma pola `price` i `fee` w Transaction. Czy `Double price` to dobry typ na pieniądze? (Podpowiedź: floating-point arithmetic → `0.1 + 0.2 != 0.3`). W Module 05+ rozważymy `BigDecimal` lub dedykowany `Money` VO.

## Sprawdzian wiedzy

- [x] Znam podstawowe pojęcia DDD: Bounded Context, Ubiquitous Language
- [x] Rozumiem różnicę między Entity (z tożsamością) a Value Object (bez tożsamości, immutable)
- [x] Wiem, czym jest Aggregate Root zarządzący wewnętrznymi encjami
- [x] Stworzyłem enum `TransactionType` i zastosowałem go w kodzie, zastępując typ `String`
