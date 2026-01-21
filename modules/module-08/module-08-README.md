# Moduł 08: Autentykacja & Autoryzacja

## Cel
Zabezpieczyć API - JWT, Spring Security, podstawy OAuth2.

---

## Tematy do opanowania

### 1. Fundamenty Security
- [ ] **Authentication (AuthN)** - KIM jesteś?
- [ ] **Authorization (AuthZ)** - CO możesz?
- [ ] Principal, Credential, Authority
- [ ] Kolejność: najpierw AuthN, potem AuthZ

### 2. Stateful vs Stateless
- [ ] **Stateful (Session):** Server przechowuje sesję, Cookie
- [ ] **Stateless (Token):** Token zawiera dane, server nie przechowuje
- [ ] Zalety stateless: skalowanie, microservices
- [ ] Wady stateless: trudne unieważnienie tokenu

### 3. JWT (JSON Web Token)
- [ ] Struktura: Header.Payload.Signature
- [ ] Header: algorytm (HS256, RS256)
- [ ] Payload: claims (sub, exp, iat, custom)
- [ ] Signature: zapobiega modyfikacji
- [ ] Weryfikacja: server oblicza signature i porównuje

### 4. Access Token + Refresh Token
- [ ] **Access Token:** krótki (15 min), do autoryzacji
- [ ] **Refresh Token:** długi (7 dni), do odnowienia access
- [ ] Flow: access wygasł → POST /refresh → nowy access
- [ ] Bezpieczeństwo: refresh token w httpOnly cookie

### 5. Gdzie przechowywać tokeny?
- [ ] ❌ localStorage - podatne na XSS
- [ ] ❌ sessionStorage - podatne na XSS
- [ ] ✅ httpOnly Cookie - JS nie może odczytać
- [ ] ✅ In-memory (dla SPA) + refresh token w cookie

### 6. Spring Security Setup
- [ ] Dependency: `spring-boot-starter-security`
- [ ] `SecurityFilterChain` - konfiguracja
- [ ] Disable CSRF dla REST API
- [ ] `SessionCreationPolicy.STATELESS`
- [ ] `authorizeHttpRequests` - reguły dostępu

### 7. JWT Filter
- [ ] Implementacja `OncePerRequestFilter`
- [ ] Wyciąganie tokenu z `Authorization: Bearer ...`
- [ ] Weryfikacja i ustawienie `SecurityContext`
- [ ] Biblioteka: `jjwt` (io.jsonwebtoken)

### 8. UserDetailsService
- [ ] Interface do ładowania użytkowników
- [ ] `loadUserByUsername(String username)`
- [ ] Zwraca `UserDetails` (username, password, authorities)

### 9. Password Encoding
- [ ] Nigdy plain text!
- [ ] `BCryptPasswordEncoder` (default, rekomendowany)
- [ ] `passwordEncoder.encode()`, `passwordEncoder.matches()`

### 10. Method Security
- [ ] `@PreAuthorize("hasRole('ADMIN')")`
- [ ] `@PreAuthorize("hasAuthority('READ_USERS')")`
- [ ] `@PreAuthorize("#userId == authentication.principal.id")`
- [ ] `@EnableMethodSecurity`

### 11. OAuth2 / OpenID Connect (podstawy)
- [ ] OAuth2 - framework autoryzacji (delegowanie dostępu)
- [ ] OIDC - warstwa tożsamości na OAuth2 (ID Token)
- [ ] Authorization Code Flow
- [ ] Integration z Google, GitHub (opcjonalnie)

### 12. OWASP Top 10 (awareness)
- [ ] Broken Access Control
- [ ] Injection (SQL, Command)
- [ ] XSS (Cross-Site Scripting)
- [ ] CSRF (Cross-Site Request Forgery)

---

## Powiązana teoria
- `docs/theory/07-security.md` → Cały plik

---

## Przykład: SecurityConfig
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

---

## Ćwiczenia
1. Dodaj Spring Security - wszystko wymaga auth
2. Zaimplementuj endpoint rejestracji (hash hasła BCrypt)
3. Zaimplementuj endpoint logowania (zwraca JWT)
4. Stwórz JwtFilter do weryfikacji tokenów
5. Dodaj role (USER, ADMIN) i `@PreAuthorize`
6. Zaimplementuj refresh token flow

---

## Sprawdzian gotowości
- [ ] Rozumiem różnicę AuthN vs AuthZ
- [ ] Wiem jak działa JWT (struktura, weryfikacja)
- [ ] Potrafię skonfigurować Spring Security
- [ ] Wiem gdzie (nie) przechowywać tokeny
- [ ] Rozumiem podstawy OAuth2/OIDC
