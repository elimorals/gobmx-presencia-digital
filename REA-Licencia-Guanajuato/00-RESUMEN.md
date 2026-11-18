# 00 · Resumen ejecutivo — Licencia Digital Guanajuato (ZTK)

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital Guanajuato |
| Package | `com.ztk.licencia.digital.gto` (**verificado** con `inspect_android_package`) |
| versionName / versionCode | 2.1.1 / 36 |
| minSdk / targetSdk | 24 / 36 |
| Pila | **Flutter** (`libflutter.so` + `libapp.so` Dart AOT) |
| Firma | ⚠️ **re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — no auténtica; cruzar con canal oficial |
| SHA-256 cert (re-firma) | `7e178888da1610a26a46a33b6fe1b8ca72305beaaa2174686cbb2b244b956963` |
| sha256 XAPK | `cc3ca853a3c797bab287aef7e57bcc2094b900e89a9f4a9c77f7207031740670` |
| Clases | 6 962 |
| Dueño | Gobierno del Estado de Guanajuato (vendor **ZTK**) |

## Propósito del análisis

Cruce de proveedores de **licencia de conducir digital**: ¿el defecto TLS trust-all del producto
**Cosmocolor** (EDOMEX/Jalisco/Chiapas) se extiende a otros estados, o cada vendor tiene lo suyo?

## Veredicto: ZTK ≠ Cosmocolor (vendor y stack distintos)

- **Stack:** Flutter (Dart AOT), **no** nativo Kotlin+Retrofit como Cosmocolor. Por tanto el defecto
  concreto de Cosmocolor (`ServiceGenerator.createService` con trust-all en Retrofit) es
  **estructuralmente inaplicable** aquí.
- **Backend:** `licenciadigitalgtoapp.appspot.com` → **Google App Engine / Firebase**, no el
  `api1.licenciamanejo.com` de Cosmocolor. Infra y proveedor distintos.
- **Permisos:** biometría, cámara, almacenamiento, INTERNET, AD_ID. **Sin SMS ni ubicación**
  (más acotado que Cosmocolor EDOMEX).

## Hallazgos (tabla)

| # | Hallazgo | Severidad | Estado |
|---|----------|-----------|--------|
| H1 | **No** repite el trust-all de Cosmocolor (stack Flutter, no Retrofit) | — | **AUSENTE-SANO** |
| H2 | TLS/cleartext: sin `usesCleartextTraffic`, sin NSC, targetSdk 36 → cleartext bloqueado | — | **AUSENTE-SANO** |
| H3 | Validación TLS real del cliente Dart (badCertificateCallback) | — | **A-CONFIRMAR** (muro AOT) |
| H4 | Backend de licencias en **Firebase/GAE** (`appspot.com`), no en `gob.mx` | BAJA | gobernanza de datos |
| H5 | Re-firmada APKPure → la firma no prueba autenticidad | INFO | proveniencia |

Detalle de red/TLS en [02-red-tls.md](02-red-tls.md); manifiesto en [01-manifiesto.md](01-manifiesto.md).
