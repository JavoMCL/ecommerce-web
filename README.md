# Ecommerce Web (Spring Boot)

Ecommerce web application built with **Spring Boot 3**, server-side rendered with **Thymeleaf**, persistence using **JPA/Hibernate + PostgreSQL**, and security handled by **Spring Security** (form-based sign-in + JWT endpoints for the API).

The project combines two workflows:
- **MVC web flow** for the storefront, cart, and HTML views.
- **REST API flow** for admin integrations or external clients.

---

## Key features

- Product and category catalog.
- Search by name or description.
- Shopping cart per authenticated user.
- Admin module protected by the `ADMIN` role.
- User registration/sign-in and JWT token issuance.
- Product image uploads stored locally (`uploads/`).
- API documentation with Swagger UI.

---

## Tech stack

- **Java 17**
- **Spring Boot 3.4.x**
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf + `thymeleaf-extras-springsecurity6`
- PostgreSQL
- Flyway
- Spring Boot Validation
- Spring Boot DevTools
- Lombok
- JWT (`jjwt`)
- Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

---

## Architecture (high level)

The structure follows a layered approach:

- `controller/`: MVC and REST controllers.
- `Service/`: business logic (`ProductService`, `CategoryService`).
- `Repository/`: data access with Spring Data JPA.
- `model/`: domain entities (`Product`, `Category`, `CartItem`).
- `auth/`: authentication/authorization (JWT, users, roles, tokens, security config).
- `src/main/resources/templates/`: Thymeleaf views.

This keeps the domain decoupled from infrastructure and makes it easier to evolve the project (for example, splitting public and internal APIs in the future).

---

## Requirements

- JDK 17+
- PostgreSQL available
- Maven (optional, because the Wrapper is included)

---

## Environment variables

The project uses environment variables for the database and JWT:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_KEY`

Optionally, you can create an initial admin:

- `application.security.admin.name`
- `application.security.admin.email`
- `application.security.admin.password`

> Note: these three properties are read from Spring configuration (for example from process environment variables, `--` arguments, or an external profile file).

Quick example (PowerShell):

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_KEY="YOUR_BASE64_OR_SECRET_KEY"
```

---

## Relevant configuration

File: `src/main/resources/application.properties`

- `spring.jpa.hibernate.ddl-auto=validate`
  - The schema must already exist and match the entities.
- `spring.profiles.active=dev`
- Swagger/OpenAPI:
  - `springdoc.api-docs.path=/v3/api-docs`
  - `springdoc.swagger-ui.path=/swagger-ui.html`

---

## Local run

On Windows (PowerShell):

```powershell
./mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

Web application: `http://localhost:8080/`

---

## API documentation (Swagger)

- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

If the UI loads but does not show endpoints, check:
- that the app started without errors,
- that `v3/api-docs` returns `200`,
- and that security allows `"/v3/api-docs"` and `"/v3/api-docs/**"`.

---

## Main endpoints

### Web (MVC)

- `GET /` - Home with latest products.
- `GET /category/{id}` - Products by category.
- `GET /search?query=...` - Product search.
- `GET /cart/view` - Cart view for the authenticated user.
- `GET /admin` - Admin panel (requires `ADMIN` role).
- `GET /login`, `GET/POST /register` - Authentication views.

### REST API

- Auth (`/auth`)
  - `POST /auth/register`
  - `POST /auth/login`
  - `POST /auth/refresh`
- Products (`/api/productos`)
  - `GET /api/productos`
  - `POST /api/productos`
  - `PUT /api/productos/{id}`
  - `DELETE /api/productos/{id}`
- Categories (`/api/categorias`)
  - `GET /api/categorias`
  - `POST /api/categorias`
  - `PUT /api/categorias/{id}`
  - `DELETE /api/categorias/{id}`
- Current user
  - `GET /users/me`

---

## Security and authentication

- `formLogin` for web navigation (`/login`).
- JWT for API endpoints (`Authorization: Bearer <token>`).
- Available roles:
  - `ROLE_USER`
  - `ROLE_ADMIN`
- Method-level access control on sensitive routes (e.g. `@PreAuthorize("hasRole('ADMIN')")`).

---

## Project structure

```text
ecommerce-web/
  src/main/java/com/ecommerce/ecommerce_web/
	auth/
	controller/
	Service/
	Repository/
	model/
	exception/
  src/main/resources/
	templates/
	application.properties
  uploads/
  pom.xml
```

---

## Quick troubleshooting

- **Database connection error**: validate `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
- **Schema error** (`ddl-auto=validate`): apply migrations or align entities/tables.
- **Swagger with no endpoints**: try `GET /v3/api-docs` directly.
- **403 / unexpected redirect**: check the active authentication and role permissions.
- **Image issues**: confirm write permissions on `uploads/`.

---

## Suggested roadmap

- Extend and maintain versioned migrations with Flyway.
- Evaluate Liquibase only if a second migration strategy is needed later.
- Integration tests for security and cart flows.
- Observability (structured logs, metrics, health checks).
- CI/CD with build, test, and quality validations.

---
