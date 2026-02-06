# Lesson 02: HTTP Methods

> GET, POST, PUT, DELETE - full CRUD

## Concept

| Method | Action | Idempotent | Body |
|--------|-------|--------------|------|
| GET | Retrieve | ✅ | ❌ |
| POST | Create | ❌ | ✅ |
| PUT | Replace entirely | ✅ | ✅ |
| PATCH | Partial update | ❌ | ✅ |
| DELETE | Delete | ✅ | ❌ |

**Idempotent** = calling the method multiple times has the same effect as calling it once.

## Spring Mappings

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
✅ GET    /wallets          - list
✅ GET    /wallets/123      - single item
✅ POST   /wallets          - create
✅ PUT    /wallets/123      - update
✅ DELETE /wallets/123      - delete

❌ GET    /getWallets       - verb in URL
❌ POST   /createWallet     - action in URL
❌ GET    /wallet/123       - singular for list access
```

## Exercise

**Task:** Create a `NoteController` with full CRUD:
- A note has: `id`, `title`, `content`
- Store data in a `List<>` (in-memory)
- No service layer yet - put everything in the controller (deliberately, for later refactoring)

**Files:** `exercises/ex02-crud-controller/`

## Checklist

- [ ] I understand the difference between PUT and PATCH
- [ ] I know the REST naming conventions
- [ ] I can map all HTTP methods