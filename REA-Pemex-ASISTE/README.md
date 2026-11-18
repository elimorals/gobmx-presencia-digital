# Ingeniería inversa de Pemex ASISTE 3.1.19 — índice

Análisis **estático** del APK `com.pemex.asiste` (etiqueta "Asiste") con REA (JADX headless +
inspección de archivo). La app **nunca se ejecutó**. Es la app oficial de **Petróleos Mexicanos
(Pemex)**, **#27 por instalaciones** del inventario gob.mx (≈500 K).

> **Flutter — lógica en el snapshot Dart (muro).** La app está hecha con **Flutter**
> (`assets/flutter_assets/`, `libapp.so` en el split de arquitectura). La lógica de negocio vive
> en el **snapshot Dart AOT**, no en el DEX → no decompilable estáticamente sin herramientas de
> Flutter (reflutter/blutter) o análisis dinámico. Pero el bundle Flutter sí trae un **`.env`
> en claro** con el backend y una **clave de Application Insights**, y el DEX revela **ExoPlayer**
> (video). Transporte **HTTPS** al dominio oficial de Pemex.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Flutter, `.env`, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos (ubicación), componentes |
| 02 | [02-flutter-env.md](02-flutter-env.md) | Flutter/Dart AOT, `.env` embebido, muro |
| 03 | [03-red-endpoints.md](03-red-endpoints.md) | `api-cloud.pemex.com`, ExoPlayer |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos y límites del muro |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `evidence/flutter.env` | `.env` con la clave real (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 5 hallazgos principales

1. **Flutter = lógica en el snapshot Dart (muro, no debilidad).** El código de negocio está en
   `libapp.so` (Dart AOT), no en el DEX. No decompilable estáticamente; vía de profundización
   = reflutter/blutter o Frida sobre el snapshot (doc 02).
2. **`.env` embebido en claro.** `assets/flutter_assets/.env` contiene
   `APP_SERVICES_ENDPOINT=https://api-cloud.pemex.com/asiste` y
   `APPSINSIGHTS_INSTRUMENTATION_KEY=<clave>` (doc 02). Práctica común en Flutter con
   `flutter_dotenv`, pero **queda legible** en el APK.
3. **(Bajo) Clave de Application Insights en el cliente.** La *instrumentation key* de Azure
   Application Insights es de **ingestión de telemetría**: quien la tenga puede **enviar/contaminar
   telemetría** a la cuenta de Pemex, no **leer** datos. Microsoft la considera de baja
   sensibilidad (deprecada a favor de connection strings), pero no debería viajar en el binario
   (doc 05). Valor real solo en `evidence/` (no versionado).
4. **Transporte HTTPS al dominio oficial (positivo).** Backend `https://api-cloud.pemex.com/asiste`;
   sin cleartext ni endpoints `http://` propios (doc 03).
5. **Permisos acotados con ubicación.** `ACCESS_FINE/COARSE_LOCATION` (localizador de estaciones
   /servicios cercanos), notificaciones, red. Sin cámara, contactos, SMS ni teléfono (doc 01).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK ni el `.env` (el valor real de la clave vive solo en `evidence/`,
`.gitignore`). Observaciones **estáticas**: no se ejecutó la app ni se llamó a ningún endpoint;
la clave de telemetría **no se usó**. **Firma:** re-firmada por APKPure (`CN=Android, O=Google
Inc.`) → arquitectura sí, integridad no. La lógica Dart no se desensambló (muro), documentado
como límite sin inventar.
