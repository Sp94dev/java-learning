# Moduł 03: Dependency Injection + Warstwy

## Cel
Zrozumieć Dependency Injection, IoC Container i wydzielić warstwy aplikacji.

---

## Tematy do opanowania

### 1. Problem bez DI
- [ ] Tight coupling - klasa tworzy swoje zależności
- [ ] Trudne testowanie - nie można podmienić na mock
- [ ] Ukryte zależności - nie wiadomo czego klasa potrzebuje

### 2. Dependency Injection - koncept
- [ ] Co to jest DI (zależności przekazywane z zewnątrz)
- [ ] Inversion of Control (IoC) - odwrócenie kontroli
- [ ] Kontener zarządza tworzeniem obiektów

### 3. Typy Injection
- [ ] **Constructor Injection** ✅ (rekomendowane)
  - [ ] Wymuszone zależności (final)
  - [ ] Immutable
  - [ ] Łatwe testowanie
- [ ] **Setter Injection** - opcjonalne zależności
- [ ] **Field Injection** ❌ (unikaj)
  - [ ] Ukryte zależności
  - [ ] Trudne testowanie

### 4. Spring IoC Container
- [ ] ApplicationContext - co to jest
- [ ] Bean - obiekt zarządzany przez Spring
- [ ] Component Scanning - jak Spring znajduje beany
- [ ] `@SpringBootApplication` włącza scanning

### 5. Stereotypy (adnotacje)
- [ ] `@Component` - generic bean
- [ ] `@Service` - warstwa biznesowa
- [ ] `@Repository` - warstwa danych
- [ ] `@Controller` / `@RestController` - warstwa web
- [ ] `@Configuration` - konfiguracja
- [ ] Różnica semantyczna (technicznie identyczne)

### 6. Bean Scopes
- [ ] **Singleton** (default) - jedna instancja
- [ ] **Prototype** - nowa instancja przy każdym żądaniu
- [ ] **Request** - per HTTP request
- [ ] **Session** - per HTTP session

### 7. Architektura warstwowa
- [ ] Controller → Service → Repository
- [ ] Zasada: zależności tylko W DÓŁ
- [ ] Controller: routing, walidacja wejścia
- [ ] Service: logika biznesowa
- [ ] Repository: dostęp do danych

### 8. Cienki Controller
- [ ] Controller nie ma logiki biznesowej
- [ ] Deleguje do Service
- [ ] Mapuje Request ↔ Response

### 9. Lombok
- [ ] `@RequiredArgsConstructor` - generuje konstruktor
- [ ] `@Data` - gettery, settery, equals, hashCode
- [ ] `@Builder` - builder pattern
- [ ] `@Slf4j` - logger

---

## Powiązana teoria
- `docs/theory/04-spring-framework.md` → DI, IoC Container, Bean Lifecycle

---

## Struktura kodu
```
com.example.wallet/
├── instrument/
│   ├── InstrumentController.java   ← @RestController
│   ├── InstrumentService.java      ← @Service
│   └── InstrumentRepository.java   ← @Repository (interface)
└── WalletApplication.java
```

---

## Ćwiczenia
1. Wydziel Service z Controller (ex05)
2. Stwórz Repository (in-memory) i wstrzyknij do Service
3. Napisz test Service z mockiem Repository

---

## Sprawdzian gotowości
- [ ] Rozumiem co to Dependency Injection i po co
- [ ] Używam Constructor Injection (nie Field Injection)
- [ ] Wiem różnicę między @Service, @Repository, @Component
- [ ] Potrafię wydzielić Controller → Service → Repository
- [ ] Controller jest "cienki" - tylko deleguje
