# Moduł 19: Messaging

> `opt` = optional

## Cel

Zrozumieć event-driven architecture i messaging.

---

## Tematy do opanowania

### 1. Po co messaging?

- [ ] Decoupling — luźne powiązanie między serwisami
- [ ] Asynchroniczność — nie blokuj producenta
- [ ] Resilience — retry, dead letter queue
- [ ] Peak handling — buforowanie przy spike'ach
- [ ] Porównanie: sync (REST) vs async (messaging)

### 2. Apache Kafka

- [ ] Architektura: broker, topic, partition, consumer group
- [ ] Producer — publikowanie wiadomości
- [ ] Consumer — odczytywanie wiadomości
- [ ] Serializacja JSON (Jackson)
- [ ] Spring Kafka: `@KafkaListener`, `KafkaTemplate`
- [ ] Docker: `confluentinc/cp-kafka`

### 3. Spring Integration `opt`

- [ ] Enterprise Integration Patterns w Spring
- [ ] Message channels, endpoints, adapters
- [ ] Flows — DSL do definiowania procesów
- [ ] Kiedy Spring Integration vs Kafka

### 4. Event-Driven Architecture

- [ ] Domain Events — publikacja wewnętrzna (`ApplicationEventPublisher`)
- [ ] Outbox Pattern — niezawodne publikowanie eventów
- [ ] Event Sourcing — przechowywanie eventów zamiast stanu (awareness)
- [ ] CQRS — Command Query Responsibility Segregation (awareness)
- [ ] Saga Pattern — transakcje rozproszone

---

## Powiązana teoria

- `docs/theory/06-architecture.md` → Microservices, DDD
- `docs/theory/04-spring-framework.md` → Event publishing

---

## Przykład: Kafka Producer

```java
@Service
@RequiredArgsConstructor
public class TransactionEventPublisher {

    private final KafkaTemplate<String, TransactionEvent> kafka;

    public void publishCreated(Transaction tx) {
        var event = new TransactionEvent(tx.id(), tx.ticker(), tx.type(), tx.amount());
        kafka.send("transactions", tx.id().toString(), event);
    }
}
```

---

## Przykład: Kafka Consumer

```java
@Component
@Slf4j
public class TransactionEventConsumer {

    @KafkaListener(topics = "transactions", groupId = "portfolio-service")
    public void onTransactionCreated(TransactionEvent event) {
        log.info("Received transaction event: {}", event);
        // Recalculate portfolio
    }
}
```

---

## docker-compose.yml (Kafka)

```yaml
services:
  kafka:
    image: confluentinc/cp-kafka:7.5.0
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@localhost:9093
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      CLUSTER_ID: "wallet-kafka-cluster"
```

---

## Ćwiczenia

1. Dodaj Kafka do docker-compose
2. Publikuj event przy tworzeniu transakcji
3. Stwórz consumer do przeliczania portfolio
4. Zaimplementuj domain events wewnętrznie (ApplicationEventPublisher)
5. Zaimplementuj Outbox Pattern (opcjonalnie)

---

## Sprawdzian gotowości

- [ ] Rozumiem kiedy sync (REST) vs async (messaging)
- [ ] Potrafię skonfigurować Kafka z Spring Boot
- [ ] Wiem co to Outbox Pattern i kiedy go stosować
- [ ] Rozumiem event-driven architecture
