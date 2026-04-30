# NovaBank

## Descripción del proyecto
NovaBank es una API RESTful desarrollada con Spring Boot que simula el núcleo de un sistema de gestión bancaria.
La aplicación maneja toda la lógica de negocio apoyándose en el ecosistema Spring, garantizando la persistencia de datos en PostgreSQL mediante Spring Data JPA. Permite registrar clientes, crear cuentas bancarias y gestionar transacciones financieras de forma segura.
En esta versión, la arquitectura ha evolucionado para incluir seguridad mediante JSON Web Tokens (JWT), validación robusta de datos de entrada, manejo global de excepciones y documentación interactiva mediante Swagger / OpenAPI.

## Tecnologías utilizadas
* **Java 17:** Lenguaje principal del proyecto.
* **Spring Boot 3:** Framework base para la API REST, Inyección de Dependencias y configuración automática.
* **Spring Security & JWT:** Para la autenticación y protección de los endpoints.
* **Spring Data JPA / Hibernate:** ORM para el mapeo de entidades y abstracción total de la base de datos.
* **Jakarta Validation:** Para el blindaje y validación de los DTOs de entrada.
* **Swagger / OpenAPI 3:** Documentación interactiva de la API.
* **PostgreSQL:** Motor de base de datos relacional.
* **JUnit 5 & Mockito:** Frameworks para el desarrollo de pruebas unitarias aislando la lógica de negocio.
* **Maven:** Herramienta para la gestión de dependencias y el ciclo de construcción.

## Requisitos del sistema
Para compilar y ejecutar este proyecto en un entorno local, es estrictamente necesario tener instalados:
* **Java:** Versión JDK 17 o superior.
* **Maven:** Versión 3.8 o superior.
* **PostgreSQL:** Debe estar instalado y en ejecución en el puerto 5432.

## Variables de conexión
La configuración de la base de datos y de la aplicación ya no se realiza manualmente. Ahora está centralizada por Spring Boot en el archivo `src/main/resources/application.properties`.
Asegúrate de que tus credenciales locales coinciden con las especificadas en dicho archivo:

- URL: <jdbc:postgresql://localhost:5432/novabank>

## Arquitectura utilizada

[ Capa Web / API ]               - Controladores REST (@RestController) <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[ Capa de Aislamiento ]          - DTOs (Data Transfer Objects) y Validaciones <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[ Capa de Negocio ]              - Servicios Transaccionales (@Service) y Excepciones <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[ Capa de Persistencia ]         - Interfaces genéricas de Spring Data (JpaRepository) <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[ Base de Datos ]                - PostgreSQL (Tablas automáticas generadas por Hibernate) <br>

## Guía de uso

Abre una terminal en la carpeta raíz del proyecto y utiliza los siguientes comandos:

**Cómo compilar y descargar dependencias:**
```bash
mvn clean install
```
**Cómo ejecutar la aplicación (Spring Boot):**
```bash
mvn spring-boot:run
```
**Cómo ejecutar las pruebas unitarias:**
```bash
mvn test
```
## Pruebas Manuales con Swagger UI

Una vez la aplicación esté en ejecución, puedes probar todos los endpoints de forma visual desde tu navegador:

1. Accede a: `http://localhost:8080/swagger-ui/index.html`
2. Utiliza el endpoint `POST /api/v1/clientes` para registrar un usuario *(anota el email y DNI)*.
3. Dirígete a `POST /api/v1/auth/login`. Introduce el **email** como `username` y el **DNI** como `password`.
4. Copia el token devuelto, haz clic en el botón verde **"Authorize"** en la parte superior y pégalo. ¡Ya puedes probar el resto de la API!

---

## Patrones de Diseño Aplicados

| Patrón | Descripción |
|--------|-------------|
| **DTO** *(Data Transfer Object)* | Encapsula los datos enviados entre el cliente web y el servidor, aislando las entidades JPA reales de la vista pública. |
| **Dependency Injection / IoC** | Gestión automática de la instanciación de servicios y repositorios, reemplazando los antiguos Singleton y Factories manuales. |
| **Controller Advice** | Utilizado en `GlobalExceptionHandler` para interceptar de manera centralizada todas las excepciones y devolver respuestas HTTP formateadas. |
| **Repository** | Abstracción del acceso a datos donde Spring Data genera las sentencias SQL en tiempo de ejecución. |

---

## 📁 Estructura del Proyecto
org.alopsalv.novabank <br>
├── controller # Expone los endpoints de la API REST <br>
├── dto # Clases y Mappers para el traslado seguro de información <br>
├── security # Filtros JWT y configuración de accesos <br>
├── exception # Manejo centralizado de errores <br>
├── service # Lógica matemática y de validación de negocio <br>
├── model # Entidades de dominio mapeadas a base de datos <br>
└── repository # Interfaces para el guardado de datos

---

## 🔗 Repositorio

[![GitHub](https://img.shields.io/badge/GitHub-NovaBank-181717?style=for-the-badge&logo=github)](https://github.com/alesalvatierra/NovaBank.git)

> 📌 `https://github.com/alesalvatierra/NovaBank.git`
