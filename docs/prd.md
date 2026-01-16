# Wallet Manager - PRD

## Cel
Import transakcji z XTB → Zobacz wartość portfela.

## Funkcjonalności

### MVP
1. **CRUD Instruments** - tickery z metadanymi
2. **CRUD Transactions** - BUY/SELL z prowizją
3. **Import XTB** - parser CSV, zamknięta pozycja = BUY + SELL
4. **Dashboard** - wartość, koszt, zysk

### Później (poza kursem)
- Pobieranie cen (Yahoo Finance)
- Multi-portfolio
- Frontend (Angular)

## Model danych

```
instruments: ticker, currency, market, type
transactions: instrument_id, type, quantity, price, fee, date, platform_id
prices: instrument_id, price, updated_at (cache)
```

## Stack
- Java 25 + Spring Boot 4
- PostgreSQL
- Docker

## Nie robimy
- Multi-user / Auth
- Multi-currency
- FIFO / podatki
- Realtime
