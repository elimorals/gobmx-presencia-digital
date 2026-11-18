# Ingeniería inversa de CONALITEG Digital 1.25 — índice

Análisis **estático** del APK `gob.mx.conaliteg.bibliotecadigital` con REA (JADX headless). La
app **nunca se ejecutó**. Es la app oficial de la **CONALITEG** (Comisión Nacional de Libros de
Texto Gratuitos) — biblioteca digital de libros de texto y contenidos educativos. **#24 por
instalaciones** del inventario gob.mx (≈500 K).

> **Nativa Kotlin/Java (lector de biblioteca digital).** Agrega contenido de múltiples
> editoriales y universidades (FCE, UPN, UNAM, Grupo SM, Trillas…). El hallazgo central es su
> **`network_security_config`**: habilita **tráfico en claro (HTTP)** hacia sus **backends de
> producción** y **confía en CAs instaladas por el usuario** (`src="user"`) → transporte débil
> y MITM-friendly. Además deja en producción artefactos de desarrollo (IP privada, loopback de
> emulador, subdominio `pruebasistemas`).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, arquitectura, hallazgos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos, componentes |
| 02 | [02-arquitectura.md](02-arquitectura.md) | Nativa, lector, dominios agregados |
| 03 | [03-red-nsc-cleartext.md](03-red-nsc-cleartext.md) | **Network Security Config: cleartext + CAs de usuario** |
| 04 | [04-firma-recursos.md](04-firma-recursos.md) | Re-firmada por APKPure, procedencia |
| 05 | [05-hallazgos-seguridad.md](05-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

| Archivo | Contenido |
|---|---|
| `AndroidManifest.xml` | Manifiesto decodificado (versionado) |
| `hashes.sha256` | SHA-256 del XAPK y APK base (versionado) |
| `evidence/network_security_config.xml` | NSC decodificado (no versionado) |
| `evidence/firma.txt` | `apksigner --print-certs` (no versionado) |

## 5 hallazgos principales

1. **(Medio) Cleartext HTTP permitido a backends de producción.** El `network_security_config`
   declara `cleartextTrafficPermitted="true"` para `api.conaliteg.gob.mx`,
   `middleware.conaliteg.gob.mx`, `libros.conaliteg.gob.mx`, `secundaria.conaliteg.gob.mx`,
   `milesdehistorias.conaliteg.gob.mx` y más — es decir, el tráfico a la **API y al middleware de
   producción puede viajar sin TLS**. Riesgo de MITM sobre contenido/tracking (doc 03).
2. **(Medio) Confianza en CAs del usuario.** El mismo `domain-config` incluye
   `<certificates src="user" />` en sus `trust-anchors` → la app **acepta certificados firmados
   por CAs instaladas por el usuario**, lo que hace **trivial el MITM** (basta instalar una CA).
   Combinado con el cleartext, la postura de transporte es débil (doc 03).
3. **(Bajo) Artefactos de desarrollo en producción.** El NSC lista una **IP privada**
   (`192.168.101.6`), el loopback de emulador (`10.0.2.2`), `127.0.0.1` y un subdominio
   **`pruebasistemas.conaliteg.gob.mx`** — residuos de un entorno de pruebas que quedaron en el
   build de producción (doc 03).
4. **Agrega contenido de terceros.** El NSC y el DEX referencian FCE
   (`docs.fondodeculturaeconomica.com`), UPN (`biblioteca.ajusco.upn.mx`,
   `editorial.upnvirtual.edu.mx`), UNAM (`www.librosoa.unam.mx`, `www.revistas.unam.mx`), Grupo
   SM (`guiasdigitales.grupo-sm.com.mx`), Trillas (`appstrillas.mx`), Cervantes Virtual,
   Fonoteca Nacional — la biblioteca federa catálogos de muchas editoriales/universidades
   (doc 02).
5. **Firma re-firmada por APKPure.** `CN=Android, O=Google Inc.` → arquitectura sí, integridad
   no (doc 04).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad.
**No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app ni se llamó a
ningún endpoint. La app sirve contenido educativo gratuito (sin PII crítica aparente), así que
el impacto del cleartext/MITM es sobre **integridad de contenido y privacidad de navegación**,
no sobre credenciales de pago. **Firma** re-firmada por APKPure → no verificable como auténtica
de CONALITEG.
