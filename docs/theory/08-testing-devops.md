# TEORIA: Testing & DevOps

---

## CZĘŚĆ 1: TESTING

---

## 1. Po co testować?

### Korzyści
1. **Pewność** - wiesz że kod działa
2. **Refactoring** - możesz zmieniać bez strachu
3. **Dokumentacja** - testy pokazują jak używać kodu
4. **Design feedback** - trudny test = zły design
5. **Regresja** - łapiesz błędy wprowadzone zmianami

### Test Pyramid

```
        /\
       /  \
      / E2E \           Mało, wolne, drogie
     /______\           Testują cały system
    /        \
   / Integration\       Średnio
  /______________\      Testują współpracę komponentów
 /                \
/    Unit Tests    \    Dużo, szybkie, tanie
/____________________\  Testują pojedyncze jednostki
```

**Zasada:** Im niżej w piramidzie, tym więcej testów.

---

## 2. Unit Tests

### Charakterystyka
- Testują **pojedynczą jednostkę** (klasa, metoda)
- **Izolowane** - mockują zależności
- **Szybkie** - milisekundy
- **Deterministyczne** - zawsze ten sam wynik

### Co testować?
- Logikę biznesową
- Edge cases
- Warunki błędów
- Boundary values

### Struktura: Given-When-Then (AAA)

```
GIVEN (Arrange)  - Przygotuj dane i mocki
WHEN (Act)       - Wykonaj testowaną operację
THEN (Assert)    - Zweryfikuj wynik
```

### Test Doubles

| Typ | Opis | Użycie |
|-----|------|--------|
| **Dummy** | Obiekt przekazywany ale nieużywany | Wypełnienie parametrów |
| **Stub** | Zwraca zaprogramowane odpowiedzi | Kontrola indirect inputs |
| **Spy** | Stub + nagrywa wywołania | Weryfikacja wywołań |
| **Mock** | Stub + expectations + weryfikacja | Interakcje z zależnościami |
| **Fake** | Działająca implementacja (uproszczona) | In-memory database |

### Co NIE testować jednostkowo?
- Gettery/settery (trivial)
- Konstruktory (trivial)
- Third-party code
- Private methods (przez publiczne API)

---

## 3. Integration Tests

### Charakterystyka
- Testują **współpracę komponentów**
- Mogą używać prawdziwej bazy (Testcontainers)
- Wolniejsze niż unit testy
- Testują "plumbing" - czy wszystko połączone

### Rodzaje w Spring

**@WebMvcTest**
- Testuje warstwę web (Controller)
- Mockuje Service
- Testuje routing, serialization, validation

**@DataJpaTest**
- Testuje warstwę danych (Repository)
- Używa embedded H2 lub Testcontainers
- Auto-rollback po każdym teście

**@SpringBootTest**
- Pełny context aplikacji
- Najwolniejszy, ale najbliższy produkcji
- Używaj z umiarem

---

## 4. Test Design Principles

### FIRST

**F - Fast:** Szybkie (sekundy, nie minuty)
**I - Isolated:** Niezależne od innych testów
**R - Repeatable:** Ten sam wynik zawsze
**S - Self-validating:** Pass/Fail bez analizy
**T - Timely:** Pisane razem z kodem

### One Assert Per Test (guideline)

Jeden test weryfikuje jedną rzecz. Łatwiej znaleźć problem.

**Wyjątek:** Kilka asserts weryfikujących TEN SAM aspekt.

### Test Naming

```
// Wzorzec: should_ExpectedBehavior_When_Condition
shouldReturnEmpty_WhenUserNotFound
shouldThrowException_WhenInvalidInput
shouldCalculateTax_ForPremiumUser

// Lub: methodName_scenario_expectedResult
createUser_validInput_returnsCreatedUser
createUser_duplicateEmail_throwsException
```

### Test Independence

Testy nie mogą zależeć od:
- Kolejności wykonania
- Stanu z poprzednich testów
- Stanu globalnego

**Każdy test:** setup → execute → verify → cleanup

---

## 5. TDD (Test-Driven Development)

### Cykl Red-Green-Refactor

```
    ┌─────────────────────────────────────────┐
    │                                         │
    ▼                                         │
┌────────┐      ┌────────┐      ┌────────┐   │
│  RED   │─────►│ GREEN  │─────►│REFACTOR│───┘
│        │      │        │      │        │
│ Write  │      │ Make   │      │ Clean  │
│failing │      │ it     │      │ up     │
│ test   │      │ pass   │      │ code   │
└────────┘      └────────┘      └────────┘
```

1. **RED:** Napisz test, który nie przechodzi
2. **GREEN:** Napisz minimalny kod, żeby przeszedł
3. **REFACTOR:** Popraw kod, testy nadal przechodzą
4. **Powtórz**

### Zalety TDD
- Design emerges from tests
- 100% coverage by definition
- Dokumentacja w testach
- Małe kroki = mniej błędów

### Kiedy TDD?
- Złożona logika biznesowa
- Ważny kod wymagający pewności
- Gdy design nie jest jasny

### Kiedy nie TDD?
- Prototyp / spike
- UI code
- Kod integracyjny (lepiej integration tests)

---

## 6. Code Coverage

### Metryki

**Line coverage:** % linii kodu wykonanych przez testy
**Branch coverage:** % gałęzi if/else wykonanych
**Method coverage:** % metod wywołanych

### Interpretacja

```
80% coverage ≠ 80% pewność że działa

Coverage mówi: "ten kod był WYKONANY"
Nie mówi: "ten kod jest POPRAWNY"
```

### Sensowne cele
- 70-80% - dobry balans
- 100% - często nie warte wysiłku
- < 50% - prawdopodobnie za mało

**Focus na:** krytyczna logika biznesowa, nie gettery/settery

---

## CZĘŚĆ 2: DEVOPS

---

## 7. CI/CD

### Continuous Integration (CI)

**Idea:** Częste integrowanie zmian + automatyczna weryfikacja.

```
Developer → Push → CI Server → Build → Test → Report
                       ↓
              Fail fast, fix fast
```

**Praktyki:**
1. Commit często (minimum raz dziennie)
2. Build musi być szybki (< 10 min)
3. Fix broken build natychmiast
4. Testy muszą przechodzić przed merge

### Continuous Delivery (CD)

**Idea:** Każdy commit może być deployed na produkcję.

```
CI → Staging Deploy → Integration Tests → Manual Approval → Production
```

### Continuous Deployment

**Idea:** Każdy commit automatycznie na produkcję (bez manual approval).

```
CI → Staging → Tests → Production (automatic)
```

**Wymaga:** Bardzo dobre testy, feature flags, monitoring.

### Pipeline Stages

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  Build  │───►│  Test   │───►│  Scan   │───►│ Deploy  │───►│ Verify  │
│         │    │         │    │         │    │ Staging │    │         │
│ Compile │    │ Unit    │    │ SAST    │    │         │    │ Smoke   │
│ Package │    │ Integ   │    │ Deps    │    │         │    │ tests   │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
                                                   │
                                                   ▼
                                            ┌─────────┐
                                            │ Deploy  │
                                            │  Prod   │
                                            └─────────┘
```

---

## 8. Containers & Docker

### Czym jest kontener?

**Izolowany proces** z własnym filesystem, networking, ale współdzielący kernel z hostem.

```
┌─────────────────────────────────────────────────────┐
│                    HOST OS                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Container 1 │  │ Container 2 │  │ Container 3 │ │
│  │ App A       │  │ App B       │  │ App C       │ │
│  │ Libs        │  │ Libs        │  │ Libs        │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
│                                                     │
│                 Docker Engine                       │
│                                                     │
│                 Linux Kernel                        │
└─────────────────────────────────────────────────────┘
```

### Container vs VM

| Container | VM |
|-----------|-----|
| Współdzieli kernel | Własny kernel |
| MB | GB |
| Sekundy start | Minuty start |
| Mniejsza izolacja | Pełna izolacja |

### Docker Image vs Container

**Image:** Niezmienne "template" - readonly filesystem
**Container:** Uruchomiona instancja image

**Analogia:** Image = klasa, Container = obiekt

### Image Layers

```
┌────────────────────────────┐
│ Layer 5: COPY app.jar     │  ← Twoja aplikacja
├────────────────────────────┤
│ Layer 4: RUN apt install  │  ← Zależności
├────────────────────────────┤
│ Layer 3: ENV JAVA_HOME    │  ← Konfiguracja
├────────────────────────────┤
│ Layer 2: JDK 25           │  ← Runtime
├────────────────────────────┤
│ Layer 1: Ubuntu base      │  ← Base OS
└────────────────────────────┘
```

**Cachowanie:** Zmiana w Layer 5 nie wymaga przebudowy Layer 1-4.

### Multi-stage Build

```dockerfile
# Stage 1: Build (duży obraz z JDK, Maven)
FROM eclipse-temurin:25-jdk AS builder
COPY . .
RUN ./mvnw package

# Stage 2: Run (mały obraz tylko z JRE)
FROM eclipse-temurin:25-jre
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Efekt:** Mniejszy finalny obraz (tylko runtime, bez build tools).

---

## 9. Container Orchestration

### Po co orchestration?

Jeden kontener = proste.
100 kontenerów = potrzebujesz:
- Scheduling (gdzie uruchomić?)
- Scaling (ile instancji?)
- Load balancing
- Health checks & restart
- Service discovery
- Rolling updates

### Kubernetes (K8s)

**De facto standard** dla container orchestration.

```
┌─────────────────────────────────────────────────────────────────┐
│                         Kubernetes Cluster                      │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │                    Control Plane                         │   │
│  │  API Server │ Scheduler │ Controller Manager │ etcd     │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐            │
│  │   Node 1    │  │   Node 2    │  │   Node 3    │            │
│  │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │            │
│  │ │   Pod   │ │  │ │   Pod   │ │  │ │   Pod   │ │            │
│  │ │Container│ │  │ │Container│ │  │ │Container│ │            │
│  │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │            │
│  │   kubelet   │  │   kubelet   │  │   kubelet   │            │
│  └─────────────┘  └─────────────┘  └─────────────┘            │
└─────────────────────────────────────────────────────────────────┘
```

**Główne koncepty:**
- **Pod:** Najmniejsza deployable unit (1+ containers)
- **Deployment:** Deklaratywny sposób zarządzania Pods
- **Service:** Stable network endpoint dla Pods
- **Ingress:** HTTP routing z zewnątrz

### Kiedy Kubernetes?

**TAK:**
- Wiele microservices
- Potrzeba auto-scaling
- Zespół ma doświadczenie
- Cloud provider oferuje managed K8s

**NIE (overkill):**
- Mała aplikacja
- Monolith
- Brak doświadczenia (start z Docker Compose)

---

## 10. Infrastructure as Code (IaC)

### Idea

Infrastruktura definiowana w plikach tekstowych, wersjonowana w Git.

**Tradycyjnie:** "Kliknij tutaj, wpisz wartość X, zatwierdź"
**IaC:** "Deklaruj co chcesz, narzędzie utworzy"

### Narzędzia

| Narzędzie | Zakres |
|-----------|--------|
| **Terraform** | Cloud resources (AWS, GCP, Azure) |
| **Ansible** | Configuration management |
| **Docker Compose** | Multi-container apps |
| **Kubernetes YAML** | Container orchestration |

### Zalety IaC
1. **Powtarzalność** - ta sama infra w dev/staging/prod
2. **Wersjonowanie** - historia zmian, code review
3. **Automatyzacja** - CI/CD dla infrastruktury
4. **Dokumentacja** - kod = dokumentacja

---

## 11. Observability

### Three Pillars

**Logs:**
- Co się wydarzyło (timestamped events)
- Debugging, audit
- Tools: ELK Stack, Loki

**Metrics:**
- Liczby agregowane w czasie
- Alerting, dashboards
- Tools: Prometheus, Grafana

**Traces:**
- Ścieżka request przez system
- Distributed systems debugging
- Tools: Jaeger, Zipkin

### SRE Concepts

**SLI (Service Level Indicator):**
Metryka (np. latency, error rate, availability)

**SLO (Service Level Objective):**
Cel dla SLI (np. 99.9% availability)

**SLA (Service Level Agreement):**
Kontrakt z klientem (np. refund if SLO missed)

**Error Budget:**
Dozwolona ilość błędów (100% - SLO). Gdy wyczerpany → stop features, fix reliability.

---

## 12. Deployment Strategies

### Big Bang
Wszystko naraz. Proste, ale ryzykowne.

### Rolling Update
Stopniowa wymiana instancji.

```
v1 v1 v1 v1    →    v1 v1 v1 v2    →    v1 v1 v2 v2    →    v2 v2 v2 v2
```

### Blue-Green
Dwa identyczne środowiska. Przełączanie ruchu.

```
      ┌─────────┐                ┌─────────┐
      │  Blue   │   switch       │  Green  │
      │  (v1)   │  ─────────►    │  (v2)   │
      │ ACTIVE  │                │ STANDBY │
      └─────────┘                └─────────┘
```

### Canary
Mały % ruchu do nowej wersji, stopniowe zwiększanie.

```
v1: 100%  →  v1: 90%, v2: 10%  →  v1: 50%, v2: 50%  →  v2: 100%
```

### Feature Flags
Nowy kod na produkcji, ale wyłączony. Włączasz dla wybranych users.

---

## Podsumowanie

| Koncept | Znaczenie |
|---------|-----------|
| **Unit Test** | Izolowany test pojedynczej jednostki |
| **Integration Test** | Test współpracy komponentów |
| **TDD** | Red-Green-Refactor |
| **CI** | Automatyczna weryfikacja po każdym commit |
| **CD** | Automatyczny deployment |
| **Container** | Izolowane środowisko dla aplikacji |
| **K8s** | Orchestration kontenerów |
| **IaC** | Infrastruktura jako kod |
| **Observability** | Logs + Metrics + Traces |
