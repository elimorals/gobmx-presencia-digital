# 01 · Manifiesto y permisos

Package `com.qvantel.bienestar.preprod`, minSdk 28 / targetSdk 35. Manifest decodificado en
`AndroidManifest.xmltree.txt`.

## H1 (ALTA) — Permisos privilegiados de operador telefónico

Para una app de autoservicio de un MVNO, este conjunto es **mucho más amplio** de lo que un cliente
necesita. Varios son permisos de **sistema/operador** (nivel `signature|privileged`), que Google Play
normalmente sólo concede a apps de operador o preinstaladas:

| Permiso | Riesgo |
|---------|--------|
| `READ_SMS` | Lectura de **todos** los SMS del dispositivo (no sólo el OTP propio). |
| `READ_CALL_LOG` | Historial de llamadas. |
| `READ_PRIVILEGED_PHONE_STATE` / `READ_PRECISE_PHONE_STATE` | Estado telefónico privilegiado (IMEI/IMSI/identificadores precisos). |
| `MODIFY_PHONE_STATE` | Modificar estado de la telefonía. |
| `WRITE_APN_SETTINGS` | Escribir la configuración de APN del dispositivo. |
| `BIND_CARRIER_SERVICES` | Enlazar servicios de operador. |
| `READ_PHONE_NUMBERS` / `READ_BASIC_PHONE_STATE` | Números y estado telefónico. |
| `ACCESS_BACKGROUND_LOCATION` + `FINE`/`COARSE` | Ubicación en segundo plano. |
| `CAMERA`, `USE_BIOMETRIC`, `USE_FINGERPRINT` | Cámara y biometría. |
| `FOREGROUND_SERVICE_LOCATION` / `_DATA_SYNC` / `_SPECIAL_USE` | Servicios en primer plano de ubicación/sync. |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`, `RECEIVE_BOOT_COMPLETED`, `SCHEDULE_EXACT_ALARM` | Persistencia/arranque. |
| `com.android.vending.CHECK_LICENSE` | Google Play Licensing. |

**Por qué importa.** `READ_SMS` + `READ_CALL_LOG` + estado telefónico privilegiado + APN +
carrier-services en una app de **158k instalaciones** es una superficie de ataque y un riesgo de
privacidad grande: si la app (o una de sus muchas dependencias embebidas: AWS SDK, Sentry,
Crashlytics, Flowable) filtra o abusa, el alcance es todo el SMS/registro de llamadas del usuario.
El principio de mínimo privilegio no se cumple para un autoservicio de cliente.

> **Matiz honesto:** no se confirmó (muro Hermes) qué código ejerce cada permiso ni a dónde van los
> datos. El hallazgo es que **se solicitan**, lo cual ya es excesivo para el caso de uso; el abuso
> concreto queda A-CONFIRMAR.

## Otros

- `android:usesCleartextTraffic="true"` (ver doc 03).
- `com.qvantel.bienestar.preprod.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` — permiso propio de RN.
- FCM (`c2dm.permission.RECEIVE`), AD_ID, install referrer.
