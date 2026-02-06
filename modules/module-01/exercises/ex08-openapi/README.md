# Exercise 08: OpenAPI / Swagger (Wallet Manager Edition)

## Task

Add auto-generated API documentation to the `wallet-manager` project. The documentation must reflect the architecture using `ResponseEntity` and DTOs.

### 1. Add Dependency (pom.xml)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

### 2. Configuration (Programmatic)

Create the `com.sp94dev.wallet.config.OpenApiConfig` class. Pay attention to the imports – we use the `.models` package, not `.annotations`, for the `@Bean` method.

```java
package com.sp94dev.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI walletApiDocs() {
        return new OpenAPI()
            .info(new Info()
                .title("Wallet Manager API")
                .version("v1")
                .description("Investment portfolio management API"));
    }
}
```

### 3. Describe `InstrumentController`

Use Swagger annotations to describe endpoints returning `ResponseEntity`.

```java
@Tag(name = "Instruments", description = "Financial instruments management")
public class InstrumentController {

    @Operation(summary = "Get instrument list", description = "Filtering by Query parameters")
    @GetMapping
    public ResponseEntity<List<InstrumentResponse>> getAll(...) { 
        // ... 
    }

    @Operation(summary = "Add new instrument")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Instrument created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<InstrumentResponse> createInstrument(@RequestBody Instrument instrumentBody) {
        // ...
    }
}
```

### 4. Describe Records (Models/DTOs)

Add metadata to record fields so Swagger generates nice examples (analogy to documenting interfaces in TS).

```java
public record InstrumentResponse(
    @Schema(description = "Unique identifier", example = "1")
    Long id,
    
    @Schema(description = "Ticker symbol", example = "AAPL")
    String ticker,
    
    @Schema(description = "Instrument currency", example = "USD")
    String currency,
    
    @Schema(description = "Exchange market", example = "NASDAQ")
    String market,
    
    @Schema(description = "Instrument type", example = "STOCK")
    String type
) { ... }
```

## Verification

1. Run the application: `./mvnw spring-boot:run`
2. Go to: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
3. Check if the return types (`InstrumentResponse`) are correctly visible in the **Schemas** section.