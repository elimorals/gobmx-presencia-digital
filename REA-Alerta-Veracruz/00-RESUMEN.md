# 00 · Resumen ejecutivo — Alerta Ciudadana Veracruz

## Identidad

| Campo | Valor |
|---|---|
| App | Alerta Ciudadana Veracruz |
| Package | `mx.gob.veracruz.c4.emrgcyveracruz` |
| versionName / versionCode | 1.2.11 / 21 |
| minSdk / targetSdk | 23 / 36 |
| Firma | `CN=Adair de Jesus Castillo Meza, L=Xalapa, ST=Veracruz` (dev individual para el C4 Veracruz) |
| SHA-256 cert | `b5b7f28a45509041904504bd4fc271d3621928005334b6c6e97c4f7d52c7f82d` |
| sha256 APK | `c01fdf9e6900d5d132214195e186d75f164cf7d36bb2b15870efa047c51d724c` |
| Clases / recursos | 4 992 / 1 118 |
| Dueño | C4 Veracruz (Centro de Control, Comando, Comunicación y Cómputo) |

Package **verificado con `inspect_android_package` antes de documentar** (regla dura): coincide.

## Pila tecnológica

- **Nativa Android (Java/Kotlin)** — sin Flutter/React Native/.NET (no hay `lib/` nativo). 3 DEX.
- **Red:** `java.net.URL`/`HttpsURLConnection` + **Apache HttpClient legacy** (`org.apache.http.impl`,
  269 refs) — biblioteca deprecada en Android moderno.
- **Push:** Firebase Cloud Messaging (C2DM `RECEIVE`).
- Sin ofuscación agresiva: la clase de red (`model.io.JsonServices`) decompila en claro.

## Función

App de **alerta/reporte ciudadano de emergencias** del C4 Veracruz: login/registro, iniciar y cerrar
alerta, envío de info de alerta, actualización de ubicación, tipos de alerta, perfil.

## Firma / proveniencia

Firmada por un **desarrollador individual** (`CN=Adair de Jesus Castillo Meza`), no por una entidad
de gobierno — matiz de proveniencia (como Mi Policía CDMX). El vínculo con el C4 Veracruz se apoya en
el backend `c4ver.gob.mx`, no en la firma.

## Hallazgos (tabla)

| # | Hallazgo | Clase | Severidad |
|---|----------|-------|-----------|
| H1 | **TLS/cleartext correcto** — todo HTTPS, sin NSC, sin trust-all | — | ✅ AUSENTE-SANO |
| H2 | Apache HttpClient **deprecado** empacado | higiene | ⚪ BAJA |
| H3 | Firma de dev individual (proveniencia) | — | ⚪ INFO |

**No cae en ningún clúster del mapa.** Ver [02-red-tls.md](02-red-tls.md) y [03-hallazgos.md](03-hallazgos.md).
