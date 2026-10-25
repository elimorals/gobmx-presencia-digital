# 04 — Criptografía y almacenamiento

## Almacén de claves

No existe un paquete de cripto propio (`com.procesar.aforemovil.commons.security` no tiene clases).
La app **delega** la protección de claves en:

- **Daon SecureKeyStore** sobre **AndroidKeyStore**: clases `com.daon.sdk.crypto.SecureKeyStore`,
  `com.daon.sdk.crypto.keystore.a..f`, y la bandera `com.daon.sdk.keystore.hardware.only`
  (string en DEX) → se exige respaldo por **hardware** (TEE/StrongBox) para el material FIDO/UAF.
- **Claves ocultas en nativo**: `libkeys.so`, `libkeys_core.so`, `libidx_facial_keys.so` guardan
  licencias Daon y secretos del módulo facial fuera del DEX (por eso no aparecen en strings del
  bytecode). Ver doc 06.

El material de autenticación (credenciales UAF, GestoPago) vive en el keystore por hardware; la
app no implementa su propio AES/RSA a mano según lo observado.

## Base de datos — Realm

Inicializada en `BaseApp.onCreate` → `RealmConfigs.INSTANCE.initRealmConfigs()`
(`librealm-jni.so`). Dos módulos de esquema y controladores:

| Elemento | Detalle |
|---|---|
| Módulos | `realm.modules.BaseRealmModule`, `GoalsRealmModule`, `RealmConfigs` |
| Controladores | `realm.controller.RealmController`, `RealmControllerGoal` |
| Migraciones | `realm.migrations.RealmBaseMigration`, `RealmGoalMigration` |
| Modelos (~25) | `Afore`, `Amount`, `Bank`, `Beneficiary*`, `CountryRealm`, `EconomicActivity`, `Employment`, `Goal`, `GestoPago`, `MasterCatalog`, `MinorRelationship`, `Navigation`, `Partner`, `Periodicity`, `PsContent`, `Scholarship`, `UXUIConfig`, `UXUIDetails`, `FaqRealm`, `FaqSection`, `FilenetImagesStatus`, `ValidityFilesInfoRealm`, `BeneficiariesParameters`, `BeneficiaryRelationship` |

Realm guarda catálogos (bancos, países, AFOREs, actividad económica), configuración de UI dirigida
por servidor (`UXUIConfig`/`UXUIDetails`/`Navigation`) y estado de usuario (metas, beneficiarios,
autorizaciones GestoPago).

### Cifrado de Realm — CONFIRMADO (ambas BD)

`RealmConfigs.initRealmConfigs()` (decompilado en
[`decompiled/RealmConfigs.initRealmConfigs.java`](decompiled/RealmConfigs.initRealmConfigs.java))
abre **las dos** bases con `encryptionKey(...)`:

```java
byte[] bytes = realmEncryptionKey().getBytes(UTF-8);
aVar.c(bytes);   // RealmConfiguration.Builder.encryptionKey(byte[])
```

- BD base: `Constants.SCHEMA_NAME_BD`, schemaVersion 4, `BaseRealmModule`, cifrada.
- BD metas: `Constants.SCHEMA_NAME_GOAL_BD_ENC` (sufijo `_ENC`), schemaVersion 1, `GoalsRealmModule`, cifrada.

La llave viene de un helper `realmEncryptionKey()` (String → bytes UTF-8; Realm exige 64 bytes, así
que el String mide 64 chars). **La procedencia de esa llave no se resolvió en este pase estático**
(el helper no se ubicó en su clase declarante). Candidatos: constante embebida (cifrado débil si es
fija), valor derivado del dispositivo/AndroidKeyStore, o material en `libkeys.so`. Es el punto a
confirmar: *que* Realm está cifrado es un hecho; la *fuerza* depende de dónde salga la llave.

## Preferencias — Jetpack DataStore

`DataStoreManager.INSTANCE.handleMigration(this)` en el arranque; `libdatastore_shared_counter.so`.
Sustituye a SharedPreferences para flags/preferencias (`UserPreferencesUiState`). La migración en
`onCreate` sugiere que vienen de una versión previa con SharedPreferences.

## Datos en reposo — respaldo y transferencia (postura fuerte)

`android:allowBackup="false"` **y** reglas explícitas que excluyen todo:

`res/xml/backup_rules.xml` (full-backup):
```xml
<full-backup-content>
  <exclude domain="sharedpref" path="." />
  <exclude domain="database"  path="." />
  <exclude domain="file"      path="." />
  <exclude domain="root"      path="." />
</full-backup-content>
```

`res/xml/data_extraction_rules.xml` (Android 12+): bajo `<cloud-backup>` **y** `<device-transfer>`
excluye `root`, `file`, `database`, `sharedpref` y `external`. → Ni respaldo en la nube ni
transferencia a un dispositivo nuevo copian datos de la app. **Mejor práctica**, correcta para una
app del SAR.

## Exposición de ficheros — FileProvider

`res/xml/file_paths.xml` expone únicamente:
```xml
<paths><cache-path name="images" path="images/" /></paths>
```
Solo la caché de imágenes se comparte por `content://` (p. ej. para previsualizar capturas).
Contrasta favorablemente con jw-library/CFE, que exponían `root-path "/"`.

## Resumen

| Aspecto | Estado |
|---|---|
| Keystore | Daon SecureKeyStore sobre AndroidKeyStore, **hardware-only** |
| Secretos de cliente | Ocultos en `.so` nativas (no en DEX) |
| DB local | Realm **cifrado** en ambas BD (`encryptionKey`); procedencia de la llave sin resolver |
| Preferencias | DataStore |
| Backup / device-transfer | **Deshabilitado y excluido por completo** |
| FileProvider | Acotado a `cache/images/` |
