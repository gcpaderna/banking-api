# Banking Ledger REST API

A REST API for bank accounts and transactions, built with Java 21 and Spring Boot. The main point is to apply core-banking rules to a modern stack. Every deposit and withdrawal is atomic. The transaction history is append-only. Money is handled with exact decimal arithmetic.

## Tech Stack

- **Java 21**, **Spring Boot 4**
- **Spring Data JPA / Hibernate**: ORM and repositories
- **MySQL**: relational database with CHECK constraints
- **Bean Validation**: request validation
- **springdoc-openapi**: OpenAPI docs and Swagger UI
- **Maven**

## Features

- Create and look up accounts
- Record deposits and withdrawals, each saved with the balance after the transaction
- Withdrawals that would overdraw the account are rejected
- Transaction history per account, newest first
- Input validation with clear HTTP error responses (400 / 404)
- *In progress:* AI-generated account insights (Spring AI)

## Design Decisions

| Decision | Why |
|---|---|
| **Atomic transactions** (`@Transactional`) | The balance update and the ledger entry commit or roll back together, so a failure never leaves a half-applied transaction. |
| **Append-only ledger** | Transactions can't be edited or deleted, only added. History stays auditable, like a real bank ledger. |
| **Balance changes only through transactions** | There is no endpoint that sets a balance directly, so the balance always matches the ledger. |
| **`BigDecimal` for money** | Avoids the rounding errors of `double`/`float`. Amounts are compared with `compareTo`. |
| **Database CHECK constraints** | `amount > 0` and the allowed transaction types are also enforced in the database, as a second line of defense behind application validation. |
| **Server-controlled fields** | New accounts always start at a balance of 0, with `id` and `createdAt` set by the server. Transaction requests use a DTO, so clients can't set `balanceAfter` or `createdAt`. |
| **Layered architecture** | Controller → Service → Repository. Business rules live in the service layer. |

## API Endpoints

Base path: `/api`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/accounts` | List all accounts |
| `GET` | `/accounts/{accountId}` | Get one account |
| `POST` | `/accounts` | Create an account (balance starts at 0) |
| `GET` | `/accounts/{accountId}/transactions` | List an account's transactions, newest first |
| `POST` | `/accounts/{accountId}/transactions` | Record a deposit or withdrawal |

### Example: record a transaction

```http
POST /api/accounts/1/transactions
Content-Type: application/json

{
  "type": "DEPOSIT",
  "amount": 50000,
  "description": "Salary",
  "category": "income"
}
```

### Error handling

| Case | Status |
|---|---|
| Invalid `type` (not `DEPOSIT` / `WITHDRAWAL`) | `400 Bad Request` |
| Missing, zero or negative `amount` | `400 Bad Request` |
| Withdrawal larger than the balance | `400 Bad Request` |
| Account does not exist | `404 Not Found` |

Stack traces are never included in error responses.

## Running Locally

### 1. Prerequisites

- Java 21
- MySQL 8

### 2. Create the database

```sql
CREATE DATABASE banking CHARACTER SET utf8mb4;
```

Then create the `account` and `transactions` tables by running [`schema.sql`](schema.sql), either in MySQL Workbench or with:

```bash
mysql -u your_mysql_user -p banking < schema.sql
```

### 3. Set environment variables

Credentials come from environment variables and are never committed.

```bash
export DB_USERNAME=your_mysql_user
export DB_PASSWORD=your_mysql_password
```

### 4. Run

```bash
./mvnw spring-boot:run      # macOS / Linux
.\mvnw spring-boot:run      # Windows PowerShell
```

### 5. Try it

Open Swagger UI: http://localhost:8080/swagger-ui/index.html

## Roadmap

- [ ] AI insights endpoint: Java computes the totals and category breakdown, and the LLM only writes them up as a short summary
- [ ] Docker Compose setup (app + MySQL)
- [ ] JUnit tests for the service layer
