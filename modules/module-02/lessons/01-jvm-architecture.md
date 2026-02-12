# Lekcja 01: JVM Architecture

> Bytecode, ClassLoaders, JIT — jak Java uruchamia Twój kod.

## Koncept

### Droga kodu: od `.java` do wykonania

```
 .java  →  javac  →  .class (bytecode)  →  JVM  →  native code
  kod       kompilator    plik binarny       maszyna    procesor
źródłowy                                    wirtualna
```

**Analogia Angular/TS:** To jak TypeScript → `tsc` → JavaScript → V8 Engine.
Różnica? Java kompiluje do **bytecodu** (pośredniego), nie do tekstu.

### Czym jest JVM?

JVM (Java Virtual Machine) to **abstrakcyjna maszyna**, która:

- Ładuje bytecode (pliki `.class`)
- Interpretuje go LUB kompiluje do kodu natywnego (JIT)
- Zarządza pamięcią (Garbage Collection)
- Zapewnia bezpieczeństwo (sandbox)

**"Write Once, Run Anywhere"** — bytecode jest identyczny na każdym OS.
JVM jest specyficzna dla platformy, ale bytecode nie.

### Architektura JVM — 3 główne bloki

```
┌─────────────────────────────────────────────────┐
│                    JVM                          │
├──────────────┬────────────────┬─────────────────┤
│  Class Loader │  Runtime Data  │  Execution     │
│  Subsystem    │  Areas         │  Engine         │
│               │                │                 │
│  ładowanie    │  pamięć        │  wykonanie      │
│  klas         │  (Stack, Heap) │  kodu           │
└──────────────┴────────────────┴─────────────────┘
```

### 1. Class Loader Subsystem

Odpowiada za **znalezienie, załadowanie i zweryfikowanie** klas.

| Loader                           | Co ładuje                                     | Analogia                               |
| -------------------------------- | --------------------------------------------- | -------------------------------------- |
| **Bootstrap**                    | Klasy rdzenia Java (`java.lang`, `java.util`) | Globalne API przeglądarki (DOM, fetch) |
| **Platform** (dawniej Extension) | Rozszerzenia JDK                              | Polyfille                              |
| **Application**                  | Twój kod + zależności (Maven/Gradle)          | Twoje moduły Angular + `node_modules`  |

**Hierarchia delegacji (Parent-First):**

```
Application → nie zna klasy
    ↓ pyta rodzica
Platform → nie zna klasy
    ↓ pyta rodzica
Bootstrap → zna! ładuje.
```

Każdy loader najpierw pyta rodzica. Dopiero gdy rodzic nie zna klasy — ładuje sam.

**Fazy ładowania:**

1. **Loading** — znalezienie pliku `.class`
2. **Linking** — weryfikacja bytecodu, przygotowanie pamięci
3. **Initialization** — wykonanie `static {}` bloków, inicjalizacja stałych

### 2. Execution Engine

To "procesor" JVM. Ma dwa tryby pracy:

| Tryb             | Jak działa                                     | Szybkość                        |
| ---------------- | ---------------------------------------------- | ------------------------------- |
| **Interpreter**  | Czyta bytecode linia po linii i wykonuje       | Wolny, ale natychmiastowy start |
| **JIT Compiler** | Kompiluje "gorące" fragmenty do kodu natywnego | Szybki po rozgrzewce            |

### JIT Compilation — klucz do wydajności

```
Start aplikacji:
  [Interpreter] → wolne, ale działa od razu
       ↓
  JVM obserwuje co się często wywołuje ("hot spots")
       ↓
  [JIT Compiler] → kompiluje gorące metody do native code
       ↓
  Kolejne wywołania → native code → SZYBKO
```

**Hot Spot** = metoda wywołana wiele razy (np. 10 000 razy).
JVM **profiluje** Twój kod w runtime i optymalizuje go.

**Warm-up time:**

- Pierwsze sekundy po starcie → JVM interpretuje (wolno)
- Po chwili → JIT kompiluje krytyczne ścieżki (szybko)
- Dlatego benchmarki w Javie wymagają "rozgrzewki"

**Analogia Angular:** Jak Angular Universal (SSR) — pierwszy render jest wolniejszy,
ale potem hydratacja przyspiesza interakcje.

### 3. Runtime Data Areas (pamięć)

Szczegóły w Lekcji 02. Na razie kluczowy podział:

| Obszar          | Co przechowuje                                  |
| --------------- | ----------------------------------------------- |
| **Stack**       | Wywołania metod, zmienne lokalne, referencje    |
| **Heap**        | Obiekty, tablice (współdzielony między wątkami) |
| **Method Area** | Metadane klas, kod metod, stałe                 |
| **PC Register** | Wskaźnik aktualnej instrukcji (per wątek)       |

## Ćwiczenie

**Zadanie:** Zaobserwuj jak JVM ładuje klasy i kompiluje JIT.

1. Stwórz prosty program z metodą wywoływaną w pętli 100 000 razy
2. Uruchom z flagą: `java -XX:+PrintCompilation NazwaKlasy`
3. Zaobserwuj które metody JIT skompilował (oznaczone `%` = on-stack replacement)

**Bonus:** Uruchom z `-verbose:class` żeby zobaczyć ładowanie klas.

```java
public class JitDemo {
    public static int compute(int x) {
        return x * x + x;
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < 100_000; i++) {
            sum += compute(i);
        }
        long end = System.nanoTime();
        System.out.println("Wynik: " + sum);
        System.out.println("Czas: " + (end - start) / 1_000_000 + " ms");
    }
}
```

## Checklist

- [ ] Potrafię opisać drogę `.java` → bytecode → wykonanie
- [ ] Rozumiem role trzech Class Loaderów i ich hierarchię
- [ ] Wiem czym jest JIT i dlaczego Java "przyspiesza" w runtime
- [ ] Rozumiem pojęcie "hot spot" i "warm-up time"
- [ ] Wiem czym się różni Interpreter od JIT Compiler
