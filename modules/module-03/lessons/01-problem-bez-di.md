# Lekcja 01: Problem bez DI + Koncept Dependency Injection

> Dlaczego `new` to problem i jak Inversion of Control zmienia zasady gry.

## Koncept

### Problem: Tight Coupling (bez DI)

Wyobraź sobie, że w Angularze nie masz DI i musisz tworzyć wszystko ręcznie:

```typescript
// ❌ Angular BEZ Dependency Injection (gdyby nie istniało)
export class InstrumentComponent {
  private service: InstrumentService;

  constructor() {
    const http = new HttpClient(); // sam tworzysz HttpClient
    const cache = new CacheService(); // sam tworzysz CacheService
    this.service = new InstrumentService(http, cache); // sam składasz
  }
}
```

**Co jest źle?**

1. **Tight coupling** — komponent ZALEŻY od konkretnych implementacji.
2. **Trudne testowanie** — nie możesz podmienić na mock.
3. **Ukryte zależności** — nie wiadomo czego klasa naprawdę potrzebuje.
4. **Kaskada zmian** — zmiana konstruktora `CacheService` psuje WSZYSTKO co go tworzy.

### Ten sam problem w Javie

```java
// ❌ Java BEZ Dependency Injection
public class InstrumentController {
    private final InstrumentService service;

    public InstrumentController() {
        // Controller TWORZY Service → Service TWORZY Repository → ...
        InMemoryInstrumentRepository repo = new InMemoryInstrumentRepository();
        this.service = new InstrumentService(repo);  // tight coupling!
    }
}
```

**Problemy:**

- Controller **wie** jaką implementację Repository używa Service → to nie jego sprawa!
- Chcesz zmienić z `InMemory` na `JPA`? Musisz zmienić Controller → absurd.
- Chcesz przetestować Controller bez prawdziwego Service? Nie da się.

---

### Rozwiązanie: Dependency Injection (DI)

**DI = zależności przekazywane Z ZEWNĄTRZ, a nie tworzone wewnątrz.**

```java
// ✅ Java Z Dependency Injection
public class InstrumentController {
    private final InstrumentService service;

    // Ktoś Z ZEWNĄTRZ daje mi Service — nie obchodzi mnie skąd
    public InstrumentController(InstrumentService service) {
        this.service = service;
    }
}
```

**Analogia Angular:** Dokładnie to, co robisz codziennie!

```typescript
// ✅ Angular — DI przez konstruktor (to samo co powyżej!)
export class InstrumentComponent {
  constructor(private service: InstrumentService) {}
  // Angular Injector tworzy InstrumentService ZA CIEBIE
}
```

### Inversion of Control (IoC) — odwrócenie kontroli

| Bez IoC                         | Z IoC                                 |
| ------------------------------- | ------------------------------------- |
| **Ty** tworzysz obiekty (`new`) | **Kontener** tworzy obiekty za Ciebie |
| **Ty** zarządzasz zależnościami | **Kontener** wstrzykuje zależności    |
| **Ty** decydujesz o kolejności  | **Kontener** decyduje o kolejności    |

```
Bez IoC:  Twój kod → tworzy zależności → używa ich
Z IoC:    Kontener → tworzy zależności → wstrzykuje do Twojego kodu → Twój kod ich używa
```

**Analogia Angular:**

```
Angular Injector  =  Spring IoC Container
providedIn: 'root' =  @Service (Singleton Bean)
@Injectable()      =  @Component / @Service
```

### Trzy korzyści DI — zapamiętaj

1. **Luźne powiązania (Loose Coupling)**
   - Klasa zależy od **interfejsu**, nie od implementacji.
   - Zmiana `InMemoryRepo` → `JpaRepo`? Zmiana w JEDNYM miejscu.

2. **Testowalność**
   - W teście wstrzykujesz **mock** zamiast prawdziwej implementacji.
   - Bez DI = brak mocków = brak unit testów.

3. **Separacja odpowiedzialności**
   - Controller nie wie JAK Service działa.
   - Service nie wie JAK Repository przechowuje dane.
   - Każdy robi SWOJE.

---

### Wizualizacja: Przed i Po DI

```
❌ BEZ DI (tight coupling):

Controller ──new──▶ Service ──new──▶ Repository ──new──▶ DataSource
     ↑                ↑                  ↑
  wie o wszystkim   wie o repo       wie o DS

✅ Z DI (loose coupling):

Container tworzy:  DataSource → Repository → Service → Controller
                                                              ↓
Controller wie TYLKO o Service (nie obchodzi go reszta)
```

## Ćwiczenie

**Zadanie:** Przeanalizuj swój obecny kod Wallet Manager.

1. Otwórz `InstrumentController.java` i `InstrumentService.java`.
2. Sprawdź: **Czy Controller tworzy Service przez `new`?**
3. Sprawdź: **Czy Service tworzy Repository przez `new`?**
4. Jeśli tak — zidentyfikuj gdzie jest tight coupling.
5. Zastanów się: co byś musiał zmienić, gdybyś chciał podmienić `InMemoryRepository` na `JpaRepository`?

**Nie zmieniaj jeszcze kodu** — to zrobimy w Lekcji 03 i 06.

## Checklist

- [x] Rozumiem problem tight coupling (klasa tworzy swoje zależności)
- [x] Wiem czym jest Dependency Injection (zależności z zewnątrz)
- [x] Rozumiem Inversion of Control (kontener zarządza, nie ja)
- [x] Widzę analogię do Angular DI (`@Injectable()` ≈ `@Service`)
- [x] Potrafię wskazać tight coupling w kodzie (szukam `new` w konstruktorze)
