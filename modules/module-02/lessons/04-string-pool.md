# Lekcja 04: String Pool

> Immutability, `intern()`, StringBuilder vs Concatenation.

## Koncept

### String w Javie — nie taki prosty jak się wydaje

`String` jest **najczęściej używanym typem** w Javie i ma kilka unikalnych właściwości:

- Jest **immutable** (niezmienny po utworzeniu)
- Ma **String Pool** (cache stringów w pamięci)
- Ma specjalne traktowanie przez JVM

### String Pool — co to jest?

String Pool to specjalny obszar pamięci w Heap, gdzie JVM **przechowuje unikalne stringi literalne**.

```java
String a = "Hello";    // → JVM szuka "Hello" w Pool → nie ma → tworzy i dodaje
String b = "Hello";    // → JVM szuka "Hello" w Pool → JEST → zwraca tę samą referencję

String c = new String("Hello");  // → ZAWSZE tworzy nowy obiekt na Heap (poza Pool!)
```

```
                     Heap:
Stack:              ┌──────────────────────────────────┐
┌──────────┐        │        String Pool                │
│ a = ref ─┼───────→│   ┌─────────────────┐            │
│ b = ref ─┼───────→│   │  "Hello"        │            │
└──────────┘        │   └─────────────────┘            │
                    │                                   │
┌──────────┐        │   Poza Pool:                      │
│ c = ref ─┼───────→│   ┌─────────────────┐            │
└──────────┘        │   │  "Hello" (kopia) │            │
                    │   └─────────────────┘            │
                    └──────────────────────────────────┘
```

### `==` vs `.equals()` dla Stringów

```java
String a = "Java";
String b = "Java";
String c = new String("Java");

System.out.println(a == b);          // true  ← ten sam obiekt z Pool
System.out.println(a == c);          // false ← różne obiekty!
System.out.println(a.equals(c));     // true  ← porównanie zawartości ✅
```

| Operator    | Porównuje                     | Dla Stringów     |
| ----------- | ----------------------------- | ---------------- |
| `==`        | Referencje (adresy w pamięci) | ⚠️ Niebezpieczne |
| `.equals()` | Zawartość (wartość tekstu)    | ✅ Poprawne      |

**Reguła:** Dla Stringów ZAWSZE używaj `.equals()`. Zawsze.

### Dlaczego String jest immutable?

```java
String name = "Java";
name.toUpperCase();           // "JAVA" — ale name się NIE ZMIENIŁ!
System.out.println(name);     // "Java"

name = name.toUpperCase();    // Teraz name = "JAVA" (nowy String, nowa referencja)
```

**Powody immutability:**

1. **Bezpieczeństwo String Pool** — gdyby String był mutowalny, zmiana `a` zmieniłaby `b` (bo wskazują na ten sam obiekt)
2. **Thread safety** — immutable obiekty są bezpieczne wielowątkowo bez synchronizacji
3. **Bezpieczeństwo** — Stringi są używane w class loading, połączeniach DB, URL-ach. Mutacja mogłaby naruszyć bezpieczeństwo
4. **Cache hashCode** — `String` cachuje swój `hashCode()` bo się nie zmieni. To przyspiesza `HashMap`

**Analogia TS:** To jak `readonly` interface w TypeScript, ale wymuszone na poziomie JVM.

### `intern()` — ręczne dodanie do Pool

```java
String a = new String("Java");  // Heap (poza Pool)
String b = a.intern();          // → dodaje "Java" do Pool (lub zwraca istniejący)
String c = "Java";              // → bierze z Pool

System.out.println(a == c);     // false (a jest poza Pool)
System.out.println(b == c);     // true  (b i c z Pool)
```

W praktyce rzadko używasz `intern()` ręcznie — JVM robi to automatycznie dla literałów.

### StringBuilder — kiedy String nie wystarczy

**Problem z konkatenacją w pętli:**

```java
// ❌ ZŁE — tworzy N nowych Stringów!
String result = "";
for (int i = 0; i < 10_000; i++) {
    result += "item" + i + ", ";  // Za każdym razem: nowy String
}
// Wynik: ~10 000 pośrednich obiektów String na Heap 💀
```

**Rozwiązanie — StringBuilder:**

```java
// ✅ DOBRE — jeden mutowalny obiekt
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10_000; i++) {
    sb.append("item").append(i).append(", ");
}
String result = sb.toString();
// Wynik: jeden obiekt, wielokrotnie rozszerzany
```

| Podejście       | Obiekty w pamięci         | Wydajność   |
| --------------- | ------------------------- | ----------- |
| `+=` w pętli    | O(n) nowych Stringów      | 💀 Wolne    |
| `StringBuilder` | 1 obiekt                  | ⚡ Szybkie  |
| `String.join()` | Wewnętrznie StringBuilder | ✅ Czytelne |

### String w nowoczesnej Javie

```java
// Java 15+ — Text Blocks (wieloliniowe stringi)
String json = """
        {
            "ticker": "AAPL",
            "name": "Apple Inc."
        }
        """;

// Java 21+ — String Templates (interpolacja) — preview feature
// String msg = STR."Hello \{name}, your balance is \{balance}";

// Klasyczne formatowanie
String msg = String.format("Ticker: %s, Cena: %.2f", "AAPL", 150.50);
String msg2 = "Ticker: %s, Cena: %.2f".formatted("AAPL", 150.50);
```

**Analogia TS:** Text Blocks → template literals (`` `backticks` ``).
String Templates → `${interpolation}`. Java późno do tego doszła.

## Ćwiczenie

**Zadanie 1:** Udowodnij istnienie String Pool:

```java
public class StringPoolDemo {
    public static void main(String[] args) {
        String a = "Pool";
        String b = "Pool";
        String c = new String("Pool");
        String d = c.intern();

        // Wypisz wyniki == i .equals() dla każdej pary (a,b), (a,c), (a,d)
        // Wytłumacz DLACZEGO każdy wynik jest taki a nie inny
    }
}
```

**Zadanie 2:** Zmierz różnicę wydajności — `+=` vs `StringBuilder` dla 100 000 iteracji:

```java
// Wersja 1: String +=
long start1 = System.nanoTime();
String s = "";
for (int i = 0; i < 100_000; i++) { s += "a"; }
long time1 = System.nanoTime() - start1;

// Wersja 2: StringBuilder
long start2 = System.nanoTime();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 100_000; i++) { sb.append("a"); }
String result = sb.toString();
long time2 = System.nanoTime() - start2;

// Porównaj czasy — jaka jest różnica?
```

## Checklist

- [ ] Wiem czym jest String Pool i kiedy JVM z niego korzysta
- [ ] Rozumiem dlaczego `==` dla Stringów jest niebezpieczne
- [ ] Potrafię wytłumaczyć dlaczego String jest immutable (4 powody)
- [ ] Wiem kiedy używać `StringBuilder` zamiast `+= `
- [ ] Znam Text Blocks (`"""..."""`) z nowoczesnej Javy
- [ ] Wiem co robi `intern()` i kiedy mógłbym go potrzebować
