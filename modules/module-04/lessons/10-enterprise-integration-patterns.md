# Lekcja 10: Enterprise Integration Patterns (EIP) `opt`

> ⚠️ Lekcja opcjonalna — poszerza horyzonty architektoniczne. Wallet Manager w Phase MVP nie wymaga message brokera.

Kiedy aplikacja nie żyje sama — musi komunikować się z innymi systemami. EIP definiuje 4 style integracji:

## 4 Style Integracji

| Styl                | Jak działa                                 | Przykład                                                      |
| ------------------- | ------------------------------------------ | ------------------------------------------------------------- |
| **File Transfer**   | Systemy wymieniają pliki (CSV, JSON)       | Import CSV z XTB (`POST /api/instruments/import` w Module 05) |
| **Shared Database** | Dwie apki czytają z jednej bazy            | ❌ Unikaj — tight coupling do schematu                        |
| **RPC (REST/gRPC)** | Synchroniczne wywołanie API                | `POST /api/advisor` wołający Azure OpenAI (Module 20)         |
| **Messaging**       | Asynchroniczne eventy przez broker (Kafka) | Ciężkie zadania AI (reindeksacja embeddingów)                 |

## Kiedy który?

| Kryterium           | File Transfer  | Shared DB     | RPC         | Messaging       |
| ------------------- | -------------- | ------------- | ----------- | --------------- |
| **Czas odpowiedzi** | Minuty/Godziny | Milisekundy   | Milisekundy | Sekundy (async) |
| **Coupling**        | Niski          | Bardzo wysoki | Średni      | Niski           |
| **Złożoność**       | Niska          | Niska         | Średnia     | Wysoka          |

---

## 🏋️ Zadanie: Mapowanie stylów na roadmapę

Otwórz `projects/PROJECT.md` i `todo.md`. Dla każdego przyszłego endpointu określ, jaki styl integracji będzie potrzebny:

| Endpoint / Feature                         | Moduł | Sugerowany styl | Uzasadnienie                      |
| ------------------------------------------ | ----- | --------------- | --------------------------------- |
| `POST /api/instruments/import` (CSV z XTB) | 05    | ?               | Plik wchodzi → przetworz → zapisz |
| `POST /api/advisor` (AI Financial Advisor) | 20    | ?               | Synchroniczne pytanie → odpowiedź |
| Cache cen instrumentów (Redis)             | 07    | ?               | Skąd Redis dostanie dane?         |

> 📚 Rekomendowana lektura: _"Enterprise Integration Patterns"_ (Hohpe, Woolf) — biblia integracji systemów Enterprise.


## Sprawdzian wiedzy

- [ ] Znam 4 główne style integracji systemów Enterprise (File, DB, RPC, Messaging)
- [ ] Potrafię dobrać odpowiedni model komunikacji na podstawie wymagań (synchroniczny vs asynchroniczny)
- [ ] Rozumiem ograniczenia i przypadki użycia współdzielonej bazy danych i API REST
