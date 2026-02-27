# Lekcja 01: Po co architektura?

> 📖 Szczegółowa teoria z diagramami: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 1.

Na frontendzie (np. w Angularze) dzielisz wszystko na Komponenty i Moduły. Wiesz z doświadczenia, że można napisać całą aplikację w jednym olbrzymim pliku `app.component.ts`, tylko... nie będziesz w stanie jej po miesiącu utrzymać ani przetestować.

Z backendem w Javie jest dokładnie tak samo. Zapamiętaj 4 filary:

## 4 Filary Dobrej Architektury

1. **Separation of Concerns (SoC)** — Każdy element robi jedno. Controller obsługuje HTTP, Service liczy logikę, Repository mówi z bazą. Jak w Angularze: komponent wyświetla, serwis pobiera dane.

2. **Low Coupling, High Cohesion** — Klasy, które się zmieniają razem, leżą razem (kohezja). Klasy, które nie muszą się znać — nie znają się (coupling). Zmiana SDK od OpenAI nie powinna wymagać refaktoru modelu transakcji.

3. **Testability** — Jeśli wyabstrahujesz bazę danych za interfejsem, testujesz serwis bez stawiania PostgreSQL. Wstrzykujesz Mock i sprawdzasz logikę w milisekundach.

4. **Changeability** — Koszt zmiany technologii (np. InMemory → PostgreSQL) mierzy jakość architektury. Jeśli wymaga przeedytowania 15 plików — coś poszło nie tak.

---

## 🏋️ Zadanie: Audyt Wallet Manager

Otwórz `InstrumentService.java` w wallet-manager i odpowiedz na pytania:

1. **Coupling:** Znajdź pole `private final InMemoryInstrumentRepository`. Czy Serwis zna konkretną implementację repozytorium? Co by się stało, gdybyś chciał zamienić `InMemoryInstrumentRepository` na `JpaInstrumentRepository` — ile plików musiałbyś zmienić?

2. **Testability:** Czy możesz napisać test jednostkowy dla `InstrumentService` bez uruchamiania Springa i bez prawdziwej bazy? Dlaczego tak/nie?

3. **SoC:** Sprawdź `InMemoryInstrumentRepository` — konstruktor ładuje testowe dane (`AAPL`, `GOOGL`...). Czy to odpowiedzialność Repository? Gdzie powinny być dane testowe?

> 💡 **Podpowiedź:** Jeśli serwis wstrzykiwałby **interfejs** `InstrumentRepository` zamiast **klasy** `InMemoryInstrumentRepository`, odpowiedzi na pyt. 1 i 2 byłyby zupełnie inne. Wrócimy do tego w Lekcji 04 (Hexagonal).

## Sprawdzian wiedzy

- [x] Zrozumiałem 4 filary dobrej architektury (SoC, Kohezja/Coupling, Testability, Changeability)
- [x] Przeprowadziłem audyt `InstrumentService` pod kątem coupling'u z `InMemoryInstrumentRepository`
- [x] Zauważyłem problem z hardkodowanymi danymi w repozytorium (łamanie SoC)
