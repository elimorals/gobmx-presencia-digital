# Plan lote 4 — apps ≥100k restantes (17)

**Objetivo:** RE a profundidad de las 17 apps ≥100k no cubiertas aún, documentadas como las 45
previas (REA-*/ con 00-RESUMEN + capítulos + manifest + hashes + .gitignore).

## Las 17 (orden de descarga = prioridad)

| Prioridad | App | Package | Foco |
|-----------|-----|---------|------|
| 1 | 911MóvilBC | `com.c4bc.alerta066m` | 🆘 emergencias — ¿cleartext como 911-CDMX? |
| 2 | Botón de Auxilio Escudo Urbano | `com.c5.boton_auxilio_jalisco_flutter` | 🆘 pánico Flutter |
| 3 | Jalisco Alerta | `mx.erlia.jal.android` | 🆘 emergencias |
| 4 | Veracruz Mujer Alerta | `com.artech.nuevoalerta.alertamujer` | 🆘 pánico + **GeneXus** (cruce vendor con SEG-Móvil) |
| 5 | Internet para el Bienestar | `com.qvantel.bienestar.preprod` | 🚩 package preprod |
| 6 | REFAyGN (SEDENA) | `com.sedena.refam` | 🚩 militar |
| 7 | ConsulApp Contigo (SRE) | `com.sre.consulapp` | consulados/pasaporte |
| 8 | Tren Maya Boletos | `com.indra.trenmaya.pro` | pagos (vendor Indra) |
| 9 | CAASIM | `mx.gob.hidalgo.caasim.app` | pagos agua |
| 10 | CONAGUAClima | `conagua.gob.mx.smntiempo` | federal clima |
| 11 | eSIT Móvil Hidalgo | `com.esitmovil` | movilidad |
| 12 | Preinscripciones Coahuila | `com.luca.preinscripciones` | datos de menores |
| 13 | Trámites SHP Jalisco | `mx.gob.jalisco.gobiernoenlinea2` | trámites |
| 14 | Tuzobús | `com.tuzobus` | transporte/pago |
| 15 | SEV Empleado Veracruz | `mx.gob.sev.empleados` | nómina |
| 16 | Sonora Digital | `mx.gob.sonora.sonoradigital` | multi-trámite |
| 17 | Descarga Cultura UNAM | `mx.unam.codc.android.descargaculturaunam` | bajo riesgo |

## Fases

1. **Descarga** (`descargas-apk/lote4/`, apkeep apk-pure arm64) — en curso.
2. **Verificación** por app: package real (`inspect_android_package`), firma (apksigner →
   auténtica vs re-firmada APKPure), sha256. Regla dura: verificar package antes de documentar.
3. **Triage de pila**: nativa / Flutter / .NET / GeneXus / Capacitor / Veritran. Ojo: libs nativas
   viven en el split `config.arm64_v8a`, no en el base.
4. **RE por batches de ~5** (forks con rea MCP): manifest, módulos, red/endpoints, TLS (trust-all?),
   cleartext, secretos, almacenamiento, SDKs terceros.
5. **Cruce** contra `analisis/MAPA-PROVEEDORES-SDK.md`: ¿911MóvilBC repite el cleartext de 911-CDMX?
   ¿Veracruz Mujer Alerta repite el defecto GeneXus de SEG-Móvil? ¿preprod de Qvantel?

## Hipótesis a probar (del mapa)

- **Apps de pánico → cleartext/TLS-off** (patrón 911-CDMX). 4 candidatas.
- **GeneXus (com.artech) → defecto de producto compartido** con SEG-Móvil.
- **`...preprod` en producción** (Internet para el Bienestar) → endpoints/entorno de prueba.
