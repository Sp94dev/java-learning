# Moduł 09: Walidacja + Error Handling


## Cel

API zwraca czytelne błędy, waliduje dane wejściowe.

---

## Tematy do opanowania

### 1. Bean Validation (JSR-380)

- [ ] Dependency: `spring-boot-starter-validation`
- [ ] `@Valid` na parametrze w Controller
- [ ] Adnotacje walidacyjne na polach DTO

### 2. Adnotacje walidacyjne

- [ ] `@NotNull` - nie może być null
- [ ] `@NotBlank` - nie może być null/pusty/whitespace (String)
- [ ] `@NotEmpty` - nie może być null/pusty (String, Collection)
- [ ] `@Size(min, max)` - długość/rozmiar
- [ ] `@Min`, `@Max` - wartość liczbowa
- [ ] `@Email` - format email
- [ ] `@Pattern(regexp)` - regex
- [ ] `@Past`, `@Future` - data
- [ ] `@Positive`, `@Negative` - znak liczby

### 3. Custom Validation Message

- [ ] `@NotBlank(message = "Ticker is required")`
- [ ] Message templates
- [ ] Internationalization (i18n) - opcjonalnie

### 4. Custom Validator

- [ ] `@Constraint(validatedBy = ...)`
- [ ] Implementacja `ConstraintValidator<A, T>`
- [ ] Kiedy custom validator (business rules)

### 5. Exception Handling

- [ ] `@ControllerAdvice` / `@RestControllerAdvice`
- [ ] `@ExceptionHandler(ExceptionType.class)`
- [ ] Centralne miejsce obsługi błędów

### 6. Standardowy format błędów

- [ ] Spójny JSON dla wszystkich błędów
- [ ] Pola: code, message, details, timestamp
- [ ] HTTP status codes (400, 404, 409, 500)

### 7. Custom Exceptions

- [ ] `ResourceNotFoundException extends RuntimeException`
- [ ] `DuplicateResourceException`
- [ ] `BusinessException`
- [ ] Kiedy checked vs unchecked

### 8. Problem Details (RFC 7807)

- [ ] Standardowy format błędów HTTP
- [ ] `ProblemDetail` class w Spring 6+
- [ ] Pola: type, title, status, detail, instance

### 9. Validation Groups

- [ ] Różna walidacja dla Create vs Update
- [ ] `groups = {Create.class}` w adnotacji
- [ ] `@Validated(Create.class)` w Controller

---

## Powiązana teoria

- `docs/theory/01-java-fundamentals.md` → Exceptions

---

## Przykład: DTO z walidacją

```java
public record CreateInstrumentRequest(
    @NotBlank(message = "Ticker is required")
    @Size(min = 1, max = 20, message = "Ticker must be 1-20 chars")
    String ticker,

    @NotNull(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
    String currency,

    @Size(max = 10)
    String market
) {}
```

---

## Przykład: GlobalExceptionHandler

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();

        return ResponseEntity.badRequest()
            .body(new ErrorResponse("VALIDATION_ERROR", "Validation failed", errors));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage(), null));
    }
}
```

---

## Przykład: ErrorResponse

```java
public record ErrorResponse(
    String code,
    String message,
    List<String> details,
    Instant timestamp
) {
    public ErrorResponse(String code, String message, List<String> details) {
        this(code, message, details, Instant.now());
    }
}
```

---

## Ćwiczenia

1. Dodaj walidację do CreateInstrumentRequest
2. Użyj `@Valid` w Controller
3. Stwórz GlobalExceptionHandler
4. Obsłuż MethodArgumentNotValidException
5. Stwórz custom exceptions (NotFound, Duplicate)
6. Zwracaj spójny ErrorResponse dla wszystkich błędów

---

## Sprawdzian gotowości

- [ ] Używam @Valid z Bean Validation
- [ ] Znam podstawowe adnotacje (@NotBlank, @Size, @Pattern)
- [ ] Mam GlobalExceptionHandler (@ControllerAdvice)
- [ ] Zwracam spójny format błędów (ErrorResponse)
- [ ] Loguję nieoczekiwane błędy (500)
