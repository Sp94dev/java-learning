# Lekcja 07: Java EE, Beany i Spring — most między Javą a frameworkiem

> Czym jest Bean, skąd wzięło się Jakarta EE i jak Spring Boot to wszystko upraszcza.

## Koncept

### Problem: Java sama w sobie nie ma "frameworka webowego"

Czysta Java (OpenJDK) daje Ci:

- Kolekcje, Stringi, I/O, wątki (to co pokrywamy w Module 02)
- **Ale nie daje:** serwera HTTP, dependency injection, ORM, bezpieczeństwa

To jak mieć silnik V8 (Node.js) bez Angulara — możesz zrobić wszystko, ale ręcznie.

Stąd powstały dwa światy:

```
1999: Java EE (J2EE) — Sun Microsystems
        ↓ ciężki, XML-owy, Enterprise JavaBeans (EJB) — koszmar
2004: Spring Framework — Rod Johnson
        ↓ "lekka alternatywa" dla Java EE, prostsza DI
2017: Java EE → Jakarta EE (Oracle oddał Eclipse Foundation)
2024: Spring Boot 4 + Jakarta EE 11 — współpracują, nie konkurują
```

**Kluczowe:** Spring **nie zastąpił** Java EE — on **używa** specyfikacji Jakarta EE
i dodaje swoją warstwę ułatwień:

```
Jakarta EE  = specyfikacja (interfejsy, standardy, "co")
Spring      = implementacja + opinia (konkretny kod, "jak")
```

**Analogia Angular:**

```
Jakarta EE  ≈  Web Standards (DOM API, Fetch API, Web Components spec)
Spring      ≈  Angular (framework który UŻYWA tych standardów + dodaje swoje)
```

---

### Co to jest Bean?

W najprostszym ujęciu: **Bean = obiekt zarządzany przez kontener** (nie przez Ciebie).

| Koncept           | Bez kontenera (czysta Java)             | Z kontenerem (Spring)                  |
| ----------------- | --------------------------------------- | -------------------------------------- |
| Tworzenie obiektu | `new InstrumentService()` — Ty tworzysz | Spring tworzy automatycznie            |
| Zależności        | Ty wstrzykujesz ręcznie                 | Kontener wstrzykuje (DI)               |
| Cykl życia        | Ty zarządzasz                           | Kontener zarządza (init, destroy)      |
| Scope             | Zależy od Ciebie                        | Singleton (domyślnie), Prototype, etc. |

**Analogia Angular:**

```
Spring Bean  ≈  Angular Service zarejestrowany w providedIn: 'root'
Spring IoC   ≈  Angular Injector
@Service     ≈  @Injectable()
```

W Angular nie robisz `new MyService()` — framework tworzy instancję za Ciebie
i wstrzykuje przez konstruktor. **W Springu jest dokładnie tak samo.**

### Typy Beanów — ważna disambiguacja

| Typ                           | Co to jest                                                        | Gdzie spotkasz                         |
| ----------------------------- | ----------------------------------------------------------------- | -------------------------------------- |
| **JavaBean**                  | Klasa z getterami/setterami, no-arg constructor. Stara konwencja. | Stare projekty, JSP                    |
| **EJB (Enterprise JavaBean)** | Ciężki komponent biznesowy zarządzany przez serwer aplikacyjny    | Legacy. **NIE używasz w Spring Boot.** |
| **Spring Bean**               | Obiekt zarządzany przez Spring IoC Container. Lekki, prosty.      | ✅ **To jest Twój Bean.**              |
| **CDI Bean (Jakarta)**        | Standard DI z Jakarta EE. Spring ma swój mechanizm.               | Rzadko w Spring Boot                   |

**TL;DR:** Gdy ktoś mówi "Bean" w kontekście Spring Boot → **Spring Bean**
(`@Service`, `@Component`, `@Repository`).

---

### Servlet — fundament HTTP w Javie

```
HTTP Request → Serwer (Tomcat) → Servlet → Twój kod
```

- `Servlet` = klasa Javy która obsługuje HTTP request/response
- **Spring Boot ukrywa Servlety** — Ty piszesz `@RestController`,
  a pod spodem Spring tworzy `DispatcherServlet` który routuje requesty
- Nigdy nie piszesz Servletów ręcznie w Spring Boot. Ale **tam siedzą**.

```java
// Tak wygląda surowy Servlet (NIE piszesz tego w Spring Boot)
@WebServlet("/api/instruments")
public class InstrumentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write("{\"ticker\": \"AAPL\"}");
    }
}

// W Spring Boot piszesz zamiast tego:
@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {
    @GetMapping
    public Instrument get() {
        return new Instrument("AAPL", "Apple");  // automatyczna serializacja do JSON
    }
}
```

**Analogia Angular:** Servlet to jak pisanie vanilla JS z `addEventListener('fetch', ...)`
zamiast używania Angular HttpClient + Router.

---

### Serwer aplikacyjny vs Embedded Server

```
Stary świat (Java EE):
  Twój .war → deploy na JBoss/WebSphere/GlassFish → serwer zarządza wszystkim

Nowy świat (Spring Boot):
  Twój .jar (z wbudowanym Tomcat) → java -jar app.jar → gotowe!
```

**Analogia Angular:**

```
Java EE deploy   ≈  budowanie Angulara i wrzucanie na Apache/Nginx (osobny serwer)
Spring Boot .jar ≈  ng serve / SSR z wbudowanym serwerem (w jednym pakiecie)
```

---

### JPA (Jakarta Persistence API) — zapowiedź Module 05

```
Twój kod → JPA (interfejs) → Hibernate (implementacja) → SQL → Database
```

- JPA = **specyfikacja** jak mapować obiekty Java na tabele w bazie
- Hibernate = **implementacja** JPA (najpopularniejsza)
- Spring Data JPA = **wrapper Springa** na JPA (jeszcze prostsza warstwa)

To spotkasz w **Module 05** — na razie wystarczy wiedzieć że istnieje.

---

### Adnotacje Jakarta które spotkasz w Spring Boot

| Adnotacja        | Źródło                  | Znaczenie                                  | Kiedy spotkasz |
| ---------------- | ----------------------- | ------------------------------------------ | -------------- |
| `@PostConstruct` | Jakarta                 | Metoda wywoływana PO utworzeniu Beana      | Module 03      |
| `@PreDestroy`    | Jakarta                 | Metoda wywoływana PRZED zniszczeniem Beana | Module 03      |
| `@Inject`        | Jakarta CDI             | DI — Spring wspiera, preferuje konstruktor | Module 03      |
| `@Entity`        | Jakarta JPA             | Klasa mapowana na tabelę DB                | Module 05      |
| `@Transactional` | Jakarta / Spring        | Zarządzanie transakcjami DB                | Module 05      |
| `@Valid`         | Jakarta Bean Validation | Walidacja danych wejściowych               | Module 09      |

---

### Mapa priorytetów — co ważne DLA CIEBIE

| Koncept                                    | Priorytet            | Kiedy                               |
| ------------------------------------------ | -------------------- | ----------------------------------- |
| **Spring Bean** (`@Service`, `@Component`) | 🔴 Kluczowy          | Module 03                           |
| **Servlet / DispatcherServlet**            | 🟡 Wiedz że istnieje | Tło dla `@RestController`           |
| **JPA / Hibernate**                        | 🟡 Niedługo          | Module 05                           |
| **Bean Validation (`@Valid`)**             | 🟡 Niedługo          | Module 09                           |
| **EJB**                                    | ⚪ Ignoruj           | Legacy, nie używasz                 |
| **CDI**                                    | ⚪ Ignoruj           | Spring ma swoje DI                  |
| **JSP / JSF**                              | ⚪ Ignoruj           | Frontend Java — martwy dla REST API |

## Podsumowanie

> Spring Boot to **opiniowana nadbudówka** nad Jakarta EE.
> Używa jej specyfikacji (JPA, Servlet, Bean Validation) ale ukrywa złożoność.
> Ty piszesz `@Service` — Spring robi resztę.

## Checklist

- [ ] Wiem że "Bean" w Spring Boot = obiekt zarządzany przez kontener IoC
- [ ] Rozumiem relację Jakarta EE (specyfikacja) vs Spring (implementacja)
- [ ] Wiem co to Servlet i dlaczego nie piszę go ręcznie w Spring Boot
- [ ] Znam różnicę między deploy `.war` na serwer vs `.jar` z embedded Tomcat
- [ ] Potrafię wskazać adnotacje Jakarta które spotkam w Spring Boot
- [ ] Wiem co to JPA/Hibernate na poziomie ogólnym (szczegóły w Module 05)
