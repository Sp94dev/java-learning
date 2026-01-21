# Moduł 15: Dobre i Złe Praktyki

## Cel
Pisać kod jak Senior - unikać anti-patterns, stosować best practices.

---

## Tematy do opanowania

### 1. Naming Conventions
- [ ] Klasy: PascalCase, rzeczowniki (User, OrderService)
- [ ] Metody: camelCase, czasowniki (createUser, calculateTotal)
- [ ] Zmienne: camelCase, znaczące nazwy (nie `d`, lecz `daysUntilExpiry`)
- [ ] Stałe: SCREAMING_SNAKE_CASE
- [ ] Boolean: is/has/can prefix (isActive, hasPermission)
- [ ] Unikaj skrótów (oprócz powszechnych: id, url)

### 2. Method Design
- [ ] Małe metody (max 20-30 linii)
- [ ] Single Responsibility - jedna metoda = jedno zadanie
- [ ] Single level of abstraction
- [ ] Max 3-4 parametry (więcej → Object)
- [ ] Early return (guard clauses)

### 3. Early Return Pattern
- [ ] Zamiast nested if → early returns
- [ ] Czytelniejszy kod
- [ ] Mniej indentacji

### 4. Immutability
- [ ] Preferuj `final` fields
- [ ] Preferuj Records dla DTO
- [ ] Nie modyfikuj parametrów metody
- [ ] Zwracaj nowe obiekty zamiast mutować

### 5. Defensive Programming
- [ ] Waliduj parametry na wejściu
- [ ] `Objects.requireNonNull()`
- [ ] Fail fast - błąd wcześnie lepszy niż późno
- [ ] Używaj Optional zamiast null

### 6. Exception Handling
- [ ] Nie łap Exception/Throwable
- [ ] Łap konkretne wyjątki
- [ ] Nie swallow exceptions (puste catch)
- [ ] Loguj z context (co się stało, jakie dane)

### 7. Logging Best Practices
- [ ] Odpowiedni poziom (INFO dla normalnych operacji)
- [ ] Parametryzowane logi: `log.info("User: {}", userId)`
- [ ] Nie loguj sensitive data (hasła, tokeny)
- [ ] Kontekst: co, kto, dlaczego

---

## Anti-patterns do unikania

### 8. God Class
- [ ] Klasa robi wszystko (2000+ linii)
- [ ] Trudna do nazwania bez "and"
- [ ] **Fix:** Podziel na mniejsze klasy

### 9. Primitive Obsession
- [ ] Używanie primitives zamiast domain objects
- [ ] `String email` zamiast `Email email`
- [ ] **Fix:** Value Objects

### 10. Feature Envy
- [ ] Metoda więcej używa innej klasy niż swojej
- [ ] **Fix:** Przenieś metodę do właściwej klasy

### 11. Anemic Domain Model
- [ ] Entity bez logiki (tylko data + getters/setters)
- [ ] Logika w Service
- [ ] **Fix:** Rich Domain Model - logika w Entity

### 12. Magic Numbers/Strings
- [ ] Hardcoded wartości bez nazwy
- [ ] `if (status == 3)` - co to 3?
- [ ] **Fix:** Named constants, enums

---

## Spring-specific Pitfalls

### 13. Field Injection
- [ ] ❌ `@Autowired private Service service;`
- [ ] ✅ Constructor Injection

### 14. @Transactional on private
- [ ] ❌ `@Transactional private void save()` - nie działa!
- [ ] ✅ Tylko na public methods

### 15. Self-invocation Problem
- [ ] Wywołanie wewnętrzne pomija proxy
- [ ] `@Transactional` nie zadziała
- [ ] **Fix:** Wydziel do osobnego beana

### 16. Circular Dependencies
- [ ] A → B → A
- [ ] **Fix:** Refaktor, wydziel wspólną logikę

### 17. N+1 Queries
- [ ] Lazy loading w pętli
- [ ] **Fix:** JOIN FETCH, @EntityGraph

---

## REST API Best Practices

### 18. HTTP Status Codes
- [ ] 200 OK - sukces
- [ ] 201 Created - zasób utworzony (+ Location header)
- [ ] 204 No Content - sukces bez body (DELETE)
- [ ] 400 Bad Request - błędne dane
- [ ] 404 Not Found - nie znaleziono
- [ ] 409 Conflict - duplikat

### 19. Consistent Error Format
- [ ] Jeden format dla wszystkich błędów
- [ ] Pola: code, message, details, timestamp

### 20. Resource Naming
- [ ] Plural nouns: `/users`, `/orders`
- [ ] Hierarchia: `/users/{id}/orders`
- [ ] Bez czasowników: `/users` nie `/getUsers`

### 21. Versioning
- [ ] URL path: `/api/v1/users`
- [ ] Lub Header: `X-API-Version: 1`

---

## Powiązana teoria
- `docs/theory/03-oop-solid.md` → SOLID, Design Patterns
- `docs/theory/09-interview-best-practices.md` → Anti-patterns, Code Quality

---

## Code Review Checklist
```
□ Nazwy są znaczące i spójne
□ Metody są krótkie i robią jedną rzecz
□ Brak magic numbers/strings
□ Wyjątki są obsługiwane poprawnie
□ Logi są sensowne (odpowiedni poziom, context)
□ Brak Field Injection
□ @Transactional na public methods
□ Brak N+1 queries
□ HTTP status codes są poprawne
□ Format błędów jest spójny
```

---

## Ćwiczenia
1. Przejrzyj swój kod i znajdź magic numbers → zamień na stałe
2. Znajdź długie metody → podziel na mniejsze
3. Znajdź nested if → zamień na early returns
4. Sprawdź czy wszędzie masz Constructor Injection
5. Upewnij się że @Transactional nie jest na private
6. Przejrzyj HTTP status codes w kontrolerach

---

## Sprawdzian gotowości
- [ ] Używam meaningful names
- [ ] Moje metody są krótkie i fokusowane
- [ ] Stosuję early returns
- [ ] Preferuję immutability
- [ ] Unikam Field Injection
- [ ] Zwracam właściwe HTTP status codes
- [ ] Mam spójny format błędów
