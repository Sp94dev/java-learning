# Lekcja 03: Typy Injection

> Constructor ✅, Setter ⚠️, Field ❌ — dlaczego Constructor Injection wygrywa.

## Koncept

### Trzy sposoby wstrzykiwania zależności

Spring oferuje 3 mechanizmy DI. Tylko jeden jest rekomendowany.

### 1. Constructor Injection ✅ (REKOMENDOWANE)

```java
@Service
public class InstrumentService {
    private final InstrumentRepository repository;  // final!

    // Spring widzi konstruktor z parametrem typu InstrumentRepository
    // → szuka Beana tego typu → wstrzykuje
    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }
}
```

**Dlaczego to najlepsze?**

| Zaleta           | Wyjaśnienie                                                     |
| ---------------- | --------------------------------------------------------------- |
| **Immutable**    | Pole jest `final` → nie można go zmienić po utworzeniu          |
| **Wymuszone**    | Nie da się stworzyć obiektu BEZ zależności (kompilator pilnuje) |
| **Testowalność** | W teście po prostu: `new InstrumentService(mockRepo)`           |
| **Jawne**        | Patrząc na konstruktor, widzisz WSZYSTKIE zależności            |

**Analogia Angular:** Dokładnie to robisz w każdym komponencie:

```typescript
// Angular — Constructor Injection
export class InstrumentComponent {
  constructor(private service: InstrumentService) {}
  //          ↑ Angular wstrzykuje — dokładnie jak Spring
}
```

**Ważne:** Od Spring 4.3, jeśli klasa ma **JEDEN konstruktor**, adnotacja `@Autowired`
jest **opcjonalna** — Spring automatycznie użyje tego konstruktora.

```java
// ✅ @Autowired NIE POTRZEBNE (jeden konstruktor)
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }
}

// ⚠️ @Autowired POTRZEBNE (wiele konstruktorów — musisz wskazać który)
@Service
public class InstrumentService {
    private final InstrumentRepository repository;

    @Autowired  // ← "użyj TEGO konstruktora"
    public InstrumentService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public InstrumentService() {
        this.repository = null; // fallback
    }
}
```

---

### 2. Setter Injection ⚠️ (OPCJONALNE ZALEŻNOŚCI)

```java
@Service
public class InstrumentService {
    private InstrumentRepository repository;  // NIE final!

    @Autowired  // ← wymagane przy setter injection
    public void setRepository(InstrumentRepository repository) {
        this.repository = repository;
    }
}
```

**Kiedy użyć?** Prawie nigdy. Jedyny sensowny przypadek: zależność jest **opcjonalna**
(np. cache, który może nie istnieć, logger opcjonalny).

**Problem:** Obiekt może istnieć BEZ ustawionej zależności → `NullPointerException` runtime.

---

### 3. Field Injection ❌ (UNIKAJ)

```java
@Service
public class InstrumentService {
    @Autowired  // ← Spring wstrzykuje bezpośrednio w pole (przez reflection)
    private InstrumentRepository repository;  // NIE final!

    // Brak konstruktora z parametrami
}
```

**Dlaczego to złe?**

| Problem               | Wyjaśnienie                                             |
| --------------------- | ------------------------------------------------------- |
| **Ukryte zależności** | Nie widać ich w konstruktorze — musisz czytać pola      |
| **Brak immutability** | Pole NIE MOŻE być `final`                               |
| **Trudne testowanie** | `new InstrumentService()` → repository = null → 💥 NPE  |
| **Reflection magic**  | Spring używa refleksji — łamie enkapsulację             |
| **God Object**        | Łatwo dodać 15 pól `@Autowired` i nie zauważyć problemu |

**Analogia Angular:** Często mylony z `inject()` function w nowym Angularze.
⚠️ **Uwaga:** Choć składniowo podobne (`private service = inject(Service)`),
w Angularze to **nowoczesne i zalecane podejście** (funkcyjne, jawne).
W Javie `@Autowired private Service` to **przestarzały anty-wzorzec** (ukrywa zależności, utrudnia testy).
Nie przenoś nawyków `inject()` na `@Autowired` w polu!

---

### Porównanie — jedna tabela

```java
// ✅ Constructor (REKOMENDOWANE)
public InstrumentService(InstrumentRepository repo) {
    this.repo = repo;  // final, jawne, testowalne
}

// ⚠️ Setter (opcjonalne zależności)
@Autowired
public void setRepo(InstrumentRepository repo) {
    this.repo = repo;  // NIE final, może być null
}

// ❌ Field (UNIKAJ)
@Autowired
private InstrumentRepository repo;  // ukryte, NIE final, reflection
```

| Aspekt           | Constructor                | Setter            | Field                |
| ---------------- | -------------------------- | ----------------- | -------------------- |
| `final`          | ✅ Tak                     | ❌ Nie            | ❌ Nie               |
| Jawne zależności | ✅ Widać w konstruktorze   | ⚠️ Rozproszone    | ❌ Ukryte            |
| Testowalność     | ✅ `new Service(mock)`     | ⚠️ Wymaga settera | ❌ Wymaga reflection |
| Obowiązkowe      | ✅ Kompilator pilnuje      | ❌ Może być null  | ❌ Może być null     |
| `@Autowired`     | Opcjonalne (1 konstruktor) | Wymagane          | Wymagane             |

### Kiedy widzisz wiele zależności w konstruktorze — to ZAPACH kodu

```java
// ⚠️ Red flag — za dużo zależności (Single Responsibility Principle naruszony)
public OrderService(
    InstrumentRepository instrumentRepo,
    TransactionRepository transactionRepo,
    UserRepository userRepo,
    NotificationService notificationService,
    PriceService priceService,
    AuditService auditService,
    CacheService cacheService
) { ... }
```

**Reguła kciuka:** Więcej niż **3-4 zależności** → rozważ podział klasy na mniejsze.
Constructor Injection sprawia, że ten problem jest **widoczny** — to zaleta, nie wada!

## Ćwiczenie

**Zadanie:** Przejrzyj swój Wallet Manager i odpowiedz na pytania:

1. Jakiego typu injection używasz teraz w kontrolerach i serwisach?
2. Czy pola zależności są `final`?
3. Czy używasz `@Autowired`? Czy jest potrzebne?
4. Ile zależności ma każda klasa (policz parametry konstruktora)?

**Nie zmieniaj jeszcze kodu** — praktyczne refaktorowanie robimy w Lekcji 06.

## Checklist

- [x] Wiem że Constructor Injection to jedyny rekomendowany sposób
- [x] Rozumiem dlaczego Field Injection jest złe (ukryte zależności, brak final)
- [x] Wiem kiedy `@Autowired` jest opcjonalne (jeden konstruktor)
- [x] Rozumiem że wiele zależności w konstruktorze = sygnał do refaktoryzacji
- [x] Widzę analogię do Angular Constructor DI
