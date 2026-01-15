# Lekcja 08: OpenAPI / Swagger

> Dokumentacja API, generowanie UI

## Po co?

- Automatyczna dokumentacja z kodu
- Interaktywne testowanie w przeglądarce
- Generowanie klientów (np. dla Angulara!)
- Kontrakt API dla frontendu

## Setup

**pom.xml:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.8</version>
</dependency>
```

**Gotowe!** Wejdź na:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Customizacja

### Opis API

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI walletManagerApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Wallet Manager API")
                .version("1.0.0")
                .description("REST API for managing wallets"));
    }
}
```

### Opis endpointów

```java
@RestController
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallets", description = "Wallet management")
public class WalletController {

    @Operation(summary = "Get all wallets", description = "Returns list of all wallets")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success")
    })
    @GetMapping
    public List<WalletResponse> getAll() { }

    @Operation(summary = "Get wallet by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getById(@PathVariable Long id) { }

    @Operation(summary = "Create new wallet")
    @ApiResponse(responseCode = "201", description = "Created")
    @PostMapping
    public ResponseEntity<WalletResponse> create(@RequestBody CreateWalletRequest req) { }
}
```

### Opis DTO

```java
@Schema(description = "Request to create new wallet")
public record CreateWalletRequest(
    @Schema(description = "Wallet name", example = "Main Account")
    String name,
    
    @Schema(description = "Currency code", example = "PLN", defaultValue = "PLN")
    String currency
) {}
```

## Generowanie klienta Angular

```bash
# Zainstaluj generator
npm install @openapitools/openapi-generator-cli -g

# Wygeneruj klienta TypeScript
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-angular \
  -o ./generated-client
```

Albo użyj: https://editor.swagger.io/ → Generate Client → typescript-angular

## application.properties

```properties
# Customowa ścieżka
springdoc.swagger-ui.path=/api-docs

# Sortowanie tagów
springdoc.swagger-ui.tagsSorter=alpha

# Grupowanie
springdoc.group-configs[0].group=v1
springdoc.group-configs[0].paths-to-match=/api/v1/**
```

## Ćwiczenie

**Zadanie:** Dodaj dokumentację do `NoteController`:
1. Dodaj zależność springdoc
2. Skonfiguruj OpenAPI info
3. Opisz każdy endpoint przez `@Operation`
4. Opisz DTOs przez `@Schema`
5. Przetestuj w Swagger UI

**Pliki:** `exercises/ex08-openapi/`

## Checklist

- [ ] Swagger UI działa pod `/swagger-ui.html`
- [ ] Wszystkie endpointy mają opisy
- [ ] Kody odpowiedzi są udokumentowane
- [ ] Potrafię wygenerować klienta dla Angulara
