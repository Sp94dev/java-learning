# TEORIA: Security

---

## 1. Podstawy bezpieczeństwa

### Authentication vs Authorization

**Authentication (AuthN)** - "Kim jesteś?"
- Weryfikacja tożsamości
- Login/password, token, certificate, biometria
- Odpowiedź: "To jest user X" lub "Nie wiem kim jesteś"

**Authorization (AuthZ)** - "Co możesz?"
- Sprawdzenie uprawnień
- Role, permissions, policies
- Odpowiedź: "Możesz / Nie możesz to zrobić"

**Kolejność:** Zawsze najpierw AuthN, potem AuthZ.

### Principal, Credential, Authority

**Principal** - tożsamość (kto)
- Username, email, ID

**Credential** - dowód tożsamości (jak udowadnia)
- Password, token, certificate

**Authority** - uprawnienie (co może)
- Role (ROLE_ADMIN), Permission (READ_USERS)

---

## 2. Stateful vs Stateless Authentication

### Stateful (Session-based)

```
1. Client: POST /login {username, password}
2. Server: Weryfikuje credentials
3. Server: Tworzy SESSION (w pamięci/Redis)
4. Server: Zwraca Cookie: JSESSIONID=abc123
5. Client: Każdy request z Cookie
6. Server: Lookup session → zna użytkownika
```

```
Client                    Server                    Session Store
  │                         │                            │
  │─── login ──────────────►│                            │
  │                         │─── create session ────────►│
  │◄── Cookie: sessionId ───│                            │
  │                         │                            │
  │─── request + Cookie ───►│                            │
  │                         │─── lookup session ────────►│
  │                         │◄── user data ──────────────│
  │◄── response ────────────│                            │
```

**Zalety:**
- Łatwe unieważnienie (usuń session)
- Server kontroluje stan

**Wady:**
- Server musi przechowywać stan
- Problem ze skalowaniem (sticky sessions lub shared store)
- CSRF vulnerability

### Stateless (Token-based)

```
1. Client: POST /login {username, password}
2. Server: Weryfikuje credentials
3. Server: Generuje TOKEN (zawiera dane użytkownika)
4. Server: Zwraca {token: "eyJ..."}
5. Client: Każdy request z Header: Authorization: Bearer token
6. Server: Weryfikuje token → zna użytkownika (bez lookup!)
```

```
Client                    Server
  │                         │
  │─── login ──────────────►│
  │◄── token ───────────────│
  │                         │
  │─── request + token ────►│
  │                         │ verify token (no DB lookup!)
  │◄── response ────────────│
```

**Zalety:**
- Server nie przechowuje stanu
- Łatwe skalowanie (każdy serwer może weryfikować)
- Dobry dla SPA, mobile, microservices

**Wady:**
- Trudne unieważnienie (token ważny do wygaśnięcia)
- Token może być duży

---

## 3. JWT (JSON Web Token)

### Struktura

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
        HEADER              .           PAYLOAD               .              SIGNATURE
```

**Header (Base64):**
```json
{
  "alg": "HS256",  // Algorytm podpisu
  "typ": "JWT"
}
```

**Payload (Base64):**
```json
{
  "sub": "user@email.com",    // Subject (kto)
  "iat": 1616239022,          // Issued at (kiedy)
  "exp": 1616242622,          // Expiration (do kiedy)
  "roles": ["USER", "ADMIN"]  // Custom claims
}
```

**Signature:**
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  SECRET_KEY
)
```

### Jak działa?

**Tworzenie:**
1. Server tworzy header + payload
2. Server podpisuje swoim SECRET_KEY
3. Server zwraca token (header.payload.signature)

**Weryfikacja:**
1. Server otrzymuje token
2. Server oblicza signature z header + payload + SECRET_KEY
3. Server porównuje z signature w tokenie
4. Jeśli równe → token niezmieniony, ufamy payload

### Symmetric vs Asymmetric

**Symmetric (HS256):**
- Ten sam klucz do podpisu i weryfikacji
- Prostsze, szybsze
- Klucz musi być współdzielony (problem w microservices)

**Asymmetric (RS256):**
- Private key do podpisu
- Public key do weryfikacji
- Lepsze dla distributed systems (public key można udostępnić)

### Access Token + Refresh Token

**Problem:** Krótki czas życia = częste logowanie. Długi = duże ryzyko.

**Rozwiązanie:**
- **Access Token** - krótki (15 min), do autoryzacji requestów
- **Refresh Token** - długi (7 dni), do odnawiania access token

```
1. Login → access token (15 min) + refresh token (7 dni)
2. Request → access token
3. Access token wygasł → POST /refresh z refresh token
4. Server → nowy access token
5. Refresh token wygasł → pełny login
```

---

## 4. Gdzie przechowywać tokeny?

### ❌ localStorage
```javascript
localStorage.setItem('token', jwt);
```
- **Problem:** Podatne na XSS (JavaScript może odczytać)
- Jeden złośliwy skrypt = token ukradziony

### ❌ sessionStorage
- Te same problemy co localStorage
- Dodatkowo: znika po zamknięciu karty

### ✅ httpOnly Cookie
```
Set-Cookie: token=jwt; HttpOnly; Secure; SameSite=Strict
```
- **HttpOnly** - JavaScript nie może odczytać
- **Secure** - tylko HTTPS
- **SameSite** - ochrona przed CSRF

### ✅ In-memory (dla SPA)
- Token w zmiennej JavaScript
- Znika przy refresh (użyj refresh token do odnowienia)
- Bezpieczniejsze od localStorage

### Rekomendacja
- **Access Token:** in-memory lub httpOnly cookie
- **Refresh Token:** httpOnly cookie (zawsze)

---

## 5. OWASP Top 10

Najczęstsze podatności webowe:

### A01: Broken Access Control
Użytkownik może robić rzeczy, których nie powinien.

```
GET /api/users/123/orders  ← User 456 może zobaczyć zamówienia User 123!
```

**Zapobieganie:**
- Deny by default
- Sprawdzaj ownership na poziomie biznesowym
- Testy integracyjne na autoryzację

### A02: Cryptographic Failures
Słabe szyfrowanie, wycieki danych.

**Zapobieganie:**
- HTTPS everywhere
- Silne algorytmy (bcrypt dla haseł)
- Nie przechowuj wrażliwych danych jeśli nie musisz

### A03: Injection
SQL Injection, Command Injection, etc.

```sql
-- User input: "'; DROP TABLE users; --"
SELECT * FROM users WHERE name = ''; DROP TABLE users; --'
```

**Zapobieganie:**
- Parametrized queries (PreparedStatement, JPA)
- Input validation
- Least privilege dla DB user

### A05: Security Misconfiguration
Domyślne hasła, verbose errors, niepotrzebne funkcje.

**Zapobieganie:**
- Wyłącz domyślne konta
- Nie pokazuj stack trace na produkcji
- Regularny audyt konfiguracji

### A07: Cross-Site Scripting (XSS)
Złośliwy JavaScript w przeglądarce użytkownika.

```html
<!-- Stored XSS -->
<div>Comment: <script>stealCookies()</script></div>
```

**Zapobieganie:**
- Escape output
- Content Security Policy (CSP)
- httpOnly cookies

### A08: Cross-Site Request Forgery (CSRF)
Zmuszenie zalogowanego użytkownika do wykonania akcji.

```html
<!-- Na złośliwej stronie -->
<img src="https://bank.com/transfer?to=attacker&amount=1000">
```

**Zapobieganie:**
- CSRF tokens (synchronizer token)
- SameSite cookies
- Weryfikacja Origin/Referer header

---

## 6. Spring Security

### Architektura

```
HTTP Request
     │
     ▼
┌─────────────────────────────────┐
│        Security Filter Chain    │
│                                 │
│  ┌─────────────────────────┐   │
│  │ CorsFilter               │   │
│  └────────────┬────────────┘   │
│               ▼                 │
│  ┌─────────────────────────┐   │
│  │ CsrfFilter               │   │
│  └────────────┬────────────┘   │
│               ▼                 │
│  ┌─────────────────────────┐   │
│  │ AuthenticationFilter     │   │ ← Weryfikuje credentials
│  └────────────┬────────────┘   │
│               ▼                 │
│  ┌─────────────────────────┐   │
│  │ AuthorizationFilter      │   │ ← Sprawdza uprawnienia
│  └────────────┬────────────┘   │
│               ▼                 │
│  ┌─────────────────────────┐   │
│  │ ExceptionTranslation     │   │
│  └────────────┬────────────┘   │
└───────────────┼─────────────────┘
                ▼
           Controller
```

### SecurityContext

Przechowuje informacje o zalogowanym użytkowniku.

```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication auth = context.getAuthentication();
String username = auth.getName();
Collection<GrantedAuthority> authorities = auth.getAuthorities();
```

**Thread-local storage:** Każdy wątek ma swój SecurityContext.

### Authentication Process

```
1. AuthenticationFilter wyciąga credentials z request
2. Tworzy Authentication object (unauthenticated)
3. AuthenticationManager.authenticate(auth)
4. AuthenticationManager deleguje do AuthenticationProvider
5. AuthenticationProvider (np. DaoAuthenticationProvider):
   - Pobiera UserDetails przez UserDetailsService
   - Weryfikuje password (PasswordEncoder)
   - Zwraca Authentication (authenticated)
6. Authentication zapisywane w SecurityContext
```

### Method Security

```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnly() { }

@PreAuthorize("hasAuthority('READ_USERS')")
public List<User> getUsers() { }

@PreAuthorize("#userId == authentication.principal.id")
public User getUser(Long userId) { }  // Tylko swoje dane

@PostAuthorize("returnObject.owner == authentication.name")
public Resource getResource(Long id) { }  // Sprawdź po pobraniu
```

---

## 7. OAuth 2.0 / OpenID Connect

### OAuth 2.0 - co to?
Framework autoryzacji - delegowanie dostępu do zasobów.

"Pozwól aplikacji X działać w moim imieniu na serwisie Y"

**Nie jest protokołem autentykacji!** (ale często tak używany)

### Role w OAuth
- **Resource Owner** - użytkownik (właściciel danych)
- **Client** - aplikacja chcąca dostęp
- **Authorization Server** - wydaje tokeny (Google, Auth0)
- **Resource Server** - API z danymi

### Authorization Code Flow (najczęstszy)

```
┌──────────┐                                    ┌──────────────────┐
│  User    │                                    │ Authorization    │
│ (Browser)│                                    │ Server (Google)  │
└────┬─────┘                                    └────────┬─────────┘
     │                                                   │
     │  1. Click "Login with Google"                     │
     │──────────────────────────────────────────────────►│
     │                                                   │
     │  2. Redirect to Google login page                 │
     │◄──────────────────────────────────────────────────│
     │                                                   │
     │  3. User logs in, grants permission               │
     │──────────────────────────────────────────────────►│
     │                                                   │
     │  4. Redirect back with authorization CODE         │
     │◄──────────────────────────────────────────────────│
     │                                                   │
     │                  ┌──────────┐                     │
     │                  │  Your    │                     │
     │                  │  Server  │                     │
     │                  └────┬─────┘                     │
     │                       │                           │
     │  5. Send CODE to your server                      │
     │──────────────────────►│                           │
     │                       │                           │
     │                       │  6. Exchange CODE for TOKEN
     │                       │──────────────────────────►│
     │                       │                           │
     │                       │  7. Access Token + ID Token
     │                       │◄──────────────────────────│
     │                       │                           │
     │  8. Set session/token │                           │
     │◄──────────────────────│                           │
```

### OpenID Connect (OIDC)
Warstwa tożsamości na OAuth 2.0.

OAuth = "aplikacja może działać w imieniu użytkownika"
OIDC = "i wiemy kim jest ten użytkownik" (ID Token)

**ID Token:** JWT z danymi użytkownika (sub, email, name).

---

## 8. Best Practices

### Password Storage
```
NIGDY: plain text, MD5, SHA-1
ZAWSZE: bcrypt, Argon2, scrypt

// Spring Security
passwordEncoder.encode("password")  // BCrypt by default
```

### HTTPS Everywhere
Wszystkie połączenia szyfrowane. Wymuszaj redirect HTTP → HTTPS.

### Least Privilege
Użytkownicy i serwisy mają minimalne uprawnienia potrzebne do działania.

### Defense in Depth
Wiele warstw zabezpieczeń. Jeśli jedna zawiedzie - jest następna.

### Security Headers
```
Strict-Transport-Security: max-age=31536000
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
Content-Security-Policy: default-src 'self'
```

### Input Validation
Waliduj wszystko co przychodzi z zewnątrz. Whitelist > blacklist.

### Audit Logging
Loguj security events: login, logout, failed auth, permission denied.

---

## Podsumowanie

| Koncept | Znaczenie |
|---------|-----------|
| **AuthN vs AuthZ** | Kto jesteś vs Co możesz |
| **Stateful** | Session na serwerze |
| **Stateless** | Token zawiera wszystko |
| **JWT** | Self-contained token (header.payload.signature) |
| **Access + Refresh** | Krótki do API, długi do odnowienia |
| **httpOnly Cookie** | Bezpieczne przechowywanie tokenów |
| **OWASP Top 10** | Najczęstsze podatności |
| **OAuth 2.0** | Delegowanie autoryzacji |
| **OIDC** | Tożsamość na OAuth |
