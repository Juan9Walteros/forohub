# ForoHub API

API REST que replica la funcionalidad de un foro de discusión, permitiendo a usuarios autenticados gestionar tópicos de manera segura. Desarrollada como Challenge del programa Oracle Next Education + Alura Latam.

---

## Tecnologías

- Java 17
- Spring Boot 3
- Spring Security
- JSON Web Token (JWT) — Auth0 java-jwt
- Spring Data JPA + Hibernate
- Flyway (migraciones de base de datos)
- PostgreSQL
- SpringDoc / Swagger UI
- Lombok

---

## Funcionalidades

- Autenticación con JWT
- Crear tópico
- Listar tópicos (paginado, ordenado por fecha)
- Ver detalle de un tópico
- Actualizar tópico
- Eliminar tópico
- Validación de duplicados (mismo título y mensaje)
- Documentación interactiva con Swagger UI

---

## Endpoints

| Método | Ruta            | Descripción            | Requiere Auth |
|--------|-----------------|------------------------|---------------|
| POST   | `/login`        | Autenticación          | No            |
| POST   | `/topicos`      | Crear tópico           | Sí            |
| GET    | `/topicos`      | Listar tópicos         | Sí            |
| GET    | `/topicos/{id}` | Detalle de un tópico   | Sí            |
| PUT    | `/topicos/{id}` | Actualizar tópico      | Sí            |
| DELETE | `/topicos/{id}` | Eliminar tópico        | Sí            |

---

## Cómo ejecutar

### Requisitos previos

- Java 17
- PostgreSQL corriendo en `localhost:5432`
- Base de datos `forohub` creada

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/Juan9Walteros/forohub.git
cd forohub

# 2. Configurar la base de datos en src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/forohub
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# 3. (Opcional) Definir el secreto JWT como variable de entorno
export JWT_SECRET=tu_secreto_seguro

# 4. Ejecutar
./mvnw spring-boot:run
```

Flyway creará las tablas automáticamente al iniciar.

La API quedará disponible en: `http://localhost:8081`

---

## Cómo usar la API

### 1. Autenticarse y obtener el token

```bash
curl -X POST http://localhost:8081/login \
  -H "Content-Type: application/json" \
  -d '{"login":"usuario","clave":"contraseña"}'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Usar el token en las siguientes solicitudes

```bash
curl http://localhost:8081/topicos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 3. Crear un tópico

```bash
curl -X POST http://localhost:8081/topicos \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Duda sobre Spring Security",
    "mensaje": "¿Cómo configuro el filtro JWT?",
    "autor": "Juan",
    "curso": "Spring Boot"
  }'
```

---

## Documentación Swagger

Con la aplicación corriendo, accede a:

```
http://localhost:8081/swagger-ui/index.html
```

---

## Estructura del proyecto

```
src/main/java/com/aluracursos/forohub/
├── controller/
│   ├── AutenticacionController.java
│   └── TopicoController.java
├── domain/
│   ├── topico/
│   │   ├── Topico.java
│   │   ├── TopicoRepository.java
│   │   ├── DatosRegistroTopico.java
│   │   ├── DatosActualizarTopico.java
│   │   ├── DatosDetalleTopico.java
│   │   ├── DatosListadoTopico.java
│   │   └── StatusTopico.java
│   └── usuario/
│       ├── Usuario.java
│       ├── UsuarioRepository.java
│       └── DatosAutenticacion.java
└── infra/
    ├── errores/
    │   └── TratadorDeErrores.java
    ├── security/
    │   ├── SecurityConfigurations.java
    │   ├── SecurityFilter.java
    │   ├── AutenticacionService.java
    │   └── TokenService.java
    └── springdoc/
        └── SpringDocConfiguration.java
```

---

## Autor

Juan Walteros — Challenge ONE Back End — Oracle Next Education + Alura Latam
