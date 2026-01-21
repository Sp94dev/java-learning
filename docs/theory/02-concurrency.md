# TEORIA: Współbieżność (Concurrency)

---

## 1. Process vs Thread

### Process (Proces)
- Izolowana jednostka wykonania
- Własna przestrzeń pamięci
- Własne zasoby (file handles, sockets)
- Komunikacja między procesami = kosztowna (IPC)
- Crash jednego procesu nie wpływa na inne

### Thread (Wątek)
- Lekka jednostka wykonania wewnątrz procesu
- **Współdzieli pamięć** z innymi wątkami tego procesu
- Własny stack, ale wspólny heap
- Komunikacja = szybka (shared memory)
- Crash wątku może zabić cały proces

```
┌─────────────────────────────────────────────┐
│                  PROCESS                    │
│  ┌─────────────────────────────────────┐   │
│  │              HEAP (shared)          │   │
│  │   [Object A]  [Object B]  [Object C]│   │
│  └─────────────────────────────────────┘   │
│                                             │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐    │
│  │ Thread 1│  │ Thread 2│  │ Thread 3│    │
│  │ [Stack] │  │ [Stack] │  │ [Stack] │    │
│  │ [PC]    │  │ [PC]    │  │ [PC]    │    │
│  └─────────┘  └─────────┘  └─────────┘    │
└─────────────────────────────────────────────┘
```

---

## 2. Dlaczego współbieżność jest trudna?

### Problem: Shared Mutable State

Gdy wiele wątków modyfikuje ten sam obiekt bez synchronizacji → **race condition**.

**Przykład: Counter**
```
Thread 1: read count (0)
Thread 2: read count (0)
Thread 1: increment (0 → 1)
Thread 2: increment (0 → 1)  // Powinno być 2!
Thread 1: write count (1)
Thread 2: write count (1)

Wynik: 1 zamiast 2 (lost update)
```

### Trzy fundamentalne problemy

1. **Atomicity** - operacja musi być niepodzielna
   - `count++` to 3 operacje: read → increment → write
   - Między nimi może wejść inny wątek

2. **Visibility** - zmiany muszą być widoczne dla innych wątków
   - CPU cache może trzymać starą wartość
   - Bez synchronizacji wątek może nie zobaczyć zmian

3. **Ordering** - instrukcje mogą być przestawione
   - Kompilator i CPU optymalizują kolejność
   - Może to złamać założenia programu

---

## 3. Java Memory Model (JMM)

### Po co JMM?
Definiuje **kiedy** zmiany zrobione przez jeden wątek są **widoczne** dla innych.

### Happens-Before Relationship

Jeśli akcja A "happens-before" akcji B, to:
- A jest widoczna dla B
- A wykonuje się przed B (logicznie)

**Gwarancje happens-before:**
1. Unlock monitora → Lock tego samego monitora
2. Zapis do volatile → Odczyt tego volatile
3. Thread.start() → Pierwsza instrukcja nowego wątku
4. Ostatnia instrukcja wątku → Thread.join() zwraca
5. Instrukcje w tym samym wątku (program order)

### Volatile

Słowo kluczowe gwarantujące visibility.

```java
private volatile boolean running = true;

// Thread 1
public void stop() {
    running = false;  // Zapis widoczny natychmiast
}

// Thread 2
public void run() {
    while (running) {  // Zawsze świeża wartość
        doWork();
    }
}
```

**Co volatile gwarantuje:**
- Visibility - zapis widoczny dla wszystkich wątków
- Ordering - brak reorderingu przed/po volatile

**Co volatile NIE gwarantuje:**
- Atomicity - `count++` nadal nie jest atomowe!

---

## 4. Synchronization

### synchronized - mutual exclusion

Tylko jeden wątek może wykonywać synchronized blok na danym obiekcie.

```java
public class Counter {
    private int count = 0;
    
    public synchronized void increment() {  // Lock na 'this'
        count++;  // Teraz atomowe (w kontekście tego locka)
    }
    
    public void incrementBlock() {
        synchronized(this) {  // Explicit lock object
            count++;
        }
    }
}
```

### Monitor concept

Każdy obiekt w Javie ma wbudowany "monitor" (lock).

```
Object
├── Header (mark word: lock state, GC info, hashCode)
├── Class pointer
└── Instance data

Lock states:
- Unlocked
- Biased (optymalizacja dla single-thread)
- Lightweight locked
- Heavyweight locked (OS mutex)
```

### Reentrant locking

Ten sam wątek może wielokrotnie wejść w synchronized na tym samym obiekcie.

```java
public synchronized void methodA() {
    methodB();  // OK - ten sam wątek, ten sam lock
}

public synchronized void methodB() {
    // ...
}
```

### Deadlock

Dwa wątki czekają na siebie nawzajem - żaden nie może kontynuować.

```
Thread 1: lock(A), trying to lock(B)
Thread 2: lock(B), trying to lock(A)

     Thread 1          Thread 2
         │                 │
         ▼                 ▼
    ┌────────┐        ┌────────┐
    │ Lock A │◄───────│ Want A │
    │        │        │        │
    │ Want B │───────►│ Lock B │
    └────────┘        └────────┘
         │                 │
         └───── DEADLOCK ──┘
```

**Zapobieganie:**
1. Zawsze lockuj w tej samej kolejności
2. Używaj timeout przy próbie locka
3. Unikaj nested locks
4. Używaj higher-level abstrakcji (java.util.concurrent)

---

## 5. java.util.concurrent

### Atomic classes

Lock-free, thread-safe operacje używające CPU CAS (Compare-And-Swap).

**AtomicInteger, AtomicLong, AtomicBoolean, AtomicReference**

```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();  // Atomowe ++counter
counter.getAndIncrement();  // Atomowe counter++
counter.compareAndSet(expected, newValue);  // CAS
```

**Jak działa CAS?**
```
1. Odczytaj wartość (old = 5)
2. Oblicz nową (new = 6)
3. Atomowo: if (current == old) set(new) else retry
```
Bez locka! CPU wspiera to natywnie.

### Locks (java.util.concurrent.locks)

Bardziej elastyczne niż synchronized.

**ReentrantLock:**
```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // critical section
} finally {
    lock.unlock();  // ZAWSZE w finally!
}

// Lub z timeout
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        // ...
    } finally {
        lock.unlock();
    }
} else {
    // nie udało się uzyskać locka
}
```

**ReadWriteLock:**
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();

// Wiele wątków może czytać jednocześnie
rwLock.readLock().lock();
try {
    return data;
} finally {
    rwLock.readLock().unlock();
}

// Tylko jeden może pisać (exclusive)
rwLock.writeLock().lock();
try {
    data = newValue;
} finally {
    rwLock.writeLock().unlock();
}
```

### ExecutorService

Abstrakcja nad wątkami - nie twórz Thread ręcznie!

```java
// Pool z fixed liczbą wątków
ExecutorService executor = Executors.newFixedThreadPool(4);

// Pool który rośnie w miarę potrzeb
ExecutorService executor = Executors.newCachedThreadPool();

// Single thread (kolejka zadań)
ExecutorService executor = Executors.newSingleThreadExecutor();

// Submit task
Future<String> future = executor.submit(() -> {
    return computeResult();
});

// Get result (blocking)
String result = future.get();
String result = future.get(5, TimeUnit.SECONDS);  // z timeout

// Shutdown
executor.shutdown();  // Dokończ bieżące, nie przyjmuj nowych
executor.shutdownNow();  // Przerwij wszystko
```

### CompletableFuture

Asynchroniczne operacje z kompozycją (jak Promise w JS).

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchData())           // Async, zwraca wartość
    .thenApply(data -> parse(data))           // Transform (map)
    .thenApply(parsed -> process(parsed))     // Chain
    .exceptionally(ex -> handleError(ex));    // Error handling

// Combine multiple
CompletableFuture<Void> all = CompletableFuture.allOf(
    fetchUser(),
    fetchOrders(),
    fetchPayments()
);

CompletableFuture<Object> any = CompletableFuture.anyOf(
    primaryService(),
    fallbackService()
);
```

**Analogia Angular/JS:**
| Java | JavaScript |
|------|------------|
| CompletableFuture | Promise |
| supplyAsync | new Promise((resolve) => ...) |
| thenApply | .then() |
| exceptionally | .catch() |
| allOf | Promise.all() |
| anyOf | Promise.race() |

---

## 6. Virtual Threads (Java 21+)

### Problem z Platform Threads
Każdy Platform Thread = OS thread (~1MB stack, expensive).
Przy 10,000 równoczesnych połączeń = 10GB RAM tylko na stacki!

### Virtual Threads - rozwiązanie
Lekkie wątki zarządzane przez JVM, nie OS.

```
Platform Thread (OS)      Virtual Threads (JVM)
┌─────────────────┐      ┌─────────────────────────┐
│ ~1MB stack      │      │ ~KB stack               │
│ OS scheduled    │      │ JVM scheduled           │
│ Max ~thousands  │      │ Max ~millions           │
└─────────────────┘      └─────────────────────────┘
```

### Jak działają?

Virtual threads są "mountowane" na platform threads (carrier threads).

```
Virtual Thread 1 ──┐
Virtual Thread 2 ──┼──► Carrier Thread (Platform) ──► CPU Core
Virtual Thread 3 ──┘

Gdy VT blokuje się na I/O:
1. JVM "odmontowuje" VT z carrier
2. Carrier bierze inny VT
3. Gdy I/O gotowe - VT wraca do kolejki
```

### Kiedy używać?

**Virtual Threads ✅:**
- I/O bound tasks (HTTP, DB, file)
- Wiele równoczesnych operacji
- Request-per-thread model

**Platform Threads ✅:**
- CPU bound tasks (obliczenia)
- Kod z synchronized na długich operacjach
- Native code/JNI

### Użycie

```java
// Pojedynczy virtual thread
Thread.startVirtualThread(() -> doWork());

// Executor z virtual threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return processRequest();
        });
    }
}
// 100,000 "wątków" - bez problemu!
```

---

## 7. Thread Safety Strategies

### 1. Immutability (najlepsze)
Obiekty których stan nie może się zmienić są automatycznie thread-safe.

```java
public record User(String name, String email) {}
// Brak setterów, pola final, wartości kopiowane
```

### 2. Thread Confinement
Każdy wątek ma swoje dane - brak współdzielenia.

```java
ThreadLocal<User> currentUser = new ThreadLocal<>();
currentUser.set(user);  // Widoczne tylko w tym wątku
User user = currentUser.get();
```

### 3. Synchronization
Kontrolowany dostęp do shared state (synchronized, locks).

### 4. Concurrent Collections
Thread-safe kolekcje bez explicit synchronization.

| Unsafe | Thread-safe |
|--------|-------------|
| HashMap | ConcurrentHashMap |
| ArrayList | CopyOnWriteArrayList |
| TreeMap | ConcurrentSkipListMap |
| LinkedList | ConcurrentLinkedQueue |

**ConcurrentHashMap** - lock striping (locki per segment, nie cała mapa)

---

## 8. Common Pitfalls

### Race Condition
```java
// Check-then-act (nie atomowe!)
if (!map.containsKey(key)) {  // Thread 2 może wejść tutaj
    map.put(key, value);       // po tym CHECKU!
}

// ✅ Użyj atomowej operacji
map.putIfAbsent(key, value);
```

### Double-Checked Locking (broken without volatile)
```java
// ❌ BROKEN (przed Java 5)
if (instance == null) {
    synchronized(lock) {
        if (instance == null) {
            instance = new Singleton();  // Może być częściowo skonstruowany!
        }
    }
}

// ✅ Z volatile (Java 5+)
private static volatile Singleton instance;
```

### Synchronizing on wrong object
```java
// ❌ WRONG - każdy wątek lockuje na INNYM obiekcie!
synchronized(new Object()) {
    count++;
}

// ❌ WRONG - String literals są shared (String Pool)
synchronized("lock") {  // Inna klasa może lockować na tym samym!
    count++;
}

// ✅ CORRECT
private final Object lock = new Object();
synchronized(lock) {
    count++;
}
```

---

## Podsumowanie

| Koncept | Kiedy używać |
|---------|--------------|
| **synchronized** | Prosty mutual exclusion |
| **volatile** | Tylko visibility (jeden writer) |
| **Atomic classes** | Pojedyncze zmienne, lock-free |
| **ReentrantLock** | Potrzebujesz tryLock, timeout |
| **ReadWriteLock** | Wiele reads, mało writes |
| **ConcurrentHashMap** | Thread-safe mapa |
| **ExecutorService** | Pool wątków |
| **CompletableFuture** | Async composition |
| **Virtual Threads** | Masowe I/O operations |
| **Immutability** | Najlepsza strategia! |
