# Moduł 10: Testy

## Cel
Pewność że kod działa - unit testy, integration testy, Testcontainers.

---

## Tematy do opanowania

### 1. Test Pyramid
- [ ] **Unit tests** - dużo, szybkie, izolowane
- [ ] **Integration tests** - średnio, wolniejsze
- [ ] **E2E tests** - mało, najwolniejsze
- [ ] Im niżej w piramidzie, tym więcej testów

### 2. JUnit 5 Basics
- [ ] `@Test` - oznacza metodę testową
- [ ] `@BeforeEach` / `@AfterEach` - setup/cleanup per test
- [ ] `@BeforeAll` / `@AfterAll` - setup/cleanup per class
- [ ] `@DisplayName` - czytelna nazwa testu
- [ ] `@Disabled` - wyłączenie testu

### 3. AssertJ (rekomendowane)
- [ ] Fluent API: `assertThat(result).isEqualTo(expected)`
- [ ] `isNotNull()`, `isNull()`
- [ ] `isEqualTo()`, `isNotEqualTo()`
- [ ] `contains()`, `hasSize()`
- [ ] `isInstanceOf()`
- [ ] `assertThatThrownBy(() -> ...).isInstanceOf(...)`

### 4. Given-When-Then (AAA)
- [ ] **Given (Arrange):** przygotuj dane
- [ ] **When (Act):** wykonaj operację
- [ ] **Then (Assert):** zweryfikuj wynik

### 5. Mockito
- [ ] `@Mock` - tworzy mock
- [ ] `@InjectMocks` - wstrzykuje mocki
- [ ] `@ExtendWith(MockitoExtension.class)`
- [ ] `when(...).thenReturn(...)` - stubbing
- [ ] `verify(mock).method()` - weryfikacja wywołania
- [ ] `any()`, `eq()`, `argThat()` - matchers

### 6. @WebMvcTest (Controller Test)
- [ ] Testuje warstwę web (Controller)
- [ ] `@MockBean` - mockuje Service
- [ ] `MockMvc` - symuluje HTTP requests
- [ ] `perform(get("/api/..."))`, `perform(post(...))`
- [ ] `.andExpect(status().isOk())`
- [ ] `.andExpect(jsonPath("$.field").value(...))`

### 7. @DataJpaTest (Repository Test)
- [ ] Testuje warstwę danych
- [ ] Auto-rollback po każdym teście
- [ ] Embedded H2 lub Testcontainers

### 8. @SpringBootTest (Integration Test)
- [ ] Pełny context aplikacji
- [ ] Najwolniejszy, ale najbliżej produkcji
- [ ] `webEnvironment = WebEnvironment.RANDOM_PORT`
- [ ] `TestRestTemplate` lub `WebTestClient`

### 9. Testcontainers
- [ ] Prawdziwa baza w Docker dla testów
- [ ] `@Testcontainers`, `@Container`
- [ ] `PostgreSQLContainer`
- [ ] `@DynamicPropertySource` - konfiguracja

### 10. Test Naming
- [ ] `should_ExpectedBehavior_When_Condition`
- [ ] `methodName_scenario_expectedResult`
- [ ] Czytelne nazwy (nie `test1`, `test2`)

### 11. Test Coverage
- [ ] JaCoCo - narzędzie do coverage
- [ ] Line coverage, branch coverage
- [ ] Cel: 70-80% (nie 100% za wszelką cenę)
- [ ] Focus na krytyczną logikę biznesową

---

## Powiązana teoria
- `docs/theory/08-testing-devops.md` → sekcja Testing

---

## Przykład: Unit Test
```java
@ExtendWith(MockitoExtension.class)
class InstrumentServiceTest {
    
    @Mock
    private InstrumentRepository repository;
    
    @InjectMocks
    private InstrumentService service;
    
    @Test
    void shouldReturnInstrument_WhenExists() {
        // Given
        var entity = new InstrumentEntity(1L, "WIG20", "PLN", "WSE", "ETF");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        // When
        var result = service.findById(1L);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().ticker()).isEqualTo("WIG20");
        verify(repository).findById(1L);
    }
}
```

---

## Przykład: @WebMvcTest
```java
@WebMvcTest(InstrumentController.class)
class InstrumentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private InstrumentService service;
    
    @Test
    void shouldReturnAllInstruments() throws Exception {
        when(service.findAll()).thenReturn(List.of(
            new InstrumentResponse(1L, "WIG20", "PLN", "WSE", "ETF")
        ));
        
        mockMvc.perform(get("/api/instruments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].ticker").value("WIG20"));
    }
}
```

---

## Przykład: Testcontainers
```java
@SpringBootTest
@Testcontainers
class InstrumentIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Test
    void shouldSaveAndRetrieve() {
        // test with real PostgreSQL
    }
}
```

---

## Ćwiczenia
1. Napisz unit test dla InstrumentService (Mockito)
2. Napisz @WebMvcTest dla InstrumentController
3. Napisz @DataJpaTest dla InstrumentRepository
4. Skonfiguruj Testcontainers z PostgreSQL
5. Dodaj JaCoCo i sprawdź coverage

---

## Sprawdzian gotowości
- [ ] Piszę unit testy z JUnit 5 + AssertJ
- [ ] Używam Mockito do mockowania
- [ ] Testuję Controller z @WebMvcTest
- [ ] Testuję Repository z @DataJpaTest
- [ ] Używam Testcontainers dla integration testów
