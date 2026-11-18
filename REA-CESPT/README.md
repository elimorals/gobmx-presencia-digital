# Ingeniería inversa de CESPT (CesptApp) 3.1.1 — índice

Análisis **estático** del APK `com.cespt.cesptApp` con REA (JADX + inspección del bundle web).
La app **nunca se ejecutó**. Es la app oficial de la **Comisión Estatal de Servicios Públicos
de Tijuana (CESPT)** — agua y alcantarillado de Baja California: consulta y **pago** de recibos,
reporte de fugas, citas. **#37 por instalaciones** del inventario gob.mx (≈100 K).

> **Híbrida Capacitor/Ionic (Angular).** La lógica es el bundle web `assets/public/*.js`
> (Angular troceado) sobre Capacitor; `appId` quedó en el default `io.ionic.starter`. Backend
> propio HTTPS (`www.cespt.gob.mx/API`). El **pago se abre en un InAppBrowser hacia la pasarela
> de Bancomer** (`/pago/pcm.aspx`) → la tarjeta se captura en la página del banco, no en la app
> (patrón correcto). Identidad delegada a **Llave Baja California** (OAuth). El punto débil es la
> **configuración de red**: `cleartextTrafficPermitted="true"` **global** + confianza en CAs del
> usuario → MITM trivial sobre todo el tráfico, incluido el del InAppBrowser de pago.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, Capacitor, pagos, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, `usesCleartextTraffic`, `allowBackup` |
| 02 | [02-arquitectura-hibrida.md](02-arquitectura-hibrida.md) | Capacitor/Ionic, bundle Angular, Llave BC |
| 03 | [03-pagos.md](03-pagos.md) | InAppBrowser → Bancomer `pcm.aspx` |
| 04 | [04-red-nsc.md](04-red-nsc.md) | **NSC: cleartext global + CAs de usuario**, endpoints |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Re-firmada por APKPure, Google Maps key |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado); `evidence/`
(`network_security_config.xml`, `capacitor.config.json`, `google-maps-key.txt`, `firma.txt`) y
`decompiled/endpoints.txt` **no versionados**.

## 6 hallazgos principales

1. **(Medio) NSC: cleartext HTTP permitido GLOBALMENTE + confía en CAs del usuario.** El
   `network_security_config` tiene `<base-config cleartextTrafficPermitted="true">` (aplica a
   **todos** los dominios, no a una lista) y `<certificates src="user" />` en los trust-anchors →
   **MITM trivial** instalando una CA, sobre todo el tráfico incluido el InAppBrowser de pago.
   Más una IP hardcodeada con cleartext (`132.147.100.89`) (doc 04).
2. **(Bajo) Google Maps API key embebida** en el bundle JS (`AIzaSy…`, valor en
   `evidence/google-maps-key.txt`). Debería restringirse por package/SHA-1 y referer (doc 05).
3. **(Bajo) `allowBackup="true"`** — respaldable; mitigado en parte por `BackupWebStorage=none`
   en la config de Capacitor (doc 01).
4. **(Bajo) `appId` default `io.ionic.starter`** — no personalizaron el identificador de
   Capacitor; descuido de build, no vulnerabilidad (doc 02).
5. **(Positivo) Pago seguro vía InAppBrowser a Bancomer.** El cobro abre `/pago/pcm.aspx`
   (ASP.NET, pasarela de BBVA Bancomer) en un InAppBrowser → **la tarjeta se captura en la
   página del banco**, no en el JS de la app; no se hallaron datos de tarjeta ni claves de
   pasarela embebidos (doc 03).
6. **(Positivo) Identidad delegada a Llave BC + endpoints propios HTTPS.** OAuth con
   `www.llave.bajacalifornia.gob.mx`; la API propia es `https://www.cespt.gob.mx/API` (doc 02,04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK ni la Maps key (valor solo en `evidence/`, `.gitignore`). Observaciones
**estáticas**: no se ejecutó la app, no se pagó ni se llamó a ningún endpoint. **Firma**
re-firmada por APKPure (`CN=Android, O=Google Inc.`) → arquitectura sí, integridad no. El manejo
real de la tarjeta vive en la página de Bancomer (fuera del APK).
