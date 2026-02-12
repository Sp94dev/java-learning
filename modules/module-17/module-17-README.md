# Moduł 17: Frontend Integration


## Cel

Połączyć Java backend z Angular frontend.

---

## Tematy do opanowania

### 1. Static Files Serving

- [ ] Spring Boot jako serwer plików statycznych
- [ ] `src/main/resources/static/`
- [ ] Monolith: backend + frontend w jednym JAR
- [ ] Kiedy to ma sens (proste aplikacje, admin panele)

### 2. Proxy Setup (Angular + Spring Boot)

- [ ] Angular dev server proxy (`proxy.conf.json`)
- [ ] Przekierowanie `/api/*` → Spring Boot
- [ ] CORS configuration w Spring
- [ ] Wspólne uruchamianie w dev mode

### 3. Docker Compose (Nginx + Java)

- [ ] Nginx jako reverse proxy
- [ ] Nginx serwuje Angular bundle
- [ ] Nginx proxy pass do Spring Boot (`/api/`)
- [ ] Docker Compose: nginx + app + db

### 4. nginx.conf

- [ ] `location /` → Angular (SPA, try_files)
- [ ] `location /api/` → Spring Boot (proxy_pass)
- [ ] Gzip compression
- [ ] Cache headers dla static assets

### 5. Build Pipeline

- [ ] Angular build → `dist/`
- [ ] Spring Boot build → JAR
- [ ] Multi-stage Docker: build both, serve with Nginx
- [ ] CI/CD: build frontend + backend w jednym pipeline

---

## Przykład: proxy.conf.json (Angular dev)

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

---

## Przykład: nginx.conf

```nginx
server {
    listen 80;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://app:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## Przykład: Docker Compose

```yaml
services:
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
      - ./frontend/dist:/usr/share/nginx/html
    depends_on:
      - app

  app:
    build: ./backend
    expose:
      - "8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
```

---

## Ćwiczenia

1. Skonfiguruj Angular proxy do Spring Boot
2. Skonfiguruj CORS w Spring Security
3. Napisz nginx.conf z proxy pass
4. Stwórz Docker Compose (nginx + app + db)
5. Zbuduj i uruchom pełny stack lokalnie

---

## Sprawdzian gotowości

- [ ] Angular komunikuje się z Spring Boot przez proxy
- [ ] CORS skonfigurowany poprawnie
- [ ] Nginx serwuje frontend i proxy'uje API
- [ ] Docker Compose uruchamia pełny stack
