# Lekcja 07: Wzorce Projektowe w Architekturze

## 1. Repository Pattern

Abstrakcja dostępu do danych za interfejsem. Dokładnie to, co zrobiłeś w Lekcji 04 — `InstrumentRepository` interface + `InMemoryInstrumentRepository` implementacja.

**Angular:** To jak `HttpClient` wstrzykiwany do serwisu. Komponent nie wie, czy dane lecą z backendu, cache'u, czy mocka.

## 2. DTO Pattern (Data Transfer Object)

Oddzielenie modelu domenowego od tego, co leci po HTTP.

**Problem w wallet-manager:** Aktualnie `InstrumentController.createInstrument()` przyjmuje **domain model** `Instrument` bezpośrednio z requestu. To znaczy, że klient może wysłać `id` w JSONie — a `id` powinien być auto-generowany.

**Rozwiązanie:** Request DTO bez `id`:

```java
public record CreateInstrumentRequest(String ticker, String currency, String market, String type) {}
```

**Angular:** To jak `interface CreateUserPayload` (bez `id`), osobny od pełnego `interface User` (z `id`).

## 3. Factory Pattern

Delegowanie tworzenia złożonych obiektów do dedykowanej klasy.

**Kiedy w wallet-manager:** Gdy konstrukcja obiektu wymaga logiki (np. kalkulacja `fee` na podstawie platformy). Zamiast robić to w kontrolerze → `TransactionFactory.create(request, platform)`.

## 4. Builder Pattern

Krok-po-kroku budowanie obiektu z wieloma opcjonalnymi parametrami. W Javie popularny z Lombokiem (`@Builder`) lub z wbudowanym `record` → metoda `with()`.

---

## 🏋️ Zadanie: Wydzielenie Request DTO

To drugie kluczowe zadanie z `PROJECT.md`:

> _"Rozdzielenie modeli domenowych od Request DTO"_

### Krok po kroku:

1. **Stwórz** `CreateInstrumentRequest.java` w `instrument/dto/`:

   ```java
   public record CreateInstrumentRequest(
       String ticker,
       String currency,
       String market,
       String type
   ) {}
   ```

2. **Zmień kontroler** `InstrumentController.createInstrument()`:
   - Parametr: `@RequestBody CreateInstrumentRequest request` (zamiast `Instrument`)
   - W ciele metody: zmapuj DTO → domain model:
     ```java
     Instrument instrument = new Instrument(null, request.ticker(), request.currency(), request.market(), request.type());
     ```

3. **Powtórz dla `Transaction`**: Stwórz `CreateTransactionRequest` w `transaction/dto/` (bez `id`, z polami z PRD).

4. **Zbuduj projekt** (`./mvnw clean install`).

5. **Przetestuj** endpointem z plików `.rest` — wyślij POST bez `id` w body i sprawdź, czy auto-generacja ID nadal działa.

> 💡 **Bonus:** Zauważ, że `Instrument.java` ma adnotacje `@Schema` (Swagger). Przenieś je na DTO, a model domenowy zostaw czysty — to krok w stronę Clean Architecture (pierścień 1 bez frameworków).


## Sprawdzian wiedzy

- [ ] Odróżniam wzorzec Repository od DTO (oddzielenie bazy/domeny od reprezentacji)
- [ ] Rozumiem, kiedy przydaje się Factory Pattern, a kiedy Builder Pattern
- [ ] Wdrożyłem wzorzec DTO, tworząc obiekty `*Request` i `*Response` w aplikacji
- [ ] Wiem, dlaczego kontrolery powinny zwracać i przyjmować DTO, a nie modele domenowe
