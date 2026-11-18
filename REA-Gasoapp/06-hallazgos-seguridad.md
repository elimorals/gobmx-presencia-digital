# Hallazgos de seguridad y preguntas abiertas

Severidad cualitativa. Todo **estático**; no se ejecutó la app, no se llamó a endpoints, no se
subió evidencia. En .NET el detalle es metadata/strings/estructura (no C# textual).

## H1 (Bajo-Medio) — QA y dominio Azure crudo en el binario de producción
3 BaseUrls embebidas (doc 03): producción `api-gasoapp.cre.gob.mx`, **QA**
`api-gasoapp-qa.cre.gob.mx` y el dominio Azure crudo
`api-estacionservicio-327800.gasoapp.p.azurewebsites.net`. Fuga de entornos/infraestructura; el
QA en prod amplía superficie si es alcanzable. **Fix:** eliminar QA del build de prod; mover el
servicio de estaciones a un dominio `cre.gob.mx`.

## H2 (Bajo, pregunta abierta) — Subida de evidencia a Blob por deviceId+permisoId
`UriContainerEvidencia?deviceId={0}&permisoId={1}` + `Microsoft.WindowsAzure.Storage` (doc 04). Si
el contenedor/SAS no acota el acceso por objeto y los `permisoId` son predecibles, superficie de
acceso a evidencia ajena. **No confirmado** (el SAS se obtiene en runtime; no hay llave embebida).
**Fix/revisar:** SAS por objeto con expiración corta; no contenedor público.

## H3 (Bajo) — Build obsoleto
Xamarin.Forms clásico (EOL), DLLs de 2020, targetSdk 29, `Microsoft.WindowsAzure.Storage`
deprecada (doc 02). Deuda de mantenimiento → vulnerabilidades no parcheadas del runtime/deps.
**Fix:** migrar a .NET MAUI + `Azure.Storage.Blobs`, subir targetSdk.

## Positivos
- **P1 — TLS sin bypass.** Validación de certificado estándar; sin trust-all (doc 03).
- **P2 — Sin secretos hardcodeados.** Ni `AccountKey`/SAS de Storage ni API keys/`ClientSecret`
  en los DLLs descomprimidos (doc 03–04).
- **P3 — Transporte HTTPS** en los 3 endpoints; permisos acotados (doc 01, 03).
- **P4 — Firma auténtica de la CRE** (doc 05).

## Límites (sin inventar)
- Cuerpo C# textual no reconstruido (sin ILSpy); metadata/strings/estructura.
- Autorización real del Blob (SAS en runtime) y rutas completas de la API bajo cada BaseUrl: no
  observables estáticamente.
- Uso real de `Microsoft.WindowsAzure.Storage` (¿lee blobs públicos? ¿sube con SAS?): se infiere
  de la config, no se trazó la llamada en IL.
