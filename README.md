# Foro Hub API 🗣️

API REST desarrollada con Spring Boot 3 que replica la funcionalidad
de un foro de discusión, permitiendo gestionar tópicos de manera segura.

## 🚀 Tecnologías

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + Flyway
- MySQL
- SpringDoc / Swagger UI
- Lombok

## 📋 Funcionalidades

- [x] Autenticación con JWT
- [ ] Crear tópico
- [ ] Listar tópicos (paginado)
- [ ] Detalle de tópico
- [ ] Actualizar tópico
- [ ] Eliminar tópico

## 🔧 Endpoints

| Método | Ruta          | Descripción       | Auth |
| ------ | ------------- | ----------------- | ---- |
| POST   | /login        | Autenticación     | No   |
| POST   | /topicos      | Crear tópico      | Sí   |
| GET    | /topicos      | Listar tópicos    | Sí   |
| GET    | /topicos/{id} | Detalle tópico    | Sí   |
| PUT    | /topicos/{id} | Actualizar tópico | Sí   |
| DELETE | /topicos/{id} | Eliminar tópico   | Sí   |

## ▶️ Cómo ejecutar

```bash
# Clonar el repositorio
git clone https://github.com/Juan9Walteros/forohub.git

# Configurar variables de ambiente
JWT_SECRET=tu_secreto
DB_URL=jdbc:mysql://localhost/forohub
DB_USERNAME=root
DB_PASSWORD=tu_password

# Ejecutar
./mvnw spring-boot:run
```

## 📄 Documentación

Una vez corriendo, accede a Swagger UI:
http://localhost:8080/swagger-ui.html
