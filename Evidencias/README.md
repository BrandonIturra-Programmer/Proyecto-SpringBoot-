## 🔐 TP3: Seguridad

### Autenticación
- `POST /api/auth/registro`: crea un usuario con rol `USER`. La contraseña se guarda hasheada con el `PasswordEncoder` de Spring Security (nunca en texto plano). Responde `409 Conflict` si el email ya existe.
- `POST /api/auth/login`: valida las credenciales y devuelve un JWT firmado (con el id y el rol del usuario). Si fallan, responde `401 Unauthorized`.
- Un filtro (`JwtAuthFilter`) lee el header `Authorization: Bearer <token>`, lo valida y deja la identidad del usuario disponible para el resto del request.
- Son públicos `/api/auth/**`, `/api/productos/**` y `/health`. Todo lo demás exige token válido (sin token, inválido o vencido: `401`).

### Autorización: dónde vive y por qué
La regla de propiedad y de rol vive en **el Service**, en el método privado `ListaService.obtenerAutorizada(id, usuario)`. Busca la lista (404 si no existe) y deja pasar solo si el usuario es **ADMIN** o es el **dueño** de la lista; si no, lanza `AccesoDenegadoException`, que el manejador de excepciones convierte en `403 Forbidden`. Devolver 403 y no 404 evita confundir "no existe" con "no es tuya".

La elegí en el Service y no en el Controller por estos motivos:
- **Es una regla de negocio** ("quién puede tocar qué lista"), no un detalle de HTTP. El Controller solo traduce requests y responses.
- **Se reutiliza** en todas las operaciones sobre una lista puntual (ver, eliminar, listar favoritos, mover favoritos). Una sola implementación evita que un endpoint nuevo se olvide del chequeo.
- **Se puede probar sin levantar el servidor**, con tests unitarios del Service.
- **Necesita datos que el Controller no tiene**: para comparar al dueño con el usuario autenticado hay que cargar la lista, y eso es responsabilidad del Service.

El filtro JWT solo **autentica** (¿quién sos?); la **autorización** sobre cada recurso (¿podés tocar este?) se decide en el Service.

Además, al crear una lista el dueño es siempre el usuario autenticado (nunca un dato del body), y `GET /api/listas` devuelve solo las listas del usuario (o todas, si es ADMIN).

### Variables de entorno
Los datos sensibles no están fijos en el código. `application.properties` usa `${VARIABLE:valorPorDefecto}`, así que en local funciona sin configurar nada:

| Variable | Uso |
|---|---|
| `DB_URL` | URL JDBC de PostgreSQL (`jdbc:postgresql://host/base`) |
| `DB_USER` | Usuario de la base |
| `DB_PASSWORD` | Contraseña de la base |
| `JWT_SECRET` | Secreto para firmar los JWT |
| `PORT` | Puerto del servidor (por defecto `8080`) |

### Cómo correr los tests
Los tests de integración usan **Testcontainers**, que levanta un PostgreSQL real en Docker; Flyway corre las migraciones contra esa base limpia en cada ejecución. Por eso **hace falta Docker instalado y corriendo**.

```bash
cd api-blank
./mvnw test
```

Para correr solo los unitarios (no requieren Docker):
```bash
./mvnw test -Dtest="*ServiceTest"
```

Qué cubre la suite:
- **Unitarios:** la lista creada queda asociada al usuario autenticado (`ListaServiceTest`) y nunca se guarda una contraseña en texto plano (`UsuarioServiceTest`).
- **Integración:** login válido (200 con token), login inválido (401), lista propia (200), lista ajena (403), `/api/listas` sin token (401) y ADMIN sobre lista ajena (200).

### Integración continua (GitHub Actions)
El workflow `.github/workflows/ci.yml` corre en **cada push**: instala Java 21 y ejecuta `mvn -B verify`, que compila y corre toda la suite (incluidas las migraciones contra una base limpia vía Testcontainers).

Cómo leer el resultado en la pestaña **Actions** del repositorio:
- 🟡 amarillo: en curso.
- ✅ verde: compiló y todos los tests pasaron.
- ❌ rojo: algo falló. Entrá a la corrida, abrí el job `build-y-tests` y buscá al final del log las líneas `[ERROR]` y `Tests run:`, que indican qué test falló.

Se comprobó que el workflow falla al romper un test a propósito y vuelve a pasar al arreglarlo (ver el historial de corridas en Actions).

Historial de corridas: https://github.com/BrandonIturra-Programmer/Proyecto-SpringBoot-/actions

### Despliegue
La aplicación está desplegada en **Render** (plan gratuito) con una base **PostgreSQL** de Render. Se usó Render porque Koyeb, el ejemplo del enunciado, ya no admite cuentas gratuitas nuevas.

- URL pública: https://proyecto-springboot-cxrt.onrender.com
- Estado: https://proyecto-springboot-cxrt.onrender.com/health
- Swagger: https://proyecto-springboot-cxrt.onrender.com/swagger-ui.html

Pasos que se siguieron:
1. Crear una base **PostgreSQL** gratuita en Render (**New, Postgres**).
2. Crear un **Web Service** desde el repositorio de GitHub, con lenguaje **Docker**, **Root Directory** `api-blank` e **Instance Type** Free (la imagen se construye con el `Dockerfile` del proyecto).
3. Cargar las variables de entorno `DB_URL`, `DB_USER`, `DB_PASSWORD` y `JWT_SECRET`. Para `DB_URL` se usa la URL interna de la base con prefijo `jdbc:postgresql://` y sin usuario ni contraseña.
4. Configurar el **Health Check Path** en `/health` y desplegar.
5. Verificar en **Logs** que Flyway se conectó a la base remota y aplicó las migraciones, y que la aplicación arrancó sin errores.
6. Verificar `GET /health` desde la URL pública y un endpoint protegido: `GET /api/listas` responde `401` sin token y `200` con un token válido.

> Nota: el plan gratuito se duerme tras 15 minutos sin tráfico (el primer pedido tarda cerca de un minuto), y la base gratuita expira a los 30 días.