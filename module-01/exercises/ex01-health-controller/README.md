# Exercise 01: Health Controller

## Status: ✅ COMPLETED

## Goal

Create a simple REST controller that exposes health check endpoints - a common pattern in microservices.

## Task

Create `HealthController` with endpoints:

1. `GET /api/health` → returns `"OK"`
2. `GET /api/health/info` → returns JSON:
   ```json
   {
     "appVersion": "1.0.0",
     "appStatus": "UP",
     "javaVersion": "25"
   }
   ```

## Hints

- Use `@RestController` annotation on the class
- Use `@RequestMapping("/api/health")` to set base path
- Use `@GetMapping` for individual endpoints
- For JSON response, return a Record or `Map<String, String>`
- Use `@Value("${property:default}")` to inject config values
- Use `System.getProperty("java.version")` for Java version

## How to Run

```bash
./mvnw spring-boot:run
```

Test:
```bash
curl http://localhost:8080/api/health
curl http://localhost:8080/api/health/info
```

## Project Structure

```
src/main/java/com/example/ex01/
├── HelloSpringApplication.java   # Main application class
├── HealthController.java         # REST controller
└── HealthResponse.java           # Record for JSON response
```

## Key Concepts Learned

- **@RestController** - combines @Controller + @ResponseBody, returns data directly (not view names)
- **@RequestMapping** - maps HTTP requests to handler methods, can set base path on class level
- **@GetMapping** - shortcut for @RequestMapping(method = GET)
- **@Value** - injects values from application.properties (with optional default after colon)
- **Java Records** - immutable data classes, automatically serialized to JSON by Spring
