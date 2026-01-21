# TEORIA: Bazy Danych i JPA

---

## 1. Relacyjne bazy danych - fundamenty

### ACID

Właściwości transakcji gwarantujące integralność danych:

**A - Atomicity (Atomowość)**
Transakcja wykonuje się w całości lub wcale. Nie ma stanów pośrednich.
```
Transfer: konto A → konto B
Albo: (A -= 100) AND (B += 100)
Albo: nic się nie dzieje
Nigdy: A -= 100, B bez zmian
```

**C - Consistency (Spójność)**
Transakcja przeprowadza bazę z jednego spójnego stanu do drugiego.
Constraints, foreign keys, triggers - wszystko musi być spełnione.

**I - Isolation (Izolacja)**
Równoległe transakcje nie widzą swoich częściowych zmian.
Poziomy izolacji definiują "jak bardzo" są odizolowane.

**D - Durability (Trwałość)**
Zatwierdzona transakcja jest permanentna - przetrwa crash systemu.
Dane zapisane na dysku, nie tylko w pamięci.

### Isolation Levels

| Level | Dirty Read | Non-repeatable Read | Phantom Read |
|-------|------------|---------------------|--------------|
| READ UNCOMMITTED | ✓ | ✓ | ✓ |
| READ COMMITTED | ✗ | ✓ | ✓ |
| REPEATABLE READ | ✗ | ✗ | ✓ |
| SERIALIZABLE | ✗ | ✗ | ✗ |

**Dirty Read:** Widzisz niezatwierdzone zmiany innej transakcji
**Non-repeatable Read:** Ten sam SELECT daje różne wyniki (ktoś zmienił)
**Phantom Read:** Pojawia się nowy wiersz pasujący do WHERE

**Trade-off:** Wyższa izolacja = więcej locków = wolniej

---

## 2. ORM - Object-Relational Mapping

### Problem impedancji
**Object world:**
- Obiekty z metodami
- Dziedziczenie
- Graf referencji
- Enkapsulacja

**Relational world:**
- Tabele i wiersze
- Brak dziedziczenia
- Foreign keys
- Wszystko publiczne

ORM mapuje między tymi światami.

### Zalety ORM
1. **Produktywność** - mniej boilerplate
2. **Abstrakcja** - zmiana DB bez zmiany kodu
3. **Type safety** - błędy w compile time
4. **Lazy loading** - ładuj gdy potrzeba

### Wady ORM
1. **Leaky abstraction** - musisz znać SQL
2. **Performance** - N+1, nadmiarowe query
3. **Complexity** - trudne debugowanie
4. **Learning curve** - trzeba rozumieć jak działa

### JPA vs Hibernate
**JPA** (Jakarta Persistence API) - specyfikacja (interfejsy)
**Hibernate** - implementacja JPA (najpopularsza)

Analogia: JPA to jak JDBC (standard), Hibernate to jak driver PostgreSQL (implementacja).

---

## 3. Entity Lifecycle

### Stany Entity

```
         ┌──────────────────────────────────────┐
         │                                      │
         ▼                                      │
    ┌─────────┐     persist()      ┌─────────┐ │
    │  NEW    │───────────────────►│ MANAGED │ │
    │(Transient)                   │         │◄┘
    └─────────┘                    └────┬────┘
                                        │
                    ┌───────────────────┼───────────────────┐
                    │                   │                   │
                    ▼                   ▼                   ▼
              detach()            flush()             remove()
                    │                   │                   │
                    ▼                   ▼                   ▼
              ┌─────────┐         ┌─────────┐         ┌─────────┐
              │DETACHED │         │   DB    │         │ REMOVED │
              └────┬────┘         └─────────┘         └─────────┘
                   │                                        │
                   │ merge()                                │
                   │                                        │
                   └────────────────►┌─────────┐◄───────────┘
                                     │ MANAGED │   (rollback)
                                     └─────────┘
```

**NEW (Transient):**
- Obiekt utworzony przez `new`
- Nie ma ID
- Nie jest śledzony przez JPA

**MANAGED:**
- Powiązany z persistence context
- Zmiany automatycznie synchronizowane z DB
- Ma ID

**DETACHED:**
- Był managed, ale persistence context zamknięty
- Ma ID, ale zmiany nie są śledzone

**REMOVED:**
- Zaplanowany do usunięcia
- Przy flush/commit → DELETE

### Persistence Context

**"First-level cache"** - bufor między aplikacją a DB.

```
┌─────────────────────────────────────────────────┐
│              Persistence Context                │
│                                                 │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐│
│  │ Entity A   │  │ Entity B   │  │ Entity C   ││
│  │ (managed)  │  │ (managed)  │  │ (managed)  ││
│  └────────────┘  └────────────┘  └────────────┘│
│                                                 │
│  Identity Map: ID → Entity                      │
│  Dirty Checking: śledzi zmiany                  │
└─────────────────────────────────────────────────┘
                      ↕ flush()
               ┌─────────────┐
               │   DATABASE  │
               └─────────────┘
```

**Gwarancje:**
1. **Identity** - `findById(1)` dwa razy zwraca TEN SAM obiekt
2. **Dirty checking** - zmiany wykrywane automatycznie
3. **Write-behind** - SQL wykonywane przy flush, nie od razu

---

## 4. Relacje między Entity

### @ManyToOne (najczęstsza)

```
Transaction N ──────────► 1 Instrument
```

Wiele transakcji → jeden instrument.
Foreign key w tabeli Transaction.

**Domyślnie:** EAGER (ładowane od razu) - zwykle chcesz LAZY!

### @OneToMany

```
Instrument 1 ──────────► N Transaction
```

Jeden instrument → wiele transakcji.
**Zwykle** dwukierunkowa z @ManyToOne.

**Domyślnie:** LAZY (ładowane na żądanie)

### @OneToOne

```
User 1 ──────────► 1 Profile
```

Jeden do jeden.
**Uwaga:** LAZY nie działa dla strony bez FK!

### @ManyToMany

```
Student N ──────────► M Course
```

Wymaga tabeli pośredniej (join table).
**Unikaj** gdy to możliwe - często lepiej explicit entity.

### Bidirectional vs Unidirectional

**Unidirectional:** Relacja w jedną stronę
```java
class Transaction {
    @ManyToOne
    Instrument instrument;  // Transaction → Instrument
}
```

**Bidirectional:** Obie strony znają się nawzajem
```java
class Transaction {
    @ManyToOne
    Instrument instrument;
}

class Instrument {
    @OneToMany(mappedBy = "instrument")
    List<Transaction> transactions;
}
```

**mappedBy** - wskazuje która strona jest "owning" (ma FK w tabeli).

---

## 5. Fetch Strategies

### EAGER vs LAZY

**EAGER:**
- Ładowane natychmiast z głównym entity
- JOIN w SQL
- Może ładować dużo niepotrzebnych danych

**LAZY:**
- Ładowane przy pierwszym dostępie
- Dodatkowy SELECT
- Wymaga otwartej sesji (LazyInitializationException!)

### N+1 Problem

**Scenariusz:**
```java
List<Portfolio> portfolios = repo.findAll();  // 1 SELECT
for (Portfolio p : portfolios) {
    p.getTransactions().size();  // N SELECT (jeden per portfolio!)
}
```

**SQL:**
```sql
SELECT * FROM portfolios;                     -- 1
SELECT * FROM transactions WHERE portfolio_id = 1;  -- N
SELECT * FROM transactions WHERE portfolio_id = 2;
SELECT * FROM transactions WHERE portfolio_id = 3;
...
```

### Rozwiązania N+1

**1. JOIN FETCH (JPQL)**
```java
@Query("SELECT p FROM Portfolio p JOIN FETCH p.transactions")
List<Portfolio> findAllWithTransactions();
```
Jeden SELECT z JOIN.

**2. @EntityGraph**
```java
@EntityGraph(attributePaths = {"transactions"})
List<Portfolio> findAll();
```

**3. @BatchSize**
```java
@OneToMany
@BatchSize(size = 100)
List<Transaction> transactions;
```
Ładuje w batchach: `WHERE portfolio_id IN (1,2,3...100)`

**4. Projection (DTO)**
Pobieraj tylko potrzebne dane, nie entity.

---

## 6. Transakcje w Spring

### @Transactional

```java
@Transactional
public void transfer(Long from, Long to, BigDecimal amount) {
    Account source = accountRepo.findById(from);
    Account target = accountRepo.findById(to);
    
    source.withdraw(amount);
    target.deposit(amount);
    
    // Commit przy wyjściu z metody (jeśli brak exception)
}
```

### Propagation

Jak transakcja zachowuje się gdy metoda wywołuje inną @Transactional metodę?

| Propagation | Zachowanie |
|-------------|------------|
| **REQUIRED** (default) | Użyj istniejącej lub utwórz nową |
| **REQUIRES_NEW** | Zawsze nowa (suspend bieżącej) |
| **MANDATORY** | Musi istnieć, inaczej exception |
| **SUPPORTS** | Jeśli jest - użyj, jeśli nie - bez |
| **NOT_SUPPORTED** | Zawsze bez transakcji |
| **NEVER** | Nie może być transakcji |
| **NESTED** | Nested transaction (savepoint) |

### Rollback

**Domyślnie:**
- RuntimeException → rollback
- Checked exception → commit (!)

**Konfiguracja:**
```java
@Transactional(rollbackFor = Exception.class)  // Wszystkie
@Transactional(noRollbackFor = BusinessException.class)  // Wyjątek
```

### Pułapki @Transactional

**1. Na private metodzie - nie działa**
AOP proxy nie widzi private metod.

**2. Self-invocation - nie działa**
```java
public void methodA() {
    methodB();  // Wywołanie wewnętrzne, pomija proxy!
}

@Transactional
public void methodB() { }
```

**3. Checked exceptions domyślnie nie rollbackują**
Musisz explicit: `rollbackFor = Exception.class`

---

## 7. Query Optimization

### Indexy

**Kiedy dodać index:**
- Kolumny w WHERE
- Kolumny w JOIN (FK)
- Kolumny w ORDER BY
- Kolumny z UNIQUE constraint

**Kiedy NIE:**
- Małe tabele
- Kolumny rzadko używane w query
- Kolumny z małą kardynalnością (boolean)

### EXPLAIN ANALYZE

```sql
EXPLAIN ANALYZE SELECT * FROM transactions WHERE instrument_id = 1;

-- Seq Scan = brak indexu (pełne skanowanie tabeli)
-- Index Scan = używa indexu (szybko)
-- Bitmap Index Scan = wiele wierszy, używa indexu
```

### Pagination

**Offset pagination (proste, wolne dla dużych offset):**
```sql
SELECT * FROM items ORDER BY id LIMIT 20 OFFSET 1000;
-- Musi przeskanować 1020 wierszy!
```

**Keyset pagination (szybsze):**
```sql
SELECT * FROM items WHERE id > 1000 ORDER BY id LIMIT 20;
-- Używa indexu, nie skanuje poprzednich
```

---

## 8. Database Migrations

### Po co?

Schemat bazy musi ewoluować razem z kodem. Migrations to "Git dla schematu DB".

### Flyway

**Koncepcja:**
1. Pliki SQL numerowane: V1__create_users.sql, V2__add_email.sql
2. Flyway wykonuje sekwencyjnie
3. Zapisuje które wykonane w tabeli `flyway_schema_history`
4. Przy starcie aplikacji - wykonuje brakujące

**Zasady:**
- Raz wykonana migracja = NIEZMIENIALANA
- Nowa zmiana = nowy plik
- Checksum zapobiega modyfikacji

### Forward-only migrations

**Nie rób rollback migrations** - zbyt ryzykowne.

Zamiast:
1. Additive changes (dodawaj, nie usuwaj)
2. Expand-contract pattern:
   - V1: Dodaj nową kolumnę
   - Deploy: Aplikacja pisze do obu
   - V2: Migruj dane
   - Deploy: Aplikacja używa tylko nowej
   - V3: Usuń starą kolumnę

---

## 9. Connection Pool

### Problem
Otwieranie połączenia do DB jest drogie (~50-100ms).

### Rozwiązanie: Pool
Pula gotowych połączeń. "Wypożyczasz" połączenie, używasz, zwracasz.

### HikariCP (default w Spring Boot)

Najszybszy pool dla JVM.

**Kluczowe parametry:**
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

**Sizing:**
Reguła: `pool_size = (core_count * 2) + disk_spindles`
Dla większości: 10-20 połączeń wystarcza.

---

## Podsumowanie

| Koncept | Znaczenie |
|---------|-----------|
| **ACID** | Gwarancje transakcji |
| **Entity Lifecycle** | NEW → MANAGED → DETACHED/REMOVED |
| **Persistence Context** | First-level cache, identity map |
| **LAZY vs EAGER** | Kiedy ładować relacje |
| **N+1** | Problem wydajności, rozwiąż JOIN FETCH |
| **@Transactional** | Demarcation transakcji |
| **Propagation** | Jak transakcje się zagnieżdżają |
| **Migrations** | Wersjonowanie schematu (Flyway) |
| **Connection Pool** | Reużycie połączeń (HikariCP) |
