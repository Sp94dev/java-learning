# Lekcja 02: Memory Model

> Stack vs Heap. Gdzie żyją zmienne i obiekty. Primitives vs Wrappers.

## Koncept

### Stack vs Heap — fundamentalny podział

```
┌─────────────────────────────────────────────────────────┐
│                        JVM Memory                       │
├──────────────────────┬──────────────────────────────────┤
│       STACK          │            HEAP                  │
│   (per wątek)        │      (współdzielony)             │
│                      │                                  │
│  • zmienne lokalne   │  • obiekty (new ...)             │
│  • referencje        │  • tablice                       │
│  • argumenty metod   │  • Stringi                       │
│  • typy prymitywne   │  • instancje klas                │
│                      │                                  │
│  LIFO (stos)         │  zarządzany przez GC             │
│  szybki              │  wolniejszy, ale elastyczny      │
│  ustalony rozmiar    │  dynamiczny rozmiar              │
└──────────────────────┴──────────────────────────────────┘
```

**Analogia Angular/TS:** W JS/TS podział jest podobny (V8 ma Stack i Heap),
ale jest niewidoczny. W Javie **musisz** go rozumieć.

### Stack — szczegóły

Każdy **wątek** ma swój własny Stack. Na Stack trafiają **Stack Frames**.

**Stack Frame** = "ramka" tworzona przy każdym wywołaniu metody:

```java
public static void main(String[] args) {     // Frame 1
    int x = 10;                               // x na Stack
    String name = greet("Java");              // → tworzy Frame 2
}

public static String greet(String lang) {     // Frame 2
    String msg = "Hello " + lang;             // msg na Stack (referencja)
    return msg;                               // Frame 2 usunięty po return
}
```

```
Stack (wątek main):
┌─────────────────┐
│ Frame: greet()  │ ← aktualny (top)
│  lang = ref→    │
│  msg  = ref→    │
├─────────────────┤
│ Frame: main()   │
│  x    = 10      │
│  name = ref→    │
│  args = ref→    │
└─────────────────┘
```

**Kluczowe:**

- Frame jest tworzony przy **wejściu** do metody
- Frame jest usuwany przy **wyjściu** z metody (return / exception)
- Zbyt głęboka rekurencja → `StackOverflowError`

### Heap — szczegóły

Heap to **wspólna** pamięć dla wszystkich wątków. Tutaj żyją obiekty.

```java
Instrument inst = new Instrument("AAPL", "Apple");
//  ↑ referencja (Stack)         ↑ obiekt (Heap)
```

```
Stack:                    Heap:
┌──────────┐             ┌──────────────────────────┐
│ inst = ──┼──────────→  │ Instrument               │
└──────────┘             │  ticker = ref → "AAPL"   │
                         │  name   = ref → "Apple"  │
                         └──────────────────────────┘
```

**Obiekty na Heap żyją tak długo**, aż Garbage Collector je usunie (Lekcja 05).

### Co trafia gdzie — tabela

| Typ danych            | Stack             | Heap                    |
| --------------------- | ----------------- | ----------------------- |
| `int x = 5`           | ✅ wartość `5`    | ❌                      |
| `double d = 3.14`     | ✅ wartość `3.14` | ❌                      |
| `boolean b = true`    | ✅ wartość `true` | ❌                      |
| `String s = "hello"`  | ✅ referencja     | ✅ obiekt String        |
| `new Instrument(...)` | ✅ referencja     | ✅ obiekt Instrument    |
| `int[] arr = {1,2,3}` | ✅ referencja     | ✅ tablica (to obiekt!) |

**Złota reguła:**

- **Primitives** → wartość na Stack
- **Obiekty** → referencja na Stack, obiekt na Heap

### Primitive vs Wrapper Types

Java ma **dwa systemy typów** — to jest mylące na początku.

| Primitive | Wrapper     | Rozmiar | Domyślna wartość |
| --------- | ----------- | ------- | ---------------- |
| `byte`    | `Byte`      | 1B      | `0`              |
| `short`   | `Short`     | 2B      | `0`              |
| `int`     | `Integer`   | 4B      | `0`              |
| `long`    | `Long`      | 8B      | `0L`             |
| `float`   | `Float`     | 4B      | `0.0f`           |
| `double`  | `Double`    | 8B      | `0.0`            |
| `boolean` | `Boolean`   | ~1b     | `false`          |
| `char`    | `Character` | 2B      | `'\u0000'`       |

**Analogia TS:** W TypeScript `number` to zawsze obiekt (wrapper).
Java rozróżnia `int` (szybki, Stack) od `Integer` (obiekt, Heap).

### Autoboxing / Unboxing

Java automatycznie konwertuje między primitive a wrapper:

```java
// Autoboxing: int → Integer (automatyczne "opakowanie")
Integer a = 42;        // kompilator widzi: Integer.valueOf(42)

// Unboxing: Integer → int (automatyczne "rozpakowanie")
int b = a;             // kompilator widzi: a.intValue()

// UWAGA: NullPointerException!
Integer c = null;
int d = c;             // 💥 NullPointerException (unboxing null)
```

### ⚠️ Integer Cache — pułapka na rozmowie rekrutacyjnej

```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);    // true  ← ???

Integer c = 128;
Integer d = 128;
System.out.println(c == d);    // false ← ???

System.out.println(c.equals(d)); // true ← poprawne porównanie
```

**Dlaczego?**

- `Integer.valueOf()` **cachuje** wartości od **-128 do 127**
- Dla tych wartości: ten sam obiekt w pamięci → `==` zwraca `true`
- Powyżej 127: nowe obiekty → `==` porównuje referencje (różne!) → `false`

**Reguła:** Dla wrapperów ZAWSZE używaj `.equals()`, nigdy `==`.

### Kiedy używać primitive, kiedy wrapper?

| Użyj Primitive              | Użyj Wrapper                           |
| --------------------------- | -------------------------------------- |
| Zmienne lokalne, obliczenia | Generics (`List<Integer>`)             |
| Pola "zawsze mają wartość"  | Gdy wartość może być `null`            |
| Wydajność jest ważna        | Kolekcje Java (`Map<String, Integer>`) |

## Ćwiczenie

**Zadanie 1:** Wywołaj `StackOverflowError` — napisz metodę z nieskończoną rekurencją.
Zaobserwuj jak głęboko Java wchodzi (policz głębokość).

```java
public class StackOverflowDemo {
    static int depth = 0;

    public static void recursive() {
        depth++;
        recursive();
    }

    public static void main(String[] args) {
        try {
            recursive();
        } catch (StackOverflowError e) {
            System.out.println("Stack overflow na głębokości: " + depth);
        }
    }
}
```

**Zadanie 2:** Udowodnij Integer Cache — porównaj `==` i `.equals()` dla wartości 127 i 128.

**Zadanie 3:** Wykaż koszt autoboxingu — zmierz czas sumowania 10 milionów liczb
z `int` vs `Integer`:

```java
// Wersja 1: primitive
long sum1 = 0;
for (int i = 0; i < 10_000_000; i++) {
    sum1 += i;
}

// Wersja 2: wrapper (autoboxing)
Long sum2 = 0L;  // 💀 każda iteracja tworzy nowy Long
for (int i = 0; i < 10_000_000; i++) {
    sum2 += i;
}
```

## Checklist

- [ ] Wiem co trafia na Stack, a co na Heap
- [ ] Potrafię narysować Stack Frame dla wywołania metody
- [ ] Rozumiem różnicę między primitive a wrapper
- [ ] Wiem co to autoboxing/unboxing i jaki ma koszt
- [ ] Znam pułapkę Integer Cache i wiem dlaczego `==` dla Integer jest niebezpieczne
- [ ] Wiem kiedy użyć `int` a kiedy `Integer`
