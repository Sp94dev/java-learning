# Lekcja 03: Package by Layer vs Package by Feature

> 📖 Porównanie z przykładami: [`docs/theory/06-architecture.md`](../../../docs/theory/06-architecture.md), sekcja 3.

## Package by Layer (Tradycyjne)

Grupowanie po roli technicznej — wszystkie kontrolery razem, wszystkie serwisy razem:

```
com.wallet/
├── controllers/     ← wszystkie kontrolery w jednym worku
├── services/        ← wszystkie serwisy
└── repositories/    ← wszystkie repozytoria
```

**Analogia Angular:** Wrzucasz wszystkie `.html` do jednego folderu, wszystkie `.service.ts` do drugiego. Chcesz naprawić bug w Transakcjach — skaczesz po 3 folderach.

## Package by Feature (Nowoczesne)

Grupowanie po domenie biznesowej — jak Angular Feature Modules:

```
com.wallet/
├── instrument/      ← wszystko związane z instrumentami
│   ├── InstrumentController.java
│   ├── InstrumentService.java
│   └── InMemoryInstrumentRepository.java
└── transaction/     ← wszystko związane z transakcjami
    ├── TransactionController.java
    ├── TransactionService.java
    └── InMemoryTransactionRepository.java
```

### Zalety:

1. **Wysoka Kohezja** — pliki modyfikowane razem leżą razem
2. **Łatwa Nawigacja** — ticket "napraw bug w transakcjach" → otwierasz `transaction/` i masz wszystko
3. **Przygotowanie do Microservices** — wyciągnięcie subdomeny to wyrwanie jednego folderu
4. **Package-Private** — w Javie klasa bez modyfikatora `public` jest widoczna tylko w swoim pakiecie. `TransactionRepository` może być ukryte przed `instrument/`

---

## 🏋️ Zadanie: Weryfikacja struktury Wallet Manager

Twój projekt **już** używa Package by Feature. Ale czy jest szczelny? Sprawdź:

1. **Cross-module imports:** Otwórz `TransactionService.java` i sprawdź importy. Czy importuje cokolwiek z pakietu `com.sp94dev.wallet.instrument`? Jeśli tak — to potencjalny wyciek między modułami.

2. **Pakiet `config/`:** Masz `config/OpenApiConfig.java`. Czy to feature? Nie — to infrastruktura współdzielona. Gdzie powinien trafić? (Podpowiedź: `common/config/`).

3. **Modyfikatory dostępu:** Sprawdź, czy `InMemoryInstrumentRepository` jest `public`. Czy musi być? Kto go używa? Jeśli tylko `InstrumentService` (z tego samego pakietu), mógłby być package-private.

4. **Wylistuj** obecne pakiety projektu i porównaj ze strukturą z `PROJECT.md` (sekcja "Aktualna Struktura Pakietów"). Czy się zgadzają?

## Sprawdzian wiedzy

- [x] Zrozumiałem różnicę między Package by Layer a Package by Feature
- [x] Wiem, dlaczego Package by Feature ułatwia nawigację i przechodzenie na mikroserwisy
- [x] Sprawdziłem projekt pod kątem przecieków między modułami (cross-module imports)
- [x] Przeanalizowałem, które klasy mogłyby mieć modyfikator package-private
