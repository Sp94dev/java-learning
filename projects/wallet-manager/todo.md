# TODO: Wallet Manager

## Phase 1: REST + Java Basics (Module 01) ✅

### Instrument Controller (`/api/instruments`)
- [x] **Get single instrument**: Implemented `GET /api/instruments/{id}` with `Optional` handling and `404 Not Found`.
- [x] **Filter instruments**: Implemented `GET /api/instruments` with multiple `@RequestParam`, sorting, and limiting (Stream API).
- [x] **Create instrument**: Implemented `POST /api/instruments` returning `201 Created` and `Location` header.
- [x] **Full CRUD**: Added `PUT` (update) and `DELETE` (204 No Content) support.

### Transaction Controller (`/api/transactions`)
- [x] **Create controller**: Implemented `TransactionController`.
- [x] **Add transaction**: Handling `POST /api/transactions` with linking to instrument by ID.
- [x] **Statistics**: Added `GET /api/transactions/stats` (Grouping and summing using `Collectors.groupingBy`).

### Architecture & Best Practices
- [x] **Layering**: Full separation of Controller -> Service -> Repository.
- [x] **Modeling**: Usage of `Java Records` for models and DTOs.
- [x] **ResponseEntity**: Correct HTTP status codes throughout the API.
- [x] **OpenAPI / Swagger**: Added interactive documentation (`springdoc-openapi`) with English descriptions.

## Phase 2: API Extension & Standards (In Progress) 🟡

- [ ] **Validation**: Transition from domain models to dedicated Request DTOs and adding `@Valid`.
- [ ] **Error Handling**: Implementation of `@ControllerAdvice` and `Problem Details` standard.

## Phase 3: Java Internals & Quality (Next Steps) 🆕

- [ ] **Profiling**: Memory usage analysis in `ConcurrentHashMap`.
- [ ] **Unit Tests**: JUnit 5 + Mockito for service logic.
