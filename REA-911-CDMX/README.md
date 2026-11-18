# Ingeniería inversa de 911 CDMX 1.4.7 — índice

Análisis **estático** del APK `com.desarrollo.c5.app_066_android_2` con REA (JADX
headless 0.7.1 + apktool). La app **nunca se ejecutó**. Es la app oficial de
emergencias de la Ciudad de México (botón de auxilio 911, denuncia de extorsión 089,
ficha de salud, red de emergencia "Sígueme"), operada por el **C5 / CAEPCCM**
(Coordinación General del Centro de Comando, Control, Cómputo, Comunicaciones y
Contacto Ciudadano). **#13 por instalaciones** del inventario gob.mx (≈1 M+).

> **Pila confirmada:** nativa Android **Kotlin/Java** (2 dex, 6 313 clases), red con
> **Volley**, sin ofuscar (nombres de paquete legibles). El triage preliminar (nativa)
> fue correcto. No es Flutter/Unity/MAUI/Capacitor/React Native.

> **Hallazgo dominante:** una app de **emergencias** que transmite **todo** —ubicación
> en tiempo real, creación de incidente, alerta a contactos, ficha de salud (tipo de
> sangre), OTP de registro— sobre **HTTP en claro** (`http://apps.c5.cdmx.gob.mx`), con
> **IPs hardcodeadas** (una interna) y **secretos de API en claro** en los recursos.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, pila, módulos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes exportados, flags de seguridad |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Paquetes propios, Volley, capa de red |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | **Endpoints reales (todos HTTP)**, WCF .NET, IPs hardcodeadas |
| 04 | [04-emergencia-ubicacion-fichasalud.md](04-emergencia-ubicacion-fichasalud.md) | Botón de auxilio, Sígueme (ubicación), ficha de salud, red de emergencia |
| 05 | [05-cripto-almacenamiento.md](05-cripto-almacenamiento.md) | PII + tipo de sangre + tokens en SharedPreferences planas |
| 06 | [06-sdks-terceros.md](06-sdks-terceros.md) | Firebase, Google Maps, Twitter (secreto en claro), analytics/ads |
| 07 | [07-firma-recursos.md](07-firma-recursos.md) | Firma auténtica del C5, estructura, procedencia |
| 08 | [08-hallazgos-seguridad.md](08-hallazgos-seguridad.md) | **Hallazgos priorizados** |

## Artefactos

| Carpeta/archivo | Contenido |
|---|---|
| `decompiled/endpoints.txt` | Endpoints reales extraídos del DEX (`DataRegister` + strings) |
| `decompiled/VolleySingleton.getRequestQueue.java` | Cliente HTTP: Volley por defecto, sin pinning |
| `decompiled/App066PreferenceManager.storage.java` | Almacenamiento en SharedPreferences planas (cita de `setBloodType`) |
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK |

> Lo propietario (APK, `evidence/` con el apktool completo y los secretos en claro)
> **no se versiona** (`.gitignore`).

## 6 hallazgos principales

1. **Backend de emergencias 100% en HTTP en claro (P1).** Crear incidente, **ubicación
   en tiempo real (Followme)**, **alerta de emergencia**, **ficha de salud** (tipo de
   sangre, seguro, contactos), **OTP de registro** y validar número de extorsión (089)
   viajan sin TLS a `http://apps.c5.cdmx.gob.mx/WebServices/…`. MITM en una red hostil
   puede leer o **alterar una llamada de auxilio** (doc 03, 08).
2. **IPs hardcodeadas, una interna (P2).** `http://10.19.113.18/WcfServices/…EavisaRed`
   (IP privada RFC1918 → fuga de topología interna del C5) e `http://187.174.224.151/WebServices/`
   (IP pública directa) embebidas en el binario de producción (doc 03).
3. **Secreto de API de Twitter en claro (P2).** `CONSUMER_KEY` y **`CONSUMER_SECRET`**
   de la app de Twitter (feed Sismológico/SASMEX) en `res/values/strings.xml` →
   recuperables con `apktool`; "ocultar" un secreto en recursos no lo protege (doc 06).
4. **PII + tipo de sangre + tokens en SharedPreferences planas (P2).** `setBloodType`
   hace `putString("bloodType", …)` sin cifrar; igual para nombre, correo, teléfono,
   domicilio, fecha de nacimiento, device key, chat token y código de activación.
   `allowBackup="true"` → respaldables (doc 05).
5. **Servicios exportados sin permiso (P3).** `SiguemeService` y `LocationService`
   declaran `exported="true"` sin permiso → otra app del dispositivo puede arrancarlos
   (abuso del seguimiento de ubicación) (doc 01, 08).
6. **Sin pinning TLS + analytics/ads en app de emergencia (P3).** `VolleySingleton` usa
   Volley por defecto (sin `CertificatePinner`/`TrustManager` custom); además integra
   Twitter, Google AdId y Measurement (doc 02, 06).

**Positivos:** firma **auténtica** del C5 (`O=CAEPCCM`, verifica; sin Source Stamp →
APK original), código **sin ofuscar** (auditable) y pantalla de protección de datos
personales in-app.

## Alcance y límites

Análisis **original** de una app pública del gobierno de la CDMX que se posee, con fines
de investigación de seguridad. **No redistribuye** el APK, los recursos ni los secretos
(`.gitignore`). Los endpoints documentados son rutas presentes en el binario, no datos
de usuarios. Observaciones **estáticas**: no se ejecutó la app, no se oprimió el botón
de emergencia ni se llamó a ningún endpoint. **No cubierto:** cuerpos de las respuestas
del servidor y el flujo exacto de la cámara/foto del incidente (requieren dinámico).
