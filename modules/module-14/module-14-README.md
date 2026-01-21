# Moduł 14: Cloud Deployment

## Cel
Wdrożenie aplikacji na cloud (Railway/Render) i podstawy AWS.

---

## Tematy do opanowania

### 1. Deployment Options Overview
- [ ] **Railway** - bardzo łatwy, free tier
- [ ] **Render** - łatwy, free tier
- [ ] **Heroku** - łatwy, paid
- [ ] **AWS ECS** - średnia złożoność
- [ ] **Kubernetes** - wysoka złożoność, enterprise

### 2. Wybór platformy
- [ ] Hobby/MVP → Railway lub Render
- [ ] Startup → Heroku, Railway Pro
- [ ] Production/Scale → AWS, GCP, Azure

### 3. Railway Deployment
- [ ] Utworzenie konta na railway.app
- [ ] Połączenie z GitHub
- [ ] Auto-detection Spring Boot
- [ ] Environment variables
- [ ] Dodanie PostgreSQL (Railway service)
- [ ] Custom domain (opcjonalnie)

### 4. Render Deployment
- [ ] Utworzenie konta na render.com
- [ ] Web Service → Connect GitHub
- [ ] Build command: `./mvnw clean package`
- [ ] Start command: `java -jar target/*.jar`
- [ ] Environment variables
- [ ] Managed PostgreSQL

### 5. Environment Configuration
- [ ] `SPRING_PROFILES_ACTIVE=prod`
- [ ] `DATABASE_URL` - connection string
- [ ] `PORT` - dynamiczny port (cloud przydziela)
- [ ] Secrets (JWT_SECRET, etc.)

### 6. application-prod.properties
- [ ] Datasource z env vars
- [ ] Wyłączony ddl-auto (`validate` lub `none`)
- [ ] Wyłączony show-sql
- [ ] Production-ready settings

### 7. Health Checks
- [ ] `/actuator/health` endpoint
- [ ] Platform sprawdza czy app żyje
- [ ] Restart jeśli unhealthy

### 8. Logging w Cloud
- [ ] Stdout/Stderr → Cloud logs
- [ ] Log aggregation (Railway, Render dashboards)
- [ ] Structured logging (JSON) dla lepszego parsowania

### 9. AWS Basics (awareness)
- [ ] **EC2** - Virtual machines
- [ ] **ECS** - Container orchestration
- [ ] **RDS** - Managed databases
- [ ] **S3** - Object storage
- [ ] **Lambda** - Serverless functions
- [ ] **VPC** - Networking

### 10. AWS ECS Fargate (opcjonalnie)
- [ ] Task Definition - jak uruchomić kontener
- [ ] Service - ile instancji, load balancing
- [ ] Fargate - serverless containers (brak zarządzania serwerami)

### 11. Infrastructure as Code (awareness)
- [ ] Terraform - provisioning cloud resources
- [ ] CloudFormation (AWS native)
- [ ] Korzyści: powtarzalność, wersjonowanie

### 12. Cost Management
- [ ] Free tiers - limity
- [ ] Railway: $5 credit/month
- [ ] Render: Free tier limits (spin down after inactivity)
- [ ] AWS: Pay-per-use (uważaj na koszty!)

---

## Powiązana teoria
- `docs/theory/08-testing-devops.md` → sekcja Deployment

---

## application-prod.properties
```properties
# Database from environment
spring.datasource.url=${DATABASE_URL}

# Port from environment (cloud assigns)
server.port=${PORT:8080}

# Production settings
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Actuator
management.endpoints.web.exposure.include=health,info
```

---

## Railway Deployment Steps
```
1. railway.app → New Project → Deploy from GitHub
2. Select repository
3. Railway auto-detects Spring Boot
4. Add PostgreSQL:
   - New → Database → PostgreSQL
   - Railway auto-connects via DATABASE_URL
5. Set environment variables:
   - SPRING_PROFILES_ACTIVE=prod
   - JWT_SECRET=your-secret
6. Deploy!
7. Get public URL
```

---

## Render Deployment Steps
```
1. render.com → New → Web Service
2. Connect GitHub repository
3. Configure:
   - Build: ./mvnw clean package -DskipTests
   - Start: java -jar target/*.jar
4. Add PostgreSQL:
   - New → PostgreSQL
   - Copy Internal Database URL
5. Environment variables:
   - DATABASE_URL=<internal url>
   - SPRING_PROFILES_ACTIVE=prod
6. Deploy!
```

---

## Ćwiczenia
1. Stwórz `application-prod.properties`
2. Deploy na Railway (lub Render)
3. Dodaj PostgreSQL jako managed service
4. Skonfiguruj environment variables
5. Przetestuj deployed API
6. Sprawdź logi w dashboard
7. (Opcjonalnie) Dodaj custom domain

---

## Sprawdzian gotowości
- [ ] Aplikacja działa na Railway/Render
- [ ] Baza danych jest w chmurze
- [ ] Environment variables są bezpiecznie przechowywane
- [ ] Health check działa
- [ ] Rozumiem podstawy AWS (EC2, RDS, ECS)
