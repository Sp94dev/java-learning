# Lekcja 05: Garbage Collection

> Cykl życia obiektu, Generacje, jak unikać Memory Leaks.

## Koncept

### Po co Garbage Collector?

W C/C++ programista **ręcznie** zarządza pamięcią (`malloc`/`free`).
W Javie robi to **Garbage Collector** (GC) — automatycznie zwalnia pamięć obiektów, które nie są już potrzebne.

**Analogia:** Wyobraź sobie biuro. GC to sprzątaczka, która automatycznie wyrzuca
kartki, których nikt nie trzyma. Nie musisz pamiętać żeby wyrzucać — ale musisz
uważać, żeby przypadkiem nie trzymać kartki, która Ci nie potrzebna (→ memory leak).

### Reachability — jak GC decyduje co usunąć?

Obiekt jest **reachable** (osiągalny) jeśli istnieje ścieżka referencji od **GC Root** do tego obiektu.

**GC Roots** to:

- Zmienne lokalne na Stack (aktywne wątki)
- Statyczne pola klas
- Aktywne wątki
- Referencje JNI

```java
public static void main(String[] args) {
    Instrument a = new Instrument("AAPL", "Apple");  // reachable (a → obiekt)
    Instrument b = new Instrument("MSFT", "Microsoft");

    b = null;  // obiekt Microsoft jest teraz UNREACHABLE → GC może go usunąć
    a = b;     // obiekt Apple też jest teraz UNREACHABLE → GC może go usunąć
}
```

```
Przed b = null:
  Stack          Heap
  a ──────→ [AAPL, Apple]     ✅ reachable
  b ──────→ [MSFT, Microsoft] ✅ reachable

Po b = null; a = b:
  Stack          Heap
  a = null       [AAPL, Apple]     ❌ unreachable → GC
  b = null       [MSFT, Microsoft] ❌ unreachable → GC
```

### Generational GC — hipoteza generacyjna

**Obserwacja:** Większość obiektów żyje BARDZO krótko (są tymczasowe).
Dlatego JVM dzieli Heap na "generacje":

```
┌──────────────────────────────────────────────────────────┐
│                         HEAP                             │
├──────────────────────────────┬───────────────────────────┤
│       Young Generation       │     Old Generation        │
│                              │     (Tenured)             │
│  ┌────────┬───────┬───────┐  │                           │
│  │  Eden  │ S0    │ S1    │  │  Obiekty które przeżyły   │
│  │        │(surv.)│(surv.)│  │  wiele cykli GC           │
│  │ nowe   │       │       │  │                           │
│  │ obiekty│       │       │  │                           │
│  └────────┴───────┴───────┘  │                           │
├──────────────────────────────┴───────────────────────────┤
│                     Metaspace                            │
│            (metadane klas, poza Heap!)                    │
└──────────────────────────────────────────────────────────┘
```

### Cykl życia obiektu

```
1. new Instrument()  →  trafia do EDEN
                              ↓
2. Eden pełny        →  MINOR GC (szybki, tylko Young Gen)
                              ↓
3. Obiekt przeżył?   →  TAK → przeniesiony do Survivor (S0 lub S1)
                         NIE → usunięty
                              ↓
4. Przeżył N cykli?  →  TAK → przeniesiony do OLD GENERATION
                         NIE → dalej w Survivor
                              ↓
5. Old Gen pełny     →  MAJOR GC / FULL GC (wolny, cały Heap)
```

### Minor GC vs Major GC

| Typ          | Zakres                             | Szybkość      | Kiedy         |
| ------------ | ---------------------------------- | ------------- | ------------- |
| **Minor GC** | Tylko Young Gen (Eden + Survivors) | Szybki (ms)   | Eden pełny    |
| **Major GC** | Old Generation                     | Wolniejszy    | Old Gen pełny |
| **Full GC**  | Cały Heap + Metaspace              | Najwolniejszy | Brak pamięci  |

**Stop-the-World (STW):** Podczas GC aplikacja jest **zatrzymana** (pauzowana).
Nowoczesne algorytmy minimalizują czas STW.

### Algorytmy GC

| Algorytm                      | Charakterystyka                         | Kiedy wybrać                   |
| ----------------------------- | --------------------------------------- | ------------------------------ |
| **G1 GC** (default od Java 9) | Dzieli Heap na regiony, balansuje pauzy | Domyślny, dobry dla większości |
| **ZGC**                       | Pauzy < 1ms, nawet na TB Heap           | Niska latencja, duży Heap      |
| **Shenandoah**                | Podobny do ZGC, konkurencyjny           | Alternatywa dla ZGC            |
| **Serial GC**                 | Jeden wątek, prosty                     | Małe aplikacje, kontenery      |

```bash
# Uruchomienie z wybranym GC
java -XX:+UseG1GC MyApp           # G1 (default)
java -XX:+UseZGC MyApp            # ZGC
java -XX:+UseShenandoahGC MyApp   # Shenandoah
```

### ⚠️ Memory Leaks w Javie — tak, są możliwe!

GC usuwa **unreachable** obiekty. Ale jeśli **trzymasz referencję** do obiektu,
którego nie potrzebujesz — GC go nie usunie. To jest memory leak.

#### Leak 1: Statyczna kolekcja rosnąca bez końca

```java
// ❌ MEMORY LEAK
public class EventCache {
    private static final List<Event> cache = new ArrayList<>();  // static = GC Root!

    public void addEvent(Event e) {
        cache.add(e);  // lista rośnie, NIGDY nie jest czyszczona
    }
    // cache jest static → GC Root → wszystkie eventy są reachable → NIGDY nie usunięte
}
```

```java
// ✅ ROZWIĄZANIE: ogranicz rozmiar lub czyść
public class EventCache {
    private static final List<Event> cache = new ArrayList<>();
    private static final int MAX_SIZE = 1000;

    public void addEvent(Event e) {
        if (cache.size() >= MAX_SIZE) {
            cache.removeFirst();  // usuń najstarszy
        }
        cache.add(e);
    }
}
```

#### Leak 2: Niezamknięte zasoby (pliki, połączenia, streamy)

```java
// ❌ MEMORY LEAK — strumień nigdy nie zamknięty
public void readFile(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();
    // reader.close() ← BRAKUJE! Deskryptor pliku trzyma pamięć
}
```

```java
// ✅ ROZWIĄZANIE: try-with-resources (Java 7+)
public void readFile(String path) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
        String line = reader.readLine();
    }  // ← automatycznie wywołuje reader.close()!
}
```

**Analogia Angular:** `try-with-resources` jest jak `takeUntilDestroyed()` / `unsubscribe()`
dla Observable w Angular — automatyczne sprzątanie po zakończeniu.

#### Leak 3: Listener / callback nie odrejestrowany

```java
// ❌ MEMORY LEAK
public class PriceMonitor {
    public PriceMonitor(EventBus bus) {
        bus.register(this);  // rejestruje listener
        // nigdy bus.unregister(this) → PriceMonitor nie może być GC'd
    }
}
```

**Analogia Angular:** To dokładnie jak zapomniany `subscribe()` bez `unsubscribe()`.

### try-with-resources — pattern obowiązkowy

Każdy obiekt implementujący `AutoCloseable` może być użyty w try-with-resources:

```java
// Wiele zasobów — wszystkie zamknięte automatycznie (w odwrotnej kolejności)
try (
    Connection conn = DriverManager.getConnection(url);
    PreparedStatement stmt = conn.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery()
) {
    while (rs.next()) {
        // przetwarzanie
    }
}  // rs.close() → stmt.close() → conn.close() (automatycznie!)
```

## Ćwiczenie

**Zadanie 1:** Zaobserwuj GC w akcji:

```java
public class GcDemo {
    public static void main(String[] args) {
        System.out.println("Tworzę 1 milion obiektów...");
        for (int i = 0; i < 1_000_000; i++) {
            new byte[1024];  // 1KB — krótko żyjące obiekty
        }
        System.out.println("Gotowe.");
    }
}
```

Uruchom z logowaniem GC:

```bash
java -Xlog:gc* GcDemo
```

Zaobserwuj:

- Ile razy wykonał się Minor GC?
- Czy był Major/Full GC?
- Jakie są czasy pauz (STW)?

**Zadanie 2:** Stwórz `OutOfMemoryError`:

```java
public class OomDemo {
    public static void main(String[] args) {
        List<byte[]> leak = new ArrayList<>();
        while (true) {
            leak.add(new byte[1024 * 1024]);  // 1MB za każdym razem
            System.out.println("Rozmiar listy: " + leak.size() + " MB");
        }
    }
}
```

Uruchom z małym Heap:

```bash
java -Xmx64m OomDemo
```

**Zadanie 3:** Przeróbka na try-with-resources — weź dowolny kod z `reader.close()`
i zamień go na try-with-resources.

## Checklist

- [ ] Wiem jak GC decyduje co usunąć (reachability od GC Roots)
- [ ] Rozumiem podział Heap na generacje (Eden, Survivor, Old)
- [ ] Znam różnicę Minor GC vs Major GC vs Full GC
- [ ] Wiem co to G1 i ZGC i kiedy który wybrać
- [ ] Potrafię wskazać 3 typowe źródła memory leak w Javie
- [ ] Stosuję try-with-resources dla każdego `AutoCloseable`
