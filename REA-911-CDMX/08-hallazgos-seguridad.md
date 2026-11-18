# 08 — Hallazgos de seguridad (priorizados)

Severidad cualitativa (impacto × facilidad), en el contexto de un servicio de **emergencias**.
Cada hallazgo cita su evidencia; no se infiere causa sin respaldo.

---

## P1 — Backend de emergencias 100% en HTTP en claro

**Qué:** todas las operaciones sensibles viajan sin TLS a `http://apps.c5.cdmx.gob.mx/WebServices/…`:
crear incidente (`CreateIncidente`), **ubicación en tiempo real** (`Followme`,
`GetlocationthismovilApp`), **alerta a la red** (`AlertEmergencyRed`), **ficha de salud**
(`registryHealthCard` → tipo de sangre), **OTP de registro** (`GetBackCode`) y validar
número de extorsión (`validarNumero`).

**Evidencia:** 30+ URLs `http://` en `utils.DataRegister` y en los strings del DEX
(`decompiled/endpoints.txt`); `android:usesCleartextTraffic="true"` sin `networkSecurityConfig`
(doc 01); `VolleySingleton` con Volley por defecto, sin TLS reforzado (doc 02).

**Impacto:** un atacante en la ruta de red (Wi-Fi pública, operador, MITM) puede **leer**
ubicación, salud, OTP y contenido del auxilio, y **alterar o suprimir** una solicitud de
emergencia o inyectar incidentes falsos. En una app de auxilio esto es crítico.

**Remediación:** forzar HTTPS en todos los endpoints, `usesCleartextTraffic="false"`,
`networkSecurityConfig` restrictivo y, dado el perfil, **certificate pinning**.

---

## P2 — IPs hardcodeadas en producción (una interna)

**Qué:** `http://10.19.113.18/WcfServices/…/EavisaRed` (IP **privada** RFC1918) e
`http://187.174.224.151/WebServices/` (IP pública) embebidas en el binario.

**Evidencia:** strings del DEX (`decompiled/endpoints.txt`).

**Impacto:** la IP `10.19.113.18` **filtra topología interna** del C5 (segmento/host de un
servicio interno), útil para un atacante ya presente en esa red y señal de que el build de
producción arrastra configuración de entorno interno. La IP pública directa impide pinning
por hostname y dificulta la rotación.

**Remediación:** usar nombres DNS y eliminar referencias a hosts internos del binario de
producción.

---

## P2 — Secreto de API de Twitter en claro

**Qué:** `com.twitter.sdk.android.CONSUMER_SECRET` (y `CONSUMER_KEY`) en `res/values/strings.xml`.

**Evidencia:** `evidence/claves-en-claro.txt` (gitignored); recurso extraído con apktool (doc 06).

**Impacto:** el secreto del consumidor OAuth es recuperable por cualquiera; permite usar la
identidad de la app de Twitter del C5 (p. ej. consumir la API en su nombre, hasta
revocación). Las apps no deben llevar secretos OAuth del lado cliente.

**Remediación:** revocar y rotar las credenciales; mover el consumo de Twitter al servidor.

---

## P2 — PII, tipo de sangre y tokens en SharedPreferences planas (+ backup)

**Qué:** toda la PII (nombre, correo, teléfono, domicilio, fecha de nacimiento), el **tipo
de sangre** y los tokens (OTP/activación, chat, Firebase, device key) se guardan sin cifrar;
`allowBackup="true"`.

**Evidencia:** cuerpo de `setBloodType` (`putString("bloodType", …)`) y patrón idéntico en
los 30+ setters (`decompiled/App066PreferenceManager.storage.java`, doc 05); manifiesto.

**Impacto:** en un dispositivo con respaldo (nube/adb) o comprometido, los datos —incluida
información de salud— son extraíbles sin root desde el respaldo o el `shared_prefs/`.

**Remediación:** `EncryptedSharedPreferences`/Keystore para credenciales y PII; excluir
`shared_prefs` del backup o `allowBackup="false"`.

---

## P3 — Servicios de ubicación exportados sin permiso

**Qué:** `services.SiguemeService` y `services.LocationService` con `exported="true"` y sin
permiso de protección.

**Evidencia:** manifiesto (doc 01).

**Impacto:** cualquier app instalada puede arrancarlos (`startService`), con potencial de
disparar o interferir con el servicio de ubicación/seguimiento.

**Remediación:** `exported="false"` o proteger con permiso `signature`.

---

## P3 — Analytics/ads en app de emergencia; sin pinning

**Qué:** Twitter SDK + Google AdId + AppMeasurement recolectan identificadores; el cliente
HTTP no tiene pinning (moot ante HTTP, pero relevante al migrar a HTTPS).

**Evidencia:** manifiesto + `VolleySingleton` (docs 02, 06).

**Impacto:** privacidad discutible para un servicio de auxilio; sin pinning, la futura
migración a HTTPS seguiría expuesta a MITM con CA instalada.

---

## Contrapesos positivos

- **Firma auténtica del C5** (`O=CAEPCCM`, verifica; sin Source Stamp → binario original) — doc 07.
- **Código sin ofuscar** → auditable (también por terceros).
- **Pantalla de protección de datos personales** in-app (`ProteccionDatosPersonalesFragment`).
- Permisos acotados a la función (sin permisos privilegiados: **`DUMP` no lo pide la app**,
  solo protege receivers de AndroidX — corrección documentada en doc 01).

## No cubierto (requiere análisis dinámico)

- Cuerpos reales de request/response y esquema de autenticación de aplicación (token/headers).
- Flujo exacto de la cámara/foto del incidente y del chat Locatel.
- Contenido del `shared_prefs` y de la base SQLite en ejecución.
