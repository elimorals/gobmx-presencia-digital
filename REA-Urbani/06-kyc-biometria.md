# 06 — KYC e identidad: doble verificación facial

Urbani integra **dos** proveedores de verificación de identidad con prueba de vida facial, más
biometría local. Para una app de transporte municipal es un stack de KYC notablemente pesado.

## 1) Baidu Face SDK (liveness)

SDK chino de reconocimiento y prueba de vida facial de Baidu.

**Clases DEX (`com.baidu.idl.face.platform.ui.*`, en manifest):**

```
HybridLivenessActivity (com.example.baidu_face_sdk)
FaceColorLivenessActivity / ...VideoActivity       (liveness por color de pantalla)
FaceSilenceLivenessActivity / ...VideoActivity     (liveness pasivo/silencioso)
FaceActionLivenessActivity / ...VideoActivity      (liveness por acción: parpadeo, giro)
FaceDistanceLivenessActivity / ...VideoActivity    (liveness por distancia)
CollectionSuccessActivity / CollectFailureActivity (resultado)
```

**Librerías nativas + modelos (en `config.armeabi_v7a`):**

```
libbdface_sdk.so                 2,925,072
libbd_facecollect_unifylicense.so  247,664   (licencia del SDK)
libbaiduprotect_sec_jni_v3_lite_face.so  50,748
```

Modelos **PaddleLite** embebidos (strings `BDFaceSecurityMark`): `occlusion` (oclusión de rostro),
`blur` (desenfoque), `detect_rgb` (detección), `rgb_align` (alineación). Son los modelos de
antifraude/calidad del liveness de Baidu.

## 2) Veriff (KYC)

Proveedor estonio de verificación de identidad (documento + selfie).

```
com.veriff.sdk.views.base.verification.VeriffActivity
com.veriff.sdk.service.SendAuthenticationFlowDataToServerService  (foregroundServiceType=dataSync)
com.veriff.sdk.camera.*   (CameraX propio del SDK)
permiso: com.miruta.mty.VERIFF_STATUS_BROADCAST_PERMISSION (signature)  "Get Veriff status reports"
```

`SendAuthenticationFlowDataToServerService` indica que Veriff **sube** los datos del flujo de
verificación a su servidor (endpoint propio de Veriff, no observable aquí).

## 3) Biometría local

- Plugin `io.flutter.plugins.localauth` (`LocalAuthPlugin`, `AuthenticationHelper`) → huella/rostro
  del dispositivo para autenticar en la app.
- Permisos `USE_BIOMETRIC`, `USE_FINGERPRINT`.

## Visión de apoyo (ML Kit)

`MlKitComponentDiscoveryService` registra **Barcode**, **Face** y **Vision** de Google ML Kit
(`libbarhopper_v3.so` = barcode), usados para escanear QR/códigos y detección facial auxiliar.

## Nota de seguridad / soberanía de datos

Es una app de **gobierno municipal** que captura **biometría facial** y la procesa con **Baidu**
(proveedor chino) y la envía a **Veriff** (proveedor europeo). Para datos biométricos de ciudadanos,
la dependencia de un SDK extranjero — en particular el stack Baidu (packer + face + licencia nativa)
— plantea una cuestión de **soberanía y tratamiento de datos personales sensibles**. Qué se envía,
a dónde y con qué consentimiento **no es observable estáticamente** (lógica en Dart cifrado); se
deja planteado como hallazgo de riesgo, no como fuga confirmada. Ver doc 10 (H3).
