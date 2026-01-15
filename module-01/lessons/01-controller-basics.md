# Lekcja 01: Controller Basics

> `@RestController`, routing, pierwszy endpoint

## Koncept

| Annotation | Rola |
|------------|------|
| `@Controller` | Zwraca widoki (HTML) - Spring MVC |
| `@RestController` | Zwraca dane (JSON) - REST API |
| `@RequestMapping` | Prefiks ścieżki dla całego kontrolera |
| `@GetMapping` | Mapuje metodę na GET request |

`@RestController` = `@Controller` + `@ResponseBody`

## Minimalny kontroler

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

## Ćwiczenie

**Zadanie:** Stwórz `HealthController` z endpointami:
- `GET /api/health` → zwraca `"OK"`
- `GET /api/health/info` → zwraca JSON z wersją aplikacji

**Pliki:** `exercises/ex01-health-controller/`

## Checklist

- [ ] Rozumiem różnicę między `@Controller` a `@RestController`
- [ ] Potrafię ustawić prefix ścieżki przez `@RequestMapping`
- [ ] Aplikacja startuje i endpoint odpowiada
