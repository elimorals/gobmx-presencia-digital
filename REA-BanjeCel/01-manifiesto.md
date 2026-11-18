# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 7.10.3.50500, compileSdk 34.

## Permisos solicitados (amplios)

| Permiso | Para qué (inferido del uso) |
|---|---|
| `CAMERA` | captura facial (biometría, doc 03) + lectura de documentos |
| `RECORD_AUDIO` | **verificación de voz** (biometría, doc 03) |
| `USE_FINGERPRINT` | desbloqueo biométrico local |
| `NFC` | lectura NFC (tarjeta / credencial) |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | geolocalización (riesgo/antifraude) |
| `READ_PHONE_STATE` | identificación de dispositivo |
| `READ_CONTACTS` | transferencias a contactos (inferido) |
| `WRITE_SETTINGS`, `WRITE_EXTERNAL_STORAGE`, `READ_EXTERNAL_STORAGE` | ajustes / almacenamiento |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | red |
| `POST_NOTIFICATIONS`, `c2dm…RECEIVE` | notificaciones push (FCM) |
| `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE` | servicios/background |
| badges de launchers (Sony/Huawei/HTC) | contador de icono |

**Lectura:** el conjunto es **amplio para una app bancaria**, pero cada permiso sensible tiene
justificación plausible en el flujo biométrico (cámara+micrófono para face+voice), huella, NFC
y antifraude (ubicación, phone-state). No se puede confirmar el uso exacto de `READ_CONTACTS`
ni de la ubicación desde el DEX (lógica en el bundle Veritran) → se reporta como permiso
declarado, no como uso verificado.

## Network security config

No se halló `res/xml/network_security_config` → política por defecto (sin cleartext en
targetSdk alto; confianza en CAs del sistema). El pinning, si existe, lo implementa la capa
Veritran/Conscrypt, no observable estáticamente.

## Componente de interés

El package incluye `VTUserApplicationBJCTMB` (la Application de Veritran) y el permiso
`DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` (patrón AndroidX). El grueso de los componentes es
runtime de Veritran + Firebase + GMS.
