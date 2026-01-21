# Moduł 12: Konteneryzacja (Docker)

## Cel
Aplikacja działa w Docker, pełny stack w Docker Compose.

---

## Tematy do opanowania

### 1. Docker Basics (przypomnienie)
- [ ] Image vs Container
- [ ] Image layers - cachowanie
- [ ] Registry (Docker Hub)
- [ ] Podstawowe komendy: `build`, `run`, `ps`, `logs`, `exec`

### 2. Dockerfile
- [ ] `FROM` - bazowy obraz
- [ ] `WORKDIR` - katalog roboczy
- [ ] `COPY` - kopiowanie plików
- [ ] `RUN` - wykonanie komendy (build time)
- [ ] `EXPOSE` - dokumentacja portu
- [ ] `ENTRYPOINT` vs `CMD` - co uruchomić

### 3. Multi-stage Build
- [ ] Stage 1: Build (JDK, Maven)
- [ ] Stage 2: Run (tylko JRE)
- [ ] Mniejszy finalny obraz
- [ ] Bezpieczniejszy (bez source code)

### 4. Dockerfile Best Practices
- [ ] Kolejność warstw (rzadko zmieniane najpierw)
- [ ] Cache dependencies oddzielnie od kodu
- [ ] Non-root user
- [ ] Health check
- [ ] JVM flags dla kontenerów

### 5. Docker Compose
- [ ] `docker-compose.yml` - definicja stacku
- [ ] Services, networks, volumes
- [ ] `depends_on` - kolejność startu
- [ ] `healthcheck` - sprawdzanie gotowości
- [ ] Environment variables

### 6. Pełny stack (app + db + redis)
- [ ] Service: app (twoja aplikacja)
- [ ] Service: postgres
- [ ] Service: redis
- [ ] Networking między serwisami
- [ ] Volumes dla persystencji

### 7. Environment Variables
- [ ] `SPRING_PROFILES_ACTIVE`
- [ ] `SPRING_DATASOURCE_URL`
- [ ] `.env` file
- [ ] Secrets management (podstawy)

### 8. Docker Commands
- [ ] `docker-compose up -d` - start w tle
- [ ] `docker-compose down` - stop
- [ ] `docker-compose down -v` - stop + usuń volumes
- [ ] `docker-compose logs -f app` - logi
- [ ] `docker-compose exec app sh` - shell w kontenerze

### 9. Debugging w Docker
- [ ] `docker logs <container>`
- [ ] `docker exec -it <container> sh`
- [ ] `docker stats` - resource usage
- [ ] Health check status

### 10. Docker Profiles (dev/prod)
- [ ] `docker-compose.override.yml` (auto-ładowane dla dev)
- [ ] `docker-compose.prod.yml`
- [ ] `docker-compose -f ... -f ... up`

---

## Powiązana teoria
- `docs/theory/08-testing-devops.md` → sekcja Containers & Docker

---

## Przykład: Dockerfile (Multi-stage)
```dockerfile
# Stage 1: Build
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:25-jre
WORKDIR /app

# Non-root user
RUN addgroup --system app && adduser --system --group app
USER app

COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
```

---

## Przykład: docker-compose.yml
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/wallet
      - SPRING_DATASOURCE_USERNAME=wallet
      - SPRING_DATASOURCE_PASSWORD=wallet123
      - SPRING_REDIS_HOST=redis
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_started
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

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
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U wallet"]
      interval: 10s
      timeout: 5s
      retries: 5

  redis:
    image: redis:7
    ports:
      - "6379:6379"

volumes:
  postgres_data:
```

---

## Ćwiczenia
1. Napisz Dockerfile (single stage)
2. Przebuduj na multi-stage
3. Dodaj non-root user
4. Stwórz docker-compose.yml (app + postgres)
5. Dodaj Redis do compose
6. Skonfiguruj health checks
7. Przetestuj `docker-compose up` - cały stack działa

---

## Sprawdzian gotowości
- [ ] Mam Dockerfile z multi-stage build
- [ ] docker-compose uruchamia app + db + redis
- [ ] Health checks działają
- [ ] Wiem jak debugować kontenery (logs, exec)
- [ ] Rozumiem volumes i networking
