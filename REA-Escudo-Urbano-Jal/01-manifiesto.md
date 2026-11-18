# 01 · Manifiesto

- **Package:** `com.c5.boton_auxilio_jalisco_flutter` · versión 2.1.7 (39).
- **minSdk 25 / targetSdk 36.** targetSdk 36 → el sistema **bloquea cleartext por defecto** salvo
  que el NSC lo permita (no lo permite; ver doc 04).
- **Permisos:** `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` (ubicación para la alerta),
  `READ_PHONE_STATE`, `READ_CONTACTS` (contactos de emergencia), `INTERNET`, `VIBRATE`, `WAKE_LOCK`,
  `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION/AD_ID`,
  `BIND_GET_INSTALL_REFERRER_SERVICE`, permiso dinámico propio `DYNAMIC_RECEIVER_NOT_EXPORTED`.
  - **Ausencias notables (positivo):** no pide ubicación en **segundo plano**, ni **SMS**, ni
    **audio** — a diferencia de otras apps de pánico.
- **Componentes exportados:** 2 (dentro de lo normal para una app Flutter con deep links / receiver
  de install referrer). No se observaron componentes sensibles exportados sin protección.
- **Flags:** sin `android:debuggable`; `allowBackup` por defecto.

Manifest decodificado (xmltree de aapt2) en [`AndroidManifest.xml`](AndroidManifest.xml).
