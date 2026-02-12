# Moduł 20: Spring AI

> `opt` = optional

## Cel

Integracja aplikacji Spring Boot z modelami AI.

---

## Tematy do opanowania

### 1. Spring AI Basics `opt`

- [ ] Co to Spring AI — framework do integracji z LLM
- [ ] Dependency: `spring-ai-openai-spring-boot-starter`
- [ ] ChatClient — wysyłanie promptów
- [ ] Modele: OpenAI, Anthropic, Ollama (local)
- [ ] Structured Output — parsowanie odpowiedzi do Java Objects

### 2. PGVector `opt`

- [ ] Co to Vector Database — przechowywanie embeddingów
- [ ] PGVector — rozszerzenie PostgreSQL
- [ ] Embedding — konwersja tekstu na wektor
- [ ] Similarity Search — wyszukiwanie podobnych dokumentów
- [ ] RAG (Retrieval-Augmented Generation) — LLM + własne dane

### 3. Tool Calling + MCP `opt`

- [ ] Tool Calling — LLM wywołuje Twoje funkcje
- [ ] `@Tool` annotation w Spring AI
- [ ] Model Context Protocol (MCP) — standard do narzędzi AI
- [ ] MCP Server — udostępnianie funkcji dla agentów AI
- [ ] Praktyczne zastosowania w Wallet Manager

---

## Powiązana teoria

- Josh Long: "Enterprise Java with Spring Boot" → sekcja Spring AI

---

## Przykład: ChatClient

```java
@Service
@RequiredArgsConstructor
public class InvestmentAdvisorService {

    private final ChatClient chatClient;

    public String analyzePortfolio(PortfolioSummary portfolio) {
        return chatClient.prompt()
            .system("You are an investment advisor. Analyze the portfolio.")
            .user("My portfolio: " + portfolio)
            .call()
            .content();
    }
}
```

---

## Przykład: Tool Calling

```java
@Component
public class PortfolioTools {

    @Tool(description = "Get current portfolio value")
    public BigDecimal getPortfolioValue(@ToolParam("userId") Long userId) {
        return portfolioService.calculateTotalValue(userId);
    }

    @Tool(description = "Get instrument price")
    public BigDecimal getPrice(@ToolParam("ticker") String ticker) {
        return priceService.getCurrentPrice(ticker);
    }
}
```

---

## Ćwiczenia

1. Dodaj Spring AI i skonfiguruj z OpenAI (lub Ollama local)
2. Stwórz endpoint do analizy portfolio przez AI
3. Dodaj PGVector i zaimplementuj RAG dla dokumentacji
4. Zaimplementuj Tool Calling — AI ma dostęp do danych portfolio
5. (Opcjonalnie) Stwórz MCP Server dla Wallet Manager

---

## Sprawdzian gotowości

- [ ] Potrafię zintegrować Spring AI z LLM
- [ ] Rozumiem co to RAG i embeddingi
- [ ] Wiem jak działa Tool Calling
- [ ] Potrafię stworzyć prosty MCP Server
