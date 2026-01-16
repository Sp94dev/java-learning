# Roadmapa

## Moduł 00: Setup + Tooling
**Cel:** Działające środowisko, rozumiesz co jest co.

### Instalacja
- JDK 25 (SDKMAN lub manual)
- Maven
- Docker Desktop
- VS Code + rozszerzenia
- Claude Code CLI

### VS Code Extensions
- Extension Pack for Java
- Spring Boot Extension Pack
- Lombok Annotations Support

### Narzędzia - co jest co
| Narzędzie | Rola | Analog z frontend |
|-----------|------|-------------------|
| JDK | Runtime + kompilator | Node.js |
| Maven | Build + zależności | npm/yarn |
| Spring Boot | Framework webowy | NestJS/Express |
| Docker | Konteneryzacja | Docker |

### Weryfikacja
```bash
java --version      # 25+
mvn --version       # 3.9+
docker --version    # 24+
claude --version    # Claude Code CLI
```

## Moduł 01: REST + Java 25
**Cel:** Wystawić pierwsze API, poznać Records i Streams.
- Controller, GET/POST
- Java Records jako DTO
- Stream API (funkcyjny styl)
- In-memory storage
- **Projekt:** GET/POST `/instruments`, `/transactions`

## Moduł 02: DI + Warstwy
**Cel:** Oddzielić logikę od HTTP, zrozumieć Spring DI.
- Warstwa Service
- Constructor Injection
- Lombok
- CRUD in-memory
- **Projekt:** Logika obliczeń (wartość, koszt, zysk)

## Moduł 03: JPA + PostgreSQL
**Cel:** Trwałość danych w prawdziwej bazie.
- Docker Compose + PostgreSQL
- Entity, Repository
- Spring Data JPA
- @Transactional
- **Projekt:** Tabele instruments, transactions, prices

## Moduł 04: Walidacja + Błędy
**Cel:** API nie "wybucha", zwraca czytelne błędy.
- @Valid, @NotNull, @Size
- @ControllerAdvice
- Spójny ErrorDto
- Kody HTTP (400, 404, 500)
- **Projekt:** Walidacja importu CSV, obsługa błędnych danych

## Moduł 05: Testy
**Cel:** Pewność że kod działa.
- JUnit 5 + AssertJ
- Unit testy (Mockito)
- Integration testy
- Test containers
- **Projekt:** Testy parsera XTB, kalkulacji zysku

## Moduł 06: Docker + Deploy
**Cel:** Aplikacja działa wszędzie.
- Maven build (JAR)
- Dockerfile
- Docker Compose (App + DB)
- Health checks
- **Projekt:** Deploy Wallet Manager
