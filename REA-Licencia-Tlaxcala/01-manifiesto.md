# 01 · Manifiesto

- **Package:** `com.dcsoluciones.licenciadigitalglobal` · v1.3 (vc26) · minSdk 23 / targetSdk 35.
- **Permisos (3, modestos):** `INTERNET`, `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE` +
  el permiso auto-generado `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`. **Sin** cámara, ubicación,
  SMS ni teléfono en el manifiesto — superficie de permisos baja para una app de licencia.
- **targetSdk 35:** cleartext bloqueado por defecto a nivel Android **salvo** lo que reabra la
  config de Capacitor (ver H1 en 02).
- Manifiesto decodificado en `AndroidManifest.xml` (aapt2 xmltree).

## Config de Capacitor (`assets/capacitor.config.json`)

```json
{
  "appId": "com.dcsoluciones.licenciadigitalglobal",
  "appName": "Licencia Digital TLX",
  "server": {
    "androidScheme": "https",
    "cleartext": true,
    "allowNavigation": ["prisma-apps.servehttp.com"]
  },
  "android": { "allowMixedContent": true }
}
```

- `cleartext:true` + `allowMixedContent:true` → el WebView **permite HTTP y contenido mixto**
  (CWE-319 enabler), aunque la API principal sea HTTPS.
- `allowNavigation` incluye `prisma-apps.servehttp.com` — un host de **DNS dinámico gratuito**
  (servehttp = No-IP). No aparece usado en el JS actual; residuo de proveedor/dev en la allowlist.
