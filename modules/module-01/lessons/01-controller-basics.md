# Lesson 01: Controller Basics

> `@RestController`, routing, first endpoint

## Concept

| Annotation | Role |
|------------|------|
| `@Controller` | Returns views (HTML) - Spring MVC |
| `@RestController` | Returns data (JSON) - REST API |
| `@RequestMapping` | Path prefix for the entire controller |
| `@GetMapping` | Maps a method to a GET request |

`@RestController` = `@Controller` + `@ResponseBody`

## Minimal Controller

```java
@RestController
@RequestMapping("/api/v1/hello")
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
```

## Exercise

**Task:** Create a `HealthController` with the following endpoints:
- `GET /api/health` → returns `"OK"`
- `GET /api/health/info` → returns a JSON with the application version

**Files:** `exercises/ex01-health-controller/`

## Checklist

- [ ] I understand the difference between `@Controller` and `@RestController`
- [ ] I can set a path prefix via `@RequestMapping`
- [ ] The application starts and the endpoint responds