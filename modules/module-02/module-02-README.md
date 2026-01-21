# Moduł 02: Java Internals

## Cel
Zrozumieć jak Java działa pod spodem - JVM, pamięć, Garbage Collection, wątki.

---

## Tematy do opanowania

### 1. JVM Architecture
- [ ] Co to jest JVM (Java Virtual Machine)
- [ ] Proces wykonania: .java → javac → .class → JVM → native
- [ ] Class Loader - hierarchia (Bootstrap, Extension, Application)
- [ ] "Write Once, Run Anywhere" - dlaczego działa

### 2. JIT Compilation
- [ ] Interpreter vs JIT Compiler
- [ ] Hot spots - co to jest
- [ ] Dlaczego Java przyspiesza w runtime
- [ ] Warm-up time

### 3. Memory Model
- [ ] Stack vs Heap - różnice
- [ ] Co trafia na Stack (primitives, references)
- [ ] Co trafia na Heap (objects, arrays)
- [ ] Stack frame - co zawiera

### 4. Pass by Value
- [ ] Java ZAWSZE pass by value
- [ ] Dla primitives: kopia wartości
- [ ] Dla obiektów: kopia referencji (nie obiektu!)
- [ ] Dlaczego reassign w metodzie nie zmienia oryginału

### 5. String Pool
- [ ] Co to jest String Pool
- [ ] Literały vs `new String()`
- [ ] `==` vs `.equals()` dla Stringów
- [ ] Dlaczego String jest immutable
- [ ] StringBuilder - kiedy używać

### 6. Garbage Collection
- [ ] Po co GC (automatyczne zarządzanie pamięcią)
- [ ] Reachability - jak GC decyduje co usunąć
- [ ] Generational GC (Young, Old, Metaspace)
- [ ] Minor GC vs Major GC
- [ ] GC algorithms: G1 (default), ZGC

### 7. Memory Leaks w Javie
- [ ] Tak, są możliwe!
- [ ] Static collections rosnące w nieskończoność
- [ ] Niezamknięte resources
- [ ] Try-with-resources

### 8. Primitive vs Wrapper Types
- [ ] Lista primitives (int, long, boolean, etc.)
- [ ] Wrapper classes (Integer, Long, Boolean)
- [ ] Autoboxing / Unboxing
- [ ] Integer cache (-128 do 127)
- [ ] `==` vs `.equals()` dla wrapperów

### 9. Exceptions
- [ ] Hierarchia: Throwable → Error / Exception
- [ ] Checked vs Unchecked exceptions
- [ ] Kiedy które używać
- [ ] Try-catch-finally
- [ ] Try-with-resources

---

## Powiązana teoria
- `docs/theory/01-java-fundamentals.md` → Cały plik
- `docs/theory/02-concurrency.md` → Wstęp do wątków

---

## Ćwiczenia praktyczne
1. Napisz kod pokazujący różnicę `==` vs `.equals()` dla Stringów
2. Udowodnij że Java jest pass-by-value (modyfikacja obiektu vs reassign)
3. Wywołaj GC i zaobserwuj logi (`-Xlog:gc*`)
4. Stwórz memory leak (static List bez czyszczenia)

---

## Sprawdzian gotowości
- [ ] Potrafię wytłumaczyć jak JVM wykonuje kod
- [ ] Wiem co jest na Stack, a co na Heap
- [ ] Rozumiem dlaczego `==` dla Stringów może być true lub false
- [ ] Wiem co robi Garbage Collector
- [ ] Rozumiem różnicę checked vs unchecked exceptions
