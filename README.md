# 🛒 Store Service API

API REST desarrollada con **Spring Boot WebFlux** para la gestión de productos y pedidos, implementando control de concurrencia mediante **Optimistic Locking** para asegurar la consistencia del stock ante múltiples solicitudes simultáneas.

---

## Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Swagger / OpenAPI
- Reactor
- Maven

---

##  Arquitectura general

- **Controller**: Exposición de endpoints REST
- **Service**: Lógica de negocio y control transaccional
- **Repository**: Acceso reactivo a datos
- **DTOs**: Separación de modelos de entrada y salida
- **Optimistic Locking**: Control de concurrencia con campo `version`

---

## Requisitos previos

- Java 17 
- Maven 3.8.8
- PostgreSQL 16.7
- Git

Verificar versiones:

```bash
java -version
mvn -version
psql --version
```

---
## Base de datos

1. Abrir pgAdmin 4.
2. Conectarse al servidor PostgreSQL.
3. Abrir Query Tool.
4. Ejecutar el siguiente comando para crear la base de datos:
```bash
CREATE DATABASE storedb;
```
5. Una vez creado ejecutar los comandos de los archivos .sql adjuntados en la carpeta /docs del repositorio

---
## Levantar el proyecto store-service
1. Clonar el repositorio con el siguiente comando
```bash
git clone https://github.com/tu-usuario/store-service.git
cd store-service
```
2. En el archivo application.yml de la ruta store-service/src/main agregar tus valores de sesión(username y password) para la conexión a la base de datos
3. Una vez actualizado ejecutar el proyecto

---
## Documentación swagger
1. Podrás visualizar toda la documentación del servicio una vez levantado ingresando a la siguiente url
    http://localhost:8080/swagger-ui.html

Desde Swagger podrás:

- Ver todos los endpoints disponibles
- Revisar los modelos de request y response
- Probar las operaciones directamente desde el navegador

---
## Pruebas en postman
1. Abrir Postman.
2. Ir a My Workspace.
3. Seleccionar Import.
4. Importar el archivo store_service.postman_collection.json ubicado en /docs del repositorio
5. Ir a el collection "Store service" y crear la variable de con los siguientes valores:
    Variable: baseURL
    Value: http://localhost:8080

La colección incluye ejemplos para:

- CRUD de productos
- Creación y gestión de pedidos









