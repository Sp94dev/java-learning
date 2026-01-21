# TEORIA: OOP i SOLID

---

## 1. Cztery filary OOP

### Enkapsulacja (Encapsulation)

**Idea:** Ukryj wewnętrzną implementację, eksponuj tylko API.

**Po co?**
- Kontrola dostępu do danych (walidacja w setterze)
- Możliwość zmiany implementacji bez wpływu na klientów
- Redukcja coupling (klient zna tylko interfejs)

**Jak w Javie:**
- `private` pola
- `public` metody dostępowe
- Pakiety jako granice modułów

**Przykład mentalny:**
Samochód enkapsuluje silnik. Kierowca używa pedału gazu (API), nie manipuluje bezpośrednio zaworami (implementacja). Producent może wymienić silnik na elektryczny - kierowca dalej używa pedału gazu.

### Dziedziczenie (Inheritance)

**Idea:** Klasa potomna przejmuje cechy klasy bazowej + dodaje własne.

**Po co?**
- Reużycie kodu
- Hierarchia typów (IS-A relationship)
- Polimorfizm

**Kiedy używać:**
- Prawdziwa relacja "jest" (Dog IS-A Animal)
- Potomek jest specjalizacją rodzica

**Kiedy NIE używać:**
- Tylko dla reużycia kodu → użyj kompozycji
- Relacja "ma" (Car HAS-A Engine) → kompozycja

**"Favor composition over inheritance"** - dziedziczenie tworzy tight coupling, kompozycja jest elastyczniejsza.

### Polimorfizm (Polymorphism)

**Idea:** Ten sam interfejs, różne implementacje. Kod operuje na abstrakcji, nie konkretach.

**Dwa rodzaje:**

1. **Compile-time (overloading)** - ta sama nazwa metody, różne parametry
   ```java
   void print(String s) { }
   void print(int i) { }
   ```

2. **Runtime (overriding)** - potomek nadpisuje metodę rodzica
   ```java
   Animal animal = new Dog();  // Deklarowany typ: Animal
   animal.makeSound();          // Wykonany: Dog.makeSound()
   ```

**Dlaczego potężne:**
Możesz pisać kod operujący na abstrakcji (interface), a konkretna implementacja jest wstrzykiwana. Łatwe testowanie, łatwa podmiana.

### Abstrakcja (Abstraction)

**Idea:** Modeluj tylko istotne cechy, ukryj nieistotne szczegóły.

**Realizacja w Javie:**
- **Abstract class** - częściowa implementacja, potomek dopełnia
- **Interface** - kontrakt bez implementacji (od Java 8: default methods)

**Różnica:**

| Abstract Class | Interface |
|----------------|-----------|
| `extends` (max 1) | `implements` (wiele) |
| Może mieć stan (pola) | Tylko stałe (public static final) |
| Może mieć konstruktor | Brak konstruktora |
| "IS-A" relationship | "CAN-DO" capability |

**Kiedy co:**
- Interface: definiujesz capability/kontrakt (Comparable, Serializable)
- Abstract class: współdzielisz implementację między potomkami

---

## 2. SOLID Principles

### S - Single Responsibility Principle (SRP)

**"Klasa powinna mieć tylko jeden powód do zmiany"**

**Źle:**
```
UserService
├── createUser()
├── sendWelcomeEmail()
├── generateReport()
└── validateCreditCard()
```
Ta klasa zmieni się gdy: zmieni się logika użytkowników, format emaili, format raportów, walidacja płatności.

**Dobrze:**
```
UserService         → zarządzanie użytkownikami
EmailService        → wysyłanie emaili
ReportGenerator     → generowanie raportów
PaymentValidator    → walidacja płatności
```

**Jak rozpoznać naruszenie:**
- Klasa ma wiele "aktorów" (grup interesariuszy)
- Trudno nazwać klasę bez "and" lub "or"
- Klasa ma > 200-300 linii

### O - Open/Closed Principle (OCP)

**"Otwarte na rozszerzenie, zamknięte na modyfikację"**

Dodając nową funkcjonalność, nie powinieneś modyfikować istniejącego kodu - tylko dodawać nowy.

**Źle:**
```java
class DiscountCalculator {
    double calculate(Order order) {
        if (order.type == "REGULAR") return order.total * 0.1;
        if (order.type == "VIP") return order.total * 0.2;
        if (order.type == "PREMIUM") return order.total * 0.3;
        // Dodanie nowego typu = modyfikacja tej metody
    }
}
```

**Dobrze:**
```java
interface DiscountStrategy {
    double calculate(Order order);
}

class RegularDiscount implements DiscountStrategy { ... }
class VipDiscount implements DiscountStrategy { ... }
class PremiumDiscount implements DiscountStrategy { ... }
// Nowy typ = nowa klasa, bez modyfikacji istniejących
```

**Mechanizmy realizacji:**
- Strategy pattern
- Template method pattern
- Dependency injection

### L - Liskov Substitution Principle (LSP)

**"Obiekty klasy bazowej powinny być zastępowalne obiektami klas potomnych bez zmiany poprawności programu"**

Jeśli `S` jest podtypem `T`, to obiekty typu `T` można zastąpić obiektami typu `S` bez zepsucia programu.

**Klasyczne naruszenie - Square/Rectangle:**
```java
class Rectangle {
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }
}

class Square extends Rectangle {
    void setWidth(int w) { width = w; height = w; }  // Zmienia też height!
    void setHeight(int h) { width = h; height = h; } // Zmienia też width!
}

// Kod klienta
void resize(Rectangle r) {
    r.setWidth(5);
    r.setHeight(10);
    assert r.getArea() == 50;  // FAIL dla Square! (area = 100)
}
```

**Zasady:**
1. Preconditions nie mogą być silniejsze w podtypie
2. Postconditions nie mogą być słabsze w podtypie
3. Invariants muszą być zachowane
4. Nie rzucaj nowych wyjątków (poza podtypami istniejących)

### I - Interface Segregation Principle (ISP)

**"Klient nie powinien być zmuszony do zależności od metod, których nie używa"**

**Źle:**
```java
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    void work() { ... }
    void eat() { /* ??? Robot nie je */ }
    void sleep() { /* ??? Robot nie śpi */ }
}
```

**Dobrze:**
```java
interface Workable { void work(); }
interface Eatable { void eat(); }
interface Sleepable { void sleep(); }

class Human implements Workable, Eatable, Sleepable { ... }
class Robot implements Workable { ... }
```

**Sygnały naruszenia:**
- Puste implementacje metod
- `throw new UnsupportedOperationException()`
- Interfejs ma > 5-7 metod

### D - Dependency Inversion Principle (DIP)

**"Moduły wysokopoziomowe nie powinny zależeć od niskopoziomowych. Oba powinny zależeć od abstrakcji."**

**Źle:**
```
┌─────────────┐
│ Controller  │
└──────┬──────┘
       │ depends on
       ▼
┌─────────────┐
│   Service   │
└──────┬──────┘
       │ depends on
       ▼
┌─────────────┐
│ MySQLRepo   │  ← konkretna implementacja
└─────────────┘
```

**Dobrze:**
```
┌─────────────┐
│ Controller  │
└──────┬──────┘
       │ depends on
       ▼
┌─────────────┐      ┌─────────────┐
│ <<interface>>│◄─────│   Service   │
│ Repository  │      └─────────────┘
└──────┬──────┘
       │ implements
       ▼
┌─────────────┐
│ MySQLRepo   │
└─────────────┘
```

Teraz Service zależy od **abstrakcji** (Repository interface), nie od konkretu (MySQLRepo). Można łatwo podmienić na PostgresRepo, InMemoryRepo, MockRepo.

---

## 3. Inne ważne zasady

### DRY - Don't Repeat Yourself

Każda wiedza powinna mieć jedną reprezentację w systemie.

**Uwaga:** DRY dotyczy **wiedzy/logiki**, nie kodu. Dwa podobne fragmenty kodu mogą reprezentować różną wiedzę - nie zawsze warto je unifikować.

### KISS - Keep It Simple, Stupid

Prostsze rozwiązanie jest lepsze. Nie over-engineer.

### YAGNI - You Aren't Gonna Need It

Nie implementuj funkcji "na zapas". Dodawaj gdy potrzebne.

### Composition over Inheritance

Preferuj składanie obiektów nad dziedziczenie.

**Dziedziczenie:**
- Tight coupling
- Statyczne (compile-time)
- Fragile Base Class problem

**Kompozycja:**
- Loose coupling
- Dynamiczne (runtime)
- Łatwiejsze testowanie

### Law of Demeter (Principle of Least Knowledge)

**"Rozmawiaj tylko z najbliższymi przyjaciółmi"**

Metoda powinna wywoływać tylko:
- Metody własnego obiektu
- Metody obiektów przekazanych jako parametry
- Metody obiektów utworzonych wewnątrz metody
- Metody obiektów będących polami klasy

**Źle:**
```java
customer.getAddress().getCity().getName()  // Train wreck
```

**Dobrze:**
```java
customer.getCityName()  // Delegacja
```

---

## 4. Design Patterns - przegląd koncepcyjny

### Creational (tworzenie obiektów)

**Singleton** - jedna instancja w aplikacji
- Użycie: konfiguracja, cache, connection pool
- W Spring: domyślny scope beana

**Factory Method** - deleguj tworzenie do podklas
- Użycie: gdy nie wiesz z góry jakiego typu obiekt utworzyć

**Builder** - konstruowanie złożonych obiektów krok po kroku
- Użycie: obiekt z wieloma opcjonalnymi parametrami

### Structural (struktura obiektów)

**Adapter** - dostosuj interfejs do oczekiwanego
- Użycie: integracja z legacy code, external libraries

**Decorator** - dodaj funkcjonalność dynamicznie
- Użycie: Java I/O streams, Spring @Transactional

**Facade** - prosty interfejs do złożonego systemu
- Użycie: upraszczanie API dla klienta

### Behavioral (zachowanie i komunikacja)

**Strategy** - wymienne algorytmy
- Użycie: różne sposoby sortowania, walidacji, płatności

**Observer** - powiadomienia o zmianach
- Użycie: event listeners, reactive streams

**Template Method** - szkielet algorytmu, kroki do nadpisania
- Użycie: frameworks (JUnit setup/teardown)

---

## 5. Kiedy łamać zasady?

Zasady to **heurystyki**, nie prawa fizyki.

**Pragmatyzm:**
- Prototyp/MVP → YAGNI ważniejsze niż SOLID
- Performance critical code → może wymagać "brzydkich" optymalizacji
- Mały projekt/skrypt → over-engineering jest gorszy niż spaghetti

**Świadome decyzje:**
Łam zasadę tylko gdy:
1. Rozumiesz dlaczego istnieje
2. Rozumiesz konsekwencje złamania
3. Zysk przewyższa koszt

**Technical Debt:**
Czasem warto zaciągnąć "dług" (szybkie rozwiązanie), ale zaplanuj spłatę (refactoring).

---

## Podsumowanie

| Zasada | Esencja |
|--------|---------|
| **SRP** | Jeden powód do zmiany |
| **OCP** | Rozszerzaj, nie modyfikuj |
| **LSP** | Podtyp musi być zastępowalny |
| **ISP** | Małe, fokusowane interfejsy |
| **DIP** | Zależność od abstrakcji |
| **DRY** | Jedna reprezentacja wiedzy |
| **KISS** | Prostota |
| **YAGNI** | Nie na zapas |
| **Composition** | Preferuj nad dziedziczenie |
| **Demeter** | Mów z przyjaciółmi |
