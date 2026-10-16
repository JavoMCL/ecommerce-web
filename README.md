# Ecommerce Web

An online store built with Spring Boot and Thymeleaf. It has a product catalog organized by category, search, a per-user shopping cart, and an admin panel for managing products. It also exposes a REST API with JWT authentication.

This is a practice project: the goal was to build a complete store end to end (views, persistence, login, and API), not to have something production-ready. At the end of this README I've listed what's still missing.

## What it does

- Home page with the 10 latest available products.
- Browse by category and search by name or description.
- Product detail page with image.
- Registration and login (web form, plus JWT via the API).
- Per-user cart: add, change quantity, and remove items.
- `/admin` panel (`ADMIN` role only) to create, edit, and delete products and categories, including stock, availability, and image.
- API documentation with Swagger UI.

## Stack

- Java 17 and Spring Boot 3.4
- Spring Web, Spring Data JPA, and Spring Security
- Thymeleaf (with `thymeleaf-extras-springsecurity6`)
- PostgreSQL
- JWT with `jjwt`
- Springdoc OpenAPI
- Lombok
- Tests with JUnit and Mockito

## Structure

```text
src/main/java/com/ecommerce/ecommerce_web/
├── auth/         # login, registration, JWT, users and roles, security config
├── controller/   # MVC controllers (views) and REST controllers (API)
├── service/      # product and category logic
├── repository/   # Spring Data repositories
├── model/        # entities: Product, Category, CartItem
├── dto/          # ProductDTO, CategoryDTO
└── exception/    # exceptions and global handler

src/main/resources/
├── templates/    # Thymeleaf views
└── static/css/
```

## Running it

### Requirements

- JDK 17 or higher
- An empty PostgreSQL database (for example `ecommerce`)

You don't need to install Maven; the project includes the wrapper.

### Environment variables

| Variable | Purpose |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/ecommerce` |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |
| `JWT_KEY` | Key used to sign tokens, **Base64-encoded** |

To generate a valid key:

```bash
openssl rand -base64 32
```

In PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_KEY="<your base64 key>"
```

### Creating an admin user (optional)

If you set these three properties, an admin is created on startup if one doesn't already exist:

```
application.security.admin.name
application.security.admin.email
application.security.admin.password
```

As environment variables they look like this: `APPLICATION_SECURITY_ADMIN_NAME`, `APPLICATION_SECURITY_ADMIN_EMAIL`, and `APPLICATION_SECURITY_ADMIN_PASSWORD`.

Note: the configured email is always treated as admin at login, even if that user already existed with the `USER` role.

### Start the app

```bash
./mvnw spring-boot:run
```

On Windows: `./mvnw.cmd spring-boot:run`

The app runs at http://localhost:8080. Tables are created automatically on first startup (`ddl-auto=update`).

### Tests

```bash
./mvnw test
```

Right now there are unit tests for `ProductService` and `CategoryService`. Controllers, security, and the cart have no tests.

## Routes

### Web

| Route | Description | Access |
|---|---|---|
| `GET /` | Home | Public |
| `GET /category/{id}` | Products in a category | Public |
| `GET /search?query=` | Search | Public |
| `GET /product/{id}` | Product detail | Public |
| `GET /login`, `GET/POST /register` | Login and registration | Public |
| `GET /cart/view` | View cart | Logged in |
| `POST /cart/add/{id}`, `/cart/update`, `/cart/remove` | Modify cart | Logged in |
| `GET /admin` | Admin panel | `ADMIN` |

### REST API

The `/auth` endpoints are public. Everything else requires a session or a token in the `Authorization: Bearer <token>` header.

- `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`
- `GET /users/me`
- `GET /api/products`, `GET /api/products/{id}`, `GET /api/products/{id}/image` (image is public)
- `POST /api/products`, `PUT /api/products/{id}`, `DELETE /api/products/{id}`
- `GET /api/categories`, `GET /api/categories/{id}`
- `POST /api/categories`, `PUT /api/categories/{id}`, `DELETE /api/categories/{id}`

Product `POST` and `PUT` requests take `multipart/form-data` (name, description, price, `categoryId`, stock, availability, and an optional image). Category requests take JSON.

Swagger UI: http://localhost:8080/swagger-ui.html

## Things to keep in mind

This is what I know is pending or half-done:

- **Checkout only empties the cart.** There are no orders, payments, or stock deduction.
- **The write API doesn't check roles.** Only the `/admin` panel requires the `ADMIN` role; the `POST/PUT/DELETE` endpoints under `/api/products` and `/api/categories` only require being authenticated. They need `@PreAuthorize` before this is exposed anywhere.
- **CSRF is disabled** across the whole app, including the web forms.
- **No migrations.** Flyway is in the `pom.xml`, but there are no scripts; Hibernate generates the schema. For production this should move to migrations with `ddl-auto` set to `validate`.
- **Images are stored in the database** (a `bytea` column). Fine for a demo, but it won't scale with many products.
- **`show-sql=true`** is enabled in `application.properties`.
- **`jjwt` dependencies use different versions** (0.12.3, 0.12.5, and 0.12.6). They should be unified.
- Integration tests and a CI pipeline are still missing.