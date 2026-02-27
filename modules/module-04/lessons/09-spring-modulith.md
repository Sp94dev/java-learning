# Lekcja 09: Spring Modulith 🆕

> ⚠️ Spring Modulith nie jest jeszcze używany w wallet-manager. Ta lekcja to **świadomość narzędzia** na przyszłość.

Spring Modulith to biblioteka, która pomaga utrzymać rygory Modularnego Monolitu — gwarantując separację modułów na poziomie frameworku.

**Analogia Angular:** Nx Workspace z zablokowanymi zależnościami między bibliotekami — lint check uderzy, jeśli `transactions-lib` zaimportuje wewnętrzną klasę z `auth-lib`.

## Co oferuje Modulith?

1. **Event-Based Integration** (`ApplicationEventPublisher`) — Moduł `transaction` zamiast wołać `InstrumentService` bezpośrednio, emituje event `TransactionCreated`. Zainteresowani nasłuchują. Zero coupling. Angular: RxJS `Subject` / Event Bus.

2. **`@Externalized`** — Eksternalizacja eventów do Kafka/RabbitMQ. Event wewnętrzny staje się eventem zewnętrznym — most pomiędzy monolitem a przyszłymi mikroserwisami.

3. **Architecture Tests** — Testy walidujące granice modułów. Build nie przejdzie, jeśli `transaction/` importuje wewnętrzną klasę z `instrument/`. Jak lint-rules w Nx.

4. **Documenter** — Auto-generacja diagramów UML zależności między modułami.

5. **Module Testing** — Ładowanie kontekstu Springa tylko dla jednego modułu (szybsze testy).

---

## 🏋️ Zadanie: Pseudokod eventowy

Nie implementujmy jeszcze Modulith, ale **zaprojektujmy** komunikację eventową:

1. **Scenariusz:** Po dodaniu transakcji (`POST /api/transactions`) chcesz automatycznie przeliczyć statystyki portfela.

2. **Napisz pseudokod** (w komentarzu lub notatce):

   ```java
   // Event (w transaction/)
   public record TransactionCreated(Long transactionId, Long instrumentId, String type, double price) {}

   // Publisher (w TransactionService)
   // Po save() → applicationEventPublisher.publishEvent(new TransactionCreated(...))

   // Listener (w osobnym module, np. portfolio/)
   // @EventListener void onTransactionCreated(TransactionCreated event) { recalculate(); }
   ```

3. **Pytanie:** Dlaczego `TransactionCreated` powinien zawierać **kopię danych** (price, type), a nie referencję do `Transaction`? (Podpowiedź: gdyby listener był w innym module/mikroserwisie, nie miałby dostępu do klasy `Transaction`).

> 📌 Implementacja eventów — docelowo w Module 05+ gdy pojawi się JPA i `@Transactional`.


## Sprawdzian wiedzy

- [ ] Wiem, czym jest Spring Modulith i do czego służy
- [ ] Rozumiem ideę event-based integration (zdarzenia zamiast bezpośrednich wywołań)
- [ ] Zrozumiałem, dlaczego zdarzenie (Event) powinno przenosić payload, a nie encję
- [ ] Wiem, jak użycie zdarzeń wpływa na luźne powiązanie (loose coupling) między modułami
