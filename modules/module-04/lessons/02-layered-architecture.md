# Lekcja 02: Layered Architecture (Architektura Warstwowa)

> 📖 Diagram warstw i szczegóły: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 2.

To klasyczne podejście do budowania backendu. Ruch płynie kaskadowo z góry na dół:

```
Presentation Layer (Controller) → Business Layer (Service) → Persistence Layer (Repository) → Database
```

**Złota zasada:** Zależności idą tylko w dół. Controller woła Service, nigdy odwrotnie.

## Zalety

- **Prostota** — jasna, linearna separacja. Łatwo zrozumieć przepływ od HTTP do bazy.
- **Niski próg wejścia** — idealny start. Większość tutoriali Spring Boot bazuje na tym wzorcu.
- **Jasna separacja techniczna** — każda warstwa ma wyraźną odpowiedzialność.

## Wady

1. **Tight Coupling z Bazą** — Warstwa biznesu (Service) zaczyna importować klasy bazodanowe. Zmiana bazy = zmiana w serwisach. Analogia Angular: tworzysz formularz, który bezpośrednio woła `fetch()` zamiast oddzielonego serwisu HTTP.

2. **Anemiczna Domena** — Model to puste DTO z `get()/set()`, a logika trafia do "God Service". Twój `Instrument` jest tylko workiem danych, zamiast sam wiedzieć, jak się walidować.

---

## 🏋️ Zadanie: Mapowanie warstw w Wallet Manager

Prześledź przepływ żądania `POST /api/instruments` przez aktualny kod:

1. **Narysuj** (na kartce lub w komentarzu) ścieżkę: który plik (klasa) obsługuje które zadanie?
   - Kto przyjmuje żądanie HTTP? → `...Controller.java`
   - Kto wywołuje logikę biznesową? → `...Service.java`
   - Kto zapisuje dane? → `InMemory...Repository.java`

2. **Sprawdź importy** w `InstrumentService.java` — czy Service importuje cokolwiek z warstwy Presentation (Controller)? Powinien?

3. **Sprawdź importy** w `InstrumentController.java` — czy Controller importuje cokolwiek z warstwy Persistence (Repository)? Powinien?

> 💡 Jeśli odpowiedzi na pyt. 2 i 3 to "nie" — Twoja aktualna struktura poprawnie realizuje zasadę "zależności tylko w dół". Brawo! Ale czy Service jest związany z **konkretną implementacją** Repository? To problem, który rozwiążemy w Lekcji 04.

## Sprawdzian wiedzy

- [x] Rozumiem układ warstw: Controller -> Service -> Repository
- [x] Znam złotą zasadę: zależności idą tylko w dół
- [x] Dostrzegam wady architektury warstwowej (np. tight coupling z bazą danych)
- [x] Przeanalizowałem przepływ żądania w obecnym kodzie i upewniłem się, że warstwy są odseparowane
