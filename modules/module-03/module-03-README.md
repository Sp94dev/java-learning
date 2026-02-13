# Moduł 03: Dependency Injection + Warstwy

> Cel: Zrozumieć Dependency Injection, IoC Container i wydzielić warstwy aplikacji.

> `opt` = optional

## Lekcje

| #   | Temat                    | Opis                                                             | Status |
| --- | ------------------------ | ---------------------------------------------------------------- | ------ |
| 01  | Problem bez DI + Koncept | Tight coupling, ukryte zależności, Inversion of Control.         | ⚪     |
| 02  | Spring IoC Container     | ApplicationContext, Bean, Component Scanning.                    | ⚪     |
| 03  | Typy Injection           | Constructor ✅, Setter, Field ❌ — dlaczego Constructor wygrywa. | ⚪     |
| 04  | Stereotypy (Adnotacje)   | @Component, @Service, @Repository, @Controller, @Configuration.  | ⚪     |
| 05  | Bean Scopes + Lifecycle  | Singleton, Prototype, Request, @PostConstruct, @PreDestroy.      | ⚪     |
| 06  | Architektura Warstwowa   | Controller → Service → Repository. Thin Controller.              | ⚪     |
| 07  | Lombok                   | @RequiredArgsConstructor, @Data, @Builder, @Slf4j.               | ⚪     |

`⚪ Not Started` · `🟡 In Progress` · `🟢 Done`

## Projekt

W tym module refaktoryzujesz **Wallet Manager API** — zamiast ręcznego tworzenia obiektów,
Spring przejmuje kontrolę nad tworzeniem i wstrzykiwaniem zależności.

Kluczowe zmiany:

- Poprawna hierarchia warstw: Controller → Service → Repository
- Constructor Injection zamiast `new`
- Jeden Bean = jedna odpowiedzialność

## Powiązana teoria

- `docs/theory/04-spring-framework.md` → DI, IoC Container, Bean Lifecycle
- Moduł 02, Lekcja 07 → Java EE, Beany i Spring (most do tego modułu)

## Wymagania wstępne

- ✅ Moduł 01 ukończony (REST API, Records, Service layer)
- ✅ Moduł 02 przejrzany (JVM, Memory, Java EE → Spring)
- 🛠 Działający projekt Wallet Manager z `InstrumentController` i `TransactionController`

## Sprawdzian gotowości

- [ ] Rozumiem co to Dependency Injection i po co
- [ ] Używam Constructor Injection (nie Field Injection)
- [ ] Wiem różnicę między @Service, @Repository, @Component
- [ ] Potrafię wydzielić Controller → Service → Repository
- [ ] Controller jest "cienki" - tylko deleguje
- [ ] Wiem co to Bean Scope i jaki jest domyślny
- [ ] Potrafię użyć Lomboka do redukcji boilerplate'u
