# Java Backend Learning

> Angular Senior → Enterprise AI Solutions Architect (Java 25 + Spring Boot 4 + Azure)

## Progress

### Phase 1: Fundamentals

| #   | Module                           | Status |
| --- | -------------------------------- | ------ |
| 00  | Setup + Tooling                  | 🟢     |
| 01  | REST + Java Basics               | 🟢     |
| 02  | Java Internals (JVM, Memory, GC) | 🟢     |

### Phase 2: Architecture

| #   | Module                          | Status |
| --- | ------------------------------- | ------ |
| 03  | DI + Layers                     | 🟢     |
| 04  | Architecture (Clean, Hexagonal) | 🟡     |

### Phase 3: Data

| #   | Module                     | Status |
| --- | -------------------------- | ------ |
| 05  | JPA + PostgreSQL           | ⚪     |
| 06  | Databases Deep Dive        | ⚪     |
| 07  | Caching (Redis, In-Memory) | ⚪     |

### Phase 4: Security

| #   | Module                      | Status |
| --- | --------------------------- | ------ |
| 08  | Auth (JWT, Spring Security) | ⚪     |

### Phase 5: Quality

| #   | Module                     | Status |
| --- | -------------------------- | ------ |
| 09  | Validation + Error Handling| ⚪     |
| 10  | Tests                      | ⚪     |
| 11  | Debugging & Profiling      | ⚪     |

### Phase 6: DevOps

| #   | Module                  | Status |
| --- | ----------------------- | ------ |
| 12  | Containerization (Docker)| ⚪     |
| 13  | CI/CD (GitHub Actions)  | ⚪     |
| 14  | Cloud Deployment        | ⚪     |

### Phase 7: Practice

| #   | Module                | Status |
| --- | --------------------- | ------ |
| 15  | Good and Bad Practices | ⚪     |
| 16  | Interview Prep        | ⚪     |
| 17  | Frontend Integration  | ⚪     |

### Phase 8: Advanced

| #   | Module                    | Status |
| --- | ------------------------- | ------ |
| 18  | Web Beyond REST (GraphQL) | ⚪     |
| 19  | Messaging (Kafka)         | ⚪     |
| 20  | Spring AI                 | ⚪     |

`⚪ Not Started` · `🟡 In Progress` · `🟢 Done`

## Project

**Wallet Manager API** - investment portfolio tracker.

## Structure

```
module-XX/           # Lessons and exercises
projects/wallet-manager/  # Main project
docs/                # Documentation
```

## Timeline

~12 months (46 weeks + buffer)

<!-- AI:START -->

## Technical Details

### Project Overview

This repository contains the source code for the "Java Backend Learning" curriculum, including the "Wallet Manager" capstone project and various learning modules.

### Tech Stack

- **Language**: Java 25
- **Framework**: Spring Boot 4.0.1
- **Build Tool**: Maven (mvnw)
- **Database**: PostgreSQL (planned), Redis (planned)

### Build & Run

**Wallet Manager:**

```bash
cd projects/wallet-manager
./mvnw clean install
./mvnw spring-boot:run
```

**Module Exercises (e.g., Module 00):**

```bash
cd modules/module-00/hello-spring
./mvnw spring-boot:run
```

### Project Structure

- `modules/`: Learning modules containing isolated exercises.
- `projects/`: Capstone projects (Wallet Manager).
- `docs/`: Theory, roadmaps, and documentation.

### Configuration

- **Application Name**: Wallet
- **Port**: 8080 (Default)
- **Properties**: `projects/wallet-manager/src/main/resources/application.properties`
<!-- AI:END -->
