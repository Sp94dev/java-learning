# Ćwiczenie 01: Health Controller

## Zadanie

Stwórz `HealthController` z endpointami:

1. `GET /api/health` → zwraca `"OK"`
2. `GET /api/health/info` → zwraca JSON:
   ```json
   {
     "status": "UP",
     "version": "1.0.0",
     "java": "25"
   }
   ```

## Hints

- Użyj `@RestController`
- Użyj `@RequestMapping("/api/health")` na klasie
- Dla JSON możesz zwrócić `Map<String, String>` lub Record

## Jak uruchomić

```bash
cd src
mvn spring-boot:run
```

Test:
```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/api/health/info
```

## Pliki do stworzenia

```
src/
└── main/java/com/example/ex01/
    ├── Ex01Application.java
    └── HealthController.java
```
