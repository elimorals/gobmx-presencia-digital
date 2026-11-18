# 01 · Manifiesto

`AndroidManifest.xml` (dump xmltree de aapt2) está junto a este archivo.

## Permisos (solo 3 — notablemente mínimo)

- `android.permission.INTERNET`
- `android.permission.ACCESS_NETWORK_STATE`
- `com.sedena.refam.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (permiso propio para proteger un
  receiver dinámico — patrón estándar de Flutter/WorkManager)

**Lo que NO pide** (relevante para una app de registro de armas): sin ubicación, sin cámara, sin
almacenamiento externo, sin SMS, sin contactos, sin biometría declarada en el manifest. La
superficie de permisos es de las más pequeñas del corpus.

## Componentes

| Tipo | line | exported | Nota |
|------|------|----------|------|
| activity | 26 | **true** | launcher (MainActivity de Flutter) — exported por ser launcher, normal |
| activity | 59 | false | interna |
| provider | 71 | false | FileProvider (para compartir PDFs) — no exportado |
| receiver | 80 | **true** | protegido por `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (Flutter) |

El único componente exportado sin protección es el launcher, que es lo esperado. El receiver
exportado está protegido por permiso propio. **Sin superficie de ataque obvia por componentes.**

## Red

- **Sin** atributo `android:usesCleartextTraffic` → con `targetSdk=34` el cleartext queda
  **bloqueado por defecto**.
- **Sin** `networkSecurityConfig` (no hay NSC permisivo ni restrictivo; se usa el default seguro de
  la plataforma).
