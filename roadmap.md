# Roadmap

## Module 00: Setup + Tooling

**Goal:** Working environment, you understand what is what.

### Installation

- JDK 21 (SDKMAN or manual)
- Maven
- Docker Desktop
- VS Code + extensions
- Claude Code CLI

### VS Code Extensions

- Extension Pack for Java
- Spring Boot Extension Pack
- Lombok Annotations Support

### Tools - What Is What

| Tool        | Role                   | Frontend Analog |
| ----------- | ---------------------- | --------------- |
| JDK         | Runtime + compiler     | Node.js         |
| Maven       | Build + dependencies   | npm/yarn        |
| Spring Boot | Web framework          | NestJS/Express  |
| Docker      | Containerization       | Docker          |

### Verification
```bash
java --version      # 21+
mvn --version       # 3.9+
docker --version    # 24+
claude --version    # Claude Code CLI
```

## Module 01: REST + Java 21

**Goal:** Expose your first API, learn Records and Streams.

- Controller, GET/POST
- Java Records as DTO
- Stream API (functional style)
- In-memory storage

## Module 02: DI + Layers

**Goal:** Separate logic from HTTP, understand Spring DI.

- Service Layer
- Constructor Injection
- Lombok
- CRUD in-memory

## Module 03: JPA + PostgreSQL

**Goal:** Data persistence in a real database.

- Docker Compose + PostgreSQL
- Entity, Repository
- Spring Data JPA
- @Transactional

## Module 04: Validation + Error Handling

**Goal:** API doesn't "crash", returns readable errors.

- @Valid, @NotNull, @Size
- @ControllerAdvice
- Consistent ErrorDto
- HTTP codes (400, 404, 500)

## Module 05: Tests

**Goal:** Confidence that code works.

- JUnit 5 + AssertJ
- Unit tests (Mockito)
- Integration tests
- Test containers

## Module 06: Docker + Deploy

**Goal:** Application works everywhere.

- Maven build (JAR)
- Dockerfile
- Docker Compose (App + DB)
- Health checks