# Lesson 08: OpenAPI / Swagger

> API Documentation, UI generation

## Why?

- Automatic documentation from the code
- Interactive testing in the browser
- Client generation (e.g., for Angular!)
- API contract for the frontend

## Setup

**pom.xml:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

**Ready!** Go to:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Customization

### API Description

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

### Endpoint Description

```java
@RestController
@RequestMapping("/api/v1/wallets")
@Tag(name = "Wallets", description = "Wallet management")
public class WalletController {

    @Operation(summary = "Get all wallets", description = "Returns a list of all wallets")
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

    @Operation(summary = "Create a new wallet")
    @ApiResponse(responseCode = "201", description = "Created")
    @PostMapping
    public ResponseEntity<WalletResponse> create(@RequestBody CreateWalletRequest req) { }
}
```

### DTO Description

```java
@Schema(description = "Request to create a new wallet")
public record CreateWalletRequest(
    @Schema(description = "Wallet name", example = "Main Account")
    String name,
    
    @Schema(description = "Currency code", example = "PLN", defaultValue = "PLN")
    String currency
) {}
```

## Generating an Angular Client

```bash
# Install the generator
npm install @openapitools/openapi-generator-cli -g

# Generate the TypeScript client
openapi-generator-cli generate \
  -i http://localhost:8080/v3/api-docs \
  -g typescript-angular \
  -o ./generated-client
```

Or use: https://editor.swagger.io/ → Generate Client → typescript-angular

## application.properties

```properties
# Custom path
springdoc.swagger-ui.path=/api-docs

# Tag sorting
springdoc.swagger-ui.tagsSorter=alpha

# Grouping
springdoc.group-configs[0].group=v1
springdoc.group-configs[0].paths-to-match=/api/v1/**
```

## Exercise

**Task:** Add documentation to the `NoteController`:
1. Add the springdoc dependency
2. Configure OpenAPI info
3. Describe each endpoint using `@Operation`
4. Describe DTOs using `@Schema`
5. Test it in the Swagger UI

**Files:** `exercises/ex08-openapi/`

## Checklist

- [ ] Swagger UI is working at `/swagger-ui.html`
- [ ] All endpoints have descriptions
- [ ] Response codes are documented
- [ ] I can generate a client for Angular