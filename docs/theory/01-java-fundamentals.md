# TEORIA: Java Fundamentals

---

## 1. Czym jest Java?

### Język + Platforma
Java to nie tylko język programowania, ale cały ekosystem:

**Język** - składnia, którą piszesz (.java)
**Kompilator** (javac) - tłumaczy kod na bytecode (.class)
**JVM** (Java Virtual Machine) - wykonuje bytecode
**JRE** (Java Runtime Environment) - JVM + biblioteki standardowe
**JDK** (Java Development Kit) - JRE + narzędzia deweloperskie

### "Write Once, Run Anywhere"
Kluczowa idea Javy: kod kompiluje się do **bytecode** - uniwersalnego formatu zrozumiałego przez JVM. JVM istnieje dla każdej platformy (Windows, Mac, Linux), więc ten sam .class działa wszędzie.

```
Twój kod (.java)
      ↓ javac
Bytecode (.class) ← uniwersalny
      ↓
   JVM Windows / JVM Mac / JVM Linux
      ↓
   Kod maszynowy (specyficzny dla CPU)
```

**Analogia Angular:** To jak TypeScript kompilujący się do JavaScript - TS działa wszędzie gdzie jest silnik JS.

---

## 2. JVM - Jak działa?

### Architektura JVM

```
┌─────────────────────────────────────────────────────────┐
│                         JVM                             │
├─────────────────────────────────────────────────────────┤
│  CLASS LOADER SUBSYSTEM                                 │
│  ├─ Loading (ładowanie klas z .class/.jar)             │
│  ├─ Linking (weryfikacja, przygotowanie, rozwiązanie)  │
│  └─ Initialization (static bloki, static pola)         │
├─────────────────────────────────────────────────────────┤
│  RUNTIME DATA AREAS (pamięć)                           │
│  ├─ Method Area (metadane klas, static)                │
│  ├─ Heap (obiekty - SHARED między wątkami)             │
│  ├─ Stack (per wątek - zmienne lokalne, wywołania)     │
│  ├─ PC Register (per wątek - current instruction)      │
│  └─ Native Method Stack (dla native code)              │
├─────────────────────────────────────────────────────────┤
│  EXECUTION ENGINE                                       │
│  ├─ Interpreter (wykonuje bytecode linia po linii)     │
│  ├─ JIT Compiler (kompiluje "hot" code do native)      │
│  └─ Garbage Collector (zarządza pamięcią)              │
└─────────────────────────────────────────────────────────┘
```

### Class Loader - hierarchia
Klasy ładowane są przez trzy loadery w hierarchii:

1. **Bootstrap ClassLoader** - ładuje core Java (java.lang.*, java.util.*)
2. **Extension ClassLoader** - ładuje rozszerzenia (jre/lib/ext)
3. **Application ClassLoader** - ładuje Twój kod i biblioteki

**Delegation Model:** Każdy loader najpierw pyta "rodzica" czy ma klasę. Dopiero gdy rodzic nie ma - ładuje sam. To zapobiega nadpisywaniu klas systemowych.

### JIT Compilation - dlaczego Java przyspiesza?

**Problem:** Interpreter jest wolny - tłumaczy bytecode → native przy każdym wywołaniu.

**Rozwiązanie:** JIT (Just-In-Time) Compiler obserwuje "hot spots" - kod wykonywany często. Kompiluje go raz do native i cachuje.

```
Start aplikacji → Interpreter (wolno, ale szybki start)
        ↓
JVM zbiera statystyki (ile razy każda metoda wywołana)
        ↓
Hot method (>10,000 wywołań) → JIT kompiluje do native
        ↓
Następne wywołania → native speed
```

**Wniosek:** Java jest wolna na starcie, ale przyspiesza w czasie działania. Dlatego benchmarki mierzą "warm" performance.

---

## 3. Pamięć w Javie

### Stack vs Heap

| Stack | Heap |
|-------|------|
| Per wątek (izolowany) | Współdzielony między wątkami |
| Szybki (LIFO) | Wolniejszy |
| Automatyczne czyszczenie | Garbage Collector |
| Zmienne lokalne, referencje | Obiekty |
| Mały rozmiar (~1MB per wątek) | Duży (GB) |

### Co gdzie trafia?

**Stack:**
- Primitive values (int, boolean, double...)
- Referencje (adresy do obiektów na Heap)
- Stack frames (kontekst wywołania metody)

**Heap:**
- Wszystkie obiekty (new Object())
- Tablice (to też obiekty)
- Stringi

### Przykład wizualny

```java
public void example() {
    int x = 10;                    // Stack: wartość 10
    String name = "Java";          // Stack: referencja → Heap: String object
    User user = new User("John");  // Stack: referencja → Heap: User object
}
```

```
STACK (Thread-1)              HEAP
┌──────────────────┐         ┌────────────────────────┐
│ x = 10           │         │  ┌──────────────────┐  │
├──────────────────┤         │  │ String "Java"    │←─┼─── name
│ name = 0x1234 ───┼────────►│  └──────────────────┘  │
├──────────────────┤         │  ┌──────────────────┐  │
│ user = 0x5678 ───┼────────►│  │ User             │  │
└──────────────────┘         │  │  name = "John"   │  │
                             │  └──────────────────┘  │
                             └────────────────────────┘
```

### Pass by Value - ZAWSZE

Java **zawsze** przekazuje przez wartość. Ale wartość referencji to adres!

**Dla primitives:**
```java
void modify(int x) {
    x = 100;  // Modyfikujesz KOPIĘ wartości
}

int a = 10;
modify(a);
// a nadal = 10
```

**Dla obiektów:**
```java
void modify(User user) {
    user.setName("New");  // Modyfikujesz obiekt przez KOPIĘ referencji
    user = new User("X"); // Zmieniasz LOKALNĄ kopię referencji
}

User u = new User("Old");
modify(u);
// u.name = "New" (obiekt zmodyfikowany)
// u nadal wskazuje na oryginalny obiekt (referencja nie zmieniona)
```

**Kluczowy insight:** Przekazujesz kopię "pilota" (referencji), nie kopię "telewizora" (obiektu). Możesz zmienić kanał (stan obiektu), ale nie możesz sprawić, że oryginalny pilot będzie sterował innym telewizorem.

---

## 4. Garbage Collection

### Po co GC?
W C/C++ programista musi ręcznie zwalniać pamięć (`free`, `delete`). To prowadzi do:
- Memory leaks (zapomniano zwolnić)
- Dangling pointers (zwolniono za wcześnie)
- Double free (zwolniono dwa razy)

Java rozwiązuje to automatycznie - GC usuwa obiekty bez referencji.

### Jak GC decyduje co usunąć?

**Reachability Analysis:** GC startuje od "root" referencji i traversuje graf obiektów. Wszystko nieosiągalne = garbage.

**Root references:**
- Zmienne lokalne na stosie
- Static pola klas
- JNI references
- Active threads

```
[ROOT: main thread stack]
         │
         ▼
      [Object A] ──────► [Object B]
                              │
                              ▼
                         [Object C]

      [Object X] ──► [Object Y]    ← GARBAGE (nieosiągalne z root)
```

### Generational GC - dlaczego?

**Hipoteza:** Większość obiektów żyje krótko ("infant mortality").

```
Liczba obiektów
     │
     │  █
     │  █
     │  █ █
     │  █ █ █
     │  █ █ █ █ █ █
     └──────────────────► Wiek obiektu
        młode    stare
```

**Konsekwencja:** Dziel heap na generacje, często czyść młode (tanie), rzadko stare (drogie).

### Struktura Heap

```
┌─────────────────────────────────────────────────────────┐
│                    YOUNG GENERATION                     │
│  ┌─────────────────┬───────────────┬───────────────┐   │
│  │      Eden       │  Survivor 0   │  Survivor 1   │   │
│  │  (nowe obiekty) │               │               │   │
│  └─────────────────┴───────────────┴───────────────┘   │
│                                                         │
│  Minor GC: częsty (~ms), tylko Young Gen               │
├─────────────────────────────────────────────────────────┤
│                    OLD GENERATION                       │
│  ┌─────────────────────────────────────────────────┐   │
│  │  Obiekty które przeżyły wiele Minor GC          │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
│  Major GC: rzadki (~s), cały heap, Stop-The-World      │
└─────────────────────────────────────────────────────────┘
```

### Lifecycle obiektu

1. `new Object()` → **Eden**
2. Przeżył Minor GC → **Survivor** (S0 lub S1, ping-pong)
3. Przeżył N Minor GC → **Old Generation** (promoted)
4. Brak referencji → GC usuwa

### GC Algorithms

| Algorithm | Charakterystyka | Use case |
|-----------|-----------------|----------|
| **Serial** | Single-threaded, stop-the-world | Małe aplikacje |
| **Parallel** | Multi-threaded, max throughput | Batch processing |
| **G1** | Balans latency/throughput, default | Większość aplikacji |
| **ZGC** | Ultra-low latency (<1ms pause) | Real-time systems |
| **Shenandoah** | Low latency, concurrent | Similar to ZGC |

### Memory Leaks w Javie - tak, są możliwe!

GC usuwa tylko obiekty BEZ referencji. Jeśli trzymasz referencję - obiekt żyje.

**Typowe przyczyny:**
1. Static collections rosnące w nieskończoność
2. Listeners/callbacks niezarejestrowane
3. Cache bez limitu/TTL
4. ThreadLocal nie czyszczony
5. Niezamknięte resources (streams, connections)

---

## 5. String Pool

### Problem
Stringi są najczęściej używanym typem. Tworzenie nowego obiektu dla każdego "hello" = waste pamięci.

### Rozwiązanie: String Pool (Interning)

String literals są przechowywane w specjalnym obszarze Heap. Identyczne literały współdzielą jeden obiekt.

```java
String a = "hello";  // String Pool
String b = "hello";  // Ten sam obiekt z Pool!
String c = new String("hello");  // Nowy obiekt na Heap (force)

a == b;  // true (ta sama referencja)
a == c;  // false (różne referencje)
a.equals(c);  // true (ta sama wartość)
```

### Dlaczego Stringi są immutable?

1. **Security** - Stringi używane jako klucze, hasła, URL nie mogą być zmienione
2. **Thread safety** - Immutable = bezpieczny bez synchronizacji
3. **Caching** - hashCode() może być cached (HashMap performance)
4. **String Pool** - Możliwy tylko dla immutable objects

### StringBuilder vs String concatenation

```java
// ❌ Źle - tworzy nowy String w każdej iteracji
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // 1000 obiektów String!
}

// ✅ Dobrze - jeden mutowalny obiekt
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

---

## 6. Typy w Javie

### Primitives vs Objects

| Primitive | Wrapper | Rozmiar | Default |
|-----------|---------|---------|---------|
| byte | Byte | 8 bit | 0 |
| short | Short | 16 bit | 0 |
| int | Integer | 32 bit | 0 |
| long | Long | 64 bit | 0L |
| float | Float | 32 bit | 0.0f |
| double | Double | 64 bit | 0.0d |
| boolean | Boolean | ~1 bit | false |
| char | Character | 16 bit | '\u0000' |

### Autoboxing / Unboxing

Java automatycznie konwertuje między primitive ↔ wrapper.

```java
Integer x = 10;        // Autoboxing: int → Integer
int y = x;             // Unboxing: Integer → int
List<Integer> list = new ArrayList<>();
list.add(5);           // Autoboxing
int z = list.get(0);   // Unboxing
```

**Uwaga na null:**
```java
Integer x = null;
int y = x;  // NullPointerException! Unboxing null
```

### Integer Cache

Java cachuje Integer od -128 do 127.

```java
Integer a = 100;
Integer b = 100;
a == b;  // true (cached)

Integer c = 200;
Integer d = 200;
c == d;  // false (new objects)
c.equals(d);  // true
```

**Wniosek:** Zawsze używaj `.equals()` dla obiektów!

---

## 7. Wyjątki (Exceptions)

### Hierarchia

```
Throwable
├── Error (nie łap - problemy JVM)
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception
    ├── RuntimeException (unchecked)
    │   ├── NullPointerException
    │   ├── IllegalArgumentException
    │   ├── IndexOutOfBoundsException
    │   └── ...
    └── Checked Exceptions
        ├── IOException
        ├── SQLException
        └── ...
```

### Checked vs Unchecked

**Checked (kompilator wymusza obsługę):**
- Dziedziczą z Exception (ale nie RuntimeException)
- Musisz `try-catch` lub `throws`
- Reprezentują recoverable errors
- Przykłady: IOException, SQLException

**Unchecked (kompilator nie wymusza):**
- Dziedziczą z RuntimeException
- Reprezentują programming errors
- Przykłady: NullPointerException, IllegalArgumentException

### Kiedy które?

```
Czy wywołujący może coś z tym zrobić?
├── TAK → Checked Exception (wymuś obsługę)
│   Przykład: plik nie istnieje → użytkownik może wybrać inny
└── NIE → Unchecked Exception (bug w kodzie)
    Przykład: null argument → napraw kod
```

### Try-with-resources

Automatyczne zamykanie zasobów (Closeable/AutoCloseable).

```java
// ❌ Stary sposób
FileReader fr = null;
try {
    fr = new FileReader("file.txt");
    // use fr
} finally {
    if (fr != null) fr.close();  // I co jeśli close() rzuci exception?
}

// ✅ Try-with-resources
try (FileReader fr = new FileReader("file.txt")) {
    // use fr
}  // Automatycznie zamknięte, nawet przy exception
```

---

## Podsumowanie - kluczowe wnioski

1. **JVM** - bytecode → interpreter → JIT dla hot code
2. **Pamięć** - Stack (lokalne, szybkie) vs Heap (obiekty, GC)
3. **Pass by value** - zawsze, ale dla obiektów to kopia referencji
4. **GC** - generational, usuwa nieosiągalne obiekty
5. **Strings** - immutable, pool dla literałów, StringBuilder dla konkatenacji
6. **equals() vs ==** - zawsze equals() dla obiektów
7. **Exceptions** - checked (wymuś obsługę) vs unchecked (bug)
