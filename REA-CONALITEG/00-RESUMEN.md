# Resumen — CONALITEG Digital 1.25 (`gob.mx.conaliteg.bibliotecadigital`)

## Identidad

- **App:** CONALITEG Digital — biblioteca digital de la **Comisión Nacional de Libros de Texto
  Gratuitos** (SEP). Lectura de libros de texto y contenidos educativos.
- **Package:** `gob.mx.conaliteg.bibliotecadigital` · **versión** 1.25 (versionCode 56).
- **#24** del inventario por instalaciones (≈500 K).
- **Firma:** re-firmada por APKPure (`CN=Android, O=Google Inc.`).

## Arquitectura (en una línea)

App **nativa Android (Kotlin/Java)** con estructura MVC propia
(`gob.mx.conaliteg.bibliotecadigital.{adapters,model,views}`). Es un **lector/agregador** de
biblioteca digital; el DEX es decompilable (no hay Flutter/Unity/.NET/WebView-wrapper). Firebase
Analytics presente (`app-measurement.com`).

## Backends (propios, de la CONALITEG)

| Host | Rol (inferido) |
|---|---|
| `api.conaliteg.gob.mx` | API principal |
| `middleware.conaliteg.gob.mx` | middleware |
| `libros.conaliteg.gob.mx` | entrega de libros |
| `secundaria.conaliteg.gob.mx` | contenido de secundaria |
| `milesdehistorias.conaliteg.gob.mx` | colección "Miles de Historias" |
| `pruebasistemas.conaliteg.gob.mx` | **entorno de pruebas (residuo en producción)** |

## Contenido de terceros (federado)

FCE (`docs.fondodeculturaeconomica.com`), UPN (`biblioteca.ajusco.upn.mx`,
`editorial.upnvirtual.edu.mx`), UNAM (`www.librosoa.unam.mx`, `www.revistas.unam.mx`), Grupo SM
(`guiasdigitales.grupo-sm.com.mx`), Trillas (`appstrillas.mx`), Cervantes Virtual, Academia.org,
Fonoteca Nacional.

## Postura (balance)

| Aspecto | Veredicto |
|---|---|
| Transporte | ⚠️ **Cleartext HTTP permitido** a backends de producción (doc 03) |
| Confianza TLS | ⚠️ **Acepta CAs del usuario** (`src="user"`) → MITM trivial (doc 03) |
| Higiene de build | ⚠️ IP privada + emulador + `pruebasistemas` en el NSC de producción |
| PII | ✅ Bajo riesgo: contenido educativo gratuito, sin credenciales de pago aparentes |
| Firma | ⚠️ Re-firmada por APKPure |

**Lectura:** la app es un lector sencillo, pero su **configuración de red es la parte débil**:
permite HTTP en claro a su propia API/middleware y confía en CAs de usuario. Para una app de
contenido gratuito el impacto es moderado (integridad de contenido y privacidad de navegación,
no pagos), pero es una mala práctica de transporte clara.
