# 10 — Hallazgos de seguridad (consolidado)

App **municipal** (transporte de Monterrey). Análisis **estático**; no se ejecutó la app ni se evadió
ninguna protección. Severidad es juicio del analista sobre el artefacto, no un CVSS formal.

## Resumen

| # | Hallazgo | Severidad | Base |
|---|---|---|---|
| H1 | Lógica Dart cifrada por packer chino Baidu | Info / opacidad | doc 03 |
| H2 | APK re-firmado (cert genérico) → autenticidad no verificable | Media | doc 09 |
| H3 | Doble KYC biométrico con SDK chino (Baidu) en app de gobierno | Media (privacidad) | doc 06 |
| H4 | Dos API keys Google en claro, restricción no verificada | Baja–Media | doc 07 |
| H5 | Tarjeta NFC HCE con `requireDeviceUnlock="false"` | Baja (config) | doc 05 |
| H6 | FileProvider `cache-path path="/"` | Baja | doc 09 |
| H7 | **Tráfico HTTP en claro** (Flutter ignora el NSC; `media-manager` por HTTP 200) + sin cert pinning | Media | docs 04, 11 |
| H8 | `libed66.so` de función no determinada | Info | doc 03 |
| D1 | **Logging verboso en release** (endpoints, JSON, GPS, FCM token en logcat) | Media | doc 11 |
| D2 | **FCM token en claro** en logcat | Baja | doc 11 |
| D3 | **GMS/App Check degradado por el re-firmado** (SecurityException gms) | Baja (efecto de H2) | doc 11 |
| D4 | **Backend en operador privado `urbani.io`** (no gob.mx) + GPS en 2º operador `urbiaccess.io` | Info / gobernanza | doc 11 |
| D9 | **Llaves de usuario server-side** (`/app/p/userkeys`) — diseño de la cripto de la tarjeta | Info | doc 11 |

## Detalle

**H1 — Dart cifrado (opacidad analítica).** `libapp.so` tiene entropía 7.995 bits/byte y 0 secciones
de snapshot Dart (magic `f5f5dcdc` ausente): el AOT está cifrado por **Baidu LianTian / Baidu
Protect**. La consecuencia para este trabajo es que **endpoints, cripto de la tarjeta y flujos de
pago/recarga no se recuperan** estáticamente. No es una vulnerabilidad en sí (es una protección),
pero sí define el límite del análisis. Además, el packer trae `EmulatorAcquisition` (anti-emulador)
y `NativeCrashIntercept`, que dificultan el pase dinámico.

**H2 — Re-firmado.** Firmado con `CN=Android, O=Google Inc.` genérico y Source Stamp con el mismo DN
(no el de Google Play). El artefacto fue re-empaquetado por un mirror (APKPure): **la autenticidad
del publicador original no es verificable** y no hay garantía de integridad frente al APK de Play.

**H3 — Biometría facial con proveedor chino (privacidad / soberanía de datos).** Una app de gobierno
municipal captura **rostro** de ciudadanos y lo procesa con el **stack Baidu** (Face SDK + modelos
PaddleLite + licencia nativa) además de **Veriff**. Qué datos salen, a dónde y con qué consentimiento
**no es observable** (Dart cifrado). Se reporta como **riesgo de privacidad/soberanía**, no como fuga
confirmada. Es el hallazgo más relevante por el tipo de dato y de operador.

**H4 — API keys Google en claro.** Maps (`AIzaSyCqw6-…`) en manifest y Firebase (`AIzaSyCkqs…`) en
`strings.xml`. Es estándar, pero su seguridad depende de estar **restringidas** (SHA-1 del cert +
paquete). Como el APK está re-firmado (H2), la restricción por SHA-1 original podría no aplicar.
Estado de restricción **no verificado** (requiere la consola del proyecto).

**H5 — HCE sin desbloqueo.** `apduservice.xml` con `requireDeviceUnlock="false"`: la tarjeta de
transporte responde con el teléfono bloqueado. Habitual en transporte; el riesgo real depende de la
cripto/datos del APDU, **no recuperados** (Dart cifrado).

**H6 — FileProvider amplio.** `cache-path path="/"` expone todo el caché vía `content://`. Menor que
un `root-path "/"`, pero más amplio de lo necesario.

**H7 — Sin pinning de plataforma.** Cleartext efectivamente **bloqueado** en release (positivo), pero
sin `<pin-set>`. Si hay pinning, estaría en Dart (indeterminado). MITM con CA de sistema en
dispositivo propio sería el vector para un pase dinámico.

**H8 — `libed66.so` desconocida.** 1.4 MB, nombre no estándar, sin atribución con la evidencia
disponible. Pendiente.

## Controles positivos

- `allowBackup="false"`.
- **`flutter_secure_storage`** para secretos/tokens (`backup_rules.xml` excluye `FlutterSecureStorage` del respaldo → almacenamiento cifrado respaldado en Keystore).
- Cleartext bloqueado por NSC **solo para la pila Android** — ojo: el tráfico Flutter lo ignora y sí
  hubo HTTP en claro (H7/D7). No contar como control efectivo.
- **Firebase App Check** (Play Integrity + SafetyNet) anti-abuso.
- Exportados peligrosos (HCE, FCM, JobService) **protegidos por permisos de sistema/firma**.
- Packer con integridad/anti-tamper (rompe repackaging casual).

## Qué exigiría un pase dinámico (fuera de alcance aquí)

Recuperar endpoints del backend Urbani, protocolo APDU de la tarjeta, cripto y origen de llaves, y
destino de los datos biométricos: todo vive en el **Dart cifrado**. Requeriría dispositivo
**rooteado** + Frida para volcar `libapp.so` descifrada en memoria, sorteando `EmulatorAcquisition` y
el anti-tamper de LianTian. No se intentó.
