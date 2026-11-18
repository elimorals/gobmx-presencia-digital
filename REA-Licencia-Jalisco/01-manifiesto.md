# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.2.5, compileSdk 36.

## Permisos (de licencia de conducir con verificación de identidad)
Conjunto típico del white-label Cosmocolor: `INTERNET`/`ACCESS_NETWORK_STATE`, **`CAMERA`**
(captura de documento + rostro para Regula), almacenamiento de medios, notificaciones. Ver
`AndroidManifest.xml` para la lista exacta.

## Componentes
App Compose nativa (22 183 clases: Retrofit, RxJava2, SQLCipher, Regula Forensics, Firebase
Analytics `app-measurement.com`). Mismo armazón que Licencia EDOMEX.

## Flags de red
El defecto de TLS **no** está en el manifiesto (no depende de `networkSecurityConfig`): está en
**código**, en `ServiceGenerator.createService` (doc 02). Si hay un `network_security_config`, se
conserva en `evidence/`; pero la anulación de validación la hace el `X509TrustManager` custom, no
la config declarativa — por eso el `https://` no protege.
