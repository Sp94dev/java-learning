# Moduł 07: Caching

## Cel
Przyspieszenie aplikacji przez cache - lokalny i rozproszony.

---

## Tematy do opanowania

### 1. Po co cache?
- [ ] Unikanie powtarzających się operacji (DB, API)
- [ ] Redukcja latency
- [ ] Odciążenie bazy danych
- [ ] Trade-off: świeżość vs szybkość

### 2. Spring Cache Abstraction
- [ ] `@EnableCaching` - włączenie cache
- [ ] `@Cacheable` - cache wynik metody
- [ ] `@CacheEvict` - usuń z cache
- [ ] `@CachePut` - zawsze aktualizuj cache
- [ ] Cache key - jak jest generowany

### 3. Cache Providers
- [ ] **Simple** (ConcurrentHashMap) - default, tylko dev
- [ ] **Caffeine** - lokalny, wysokowydajny
- [ ] **Redis** - rozproszony, zewnętrzny serwis

### 4. Caffeine (Local Cache)
- [ ] Dependency: `caffeine`
- [ ] Konfiguracja: `maximumSize`, `expireAfterWrite`
- [ ] Eviction policies (LRU, size-based, time-based)
- [ ] Zalety: szybki (in-process), brak sieci
- [ ] Wady: per-instance (nie shared między serwerami)

### 5. Redis (Distributed Cache)
- [ ] Kiedy Redis zamiast local cache
- [ ] Docker: `redis:7`
- [ ] Dependency: `spring-boot-starter-data-redis`
- [ ] Konfiguracja: `spring.redis.host`, `spring.redis.port`
- [ ] Serialization (JSON)

### 6. Cache Strategies
- [ ] **Cache-Aside** - app sprawdza cache, jeśli brak → DB
- [ ] **Read-Through** - cache sam pobiera z DB
- [ ] **Write-Through** - zapis do cache + DB synchronicznie
- [ ] **Write-Behind** - zapis do cache, async do DB

### 7. Cache Invalidation
- [ ] TTL (Time To Live) - automatyczne wygasanie
- [ ] Manual eviction (`@CacheEvict`)
- [ ] Event-driven invalidation
- [ ] "Cache invalidation is one of the hardest problems"

### 8. Cache Key Design
- [ ] Unikalny klucz per dane
- [ ] SpEL w `@Cacheable(key = "#id")`
- [ ] Composite keys

---

## Powiązana teoria
- `docs/theory/05-databases-jpa.md` → sekcja Cache (jeśli jest)

---

## Przykład: Caffeine
```java
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES));
        return manager;
    }
}
```

---

## Przykład: @Cacheable
```java
@Service
public class InstrumentService {
    
    @Cacheable(value = "instruments", key = "#id")
    public InstrumentResponse findById(Long id) {
        log.info("Fetching from DB: {}", id); // Tylko przy pierwszym wywołaniu
        return repository.findById(id)
            .map(this::toResponse)
            .orElseThrow();
    }
    
    @CacheEvict(value = "instruments", key = "#id")
    public void update(Long id, UpdateRequest request) {
        // Cache usunięty po update
    }
}
```

---

## docker-compose.yml (z Redis)
```yaml
services:
  redis:
    image: redis:7
    ports:
      - "6379:6379"
```

---

## Ćwiczenia
1. Dodaj `@EnableCaching` i prosty cache (Simple)
2. Skonfiguruj Caffeine z TTL
3. Dodaj Redis do docker-compose
4. Przenieś cache do Redis
5. Zaimplementuj cache dla cen instrumentów

---

## Sprawdzian gotowości
- [ ] Rozumiem @Cacheable, @CacheEvict, @CachePut
- [ ] Potrafię skonfigurować Caffeine
- [ ] Wiem kiedy użyć Redis zamiast local cache
- [ ] Rozumiem cache invalidation strategies
- [ ] Potrafię zaprojektować cache key
