# java-sample-api

> 🚀 A sample **Spring Boot 3 REST API** designed to demonstrate automated k6 performance testing via GitHub Actions.

This repo is the companion to the [k6-auto-performance-testing](https://github.com/shadman-k6-mcp/k6-auto-pr-performance-tests) workflow. Every pull request is automatically load-tested with k6, and results are posted as a PR comment.

---

## API Endpoints

### Products

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/products` | List all products (optional `?category=` or `?search=`) |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/products` | Create a new product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Users

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/users` | List all users (optional `?role=`) |
| `GET` | `/api/users/{id}` | Get user by ID |
| `POST` | `/api/users` | Create a new user |
| `PUT` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |

### Health & Monitoring

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/` | Root endpoint — always 200 |
| `GET` | `/health` | Simple health check |
| `GET` | `/actuator/health` | Spring Boot Actuator health (detailed) |
| `GET` | `/actuator/metrics` | Metrics |
| `GET` | `/h2-console` | H2 database console (dev only) |

---

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.8+

```bash
git clone https://github.com/YOUR_USERNAME/java-sample-api.git
cd java-sample-api
mvn spring-boot:run
```

The app starts on **port 8080** with an in-memory H2 database seeded with 8 products and 5 users.

### Sample requests

```bash
# List all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Widget","description":"A test product","price":29.99,"stock":100,"category":"electronics"}'

# Filter by category
curl "http://localhost:8080/api/products?category=electronics"

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com","phone":"+1-555-0100","role":"user"}'

# Health check
curl http://localhost:8080/actuator/health
```

---

## k6 Performance Testing

### Pre-written k6 script: `tests/k6/performance-test.js`

A comprehensive k6 test is already committed. It covers:
- ✅ Health endpoints (`/`, `/health`, `/actuator/health`)
- ✅ `GET /api/products` — list & filter
- ✅ `GET /api/products/{id}` — read random seeded product
- ✅ `POST /api/products` — create with random data
- ✅ `GET /api/users` — list all
- ✅ `GET /api/users/{id}` — read random seeded user
- ✅ `POST /api/users` — create with random email (avoids 409)

**Thresholds:**
- P95 response time < 500ms
- Error rate < 1%

### Run locally

```bash
# Install k6: https://k6.io/docs/getting-started/installation/
k6 run tests/k6/performance-test.js
```

---

## GitHub Actions Setup

The workflow in `.github/workflows/k6-performance-test.yml` runs automatically on every PR.

### Required secret

| Secret | Where to get it |
|--------|-----------------|
| `GROQ_API_KEY` | [console.groq.com](https://console.groq.com) — free, used only if no k6 script exists |

Add it at: **Settings → Secrets and variables → Actions → New repository secret**

> Since `tests/k6/performance-test.js` already exists in this repo, **Groq is never called** — the existing script is reused on every PR.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2 |
| Language | Java 17 |
| Database | H2 (in-memory, auto-seeded) |
| Build | Maven |
| Persistence | Spring Data JPA / Hibernate |
| Monitoring | Spring Boot Actuator |
| Performance Testing | k6 |
| CI | GitHub Actions |
