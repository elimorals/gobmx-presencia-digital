# SDKs de terceros

La app mantiene **doble ecosistema** (Google **y** Huawei) porque se distribuye en Play Store
y en Huawei AppGallery. Inventario por evidencia de manifiesto, assets y clases.

## Google / Firebase

| SDK | Evidencia | Rol |
|---|---|---|
| Firebase Cloud Messaging | `.FirebaseMessagingService`, `FirebaseInstanceIdReceiver` | Push |
| Firebase Auth | `GenericIdpActivity`, `RecaptchaActivity`, `default_web_client_id` | Federación / reCAPTCHA |
| Firebase Analytics | `AppMeasurementReceiver/Service`, `app-measurement.com` | Métricas de uso |
| Firebase Crashlytics | `mapping_file_id` en strings | Reporte de crashes |
| Firebase In-App Messaging | `firebase-inappmessaging*.properties` | Mensajes in-app |
| Firebase Remote Config / ABT | `firebase-abt.properties` | Flags remotos / A-B |
| Firebase Realtime Database | `cfe-contigo.firebaseio.com` | DB en tiempo real (uso puntual) |
| Google Play Core | `PlayCoreDialogWrapperActivity` | Updates in-app |
| Google Maps | `<package com.google.android.apps.maps>`, `ACCESS_FINE_LOCATION` | Ubicar centros CAC |
| Install Referrer | `BIND_GET_INSTALL_REFERRER_SERVICE` | Atribución de instalación |

Proyecto Firebase: `506803695643` (app id `1:506803695643:android:795214df7d4a207e`).

## Huawei Mobile Services (HMS)

| SDK | Evidencia | Rol |
|---|---|---|
| HMS Push | `HuaweiMessagingService`, `PushMsgReceiver`, `PushReceiver`, `PushProvider` | Push en dispositivos Huawei |
| AGConnect | `AGConnectInitializeProvider`, `agconnect-core.properties` | Backend-as-a-service Huawei |
| HMS Core / aaid | `com.huawei.hms.aaid.InitProvider` | ID de anuncios/instancia |
| GRS (Global Routing Service) | `assets/grs_sdk_*.json`, `grs_sp.bks` | Enrutado de servicios HMS por región |
| Almacenes de CA | `hmsincas.bks`, `hmsrootcas.bks` | Validación TLS de servicios HMS |

## Identidad / OAuth

- **AppAuth** (`net.openid.appauth`): OAuth2/OIDC Authorization Code + PKCE, redirect
  `mx.com.cfe.cfecontigo://` (doc 02).

## Anti-fraude

- **LexisNexis ThreatMetrix** (`com.lexisnexisrisk.threatmetrix.rl.*`, `h-sdk.online-metrix.net`):
  fingerprinting de dispositivo y scoring de riesgo para pagos/contratación (doc 05, 06).

## Utilidades

- **ZXing** vía `com.journeyapps.barcodescanner` (`CaptureActivity`): lectura de QR / código
  de barras (p.ej. número de servicio en el recibo).
- Mapas de Huawei: referencia `9at.bluesource.choicesdk.maps.com` (ChoiceSDK, capa que abstrae
  Google Maps vs. Huawei Maps).

## Consideraciones de privacidad

La app envía telemetría a **múltiples terceros**: Google (Analytics, Crashlytics), Huawei
(HMS/AGConnect) y LexisNexis (ThreatMetrix). Para una app de una entidad pública que maneja
datos fiscales y de pago, el flujo de datos de dispositivo hacia ThreatMetrix y los analytics
de Google/Huawei es el punto de privacidad más relevante a revisar contra su aviso
(`assets/avisoprivacidad.md`, embebido en la app).
