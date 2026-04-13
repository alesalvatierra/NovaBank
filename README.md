# NovaBank

## Descripción del proyecto
NovaBank es una aplicación interactiva por consola que simula un sistema de gestión bancaria. La aplicación maneja toda la lógica de negocio en memoria, permitiendo registrar clientes, crear cuentas bancarias y realizar transacciones como depósitos, retiros y transferencias, además de generar un historial detallado de movimientos.

## Tecnologías utilizadas
* **Java 17:** Lenguaje principal del proyecto.
* **Maven:** Herramienta para la gestión de dependencias y el ciclo de vida de construcción.
* **JUnit 5:** Framework para el desarrollo de pruebas unitarias.
* **Mockito:** Librería para la creación de *mocks* en los tests.
* **Git:** Sistema de control de versiones.

## Requisitos del sistema
Para compilar y ejecutar este proyecto en un entorno local, es estrictamente necesario tener instalados:
* **Java:** Versión JDK 17 o superior.
* **Maven:** Versión 3.8 o superior.

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
**Estructura del proyecto:** 
    
- `org.alopsalv.novabank.model`: Contiene las clases de dominio (Cliente, Cuenta, Movimiento) y el Enum TipoMovimiento.
- `org.alopsalv.novabank.repository`: Simula el acceso a datos gestionando el almacenamiento temporal en memoria.
- `org.alopsalv.novabank.service`: Contiene la lógica de negocio y las validaciones de las operaciones.
- `org.alopsalv.novabank.presentation.Main`: Clase principal que arranca la aplicación y gestiona el menú por consola.

**Enlace al repositorio de GitHub:** <https://github.com/alesalvatierra/NovaBank.git>