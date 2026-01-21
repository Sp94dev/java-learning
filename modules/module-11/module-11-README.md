# Moduł 11: Debugowanie & Profiling

## Cel
Znajdować i rozwiązywać problemy w aplikacji.

---

## Tematy do opanowania

### 1. Debugger (IDE)
- [ ] Breakpoints - zatrzymanie na linii
- [ ] Conditional breakpoints - zatrzymanie gdy warunek spełniony
- [ ] Exception breakpoints - zatrzymanie przy wyjątku
- [ ] Step Over (F8) - następna linia
- [ ] Step Into (F7) - wejdź do metody
- [ ] Step Out - wyjdź z metody
- [ ] Evaluate Expression - wykonaj kod w runtime

### 2. Remote Debugging
- [ ] Uruchomienie z opcjami debug: `-agentlib:jdwp=...`
- [ ] Podłączenie IDE do remote JVM
- [ ] Port debug (5005)

### 3. Logging
- [ ] SLF4J - facade
- [ ] Logback - implementacja (default w Spring Boot)
- [ ] Log levels: TRACE < DEBUG < INFO < WARN < ERROR
- [ ] Kiedy który level używać
- [ ] Logger per klasa: `@Slf4j` (Lombok)

### 4. Konfiguracja logowania
- [ ] `logging.level.root=INFO`
- [ ] `logging.level.com.example=DEBUG`
- [ ] `logging.level.org.hibernate.SQL=DEBUG` (show SQL)
- [ ] Pattern logów
- [ ] Logowanie do pliku

### 5. Structured Logging
- [ ] JSON format (dla ELK, CloudWatch)
- [ ] Logstash encoder
- [ ] Correlation ID (request tracking)

### 6. Common Exceptions - rozpoznawanie
- [ ] `NullPointerException` - null reference
- [ ] `LazyInitializationException` - dostęp do lazy poza sesją
- [ ] `DataIntegrityViolationException` - constraint violation
- [ ] `HttpMessageNotReadableException` - błędny JSON
- [ ] `MethodArgumentNotValidException` - validation failed

### 7. Stack Trace Reading
- [ ] Czytanie od góry (gdzie błąd)
- [ ] "Caused by" - root cause
- [ ] Filtrowanie frameworkowych klas
- [ ] Znajdowanie swojego kodu

### 8. Profiling
- [ ] JVisualVM / JConsole - monitoring JVM
- [ ] CPU profiling - co zużywa CPU
- [ ] Memory profiling - co zużywa pamięć
- [ ] Flame graphs

### 9. Heap Dump & Thread Dump
- [ ] Heap dump - snapshot pamięci
- [ ] `jmap -dump:format=b,file=heap.hprof <pid>`
- [ ] Thread dump - snapshot wątków
- [ ] `kill -3 <pid>` lub jstack
- [ ] Analiza w VisualVM / Eclipse MAT

### 10. Spring Boot Actuator
- [ ] Dependency: `spring-boot-starter-actuator`
- [ ] `/actuator/health` - status aplikacji
- [ ] `/actuator/metrics` - metryki
- [ ] `/actuator/loggers` - zmiana log level w runtime
- [ ] `/actuator/env` - environment
- [ ] Security dla actuator endpoints

### 11. Troubleshooting Checklist
- [ ] 1. Sprawdź logi (gdzie błąd, stack trace)
- [ ] 2. Reprodukuj lokalnie
- [ ] 3. Izoluj problem (który komponent)
- [ ] 4. Debug (breakpoint przed błędem)
- [ ] 5. Szukaj wzorców (zawsze ten sam input?)

---

## Powiązana teoria
- `docs/theory/08-testing-devops.md` → sekcja Observability

---

## Przykład: Logging
```java
@Slf4j
@Service
public class InstrumentService {
    
    public InstrumentResponse create(CreateRequest request) {
        log.info("Creating instrument: {}", request.ticker());
        log.debug("Full request: {}", request);
        
        try {
            var result = repository.save(toEntity(request));
            log.info("Created with id: {}", result.getId());
            return toResponse(result);
        } catch (Exception e) {
            log.error("Failed to create: {}", request.ticker(), e);
            throw e;
        }
    }
}
```

---

## application.properties (Logging)
```properties
# Levels
logging.level.root=INFO
logging.level.com.sp94dev.wallet=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# File output
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## Actuator Config
```properties
management.endpoints.web.exposure.include=health,info,metrics,loggers
management.endpoint.health.show-details=always
```

---

## Ćwiczenia
1. Ustaw breakpoint i debug prosty flow
2. Skonfiguruj logi na poziomie DEBUG dla swojego pakietu
3. Dodaj Actuator i sprawdź /health, /metrics
4. Zmień log level przez /actuator/loggers (runtime)
5. Wygeneruj heap dump i przeanalizuj w VisualVM
6. Dodaj structured logging (JSON)

---

## Sprawdzian gotowości
- [ ] Umiem używać debuggera (breakpoints, step, evaluate)
- [ ] Loguję na odpowiednich poziomach
- [ ] Potrafię czytać stack trace
- [ ] Rozpoznaję popularne exceptions
- [ ] Używam Actuator do diagnostyki
