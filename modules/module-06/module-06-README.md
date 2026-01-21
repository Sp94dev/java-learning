# Moduł 06: Bazy Danych Deep Dive

## Cel
Efektywna praca z bazą danych - relacje, optymalizacja, migracje.

---

## Tematy do opanowania

### 1. N+1 Problem
- [ ] Co to jest N+1 (1 query + N dodatkowych)
- [ ] Jak rozpoznać (logi SQL, performance)
- [ ] **Rozwiązanie 1:** JOIN FETCH w JPQL
- [ ] **Rozwiązanie 2:** @EntityGraph
- [ ] **Rozwiązanie 3:** @BatchSize

### 2. Lazy vs Eager Loading
- [ ] **LAZY** - ładowane przy dostępie (default dla kolekcji)
- [ ] **EAGER** - ładowane od razu (default dla @ManyToOne)
- [ ] Rekomendacja: wszystko LAZY, fetch gdy potrzeba
- [ ] `LazyInitializationException` - przyczyna i rozwiązanie

### 3. Entity Relationships
- [ ] **@ManyToOne** - wiele do jeden (FK w tej tabeli)
- [ ] **@OneToMany** - jeden do wielu (mappedBy)
- [ ] **@OneToOne** - jeden do jeden
- [ ] **@ManyToMany** - wiele do wielu (join table)
- [ ] Bidirectional vs Unidirectional

### 4. Cascade Types
- [ ] `CascadeType.PERSIST` - save parent → save children
- [ ] `CascadeType.MERGE` - update parent → update children
- [ ] `CascadeType.REMOVE` - delete parent → delete children
- [ ] `CascadeType.ALL` - wszystkie powyższe
- [ ] `orphanRemoval = true` - usuń "sieroty"

### 5. Indexy
- [ ] Po co index (przyspieszenie wyszukiwania)
- [ ] Kiedy dodać (kolumny w WHERE, JOIN, ORDER BY)
- [ ] `@Index` w `@Table`
- [ ] EXPLAIN ANALYZE - sprawdzanie query plan

### 6. Database Migrations (Flyway)
- [ ] Po co migracje (wersjonowanie schematu)
- [ ] Naming: `V1__create_instruments.sql`
- [ ] Lokalizacja: `src/main/resources/db/migration/`
- [ ] `flyway_schema_history` - tabela z historią
- [ ] Niezmienialność wykonanych migracji

### 7. Pagination
- [ ] `Pageable` interface
- [ ] `PageRequest.of(page, size, sort)`
- [ ] `Page<T>` vs `Slice<T>` vs `List<T>`
- [ ] Repository method: `Page<Entity> findAll(Pageable pageable)`

### 8. Projections
- [ ] Interface-based projection (getter methods)
- [ ] Class-based projection (DTO)
- [ ] Pobieranie tylko potrzebnych kolumn

### 9. Connection Pool (HikariCP)
- [ ] Po co pool (reużycie połączeń)
- [ ] HikariCP - default w Spring Boot
- [ ] Konfiguracja: `maximum-pool-size`, `connection-timeout`

---

## Powiązana teoria
- `docs/theory/05-databases-jpa.md` → N+1, Relationships, Transactions, Migrations

---

## Przykład N+1 Fix
```java
// ❌ N+1
List<Portfolio> portfolios = repo.findAll();
portfolios.forEach(p -> p.getTransactions().size()); // N queries!

// ✅ JOIN FETCH
@Query("SELECT p FROM Portfolio p JOIN FETCH p.transactions")
List<Portfolio> findAllWithTransactions();
```

---

## Przykład Flyway
```
src/main/resources/db/migration/
├── V1__create_instruments.sql
├── V2__create_transactions.sql
└── V3__add_index_on_transactions.sql
```

---

## Ćwiczenia
1. Dodaj relację @ManyToOne (Transaction → Instrument)
2. Wykryj i napraw N+1 problem
3. Dodaj index na kolumnę `instrument_id`
4. Skonfiguruj Flyway i napisz migracje
5. Zaimplementuj paginację dla transakcji

---

## Sprawdzian gotowości
- [ ] Rozumiem N+1 problem i potrafię go rozwiązać
- [ ] Wiem różnicę między LAZY i EAGER
- [ ] Potrafię mapować relacje (@ManyToOne, @OneToMany)
- [ ] Używam Flyway do migracji
- [ ] Potrafię zrobić paginację
