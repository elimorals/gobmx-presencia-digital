# Ingeniería inversa de Bienvenido a México 3.0.0 — índice

Análisis **estático** del APK `com.tcs.programapaisano` con REA (JADX headless). La app **nunca
se ejecutó**. Es la app oficial del **Programa Paisano** (SAT/ANAM) — apoyo a connacionales que
entran a México: información, contactos consulares, consulta de permisos de importación, mapa de
tráfico y **denuncias**. **#32 por instalaciones** del inventario gob.mx (≈500 K). Desarrollada
por **TCS** (Tata Consultancy Services) para el SAT.

> **Nativa Android (Kotlin/Java), código legacy sin ofuscar.** Arquitectura MVC clásica
> (Connection / WebServices / Entities / DBManager / Parsers). El hallazgo central es de **red**:
> el manifiesto habilita **`usesCleartextTraffic="true"`** y el binario embebe **endpoints HTTP
> en claro** del SAT **más un mapa completo de ambientes de desarrollo/pruebas en producción** —
> incluido un endpoint en **`gear.host` (hosting gratuito)** y uno de **desarrollo en Azure**
> (`develmobile.cloudapp.net`). Firma **auténtica del SAT**.

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | `usesCleartextTraffic=true`, `allowBackup`, Apache legacy |
| 02 | [02-arquitectura.md](02-arquitectura.md) | MVC (Connection/WebServices/Entities), SQLite |
| 03 | [03-red-ambientes.md](03-red-ambientes.md) | **Endpoints HTTP + ambientes dev/UAT/QA en producción** |
| 04 | [04-denuncia-permisos.md](04-denuncia-permisos.md) | Flujo de denuncia, permisos de importación, PII |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma **auténtica del SAT**, procedencia |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del APK (versionado) |
| `evidence/endpoints.txt` | 10 endpoints embebidos (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |
| `decompiled/class-map.txt` | Mapa de clases propias por paquete (no versionado) |

## 6 hallazgos principales

1. **(Medio) Cleartext HTTP habilitado globalmente.** `android:usesCleartextTraffic="true"` en el
   manifiesto, con **endpoints `http://` del SAT** embebidos: `m.sat.gob.mx` y `www.sat.gob.mx`
   (`_layouts/listfeed.aspx`, feeds de SharePoint). El contenido viaja sin TLS → MITM puede
   leer/alterar la información mostrada al usuario (doc 01, 03).
2. **(Medio) Ambientes de desarrollo/pruebas embebidos en producción.** `ConfiguracionAmbiente`
   trae el mapa completo: **UAT** (`piuat.sat.gob.mx`, `moviluat.siat.sat.gob.mx`), **desarrollo
   en Azure** (`http://develmobile.cloudapp.net/`) y **QA en un hosting gratuito**
   (`http://satmovilqd.gear.host`). Fuga de infraestructura interna y superficie innecesaria
   (doc 03).
3. **(Bajo-Medio) Endpoint del SAT en hosting gratuito.** `satmovilqd.gear.host` — un recurso
   relacionado con el SAT alojado en **gear.host (hosting gratuito de terceros)**, por HTTP. Mala
   práctica de infraestructura para una dependencia federal (doc 03).
4. **Flujo de denuncia + permisos de importación.** `DenunciaDao`/`DenunciaEntity`/
   `FoliosDenunciaEntity` (denuncias de paisanos) y `ConsultaPermisoDao`/`TramiteDePermisoFragment`
   (permisos de importación vehicular, con enlace a `banjercito.com.mx/registro`). Estos flujos
   usan la misma capa de red con cleartext habilitado (doc 04).
5. **(Bajo) `allowBackup=true` + Apache HTTP legacy + support lib vieja.** Respaldo permitido de
   la base SQLite local; usa `org.apache.http.legacy` (cliente HTTP obsoleto) y
   `android.support.v4` (no AndroidX) → **código legacy** (doc 01).
6. **Firma auténtica del SAT (positivo).** `CN=Angel Fernandez, OU=AGCTI, O=SAT` verifica →
   binario íntegro del emisor, no re-firmado (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. Los endpoints documentados son rutas presentes en el binario, no secretos. El
contenido exacto de la denuncia y de los permisos (qué PII viaja) se infiere de las entidades;
el recorrido dinámico de cada request queda fuera de alcance. **Firma auténtica del SAT.**
