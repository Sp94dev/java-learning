# Ćwiczenie 08: OpenAPI / Swagger

## Zadanie

Dodaj pełną dokumentację API do aplikacji z notatkami.

### 1. Dodaj zależność

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.8</version>
</dependency>
```

### 2. Konfiguracja OpenAPI

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI noteApiDocs() {
        return new OpenAPI()
            .info(new Info()
                .title("Notes API")
                .version("1.0.0")
                .description("REST API for managing notes - Module 01 exercise"));
    }
}
```

### 3. Opisz kontroler

```java
@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "Note management endpoints")
public class NoteController {

    @Operation(
        summary = "Get all notes",
        description = "Returns all notes, optionally filtered by author or search term"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of notes")
    })
    @GetMapping
    public List<NoteResponse> getAll(
        @Parameter(description = "Filter by author name")
        @RequestParam(required = false) String author,
        
        @Parameter(description = "Search in title and content")
        @RequestParam(required = false) String search
    ) { }

    @Operation(summary = "Get note by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Note found"),
        @ApiResponse(responseCode = "404", description = "Note not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getById(
        @Parameter(description = "Note ID", required = true)
        @PathVariable Long id
    ) { }

    @Operation(summary = "Create new note")
    @ApiResponse(responseCode = "201", description = "Note created")
    @PostMapping
    public ResponseEntity<NoteResponse> create(
        @RequestBody CreateNoteRequest request
    ) { }
}
```

### 4. Opisz DTOs

```java
@Schema(description = "Request to create a new note")
public record CreateNoteRequest(
    @Schema(description = "Note title", example = "Meeting Notes", requiredMode = REQUIRED)
    String title,
    
    @Schema(description = "Note content", example = "Discussion about Q1 goals")
    String content,
    
    @Schema(description = "Author name", example = "John Doe")
    String author
) {}

@Schema(description = "Note response")
public record NoteResponse(
    @Schema(description = "Unique identifier", example = "1")
    Long id,
    
    @Schema(description = "Note title", example = "Meeting Notes")
    String title,
    
    @Schema(description = "Note content")
    String content,
    
    @Schema(description = "Author name")
    String author,
    
    @Schema(description = "Creation timestamp")
    LocalDateTime createdAt
) {}
```

### 5. (Opcjonalnie) application.properties

```properties
# Custom path
springdoc.swagger-ui.path=/docs

# Show operation IDs
springdoc.swagger-ui.displayOperationId=true

# Sort tags alphabetically
springdoc.swagger-ui.tagsSorter=alpha
```

## Test

1. Uruchom aplikację
2. Otwórz: http://localhost:8080/swagger-ui.html
3. Sprawdź czy:
   - Wszystkie endpointy są widoczne
   - Opisy są czytelne
   - Możesz wykonać request z UI
   - Examples są sensowne

## Bonus: Generowanie klienta Angular

```bash
# Pobierz spec
curl http://localhost:8080/v3/api-docs -o api-spec.json

# Lub użyj online: https://editor.swagger.io/
# Import → URL → http://localhost:8080/v3/api-docs
# Generate Client → typescript-angular
```
