# Lekcja 03: Pass by Value

> Obalenie mitu "Pass by Reference". Java ZAWSZE przekazuje przez wartość.

## Koncept

### Mit: "Java przekazuje obiekty przez referencję"

To **najczęstszy błąd** w rozumieniu Javy. Prawda jest taka:

> **Java ZAWSZE przekazuje przez wartość (pass by value).**
> Ale dla obiektów — wartością jest **kopia referencji**, nie kopia obiektu.

### Dla primitives — kopia wartości

```java
public static void doubleIt(int x) {
    x = x * 2;          // modyfikujesz KOPIĘ
    System.out.println("Wewnątrz: " + x);  // 20
}

public static void main(String[] args) {
    int num = 10;
    doubleIt(num);
    System.out.println("Na zewnątrz: " + num);  // 10 ← nie zmieniony!
}
```

```
main():              doubleIt():
┌──────────┐        ┌──────────┐
│ num = 10 │   →    │ x = 10   │  (kopia wartości)
└──────────┘    ↗   │ x = 20   │  (zmiana kopii)
                    └──────────┘
```

**Proste.** Kopia wartości. Zmiana kopii nie wpływa na oryginał.

### Dla obiektów — kopia REFERENCJI (nie obiektu!)

```java
public static void rename(Instrument inst) {
    inst = new Instrument("MSFT", "Microsoft");  // reassign KOPII referencji
    System.out.println("Wewnątrz: " + inst.name());  // Microsoft
}

public static void main(String[] args) {
    Instrument apple = new Instrument("AAPL", "Apple");
    rename(apple);
    System.out.println("Na zewnątrz: " + apple.name());  // Apple ← nie zmieniony!
}
```

```
main():                        Heap:
┌────────────────┐
│ apple = ref ───┼──────→  [Instrument: AAPL, Apple]
└────────────────┘

rename():
┌────────────────┐
│ inst = ref ────┼──────→  [Instrument: AAPL, Apple]  (kopia referencji!)
│                │
│ inst = ref ────┼──────→  [Instrument: MSFT, Microsoft]  (nowy obiekt)
└────────────────┘

apple nadal wskazuje na oryginał!
```

**Kluczowe:** `inst = new ...` zmienia **kopię referencji**, nie oryginał.

### ALE: modyfikacja obiektu przez referencję DZIAŁA

```java
public static void addToList(List<String> list) {
    list.add("Spring Boot");  // modyfikujesz obiekt, do którego masz referencję
}

public static void main(String[] args) {
    List<String> techs = new ArrayList<>();
    techs.add("Java");
    addToList(techs);
    System.out.println(techs);  // [Java, Spring Boot] ← zmieniony!
}
```

```
main():                          Heap:
┌────────────────┐
│ techs = ref ───┼──────→  [ArrayList: "Java"]
└────────────────┘                ↑
                                  │ ← ten sam obiekt!
addToList():                      │
┌────────────────┐                │
│ list = ref ────┼────────────────┘
└────────────────┘
list.add("Spring Boot")  →  [ArrayList: "Java", "Spring Boot"]
```

**Dlaczego?** Bo `list` i `techs` to dwie różne referencje do **tego samego obiektu**.
Nie zmieniasz referencji — zmieniasz obiekt, na który referencja wskazuje.

### Podsumowanie — 3 reguły

| Scenariusz                                         | Efekt                     | Dlaczego                            |
| -------------------------------------------------- | ------------------------- | ----------------------------------- |
| Zmiana primitive w metodzie                        | ❌ Nie wpływa na oryginał | Kopia wartości                      |
| Reassign obiektu w metodzie (`param = new ...`)    | ❌ Nie wpływa na oryginał | Kopia referencji                    |
| Modyfikacja obiektu w metodzie (`param.setX(...)`) | ✅ Wpływa na oryginał     | Ta sama referencja → ten sam obiekt |

### Analogia Angular/TS

W TypeScript zachowanie jest **identyczne** — ale nikt o tym nie mówi, bo JS nie ma primitives na poziomie API.

```typescript
// TypeScript — to samo!
function rename(obj: { name: string }) {
  obj = { name: "new" }; // reassign → nie wpływa na oryginał
}

function mutate(obj: { name: string }) {
  obj.name = "new"; // mutacja → wpływa na oryginał
}
```

**Różnica:** W Javie pytają o to na KAŻDEJ rozmowie rekrutacyjnej. W JS — nigdy.

### Jak Java Records wpływają na to?

Records są **immutable** — nie mają setterów:

```java
public record Instrument(String ticker, String name) {}

public static void tryToChange(Instrument inst) {
    // inst.setName("X");   // ❌ nie skompiluje się — brak setterów!
    // inst.name = "X";     // ❌ pola są final
    inst = new Instrument("NEW", "New");  // ✅ ale zmienia tylko kopię ref
}
```

**Dlatego Records są bezpieczne** — nawet gdy przekażesz referencję,
nikt nie zmieni obiektu, bo jest immutable.

## Ćwiczenie

**Zadanie:** Napisz program który udowadnia 3 scenariusze:

1. **Primitive pass** — przekaż `int` do metody, zmień go, pokaż że oryginał nie zmieniony
2. **Object reassign** — przekaż obiekt, zrób `=new` w metodzie, pokaż że oryginał nie zmieniony
3. **Object mutation** — przekaż `ArrayList`, dodaj element, pokaż że oryginał zmieniony

```java
public class PassByValueDemo {
    // 1. primitiveTest(int value) — zmień i wypisz
    // 2. reassignTest(StringBuilder sb) — zrób new i wypisz
    // 3. mutateTest(List<String> list) — dodaj element i wypisz

    public static void main(String[] args) {
        // Wywołaj każdy test i wypisz wynik "przed" i "po"
    }
}
```

**Pytanie do przemyślenia:** Dlaczego `String` zachowuje się jak "pass by value"
mimo że jest obiektem? (Podpowiedź: immutability)

## Checklist

- [ ] Wiem że Java ZAWSZE jest pass by value
- [ ] Rozumiem różnicę: kopia wartości (primitive) vs kopia referencji (obiekt)
- [ ] Wiem dlaczego `param = new ...` nie zmienia oryginału
- [ ] Wiem dlaczego `param.add(...)` zmienia oryginał
- [ ] Potrafię to wytłumaczyć używając diagramów Stack/Heap
- [ ] Rozumiem dlaczego Records (immutable) są "bezpieczne" w tym kontekście
