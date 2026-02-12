# Moduł 02: Java Internals + Core Language Features

> Cel: Zrozumieć "silnik" Javy (JVM) i opanować kluczowe mechanizmy języka, których TypeScript nie ma.

> `opt` = optional

## Lekcje — JVM & Memory

| #   | Temat              | Opis                                                        | Status |
| --- | ------------------ | ----------------------------------------------------------- | ------ |
| 01  | JVM Architecture   | Bytecode, ClassLoaders, JIT — jak kod jest uruchamiany.     | ⚪     |
| 02  | Memory Model       | Stack vs Heap. Gdzie żyją zmienne i obiekty.                | ⚪     |
| 03  | Pass by Value      | Obalenie mitu "Pass by Reference". Referencje vs Wartości.  | ⚪     |
| 04  | String Pool `opt`  | Immutability, `intern()`, StringBuilder vs Concatenation.   | ⚪     |
| 05  | Garbage Collection | Cykl życia obiektu, Generacje, jak unikać Memory Leaks.     | ⚪     |
| 06  | Exceptions         | Checked vs Unchecked. Dlaczego `try-catch` to nie wszystko. | ⚪     |
| 07  | Java EE i Spring   | Beany, Jakarta EE, Servlety — most między Javą a Springiem. | ⚪     |

## Lekcje — Core Language Features 🆕

| #   | Temat                     | Opis                                                                 | Status |
| --- | ------------------------- | -------------------------------------------------------------------- | ------ |
| 08  | Generics + Type Erasure   | Bounded types, wildcards, erasure na runtime. Kluczowe dla kolekcji. | ⚪     |
| 09  | Collections Framework     | List, Set, Map, Queue — hierarchia, implementacje, kiedy co.         | ⚪     |
| 10  | Enums jako klasy          | Metody, pola, implementacja interfejsów. Nie jak TS `enum`.          | ⚪     |
| 11  | Optional + Null Handling  | `Optional<T>`, `Objects.requireNonNull()`. Zamiast TS `?.` i `??`.   | ⚪     |
| 12  | Functional Interfaces     | Predicate, Function, Consumer, Supplier, lambdy, `::` references.    | ⚪     |
| 13  | Date/Time API             | LocalDate, ZonedDateTime, Instant, Duration. Inne niż JS `Date`.     | ⚪     |
| 14  | Sealed Classes + Patterns | Sealed types, pattern matching z instanceof, switch expressions.     | ⚪     |
| 15  | var, final, wrappers      | Type inference, keyword `final`, autoboxing, Integer Cache.          | ⚪     |

`⚪ Not Started` · `🟡 In Progress` · `🟢 Done`

## Projekt

W tym module nie budujemy funkcjonalności biznesowej w `wallet-manager`.
Zamiast tego tworzymy małe, izolowane programy w `exercises/`, które udowadniają działanie JVM
i kluczowych mechanizmów języka.

## Powiązana teoria

- `docs/theory/01-java-fundamentals.md`
- `docs/theory/02-concurrency.md` → Wstęp do wątków

## Wymagania wstępne

- ✅ Moduł 01 ukończony (REST API, Records, Stream API)
- 🛠 Zainstalowane JDK 25 (narzędzia: `jvisualvm`, `jconsole`)
