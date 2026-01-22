# TODO: Wallet Manager - Etap 1 (Controller & Input Handling)

Lista zadań wynikająca z Lekcji 3 (Input Handling) oraz wymagań MVP z PRD.

## Instrument Controller (`/api/instruments`)

- [ ] **Pobieranie pojedynczego instrumentu**:
    - Endpoint: `GET /api/instruments/{id}`
    - Użycie: `@PathVariable` do pobrania ID.
    - Logika: Znalezienie instrumentu w liście/mapie po ID.
- [ ] **Filtrowanie instrumentów**:
    - Endpoint: `GET /api/instruments`
    - Użycie: `@RequestParam` do filtrowania (np. `?type=STOCK`, `?currency=PLN`).
    - Wymagania: Obsługa parametrów opcjonalnych (zwraca wszystko, jeśli brak filtrów).
- [ ] **Tworzenie instrumentu**:
    - Endpoint: `POST /api/instruments`
    - Użycie: `@RequestBody` do przyjęcia obiektu JSON.
    - Pola (zgodnie z PRD): `ticker`, `currency`, `market`, `type`.

## Transaction Controller (`/api/transactions`) - *Zalążek*

- [ ] **Utworzenie kontrolera**:
    - Stworzenie klasy `TransactionController` z mapowaniem `/api/transactions`.
- [ ] **Dodawanie transakcji**:
    - Endpoint: `POST /api/transactions`
    - Użycie: `@RequestBody`.
    - Pola (zgodnie z PRD): `instrumentId`, `type` (BUY/SELL), `quantity`, `price`, `date`.

## Weryfikacja

- [ ] Sprawdzić poprawność mapowania JSON -> Obiekt Java.
- [ ] Przetestować endpointy w `rest/instrument.rest` (lub Postman).