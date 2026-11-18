# 01 · Manifiesto

Manifest decodificado completo en [`AndroidManifest.xml`](AndroidManifest.xml) (aapt2 xmltree, 453
líneas).

## Permisos

Sensibles:
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` — ubicación (núcleo de la alerta).
- `READ_CONTACTS`, `WRITE_CONTACTS` — contactos (de confianza para la alerta).
- `CAMERA` — evidencia.
- `FOREGROUND_SERVICE`, `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK` — servicio persistente.
- `INTERNET`, `ACCESS_NETWORK_STATE`, `CHANGE_NETWORK_STATE`, `ACCESS_WIFI_STATE`.
- `WRITE_EXTERNAL_STORAGE`.
- Push: `com.google.android.c2dm.permission.RECEIVE`, `C2D_MESSAGE` propio.

Resto: una batería larga de permisos de *badge/shortcut* de launchers OEM (Samsung, HTC, Sony, Oppo,
Huawei, etc.) — típico del template GeneXus, sin impacto de seguridad.

No declara `SEND_SMS`/`READ_SMS`: la alerta viaja por la API REST (push/HTTP), no por SMS.

## Componentes

~30+ componentes (actividades, servicios, receivers, providers). Varios **exported=true**
(actividad launcher, receiver de arranque, servicios FCM) — estándar del runtime GeneXus + Firebase.
No se halló un componente exportado que exponga lógica sensible sin permiso más allá de lo esperado;
el riesgo dominante es de red (ver doc 03), no de IPC.

## `networkSecurityConfig`

`application android:networkSecurityConfig=@0x7f120002` → `res/8G.xml`. Contenido en doc 03: permite
cleartext para `alertagenero.sspver.gob.mx`. **No** declara `usesCleartextTraffic` a nivel app (lo
gobierna el NSC).
