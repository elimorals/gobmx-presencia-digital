# 04 — Autenticación: Llave CDMX

La identidad del ciudadano en App CDMX se centraliza en **Llave CDMX**, el sistema de
identidad única del Gobierno de la Ciudad de México. El módulo `mx.gob.cdmx.adip.llave` es
transversal: varios servicios exigen una sesión Llave para operar.

## Evidencia estática

Clases y modelos (de los DEX):

```
Lmx/gob/cdmx/adip/llave/ui/activities/LlaveActivity;
Lmx/gob/cdmx/adip/llave/model/Type;
Lmx/gob/cdmx/adip/llave/model/ResponseUsers;
Lmx/gob/cdmx/adip/llave/model/ResponseUsersAPI;
Lmx/gob/cdmx/adip/utils/model/LlaveData;
Lmx/gob/cdmx/adip/utils/LlavePreferencesManager;
```

Claves de preferencias (sesión persistida):

```
mx.gob.cdmx.adip.app.llave.release.KEY_LOGIN
mx.gob.cdmx.adip.app.llave.release.KEY_USER_INFO
KEY_LLAVE_PREFERENCES_REQUIRE_UPDATE
```

Endpoints:

```
https://llave.cdmx.gob.mx/
https://actualizadatosllave.cdmx.gob.mx/
```

Helpers de UI: `btnLlaveInfo`, `getLlaveData` / `setLlaveData` / `useLlaveData`,
`showModuleMessageRequireLlaveCDMX` (mensaje "este módulo requiere Llave CDMX"),
`Events/Llave` (evento de analítica del flujo de login).

## Flujo inferido

1. `LlaveActivity` lanza el login contra `llave.cdmx.gob.mx` (WebView / OAuth del portal
   Llave — no se confirma el `grant_type` por estática).
2. La respuesta (`ResponseUsers` / `ResponseUsersAPI`) se materializa en `LlaveData` y se
   guarda vía `LlavePreferencesManager` (`KEY_LOGIN`, `KEY_USER_INFO`).
3. `KEY_LLAVE_PREFERENCES_REQUIRE_UPDATE` y `actualizadatosllave.cdmx.gob.mx` implementan
   un flujo de **actualización obligatoria de datos** del perfil Llave.
4. Módulos que requieren identidad llaman `showModuleMessageRequireLlaveCDMX` si no hay
   sesión y redirigen a `LlaveActivity`.

## Biometría

Permiso `USE_BIOMETRIC` + referencias a `BiometricPrompt`: la app ofrece desbloqueo
biométrico de la sesión local (patrón: proteger el acceso a la sesión Llave persistida
tras el primer login, sin re-autenticar contra el servidor en cada apertura).

## Notas de seguridad (estáticas, a verificar en runtime)

- **Dónde vive la sesión — CONFIRMADO (decompilado, ver `decompiled/LlavePreferencesManager.java.md`).**
  `LlavePreferencesManager.mPref` es un `android.content.SharedPreferences` **plano**, y
  `saveUserInfo(String)` hace `prefs.putString(KEY_USER_INFO, str)`. El blob de sesión Llave
  se guarda **en claro**, sin `EncryptedSharedPreferences` ni Tink (que sí está en el binario,
  doc 08). En almacenamiento privado es inaccesible sin root; con `allowBackup="false"` el
  vector de backup está mitigado, pero en dispositivo rooteado/forense la sesión queda legible.
  Es el hallazgo de seguridad más concreto del análisis.
- **Un login, N servicios.** Comprometer Llave da acceso transversal (pagos, documentos,
  taxi, salud). El modelo de amenazas debe tratar la sesión Llave como credencial de alto
  valor, no como login de una sola app.
- **Duplicación de `LlaveData`.** El mismo modelo existe en `llave.model`,
  `locatel.model.report` y `locatelreport.model.report`: cada módulo reimplementa el DTO,
  lo que dificulta un manejo uniforme (y auditable) de la identidad.
