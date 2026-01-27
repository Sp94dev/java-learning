# Ćwiczenie 08: OpenAPI / Swagger

## Zadanie

Dodaj automatycznie generowaną dokumentację API do projektu `wallet-manager`.

### 1. Dodaj zależność (pom.xml)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version> <!-- Sprawdź najnowszą wersję -->
</dependency>
```

### 2. Konfiguracja

Stwórz klasę `com.sp94dev.wallet.config.OpenApiConfig`:

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI walletApiDocs() {
        return new OpenAPI()
            .info(new Info()
                .title("Wallet Manager API")
                .version("v1")
                .description("API do zarządzania portfelem inwestycyjnym"));
    }
}
```

### 3. Opisz `InstrumentController`

Użyj adnotacji Swaggera, aby opisać endpointy.

```java
@Tag(name = "Instruments", description = "Zarządzanie instrumentami finansowymi")
public class InstrumentController {

    @Operation(summary = "Pobierz listę instrumentów", description = "Pozwala na filtrowanie po typie, walucie itp.")
    @GetMapping
    public List<Instrument> getAll(...) { ... }

    @Operation(summary = "Dodaj nowy instrument")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Utworzono instrument"),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane")
    })
    @PostMapping
    public ResponseEntity<Instrument> create(...) { ... }
}
```

### 4. Opisz Rekordy (Modele)

Dodaj opisy do pól w rekordzie `Instrument`.

```java
public record Instrument(
    @Schema(description = "Unikalne ID", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @Schema(description = "Symbol giełdowy", example = "AAPL")
    String ticker,
    
    // ...
) {}
```

## Weryfikacja

1. Uruchom aplikację: `./mvnw spring-boot:run`
2. Wejdź na: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
3. Przetestuj endpointy bezpośrednio z przeglądarki.