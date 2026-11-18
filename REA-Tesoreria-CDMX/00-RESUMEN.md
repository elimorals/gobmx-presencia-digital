# Resumen ejecutivo — Tesorería CDMX

## Identidad

| Campo | Valor |
|---|---|
| Nombre | Tesorería CDMX |
| Package | `mx.wdd.sefin` |
| Dependencia | Secretaría de Administración y Finanzas (SAF), Gobierno CDMX |
| Orden/poder | Estatal (CDMX) · Ejecutivo |
| Instalaciones | ≈500 K+ (Google Play), #22 del inventario |
| Firma | **Auténtica**: `CN=Eduardo Martinez, O=cweb, L=México` (vendor), verifica |
| Firebase project | `app-pagos-tesoreria` (sender `1015908174795`) |

## Pila

**Híbrida Capacitor** (appId `mx.wdd.sefin`, `webDir: www`) con frontend
**Angular/Ionic** compilado a chunks JS en `assets/public/` (legibles, no
ofuscados). Capa nativa Android mínima (plugins Capacitor). Backend **Laravel**
(rutas `*/public/api/`, OAuth2 Passport). Realtime con **Pusher**; push con
**Firebase/FCM**; escaneo con **MLKit Barcode**.

## Módulos (por endpoints y pantallas del bundle)

- **Pagos de tesorería**: predial, agua, tenencia/control vehicular, líneas de
  captura (siscorApis/srp), con pasarelas **Banorte** y **BBVA**.
- **Cuenta de usuario**: registro/login (OAuth2 ROPC), cambio de contraseña,
  biometría (re-login).
- **Consulta**: adeudos de tenencia por placa, campos por línea de captura, CSF
  (constancia de situación fiscal), citas.
- **Notificaciones**: push (FCM) + notificaciones locales.

## Hallazgos (detalle en doc 08)

1. Credenciales de comercio **Banorte** hardcodeadas en `main.*.js`.
2. `client_secret` de **Laravel Passport** hardcodeado (OAuth2 ROPC).
3. `usesCleartextTraffic=true` + NSC con cleartext a dominio de pagos e **IP interna**.
4. Endpoint **pre-producción** `prepro.adquiracloud.mx` en binario de producción.
5. Permisos `DUMP` y `RECORD_AUDIO` sin justificación visible.

## Método

Estático. `apktool d -s` para manifiesto/recursos; extracción de `assets/public/`
(bundle Capacitor) y `grep` sobre el JS para endpoints, configuración y secretos;
`apksigner verify --print-certs` para procedencia. No se ejecutó la app.
