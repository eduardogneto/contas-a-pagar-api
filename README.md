# Accounts Payable Management API

Desenvolvido por **Eduardo Gavronski Neto**

API REST para gestão de contas a pagar, com processamento assíncrono de importação via Kafka, persistência em PostgreSQL e autenticação JWT.

---

## Tecnologias

- Java 17
- Spring Boot 3.5.15
- Spring Data JPA
- PostgreSQL
- Flyway
- Apache Kafka
- Spring Security + JWT
- Springdoc OpenAPI (Swagger)
- Docker + Docker Compose
- JUnit 5 + Mockito

---

## Como executar

### Pré-requisitos
- Docker e Docker Compose instalados

### Subir toda a aplicação (banco + Kafka + API)

```bash
docker compose up -d --build
```

Isso sobe três containers:
- `contasapagar-postgres` - banco de dados (porta 5432)
- `contasapagar-kafka` - broker de mensageria (porta 9092)
- `contasapagar-app` - a API (porta 8080)

### Verificar se subiu corretamente

```bash
docker compose ps
docker compose logs app --tail 50
```

Procure por `Started ContasapagarApplication` nos logs.

### Parar a aplicação

```bash
docker compose down
```

---

## Swagger

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## Autenticação

```
username: admin
password: admin123
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Resposta:**
```json
{ "token": "TOKEN" }
```

Use esse token em todos os demais endpoints, no header:
```
Authorization: Bearer {token}
```

---

## Endpoints

### Suppliers

**Criar fornecedor**
```bash
curl -X POST http://localhost:8080/suppliers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"name": "Test Supplier LTDA"}'
```

**Buscar fornecedor por ID**
```bash
curl -X GET http://localhost:8080/suppliers/1 \
  -H "Authorization: Bearer {token}"
```

**Listar fornecedores**
```bash
curl -X GET http://localhost:8080/suppliers \
  -H "Authorization: Bearer {token}"
```

---

### Accounts

**Criar conta**
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "dueDate": "2026-07-15",
    "amount": 1500.00,
    "description": "Aluguel Julho",
    "supplierId": 1
  }'
```

**Buscar conta por ID**
```bash
curl -X GET http://localhost:8080/accounts/{id} \
  -H "Authorization: Bearer {token}"
```

**Atualizar conta**
```bash
curl -X PUT http://localhost:8080/accounts/{id} \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "dueDate": "2026-07-20",
    "amount": 1600.00,
    "description": "Aluguel Julho (reajustado)",
    "supplierId": 1
  }'
```

**Alterar status (pagar / cancelar)**
```bash
curl -X PATCH http://localhost:8080/accounts/{id}/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"status": "PAID", "paymentDate": "2026-07-10"}'
```

Valores possíveis para `status`: `PENDING`, `PAID`, `CANCELLED`.

**Listar contas (paginado, com filtros)**
```bash
curl -X GET "http://localhost:8080/accounts?description=aluguel&dueDate=2026-07-15&page=0&size=20" \
  -H "Authorization: Bearer {token}"
```

**Relatório de valor total pago por período**
```bash
curl -X GET "http://localhost:8080/accounts/reports/paid-amount?start=2026-07-01&end=2026-07-31" \
  -H "Authorization: Bearer {token}"
```

**Deletar conta**
```bash
curl -X DELETE http://localhost:8080/accounts/{id} \
  -H "Authorization: Bearer {token}"
```

---

### Importação assíncrona de contas via CSV

**Formato esperado do CSV:**
```csv
dueDate,amount,description,supplierId
2026-08-01,2000.00,Energia,1
2026-08-15,750.50,Telefone,1
```

**Enviar arquivo para importação**
```bash
curl -X POST http://localhost:8080/accounts/import \
  -H "Authorization: Bearer {token}" \
  -F "file=@caminho/para/accounts.csv"
```

**Resposta (202 Accepted):**
```json
{ "protocol": "uuid-gerado" }
```

Acompanhe nos logs:
```bash
docker compose logs app -f
```

---

## Rodando os testes

```bash
./mvnw test
```