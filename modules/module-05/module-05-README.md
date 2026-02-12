# Moduł 05: JPA + PostgreSQL

## Cel

Wprowadzić trwałość danych z użyciem JPA i PostgreSQL.

---

## Tematy do opanowania

### 1. Docker Compose + PostgreSQL

- [ ] `docker-compose.yml` z PostgreSQL
- [ ] Environment variables (POSTGRES_DB, USER, PASSWORD)
- [ ] Volumes - persystencja danych
- [ ] Komendy: `docker-compose up -d`, `down`, `logs`

### 2. Spring Data JPA Setup

- [ ] Dependency: `spring-boot-starter-data-jpa`
- [ ] Driver: `postgresql`
- [ ] `application.properties` - konfiguracja datasource
- [ ] `spring.jpa.hibernate.ddl-auto` (update, validate, create)

### 3. JPA Entity

- [ ] `@Entity` - klasa = tabela
- [ ] `@Table` - nazwa tabeli
- [ ] `@Id` - primary key
- [ ] `@GeneratedValue` - auto-increment
- [ ] `@Column` - konfiguracja kolumny (nullable, unique, length)
- [ ] Konstruktor bezargumentowy (wymagany przez JPA)

### 4. Entity Lifecycle

- [ ] **NEW** (Transient) - przed persist
- [ ] **MANAGED** - śledzony przez persistence context
- [ ] **DETACHED** - po zamknięciu sesji
- [ ] **REMOVED** - do usunięcia

### 5. Spring Data Repository

- [ ] `JpaRepository<Entity, ID>` - gotowe CRUD
- [ ] Dostępne metody: `save`, `findById`, `findAll`, `deleteById`
- [ ] Zero implementacji - Spring generuje

### 6. Query Methods

- [ ] Generowanie SQL z nazwy metody
- [ ] `findByTicker(String ticker)`
- [ ] `findByCurrency(String currency)`
- [ ] `findByTickerContaining(String part)`
- [ ] `countByCurrency(String currency)`
- [ ] `existsByTicker(String ticker)`

### 7. Custom Queries (@Query)

- [ ] JPQL - obiektowy SQL
- [ ] Native SQL (`nativeQuery = true`)
- [ ] `@Param` - named parameters
- [ ] `@Modifying` - dla UPDATE/DELETE

### 8. @Transactional

- [ ] Co to jest transakcja (ACID)
- [ ] `@Transactional` na metodzie Service
- [ ] Rollback przy RuntimeException
- [ ] `readOnly = true` dla SELECT (optymalizacja)

### 9. MongoDB (teoria + porównanie) 🆕

- [ ] SQL vs NoSQL — kiedy co
- [ ] Spring Data MongoDB — Document, MongoRepository
- [ ] Porównanie z JPA — analogie i różnice
- [ ] Kiedy Mongo ma sens (flexible schema, scale-out)

### 10. Spring Batch 🆕

- [ ] ETL — Extract, Transform, Load
- [ ] Job, Step, ItemReader, ItemProcessor, ItemWriter
- [ ] Import CSV → DB (praktyczny use case: XTB format)
- [ ] Chunk size i przetwarzanie w partiach

---

## Powiązana teoria

- `docs/theory/05-databases-jpa.md` → ACID, Entity Lifecycle, Transactions

---

## docker-compose.yml

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: wallet
      POSTGRES_USER: wallet
      POSTGRES_PASSWORD: wallet123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

---

## application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/wallet
spring.datasource.username=wallet
spring.datasource.password=wallet123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Ćwiczenia

1. Uruchom PostgreSQL w Docker
2. Stwórz `InstrumentEntity` z adnotacjami JPA
3. Stwórz `InstrumentRepository extends JpaRepository`
4. Napisz Query Methods (findByCurrency, existsByTicker)
5. Dodaj `@Transactional` do Service

---

## Sprawdzian gotowości

- [ ] PostgreSQL działa w Docker
- [ ] Mam Entity z @Id, @Column
- [ ] Repository extends JpaRepository
- [ ] Potrafię pisać Query Methods
- [ ] Rozumiem @Transactional
