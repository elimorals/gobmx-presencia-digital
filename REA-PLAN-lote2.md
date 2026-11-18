# Plan de trabajo — Ingeniería inversa lote 2 (apps #11–#30 de gob.mx)

> Extiende el método de las 10 apps ya documentadas (`REA-*/`) a las 19 siguientes
> por instalaciones en Google Play. Mismo principio: **observación estática** de
> artefactos públicos de tiendas oficiales, cada afirmación con evidencia, sin
> atacar infraestructura en vivo. Fecha de arranque: 2026-11-17.

## Alcance

19 apps (rangos #11–#30 por instalaciones, **se omite ECOBICI** por ser de Lyft,
no gobierno):

| # | Slug | App | Package | Instalaciones |
|---|---|---|---|---|
| 11 | `banjecel` | BanjeCel (Banjército) | `mx.com.banjercito.model.common.VTUserApplicationBJCTMB` | 1 M+ |
| 12 | `mipolicia-cdmx` | Mi Policía (SSC CDMX) | `com.moobky.MiPolicia` | 1 M+ |
| 13 | `911-cdmx` | 911 CDMX (C5) | `com.desarrollo.c5.app_066_android_2` | 1 M+ |
| 14 | `valida-ine-qr` | Valida INE-QR (INE) | `mx.ine.rfe.lectorqr` | 1 M+ |
| 15 | `tarjeta-rosa` | Tarjeta Rosa (Guanajuato) | `gob.guanajuato.TarjetaRosa` | 1 M+ |
| 16 | `asissste` | ASISSSTE Móvil (ISSSTE) | `com.app.asssistemovilapp` | 1 M+ |
| 17 | `une-sonora` | UNE Transporte Sonora (IMTES) | `com.bigbangdev.bussonora` | 1 M+ |
| 18 | `vayven` | Va-y-Ven (Yucatán) | `gob.yucatan.vayven` | 1 M+ |
| 19 | `qrobus` | Qrobús (IQT) | `mx.gob.iqt.qrobus` | 500 K+ |
| 20 | `finabien` | Financiera para el Bienestar | `com.telecomm.vapp` | 500 K+ |
| 21 | `seg-movil` | SEG Móvil | `com.artech.sdsigueme.sigueme` | 500 K+ |
| 22 | `tesoreria-cdmx` | Tesorería CDMX (SAF) | `mx.wdd.sefin` | 500 K+ |
| 23 | `licencia-edomex` | Licencia Digital EDOMEX | `mx.com.cosmocolor.movapp` | 500 K+ |
| 24 | `conaliteg` | CONALITEG Digital | `gob.mx.conaliteg.bibliotecadigital` | 500 K+ |
| 25 | `icvnl` | ICVNL | `com.icv.app` | 500 K+ |
| 26 | `apoyo-ciudadano-ine` | Apoyo Ciudadano - INE | `mx.ine.apoyociudadano` | 500 K+ |
| 27 | `pemex-asiste` | Pemex ASISTE | `com.pemex.asiste` | 500 K+ |
| 29 | `sat-factura-movil` | SAT Factura Móvil | `mx.gob.sat.facmovapp` | 500 K+ |
| 30 | `llave-edomex` | Llave EdoMex | `gob.mx.edomex.llave` | 500 K+ |

---

## Fase 0 — Descarga + verificación de autenticidad

**Herramienta:** `apkeep` (no navegador). APKPure/APKMirror/APKCombo por Chrome
llegan al mismo binario re-firmado; apkeep lo hace en lote y verificable.

```bash
export PATH=$PATH:/Users/elias/Library/Android/sdk/build-tools/36.1.0   # apksigner, aapt2
apkeep -a <pkg> -d apk-pure -o 'acknowledge_dangers=true,arch=arm64-v8a' descargas-apk/<slug>/
```

**Verificación obligatoria por APK (3 chequeos):**

1. **Package real** coincide con el esperado:
   `aapt2 dump packagename descargas-apk/<slug>/*.apk`
2. **Certificado de firma** (clasifica autenticidad):
   `apksigner verify --print-certs descargas-apk/<slug>/*.apk`
   - DN propio del emisor (`O=Banjercito`, `L=Mexico`, etc.) → **auténtico de Play**.
   - `CN=Android, O=Google Inc.` genérico → **re-firmado por APKPure** (útil para
     arquitectura, **no para conclusiones de integridad**). Mismo caso que CFE.
3. **SHA-256** → `shasum -a 256 descargas-apk/<slug>/*` a `hashes.sha256`.

**Fallback (y único uso de Claude Chrome):** si apkeep falla en apk-pure (apps
chicas de gobierno a veces no están), buscar en APKMirror/APKCombo por Chrome,
descargar, y correr **los mismos 3 chequeos**. Si el package no coincide → descartar
(lección Banco del Bienestar: era Aptoide mal nombrado).

**Estado Fase 0 (2026-11-17, COMPLETADA para 15/19):**

- **15 bajaron de APKPure, package correcto en los 15.** Hashes en `descargas-apk/HASHES.sha256`.
- **9 con firma ORIGINAL preservada (auténticos, firma válida del emisor):** BanjeCel
  (`O=Banco Nacional del Ejército`), 911 CDMX (`O=CAEPCCM`), Valida INE-QR y Apoyo
  Ciudadano (`O=INE`), ICVNL (`O=ICVNL`), Mi Policía (`CN=Saul Espinosa Ruiz`),
  UNE Sonora (`O=Bus Sonora`), SEG Móvil (`O=Artech`), Tesorería CDMX (`O=cweb`).
  → más fuerte que CFE: la firma verifica y es la llave del emisor.
- **6 re-firmados por APKPure (`CN=Android, O=Google Inc.`):** Tarjeta Rosa, Va-y-Ven,
  Qrobús, Licencia EDOMEX, CONALITEG, Pemex ASISTE. → arquitectura sí, integridad no.
- **4 NO en APKPure (pendiente Chrome):** ASISSSTE (`com.app.asssistemovilapp`),
  Finabien (`com.telecomm.vapp`), SAT Factura Móvil (`mx.gob.sat.facmovapp`),
  Llave EdoMex (`gob.mx.edomex.llave`).

---

## Fase 1 — Triage de pila (antes de asignar esfuerzo)

El esfuerzo y la plantilla de capítulos dependen de con qué está hecha la app.
Antes de lanzar el RE a fondo, clasificar cada APK con señales rápidas:

```bash
unzip -l app.apk | grep -iE 'libapp.so|libflutter|il2cpp|unity|lib.*veritran|\.szip|cordova|capacitor|assemblies|xamarin|libmono|react|index.android.bundle'
```

| Señal | Pila | Dificultad | Rendimiento esperado |
|---|---|---|---|
| Kotlin/Java DEX normal | Nativa | **Fácil** | Alto (endpoints, lógica legibles) |
| `assemblies.blob` / `libmono` | Xamarin/.NET | **Fácil** | Muy alto (C# descompilable → como IMSS) |
| `capacitor`/`cordova`/`www/` | Híbrida JS | **Fácil** | Alto (JS legible → como Licencia) |
| `index.android.bundle` | React Native | **Media** | Medio (bundle, a veces Hermes) |
| `libflutter.so` + `libapp.so` | Flutter AOT | **Difícil** | Bajo (lógica en snapshot Dart → como SAT/Urbani) |
| `il2cpp`/`libunity` | Unity IL2CPP | **Difícil** | Bajo-medio (Il2CppDumper → como BilletesMx) |
| `.szip` / Veritran / packer | Low-code/empacado | **Muy difícil** | Casi nulo (como Banco del Bienestar) |

**Sospecha a priori:** BanjeCel (`...VTUserApplication...`) huele a **Veritran** como
Banco del Bienestar → probablemente "documentar el muro" (RASP, no decompilable),
no un análisis a fondo. Confirmar en triage antes de invertirle un subagente pesado.

**Resultado triage (2026-11-17, sobre los 15 descargados):**

| Pila | Dificultad | Apps |
|---|---|---|
| Híbrida JS | **Fácil (alto rendimiento)** | `tesoreria-cdmx`, `vayven` |
| Nativa Kotlin/Java | **Media** (ojo: puede estar ofuscada) | 911 CDMX, Apoyo Ciudadano, CONALITEG, ICVNL, Licencia EDOMEX, Mi Policía, Pemex ASISTE, Qrobús, SEG Móvil, Tarjeta Rosa, UNE Sonora, Valida INE-QR |
| Veritran/low-code | **Muy difícil (muro)** | `banjecel` (confirmado: `.szip`/Duktran) |

Sin Flutter/Unity/React Native en el lote. `apoyo-ciudadano-ine` trae OpenCV + cardIO +
OCR (tesseract) — SDKs de captura de identidad, pero sigue siendo nativa Java.
Las 4 pendientes de Chrome entran a triage al descargarse.

---

## Fase 2 — RE por app con subagentes

### Pipeline estándar por app (lo que hace cada subagente)

1. **Abrir** el APK verificado con REA (`mcp__rea__open_binary` / `binary_session`).
2. **Manifiesto**: `inspect_android_package`, decodificar `AndroidManifest.xml`,
   permisos peligrosos, componentes exportados, `usesCleartextTraffic`,
   `networkSecurityConfig`.
3. **Grafo de la app**: `project_android_application_graph` para módulos y superficie.
4. **Decompilar** lo relevante: `batch_decompile` / `inspect_android_method`
   (según pila — ver Fase 1).
5. **Red/endpoints**: `search_android_classes` + `list_strings`/`search_strings`
   sobre dominios, `http://`, rutas de API, IPs.
6. **Cripto/almacenamiento**: uso de keystore, SQLCipher, prefs, pinning TLS
   (`getUnsafeOkHttpClient` y similares — ya salió crítico en Infonavit).
7. **SDKs terceros**: Firebase, analytics, KYC/biometría, pagos.
8. **Firma/recursos**: `apksigner` (ya en Fase 0), estructura, assets.
9. **Hallazgos priorizados** con evidencia y fecha.

### Plantilla de entregable (idéntica a las 10 existentes)

Carpeta `REA-<Nombre>/` con:

```
README.md              ← índice: identidad, pila, tabla de documentos, artefactos,
                          "N hallazgos principales", alcance y límites
00-RESUMEN.md          ← resumen ejecutivo
01-manifiesto.md       ← permisos, componentes, flags de seguridad
02..NN-*.md            ← capítulos SEGÚN PILA (no fijos):
                          nativa → auth / cripto / red / ofuscación / pagos / SDKs
                          Xamarin → arquitectura-xamarin / endpoints / identidad
                          Flutter → por qué la lógica no está en el DEX
                          Veritran → arquitectura / RASP / "muro"
XX-firma-recursos.md   ← procedencia y certificado (auténtico vs re-firmado)
XX-hallazgos-seguridad.md ← hallazgos priorizados
AndroidManifest.xml    ← versionado
hashes.sha256          ← versionado
.gitignore             ← copia del de REA-App-CDMX (bloquea apk/so/dex/decompiled/evidence)
decompiled/ evidence/  ← NO versionado (artefactos pesados)
```

**Reglas de calidad (de CLAUDE.md del usuario):** sin causa inventada cuando la
evidencia no la identifica; si una app no se puede decompilar, se dice y se
documenta el muro — no se rellena. Cada endpoint/hallazgo lleva de dónde salió.

### Diseño de subagentes

- **Tipo:** `general-purpose` (tiene `*` → acceso a `mcp__rea__*` + Bash). **No**
  usar el agente `reverse-engineer`: carece de Bash y MCP, no puede decompilar.
- **Paralelismo:** **3–4 a la vez**, no los 19. Motivo: decompilar (grafo + batch
  decompile) es intensivo y varios REA simultáneos sobre APKs grandes compiten por
  recursos; además permite **revisar la calidad del primer batch** antes de seguir.
- **Orden sugerido:** primero los **Fáciles de alto rendimiento** (nativas/Xamarin/
  híbridas) para maximizar hallazgos por token; los **Difíciles/empacados** al final
  con alcance acotado (documentar la pila y el muro, no perseguir snapshots cifrados).
- **Brief por subagente** (lo que se le pasa, autocontenido):
  1. Ruta del APK verificado y su resultado de firma (auténtico/re-firmado).
  2. Package, nombre, dependencia, instalaciones.
  3. Resultado de triage (pila + dificultad + qué capítulos aplican).
  4. Ruta de una `REA-*` existente de la **misma pila** como molde exacto
     (p.ej. IMSS para Xamarin, Licencia para híbrida, SAT/Urbani para Flutter,
     BilletesMx para Unity, Banco del Bienestar para Veritran, AforeMóvil/CFE
     para nativa).
  5. El pipeline de 9 pasos y la plantilla de carpeta de arriba.
  6. Regla de evidencia y de "no inventar causa".

### Lotes propuestos (ajustar tras triage)

- **Lote A (fáciles, alto rendimiento):** las nativas/Xamarin/híbridas que confirme
  el triage. Lanzar 3–4 subagentes.
- **Lote B (medias):** React Native y similares.
- **Lote C (difíciles, alcance acotado):** Flutter/Unity/Veritran. 1 subagente c/u,
  objetivo = documentar pila + muro + lo recuperable de strings/manifiesto.

---

## Fase 3 — Integración

Al cerrar cada app (o cada lote):

1. **Tabla del README raíz** (`## Ingeniería inversa de apps REA`): agregar fila con
   package, dependencia, pila/protección, hallazgo clave. Hoy dice "10 apps" → subir
   el conteo.
2. **Webapp** (opcional, lo preguntó el usuario aparte): marca "con ingeniería
   inversa" en las fichas de `/apps` correspondientes.
3. **Memoria del proyecto**: una nota por app con pila + hallazgo, enlazada desde
   `MEMORY.md` (como existe ya `rea-urbani-miruta-mty.md`).
4. **Commit** por app o por lote (atribución = `elimorals`, sin trailer de Claude).

---

## Resumen de decisiones

- **Descarga:** apkeep (no Chrome); Chrome solo si apkeep falla, con los mismos 3 chequeos.
- **Autenticidad:** se documenta siempre; APKPure re-firma → vale para arquitectura, no para integridad.
- **Subagentes:** `general-purpose` (no `reverse-engineer`), 3–4 en paralelo, fáciles primero.
- **No inventar:** app no decompilable → se documenta el muro, no se rellena.
- **Molde:** cada app copia la `REA-*` existente de su misma pila.
