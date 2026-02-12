# Moduł 16: Interview Prep


## Cel

Przygotować się na rozmowę rekrutacyjną - teoria, coding, behavioral.

---

## Tematy do opanowania

### 1. Java Core Questions

#### equals() vs ==

- [ ] `==` porównuje referencje
- [ ] `equals()` porównuje wartości
- [ ] String Pool - kiedy == działa dla Stringów
- [ ] Kontrakt equals() i hashCode()

#### HashMap Internals

- [ ] Struktura: array of buckets
- [ ] hashCode() → bucket index
- [ ] equals() → find in bucket
- [ ] Load factor, rehashing
- [ ] Dlaczego immutable keys

#### Checked vs Unchecked Exceptions

- [ ] Checked: IOException, SQLException (kompilator wymusza)
- [ ] Unchecked: RuntimeException (programming errors)
- [ ] Kiedy które używać

#### Generics - Type Erasure

- [ ] Generic info usunięte w runtime
- [ ] Nie możesz: `new T()`, `instanceof List<String>`
- [ ] Wildcards: `<? extends T>`, `<? super T>`

#### String Immutability

- [ ] Dlaczego String jest immutable
- [ ] String Pool
- [ ] StringBuilder dla konkatenacji

### 2. Spring Interview Questions

#### Bean Lifecycle

- [ ] Instantiation → Populate properties → Aware callbacks
- [ ] @PostConstruct → InitializingBean → custom init
- [ ] @PreDestroy → DisposableBean → custom destroy

#### @Transactional Propagation

- [ ] REQUIRED (default)
- [ ] REQUIRES_NEW
- [ ] NESTED
- [ ] Kiedy które

#### Why @Transactional on private doesn't work?

- [ ] AOP Proxy pattern
- [ ] Proxy nie widzi private metod
- [ ] Self-invocation problem

#### Circular Dependencies

- [ ] Przyczyny
- [ ] Rozwiązania: refaktor, @Lazy, setter injection

#### @Component vs @Bean

- [ ] @Component: na klasie, auto-detected
- [ ] @Bean: na metodzie w @Configuration, explicit

### 3. System Design Basics

#### Load Balancing

- [ ] Round Robin, Least Connections, IP Hash
- [ ] L4 vs L7

#### Caching Strategies

- [ ] Cache-Aside
- [ ] Read-Through, Write-Through
- [ ] Cache invalidation

#### Database Scaling

- [ ] Vertical scaling
- [ ] Read replicas
- [ ] Sharding

#### CAP Theorem

- [ ] Consistency, Availability, Partition tolerance
- [ ] Możesz mieć max 2 z 3
- [ ] CP vs AP systems

#### Message Queues

- [ ] Kiedy używać (async, decoupling, peak handling)
- [ ] RabbitMQ, Kafka, SQS

#### Microservices vs Monolith

- [ ] Trade-offs
- [ ] Kiedy microservices (skalowanie, niezależność)
- [ ] Kiedy monolith (prostota, start)

### 4. Coding Challenges

#### LeetCode Topics (Easy/Medium)

- [ ] Arrays & Strings (Two Pointers, Sliding Window)
- [ ] HashMaps (frequency counting, Two Sum)
- [ ] Linked Lists (reverse, detect cycle)
- [ ] Trees (traversal, BFS/DFS)
- [ ] Binary Search

#### Przykładowe zadania

- [ ] Two Sum
- [ ] Valid Parentheses
- [ ] Merge Sorted Arrays
- [ ] Reverse Linked List
- [ ] Binary Tree Level Order Traversal

#### Live Coding Tips

- [ ] Clarify requirements - pytaj o edge cases
- [ ] Think out loud - mów co robisz
- [ ] Start with brute force - potem optymalizuj
- [ ] Write tests first (jeśli czas)
- [ ] Handle edge cases (null, empty, single)
- [ ] Analyze complexity (Time & Space)

### 5. Behavioral Questions (STAR)

#### STAR Method

- [ ] **S**ituation - kontekst
- [ ] **T**ask - co miałeś zrobić
- [ ] **A**ction - co zrobiłeś (TY, nie "my")
- [ ] **R**esult - efekt, liczby jeśli możliwe

#### Typowe pytania

- [ ] "Tell me about a challenging project"
- [ ] "Tell me about a time you disagreed with a teammate"
- [ ] "How do you handle tight deadlines"
- [ ] "Describe a bug you fixed"
- [ ] "Tell me about a time you failed"

#### Przygotuj historie na:

- [ ] Trudny problem techniczny
- [ ] Konflikt w zespole
- [ ] Deadline pressure
- [ ] Nauka nowej technologii
- [ ] Błąd i jak go naprawiłeś

### 6. Questions to Ask Interviewer

- [ ] "Jak wygląda typowy dzień developera?"
- [ ] "Jaki jest proces code review?"
- [ ] "Jak wygląda tech stack i czy planujecie zmiany?"
- [ ] "Jak wygląda onboarding?"
- [ ] "Jakie są biggest challenges dla zespołu?"
- [ ] "Jak mierzycie sukces w tej roli?"

---

## Powiązana teoria

- `docs/theory/09-interview-best-practices.md` → Cały plik
- `docs/theory/01-java-fundamentals.md` → Java Core
- `docs/theory/04-spring-framework.md` → Spring

---

## Flashcards - Quick Review

### Java

```
Q: equals() vs ==?
A: == referencje, equals() wartości

Q: Dlaczego String immutable?
A: Security, thread-safety, String Pool, caching hashCode

Q: Checked vs Unchecked?
A: Checked = kompilator wymusza, Unchecked = RuntimeException
```

### Spring

```
Q: Dlaczego @Transactional na private nie działa?
A: AOP Proxy nie widzi private metod

Q: REQUIRED vs REQUIRES_NEW?
A: REQUIRED używa istniejącej, REQUIRES_NEW zawsze nowa

Q: Constructor vs Field Injection?
A: Constructor - testowalne, wymusza deps, final
```

### System Design

```
Q: CAP Theorem?
A: Consistency, Availability, Partition tolerance - max 2 z 3

Q: Kiedy microservices?
A: Niezależne skalowanie, deployment, różne technologie

Q: Cache invalidation strategies?
A: TTL, manual eviction, event-driven
```

---

## Plan przygotowania (2-4 tygodnie)

### Tydzień 1-2: Teoria

- [ ] Przejrzyj theory docs (1-2 dziennie)
- [ ] Zrób notatki z kluczowych konceptów
- [ ] Odpowiedz na Java/Spring questions

### Tydzień 2-3: Coding

- [ ] LeetCode Easy: 10-15 zadań
- [ ] LeetCode Medium: 5-10 zadań
- [ ] Focus: Arrays, HashMaps, Strings

### Tydzień 3-4: Behavioral + Mock

- [ ] Przygotuj STAR stories (5-7 historii)
- [ ] Mock interview z kimś
- [ ] Przygotuj pytania do rekrutera

---

## Ćwiczenia

1. Odpowiedz na wszystkie Java Core questions (zapisz)
2. Odpowiedz na Spring questions (zapisz)
3. Rozwiąż 5 LeetCode Easy
4. Przygotuj 5 STAR stories
5. Przygotuj 5 pytań do rekrutera
6. Przeprowadź mock interview (z przyjacielem lub nagraj się)

---

## Sprawdzian gotowości

- [ ] Znam odpowiedzi na Java Core questions
- [ ] Znam odpowiedzi na Spring questions
- [ ] Potrafię narysować basic system design
- [ ] Rozwiązuję LeetCode Easy bez problemu
- [ ] Mam przygotowane STAR stories
- [ ] Mam pytania do rekrutera
