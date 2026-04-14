# NovaBank

## Descripción del proyecto
NovaBank es una aplicación interactiva por consola que simula un sistema de gestión bancaria.
La aplicación maneja toda la lógica de negocio en una base de datos con PostgreSQL, para la
persistencia real de datos, permitiendo registrar clientes, crear cuentas bancarias y
gestionar transacciones atómicas para transferencias y aplica patrones de diseño clásicos
para garantizar un código mantenible y escalable.
Además, genera un historial detallado de movimientos.


## Tecnologías utilizadas
* **Java 17:** Lenguaje principal del proyecto.
* **Maven:** Herramienta para la gestión de dependencias y el ciclo de vida de construcción.
* **JUnit 5:** Framework para el desarrollo de pruebas unitarias.
* **Mockito:** Librería para la creación de *mocks* en los tests.
* **Git:** Sistema de control de versiones.
* **PostgreSQL** Motor de base de datos relacional.

## Requisitos del sistema
Para compilar y ejecutar este proyecto en un entorno local, es estrictamente necesario tener instalados:
* **Java:** Versión JDK 17 o superior.
* **Maven:** Versión 3.8 o superior.
* **PostgreSQL** Debe estar instalado y en ejecución en el puerto 5432.

## Variables de conexión
La configuración se centraliza en la clase `DatabaseConnectionManager`.
Para modificar el usuario o la contraseña, edita las constantes en 
dicha clase o el archivo `src/main/resources/database.properties`:

- URL: <jdbc:postgresql://localhost:5432/novabank>

## Arquitectura utilizada

[ Capa de Presentación ]         - Main (Menú interactivo) <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[   Capa de Servicio   ]        - Lógica de negocio y Validaciones  <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[ Capa de Persistencia ]         - Interfaces Repository e Implementaciones JDBC  <br>
&emsp; &emsp; &emsp; &emsp; ⬇️ <br>
[   Base de Datos      ]          - PostgreSQL (Tablas: clientes, cuentas, movimientos)  <br>

## Guía de uso

Abre una terminal en la carpeta raíz del proyecto y utiliza los siguientes comandos:

**Cómo compilar:**
```bash
mvn clean compile
```
**Cómo ejecutar:**
```bash
mvn exec:java
```
**Cómo ejecutar los tests:**
```bash
mvn test
```

## Patrones de diseño aplicados

- **Singleton:** Aplicado en `DatabaseConnectionManager` para garantizar una única instancia del gestor de conexiones.
- **Factory:** Utilizado en `MovimientoFactory` para centralizar la creación de los distintos tipos de movimientos bancarios.
- **Builder:** Implementado en `ClienteBuilder` para facilitar la creación legible de objetos cliente, especialmente útil en los tests.
- **Repository:** Capa de abstracción que separa la lógica de negocio del acceso a datos mediante JDBC.

**Estructura del proyecto:** 
    
- `org.alopsalv.novabank.model`: Contiene las clases de dominio (Cliente, Cuenta, Movimiento) y el Enum TipoMovimiento.
- `org.alopsalv.novabank.repository`: Gestiona la persistencia real de datos en PostgreSQL mediante JDBC.
- `org.alopsalv.novabank.service`: Contiene la lógica de negocio y las validaciones de las operaciones.
- `org.alopsalv.novabank.presentation.Main`: Clase principal que arranca la aplicación y gestiona el menú por consola.

**Enlace al repositorio de GitHub:** <https://github.com/alesalvatierra/NovaBank.git>