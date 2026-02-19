# Kolejność adnotacji (Java / Spring / Lombok)

## Na klasie — od ogólnego do szczegółowego

```java
@Slf4j                      // 1. Lombok (tooling)
@RequiredArgsConstructor    // 1. Lombok
@RestController             // 2. Spring stereotype (CO to jest)
@RequestMapping("/api/...") // 3. Spring config (JAK się zachowuje)
@Tag(name = "...")          // 4. Metadata (dokumentacja)
```

| Pozycja | Typ               | Przykłady                                    |
| ------- | ----------------- | -------------------------------------------- |
| 1       | Lombok (tooling)  | `@Slf4j`, `@RequiredArgsConstructor`         |
| 2       | Spring stereotype | `@RestController`, `@Service`, `@Repository` |
| 3       | Spring config     | `@RequestMapping`, `@Conditional`            |
| 4       | Metadata          | `@Tag`, `@Profile`                           |

## Na metodzie

```java
@GetMapping("/{id}")                    // 1. HTTP mapping (CO robi)
@Operation(summary = "Get by ID")       // 2. Dokumentacja (meta)
@PreAuthorize("hasRole('ADMIN')")       // 3. Security (przyszłe moduły)
@Cacheable("instruments")              // 4. Inne cross-cutting
```

## Zasada

Spójność > konkretna kolejność. Wybierz jedną konwencję i trzymaj się jej w całym projekcie.
