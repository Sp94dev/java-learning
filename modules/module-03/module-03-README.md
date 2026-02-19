# Module 03: Dependency Injection + Layers

> Goal: Understand Dependency Injection, IoC Container, and separate application layers.

> `opt` = optional

## Lessons

| #   | Topic                     | Description                                                     | Status |
| --- | ------------------------- | --------------------------------------------------------------- | ------ |
| 01  | The Problem Without DI    | Tight coupling, hidden dependencies, Inversion of Control.      | 🟢     |
| 02  | Spring IoC Container      | ApplicationContext, Bean, Component Scanning.                   | 🟢     |
| 03  | Injection Types           | Constructor ✅, Setter, Field ❌ — why Constructor wins.        | 🟢     |
| 04  | Stereotypes (Annotations) | @Component, @Service, @Repository, @Controller, @Configuration. | 🟢     |
| 05  | Bean Scopes + Lifecycle   | Singleton, Prototype, Request, @PostConstruct, @PreDestroy.     | 🟢     |
| 06  | Layered Architecture      | Controller → Service → Repository. Thin Controller.             | 🟢     |
| 07  | Lombok                    | @RequiredArgsConstructor, @Data, @Builder, @Slf4j.              | 🟢     |

`⚪ Not Started` · `🟡 In Progress` · `🟢 Done`

## Project

In this module you refactor the **Wallet Manager API** — instead of manually creating objects,
Spring takes control over creating and injecting dependencies.

Key changes:

- Proper layer hierarchy: Controller → Service → Repository
- Constructor Injection instead of `new`
- One Bean = one responsibility

## Related Theory

- `docs/theory/04-spring-framework.md` → DI, IoC Container, Bean Lifecycle
- Module 02, Lesson 07 → Java EE, Beans, and Spring (bridge to this module)

## Prerequisites

- ✅ Module 01 completed (REST API, Records, Service layer)
- ✅ Module 02 reviewed (JVM, Memory, Java EE → Spring)
- 🛠 Working Wallet Manager project with `InstrumentController` and `TransactionController`

## Readiness Checklist

- [x] I understand what Dependency Injection is and why it matters
- [x] I use Constructor Injection (not Field Injection)
- [x] I know the difference between @Service, @Repository, @Component
- [x] I can separate Controller → Service → Repository
- [x] Controller is "thin" — only delegates
- [x] I know what Bean Scope is and what the default is
- [x] I can use Lombok to reduce boilerplate
