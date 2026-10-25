# 03 · Red y endpoints

Estático. No se ejecutó la app ni se interceptó tráfico.

## No hay endpoint del backend en texto plano

Barrido de URLs sobre **todo** el árbol decodificado (smali + recursos + assets): no aparece ninguna
URL de backend del banco ni de gateway Veritran. Lo único hallado son URLs de librerías de terceros:

- `app-measurement.com` (Firebase Analytics), `firebase-settings.crashlytics.com` (Crashlytics),
  `openid.net`/`kubernetes.io` (refs de documentación en libs). Nada de `bancodelbienestar`/`veritran`.

**Causa (coherente con doc 02):** en una app Veritran el endpoint del gateway se resuelve en runtime
desde la configuración/`.szip` (y los `contract` de Veritran), no como literal en el binario. Las
rutas reales de API **sólo** se obtienen con análisis dinámico (MITM) — aquí **no se afirman**.

## Capa de red

| Componente | Evidencia |
|---|---|
| **Retrofit2 + OkHttp3** | `retrofit2/converter`, `okhttp3/internal` |
| **Conscrypt** (proveedor TLS moderno) | `org/conscrypt`, `libconscrypt_jni.so` |

El uso de Conscrypt (BoringSSL de Google) da TLS actualizado independiente de la versión de Android.

## Certificate pinning

- `android:networkSecurityConfig="@xml/network_security_config"` **pero el archivo está vacío**:
  `<network-security-config />`. → **sin pinning declarativo** y sin overrides de confianza.
- Con `targetSdk 36`, el tráfico en claro está deshabilitado por defecto de plataforma.
- Si hay pinning, estaría implementado **en código** (OkHttp `CertificatePinner`/Conscrypt) o en la
  **capa nativa de Veritran**. No se determinó estáticamente; **no se afirma** ni su presencia ni su
  ausencia a nivel código (sólo que a nivel manifiesto/NSC no lo hay).

## Push y OTP

- **Firebase Cloud Messaging** — proyecto `app-banco-del-bienestar` (sender `56592496113`,
  app id `1:56592496113:android:399b54116e6c1ba96d8050`).
- **SMS Retriever API** (`SMSBroadcastReceiver`) — autocompletado de OTP sin permiso de SMS.

## Qué queda abierto (honesto)

- Gateway Veritran, rutas de API, formato de payload y tokens: **no determinados** (requieren
  dinámica + posible desempacado del `.szip`). No se inventan.
