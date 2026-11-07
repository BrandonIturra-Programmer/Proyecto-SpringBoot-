# Web II - Trabajo Practico

## 🏛️ Estructura del Proyecto
El proyecto implementa una separación de responsabilidades:
- **`controller`**: Endpoints de la API REST y manejo de peticiones HTTP.
- **`service`**: Lógica de negocio.
- **`repository`**: Persistencia y acceso a datos.
- **`model` / `dto`**: Entidades de dominio y objetos de transferencia de datos.
- **`client`**: Consumo de APIs externas.
- **`exception`**: Manejo centralizado de errores y validaciones.

## 🛠️ Stack Tecnológico
- **Java 21**
- **Spring Boot** (Web, Validation, SpringDoc OpenAPI / Swagger)
- **PostgreSQL** con **Flyway** para las migraciones
- **Maven**

## 🚀 Instrucciones para Levantar el Proyecto (Proxima revisión)
1. Abrir el proyecto en Visual Studio Code.
2. Abrir la clase principal `ApiBlankApplication.java` dentro de `src/main/java/ar/edu/unvime/api_blank/`.
3. Hacer clic en el botón **Run**

## Puertos y adaptadores: que cambio y que no

`FavoritoRepository` es un **puerto**: una interfaz que define qué operaciones
existen (listar, buscar, guardar, eliminar), sin decir como se hacen.
`FavoritoService` depende solo de ese contrato, no de una clase concreta.

En el TP1 el contrato lo cumplía un adapter en memoria. Ahora lo cumple
`FavoritoRepositoryAdapter`, que usa JPA y PostgreSQL. Como los dos cumplen
la misma interfaz, son intercambiables: cambié el adapter, pero el Service
y el Controller no se enteraron.

### Clases nuevas para pasar a JPA
- `FavoritoEntity`
- `FavoritoJpaRepository`
- `FavoritoRepositoryAdapter`

### Clases que quedaron iguales
- `FavoritoService`
- `FavoritoController`
- DTOs de favoritos
- `Favorito` (dominio)


## Evolución del esquema
Para hacer `lista_id` obligatorio se creo una migracion nueva (`V4`) y no se
edito la `V3`. Flyway guarda un checksum de cada migracion ya aplicada y, si el
archivo cambia, se niega a arrancar. Además, las bases que ya ejecutaron la V3
no volverian a correrla, así que quedarian distintas de una base nueva. Con una
migración nueva, todas las bases siguen el mismo camino y terminan iguales.

## Transacciones
`moverFavoritos`reasigna cada favorito a la listad destino y después elimina la lista origen. 
Sin `@Transactional`, cada escritura se confirma por separado. 
Si el borrado de la lista fallara después de reasignar los favoritos, esos cambios ya estarían 
guardados y no se deshacerian. Entonces la base quedaría a medio camino, con la operación hecha solo en
parte.

## Cómo levantar PostgreSQL y las migraciones
Utilizamos base local..
Al levantar la app, Flyway aplica solo las migraciones de
`src/main/resources/db/migration`
Podemos buscarla en el log`Successfully applied ... migration` o consultá desde psql:

    SELECT version, description, success FROM flyway_schema_history;

## Pruebas
El archivo `api-blank/pruebas-tp2.http` tiene casos de éxito y de error de
listas, favoritos y mover favoritos. Se ejecuta con la extensión REST Client.

## Autorización
Quien puede acceder a una lista vive en `ListaService`, en el
método `obtenerAutorizada`. Ahí se comprueba que la lista pertenezca al usuario
o que su rol sea ADMIN; si no, se responde 403.

La puse en el Service y no en el Controller porque es lógica de negocio, no de
HTTP.

JWT solo autentica (¿quién sos?); decidir qué puede hacer cada
usuario (¿es tuyo?) depende de los datos de la lista, y por eso se resuelve en
el Service.

## Evidencias del TP3

Las capturas y la documentación del TP3 están en la carpeta [`Evidencias`](Evidencias/). El detalle de cómo correr los tests, leer el CI y los pasos del despliegue.

**Despliegue en Render:** https://proyecto-springboot-cxrt.onrender.com
