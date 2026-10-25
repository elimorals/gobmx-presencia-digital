# 03 — Red y endpoints

A diferencia de CFE Contigo (DexGuard cifró todas las URLs), en App CDMX **los endpoints
están en claro** en los DEX: PairIP protege integridad y flujo, no literales de string.
Se extrajeron 54 URLs (`evidence/endpoints.txt`). Pila: **Retrofit2 + OkHttp3 + Gson**.

## API principal — monolito CodeIgniter

Todo el backend propio cuelga de una sola base con enrutado estilo CodeIgniter
(`index.php/`), versionada en `/v4/`:

```
https://apiapp.cdmx.gob.mx/index.php/v4/
```

### Sub-servicios observados (`/v4/<Servicio>/`)

| Recurso | Servicio asociado |
|---|---|
| `Startup/` | Bootstrap de configuración al abrir la app |
| `Users/`, `Users/Notifications/`, `Users/Routes/`, `Users/Vehicles/` | Perfil, notificaciones, rutas y vehículos del usuario |
| `Location/` | Geolocalización / catálogo de ubicaciones |
| `MovilidadIntegrada/`, `.../Incidents/`, `.../MetrobusArrivalTime/`, `.../Schedules/` | Transporte: incidencias, arribo de Metrobús, horarios |
| `Taxi/`, `.../Requests/`, `.../Trips/`, `.../Route/`, `.../Driver/`, `.../Badges/`, `.../CheckCards/` | Mi Taxi (pasajero y chofer) |
| `MP/`, `SafPayments/`, `MIVC/` | Pagos (Mercado Pago, pagos seguros, tarjeta virtual) |
| `ClinicaCondesa/` | Clínica Condesa |
| `CulturaEntretenimiento/` | Cultura y entretenimiento |
| `DigitalDocuments/` | Documentos digitales |
| `DigitalComplaint/`, `AnticorruptionComplaint/`, `CitizenReport/ComplaintHistory/` | Denuncia digital, anticorrupción, reporte ciudadano |
| `Locatel/Chat/`, `Locatel/Reports/` | LOCATEL |
| `Emergencies/` | Emergencias / botón de pánico |
| `AirQuality/`, `Weather/` | Calidad del aire y clima |
| `Verificentros/` | Verificación vehicular |
| `NC/` | (sin determinar — posible "Notificaciones/Contenido") |

> El versionado `/v4/` indica evolución larga de la API. Un monolito `index.php` enrutando
> ~25 servicios es un **punto único** de disponibilidad y de superficie: conviene tratar
> `apiapp.cdmx.gob.mx` como el activo crítico del ecosistema.

## Pagos — Mercado Pago (ver doc 06)

```
https://api.mercadopago.com/semovi/appcdmx/v1/transactions/confirm
https://www.app.cdmx.gob.mx/MP/Payment
https://www.app.cdmx.gob.mx/MP/Challenge
```

## Identidad — Llave CDMX (ver doc 04)

```
https://llave.cdmx.gob.mx/
https://actualizadatosllave.cdmx.gob.mx/
```

## Otros hosts gob.mx (WebViews / trámites / avisos)

| Host | Uso |
|---|---|
| `311locatel.cdmx.gob.mx` | Portal LOCATEL 311 |
| `app.semovi.cdmx.gob.mx/citas/` | Citas SEMOVI |
| `sedema.cdmx.gob.mx/programas/.../verificacion-vehicular` | Info verificación |
| `tramites.cdmx.gob.mx/fotocivicas/public/` | Fotocívicas (fotomultas) |
| `atencionalaviolencia.semujeres.cdmx.gob.mx` | Atención a la violencia (SEMUJERES) |
| `denunciadigital.cdmx.gob.mx/aviso-de-privacidad` | Aviso de privacidad |
| `adip.cdmx.gob.mx/storage/.../Documentos/AVISO...` | Avisos legales |
| `app.cdmx.gob.mx/Mundial/*` | Resultados Mundial 2026 |
| `www.cdmx.gob.mx/` | Portal institucional |

## Infraestructura de terceros

- `firebaseinstallations.googleapis.com/v1/` — Firebase Installations (FID).
- `www.googleapis.com/auth/{appstate, datastoremobile, drive, drive.appdata, drive.file,
  drive.apps}` — scopes OAuth de Google (respaldo/estado vía Drive y Firebase Auth).

## Seguridad de transporte

`usesCleartextTraffic="false"` (manifiesto) + el asset `assets/ca.cert.der` sugieren
**certificate pinning** contra una CA propia (doc 07). No se observaron dominios dev/QA en
claro. Las cadenas de cipher suites halladas en DEX son la lista estándar de OkHttp
(TLS 1.2/1.3), no configuración insegura.
