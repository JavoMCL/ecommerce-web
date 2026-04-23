# Ecommerce Web (Spring Boot)

Aplicacion web de ecommerce construida con **Spring Boot 3**, con interfaz server-side usando **Thymeleaf**, persistencia con **JPA/Hibernate + PostgreSQL**, y seguridad con **Spring Security** (inicio de sesion por formulario + endpoints JWT para API).

El proyecto combina dos estilos:
- **Flujo web MVC** para tienda, carrito y vistas HTML.
- **Flujo API REST** para integraciones administrativas o clientes externos.

---

## Caracteristicas principales

- Catalogo de productos y categorias.
- Busqueda por nombre o descripcion.
- Carrito de compras por usuario autenticado.
- Modulo administrativo protegido por rol `ADMIN`.
- Registro/login de usuarios y emision de tokens JWT.
- Subida de imagenes de producto en disco local (`uploads/`).
- Documentacion de API con Swagger UI.

---

## Stack tecnologico

- **Java 17**
- **Spring Boot 3.4.x**
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf + `thymeleaf-extras-springsecurity6`
- PostgreSQL
- JWT (`jjwt`)
- Springdoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

---

## Arquitectura (alto nivel)

La estructura sigue una separacion por capas:

- `controller/`: controladores MVC y REST.
- `Service/`: logica de negocio (`ProductoService`, `CategoriaService`).
- `Repository/`: acceso a datos con Spring Data JPA.
- `model/`: entidades de dominio (`Producto`, `Categoria`, `CarritoItem`).
- `auth/`: autenticacion/autorizacion (JWT, usuarios, roles, tokens, config de seguridad).
- `src/main/resources/templates/`: vistas Thymeleaf.

Esto permite mantener el dominio desacoplado de la infraestructura y facilita evolucionar el proyecto (por ejemplo, separar API publica e interna en el futuro).

---

## Requisitos

- JDK 17+
- PostgreSQL disponible
- Maven (opcional, porque se incluye Wrapper)

---

## Variables de entorno

El proyecto usa variables de entorno para base de datos y JWT:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_KEY`

Opcionalmente puedes crear un admin inicial:

- `application.security.admin.name`
- `application.security.admin.email`
- `application.security.admin.password`

> Nota: estas tres propiedades se leen desde configuracion de Spring (por ejemplo en variables de entorno del proceso, argumentos `--`, o archivo de perfil externo).

Ejemplo rapido (PowerShell):

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_KEY="TU_CLAVE_BASE64_O_SECRETA"
```

---

## Configuracion relevante

Archivo: `src/main/resources/application.properties`

- `spring.jpa.hibernate.ddl-auto=validate`
  - El esquema debe existir y estar alineado con las entidades.
- `spring.profiles.active=dev`
- Swagger/OpenAPI:
  - `springdoc.api-docs.path=/v3/api-docs`
  - `springdoc.swagger-ui.path=/swagger-ui.html`

---

## Ejecucion local

En Windows (PowerShell):

```powershell
./mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

Aplicacion web: `http://localhost:8080/`

---

## Documentacion API (Swagger)

- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Si la UI carga pero no muestra endpoints, revisa:
- que la app arranco sin errores,
- que `v3/api-docs` responde `200`,
- y que seguridad permite `"/v3/api-docs"` y `"/v3/api-docs/**"`.

---

## Endpoints principales

### Web (MVC)

- `GET /` - Home con ultimos productos.
- `GET /categoria/{id}` - Productos por categoria.
- `GET /buscar?query=...` - Busqueda de productos.
- `GET /carrito/ver` - Vista carrito del usuario autenticado.
- `GET /admin` - Panel admin (requiere rol `ADMIN`).
- `GET /login`, `GET/POST /registro` - Vistas de autenticacion.

### API REST

- Auth (`/auth`)
  - `POST /auth/register`
  - `POST /auth/login`
  - `POST /auth/refresh`
- Productos (`/api/productos`)
  - `GET /api/productos`
  - `POST /api/productos`
  - `PUT /api/productos/{id}`
  - `DELETE /api/productos/{id}`
- Categorias (`/api/categorias`)
  - `GET /api/categorias`
  - `POST /api/categorias`
  - `PUT /api/categorias/{id}`
  - `DELETE /api/categorias/{id}`
- Usuario actual
  - `GET /users/me`

---

## Seguridad y autenticacion

- `formLogin` para navegacion web (`/login`).
- JWT para endpoints API (`Authorization: Bearer <token>`).
- Roles disponibles:
  - `ROLE_USER`
  - `ROLE_ADMIN`
- Control de acceso por metodo en rutas sensibles (ej. `@PreAuthorize("hasRole('ADMIN')")`).

---


## Estructura del proyecto

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

## Troubleshooting rapido

- **Error de conexion a DB**: valida `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
- **Error por esquema** (`ddl-auto=validate`): aplica migraciones o alinea entidades/tablas.
- **Swagger sin endpoints**: prueba `GET /v3/api-docs` directamente.
- **403/redirect inesperado**: revisa autenticacion activa y permisos de rol.
- **Problemas con imagenes**: confirma permisos de escritura en `uploads/`.

---

## Roadmap sugerido

- Migraciones versionadas con Flyway/Liquibase.
- Tests de integracion para seguridad y carrito.
- Observabilidad (logs estructurados, metricas, health checks).
- CI/CD con validaciones de build, test y calidad.

---
