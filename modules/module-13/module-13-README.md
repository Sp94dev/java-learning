# Moduł 13: CI/CD (GitHub Actions)


## Cel

Automatyzacja build, test, deploy z GitHub Actions.

---

## Tematy do opanowania

### 1. CI/CD Concepts

- [ ] **CI (Continuous Integration):** Auto build + test przy każdym commit
- [ ] **CD (Continuous Delivery):** Każdy commit MOŻE być deployed
- [ ] **CD (Continuous Deployment):** Każdy commit JEST deployed
- [ ] Fail fast, fix fast

### 2. GitHub Actions Basics

- [ ] Workflow file: `.github/workflows/*.yml`
- [ ] Trigger: `on: [push, pull_request]`
- [ ] Jobs - niezależne zadania
- [ ] Steps - kroki w job
- [ ] Actions - reusable steps

### 3. Workflow Structure

- [ ] `name` - nazwa workflow
- [ ] `on` - trigger (push, pull_request, schedule)
- [ ] `jobs` - lista jobów
- [ ] `runs-on` - runner (ubuntu-latest)
- [ ] `steps` - lista kroków

### 4. Common Actions

- [ ] `actions/checkout@v4` - checkout kodu
- [ ] `actions/setup-java@v4` - setup JDK
- [ ] `actions/cache@v4` - cache dependencies
- [ ] `docker/build-push-action@v5` - build & push Docker

### 5. Basic CI Pipeline

- [ ] Checkout
- [ ] Setup Java
- [ ] Cache Maven dependencies
- [ ] Build: `mvn clean verify`
- [ ] Upload test results (artifact)

### 6. CI with Database (Testcontainers/Services)

- [ ] `services:` - kontenery dla testów
- [ ] PostgreSQL service
- [ ] Environment variables dla testów

### 7. Build & Push Docker Image

- [ ] Trigger on tag: `on: push: tags: ['v*']`
- [ ] Login to Docker Hub: `docker/login-action`
- [ ] Build and push: `docker/build-push-action`
- [ ] Tagging: `latest` + version tag

### 8. Secrets Management

- [ ] Repository Secrets w GitHub
- [ ] `${{ secrets.DOCKER_USERNAME }}`
- [ ] `${{ secrets.DOCKER_TOKEN }}`
- [ ] Nigdy nie commituj secrets!

### 9. Quality Gates

- [ ] Code coverage (JaCoCo)
- [ ] Static analysis (opcjonalnie: SonarQube)
- [ ] Dependency scanning
- [ ] Fail pipeline jeśli quality gate nie przejdzie

### 10. Caching

- [ ] Cache Maven: `~/.m2/repository`
- [ ] Cache Docker layers
- [ ] Przyspieszenie pipeline

### 11. Artifacts

- [ ] Upload test results
- [ ] Upload coverage reports
- [ ] Download w kolejnych jobs

### 12. Branch Protection

- [ ] Require CI pass before merge
- [ ] Require reviews
- [ ] Settings → Branches → Branch protection rules

---

## Powiązana teoria

- `docs/theory/08-testing-devops.md` → sekcja CI/CD

---

## Przykład: Basic CI

```yaml
# .github/workflows/ci.yml
name: CI

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 25
        uses: actions/setup-java@v4
        with:
          java-version: "25"
          distribution: "temurin"
          cache: maven

      - name: Build and Test
        run: mvn clean verify

      - name: Upload Test Results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: target/surefire-reports/
```

---

## Przykład: CI with PostgreSQL

```yaml
jobs:
  test:
    runs-on: ubuntu-latest

    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: wallet_test
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: "25"
          distribution: "temurin"
          cache: maven

      - name: Run tests
        run: mvn test
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/wallet_test
          SPRING_DATASOURCE_USERNAME: test
          SPRING_DATASOURCE_PASSWORD: test
```

---

## Przykład: Build & Push Docker

```yaml
name: Build and Push

on:
  push:
    tags: ["v*"]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v3

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKER_USERNAME }}
          password: ${{ secrets.DOCKER_TOKEN }}

      - name: Build and push
        uses: docker/build-push-action@v5
        with:
          context: .
          push: true
          tags: |
            ${{ secrets.DOCKER_USERNAME }}/wallet-manager:latest
            ${{ secrets.DOCKER_USERNAME }}/wallet-manager:${{ github.ref_name }}
          cache-from: type=gha
          cache-to: type=gha,mode=max
```

---

## Ćwiczenia

1. Stwórz `.github/workflows/ci.yml`
2. Skonfiguruj build + test
3. Dodaj caching dla Maven
4. Dodaj PostgreSQL service dla integration tests
5. Stwórz workflow do build & push Docker
6. Skonfiguruj secrets (DOCKER_USERNAME, DOCKER_TOKEN)
7. Włącz branch protection (require CI pass)

---

## Sprawdzian gotowości

- [ ] Mam CI pipeline w GitHub Actions
- [ ] Testy uruchamiają się na każdy PR
- [ ] Docker image jest budowany automatycznie
- [ ] Używam secrets do credentials
- [ ] Branch protection wymaga CI pass
