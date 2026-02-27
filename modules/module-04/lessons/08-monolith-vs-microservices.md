# Lekcja 08: Monolith vs Microservices

> 📖 Diagramy i decision tree: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 7.

## Monolith

Jeden projekt, jeden `.jar`, jeden deployment. To **Wallet Manager teraz**.

**Zalety:** Prostota, brak sieciowych opóźnień, łatwe transakcje ACID.
**Wady:** Skalowanie całości, nie części. Ciężki build w dużym zespole.

## Microservices

Każdy feature to osobna usługa z własną bazą. Angular: jak Micro-Frontends (Module Federation).

**Zalety:** Niezależne skalowanie, niezależny deploy, fault isolation.
**Wady:** Złożoność operacyjna, network latency, distributed transactions.

## 🏆 Modular Monolith (Złoty Środek)

**"Monolith first"** — pisz Modułami per Feature **wewnątrz jednej paczki**. Przygotuj granice tak, żeby wydzielenie mikroserwisu było kwestią godzin, nie tygodni.

To właśnie robisz w wallet-manager: `instrument/`, `transaction/` to osobne moduły wewnątrz monolitu.

---

## 🏋️ Zadanie: Analiza gotowości do wydzielenia

Wyobraź sobie, że za rok `transaction/` musi stać się osobnym mikroserwisem.

1. **Sprawdź cross-module dependencies:** Otwórz KAŻDY plik w `transaction/` i wypisz importy z pakietu `com.sp94dev.wallet.instrument`. Jeśli są — to **przeszkoda** w wydzieleniu.

2. **Model `Transaction`** — czy odwołuje się do `Instrument` przez obiekt czy przez `instrumentId` (Long)? Który sposób jest lepszy dla przyszłej separacji? (Przechowywanie ID zamiast referencji = luźniejsze powiązanie).

3. **Odpowiedz:** Gdybyś teraz usunął folder `instrument/` z projektu — czy `transaction/` nadal by się skompilował? Jeśli nie — wypisz co trzeba odciąć.

> 💡 Ideał: moduły komunikują się wyłącznie przez publiczne API lub eventy, nigdy przez import wewnętrznych klas.


## Sprawdzian wiedzy

- [ ] Znam główne różnice i wady/zalety Monolitu i Mikroserwisów
- [ ] Rozumiem koncepcję Modular Monolith jako kompromisu i punktu wyjścia
- [ ] Wiem, dlaczego "Monolith first" to rekomendowane podejście
- [ ] Przeanalizowałem, czy usunięcie jednego pakietu biznesowego zepsułoby inny w obecnym kodzie
