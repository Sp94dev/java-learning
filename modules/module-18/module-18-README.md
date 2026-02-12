# Moduł 18: Web Beyond REST

> `opt` = optional

## Cel

Poznać alternatywne protokoły komunikacji poza REST.

---

## Tematy do opanowania

### 1. GraphQL w Spring

- [ ] Co to GraphQL — query language dla API
- [ ] Porównanie z REST (over-fetching, under-fetching)
- [ ] Schema Definition Language (SDL)
- [ ] `@QueryMapping` — queries
- [ ] `@MutationMapping` — mutations
- [ ] `@SchemaMapping` — nested resolvers
- [ ] `@BatchMapping` — N+1 w GraphQL
- [ ] Dependency: `spring-boot-starter-graphql`

### 2. gRPC + Protocol Buffers `opt`

- [ ] Co to gRPC — high-performance RPC framework
- [ ] Protocol Buffers — definicja schematu (`.proto`)
- [ ] Generowanie kodu z proto
- [ ] Streaming (unary, server, client, bidirectional)
- [ ] Kiedy gRPC zamiast REST (inter-service, performance)
- [ ] Spring Boot + gRPC

### 3. Deklaratywny HTTP Client

- [ ] `@HttpExchange` — interfejs jako HTTP client
- [ ] Porównanie z RestTemplate, WebClient, Feign
- [ ] Definiowanie requestów przez adnotacje
- [ ] Rejestracja jako bean

---

## Powiązana teoria

- `docs/theory/06-architecture.md` → Microservices communication

---

## Przykład: GraphQL Schema

```graphql
type Query {
  instruments: [Instrument!]!
  instrument(id: ID!): Instrument
}

type Mutation {
  createInstrument(input: CreateInstrumentInput!): Instrument!
}

type Instrument {
  id: ID!
  ticker: String!
  currency: String!
  transactions: [Transaction!]!
}
```

---

## Przykład: @HttpExchange

```java
@HttpExchange("/api/prices")
public interface PriceClient {

    @GetExchange("/{ticker}")
    PriceResponse getPrice(@PathVariable String ticker);

    @GetExchange
    List<PriceResponse> getAllPrices();
}
```

---

## Ćwiczenia

1. Dodaj GraphQL schema dla Instrumentów
2. Zaimplementuj Query i Mutation
3. Rozwiąż N+1 za pomocą @BatchMapping
4. Stwórz Deklaratywny HTTP Client do zewnętrznego API

---

## Sprawdzian gotowości

- [ ] Rozumiem różnicę GraphQL vs REST
- [ ] Potrafię stworzyć GraphQL schema i resolvers
- [ ] Wiem kiedy użyć gRPC
- [ ] Potrafię stworzyć deklaratywny HTTP Client
