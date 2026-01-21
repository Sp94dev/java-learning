# Lekcja 02: HTTP Methods

> GET, POST, PUT, DELETE - pełne CRUD

## Koncept

| Metoda | Akcja | Idempotentna | Body |
|--------|-------|--------------|------|
| GET | Pobierz | ✅ | ❌ |
| POST | Utwórz | ❌ | ✅ |
| PUT | Zastąp całość | ✅ | ✅ |
| PATCH | Aktualizuj część | ❌ | ✅ |
| DELETE | Usuń | ✅ | ❌ |

**Idempotentna** = wielokrotne wywołanie daje ten sam efekt.

## Mapowania w Spring

```java
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @GetMapping
    public List<Item> getAll() { }

    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) { }

    @PostMapping
    public Item create(@RequestBody Item item) { }

    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) { }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { }
}
```

## REST Naming Conventions

```
✅ GET    /wallets          - lista
✅ GET    /wallets/123      - jeden
✅ POST   /wallets          - utwórz
✅ PUT    /wallets/123      - aktualizuj
✅ DELETE /wallets/123      - usuń

❌ GET    /getWallets       - czasownik w URL
❌ POST   /createWallet     - akcja w URL
❌ GET    /wallet/123       - singular dla listy
```

## Ćwiczenie

**Zadanie:** Stwórz `NoteController` z pełnym CRUD:
- Notatka ma: `id`, `title`, `content`
- Dane trzymaj w `List<>` (in-memory)
- Bez serwisu - wszystko w kontrolerze (celowo, żeby potem refaktorować)

**Pliki:** `exercises/ex02-crud-controller/`

## Checklist

- [ ] Rozumiem różnicę między PUT a PATCH
- [ ] Znam konwencje nazewnictwa REST
- [ ] Potrafię zmapować wszystkie metody HTTP
