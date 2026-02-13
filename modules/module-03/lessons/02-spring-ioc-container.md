# Lekcja 02: Spring IoC Container

> ApplicationContext, Bean, Component Scanning — silnik DI w Spring Boot.

## Koncept

### Co to jest Spring IoC Container?

W Angularze masz **Injector** — obiekt który wie jak tworzyć serwisy.
W Springu masz **ApplicationContext** — to jest Twój Injector na sterydach.

```
Angular:    Injector → tworzy @Injectable() serwisy → wstrzykuje
Spring:     ApplicationContext → tworzy @Component beany → wstrzykuje
```

### ApplicationContext — "rejestr wszystkich beanów"

Kiedy uruchamiasz `./mvnw spring-boot:run`, Spring Boot:

1. **Skanuje** pakiety w poszukiwaniu adnotacji (`@Component`, `@Service`, etc.)
2. **Tworzy** instancje znalezionych klas (Beany)
3. **Wstrzykuje** zależności między nimi
4. **Przechowuje** je w `ApplicationContext`
5. **Serwuje** je gdy ktoś ich potrzebuje

```
Start aplikacji:
  @SpringBootApplication
        ↓
  Component Scan (skanuje pakiety)
        ↓
  Znalezione: InstrumentController, InstrumentService, InMemoryRepository
        ↓
  Spring tworzy: Repository → Service(repository) → Controller(service)
        ↓
  Wszystko siedzi w ApplicationContext — gotowe do użycia
```

### Co to jest Bean?

**Bean = obiekt zarządzany przez Spring.** Nie Ty go tworzysz `new` — Spring to robi.

| Aspekt         | Zwykły obiekt (`new`)  | Spring Bean                     |
| -------------- | ---------------------- | ------------------------------- |
| Kto tworzy?    | Ty (`new MyService()`) | Spring (automatycznie)          |
| Kto zarządza?  | Ty (cykl życia)        | Spring (init → use → destroy)   |
| Ile instancji? | Ile razy zrobisz `new` | Domyślnie 1 (Singleton)         |
| Zależności?    | Ręcznie przekazujesz   | Spring wstrzykuje automatycznie |

**Analogia Angular:**

```typescript
// Angular: serwis zarejestrowany jako Bean
@Injectable({
  providedIn: "root", // ≈ Singleton Bean w Springu
})
export class InstrumentService {}
```

```java
// Spring: serwis zarejestrowany jako Bean
@Service  // ≈ @Injectable({ providedIn: 'root' })
public class InstrumentService { }
```

### Component Scanning — jak Spring znajduje Beany

Spring skanuje **pakiet główny** (gdzie jest `@SpringBootApplication`) i **wszystkie pod-pakiety**.

```
com.sp94dev.wallet/
├── WalletApplication.java       ← @SpringBootApplication (stąd skanuje)
├── instrument/
│   ├── InstrumentController.java  ← @RestController → znaleziony! ✅
│   ├── InstrumentService.java     ← @Service → znaleziony! ✅
│   └── InMemoryInstrumentRepository.java ← @Repository → znaleziony! ✅
└── transaction/
    ├── TransactionController.java ← @RestController → znaleziony! ✅
    └── ...
```

**⚠️ Pułapka:** Jeśli klasa jest POZA pakietem `com.sp94dev.wallet`, Spring jej **NIE znajdzie**.

```
com.sp94dev.wallet/          ← skanowane ✅
com.sp94dev.wallet.instrument/ ← skanowane ✅ (pod-pakiet)
com.sp94dev.other/           ← NIE skanowane ❌ (inny pakiet!)
```

**Analogia Angular:** To jak Angular Module — jeśli nie zadeklarujesz komponentu
w `declarations` (lub nie ma `providedIn`), Angular go nie widzi.

### @SpringBootApplication — co robi pod spodem

```java
@SpringBootApplication  // = 3 adnotacje w jednej:
// @Configuration      → ta klasa to konfiguracja
// @EnableAutoConfiguration → Spring Boot konfiguruje automatycznie (Tomcat, Jackson, etc.)
// @ComponentScan      → skanuj ten pakiet i pod-pakiety
public class WalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
        // ↑ tutaj Spring tworzy ApplicationContext, skanuje, tworzy beany
    }
}
```

### Jak Spring buduje graf zależności?

Spring analizuje konstruktory Beanów i automatycznie rozwiązuje zależności:

```
Analiza:
  InstrumentController(InstrumentService) → potrzebuje InstrumentService
  InstrumentService(InstrumentRepository) → potrzebuje InstrumentRepository
  InMemoryInstrumentRepository() → nie potrzebuje niczego

Kolejność tworzenia (od "liści" do "korzenia"):
  1. InMemoryInstrumentRepository  (brak zależności)
  2. InstrumentService(repository) (wstrzykuje repository)
  3. InstrumentController(service) (wstrzykuje service)
```

**Co jeśli Spring nie może rozwiązać zależności?**

```
***************************
APPLICATION FAILED TO START
***************************
Parameter 0 of constructor in InstrumentService required a bean
of type 'InstrumentRepository' that could not be found.
```

Ten błąd zobaczysz często — oznacza: "Potrzebuję Beana typu X, ale nie znalazłem go
w ApplicationContext". Rozwiązanie? Dodaj odpowiednią adnotację (`@Service`, `@Repository`, etc.).

## Ćwiczenie

**Zadanie:** Zbadaj ApplicationContext swojej aplikacji.

1. Uruchom Wallet Manager: `./mvnw spring-boot:run`
2. Poszukaj w logach linii: `Tomcat started on port 8080`
3. Dodaj tymczasowo do `WalletApplication.java`:

```java
@SpringBootApplication
public class WalletApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(WalletApplication.class, args);

        // Wypisz wszystkie beany
        String[] beanNames = context.getBeanDefinitionNames();
        System.out.println("=== Załadowane beany: " + beanNames.length + " ===");
        for (String name : beanNames) {
            System.out.println("  → " + name);
        }
    }
}
```

4. Znajdź na liście swoje beany (instrument, transaction).
5. Zwróć uwagę ile beanów Spring tworzy automatycznie (auto-configuration).

## Checklist

- [ ] Wiem że ApplicationContext = kontener na wszystkie Beany
- [ ] Rozumiem Component Scanning (skanuje pakiet @SpringBootApplication + pod-pakiety)
- [ ] Wiem że Bean = obiekt zarządzany przez Spring (nie tworzony przez `new`)
- [ ] Rozumiem co @SpringBootApplication robi pod spodem (3 adnotacje)
- [ ] Potrafię zdiagnozować błąd "bean not found" (brak adnotacji sterotypowej)
