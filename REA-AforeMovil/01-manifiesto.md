# 01 — Manifiesto, permisos y superficie de ataque

Fuente: `AndroidManifest.xml` decodificado (apktool 3.0.3). Ver copia completa en
[`AndroidManifest.xml`](AndroidManifest.xml).

## Permisos

| Permiso | Para qué (inferido) | Nota |
|---|---|---|
| `INTERNET`, `ACCESS_NETWORK_STATE` | Red | — |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Ubicación de sucursales/citas (Maps) | Peligroso; valorar si es necesaria la ubicación fina |
| `CAMERA` + `uses-feature camera / camera.front / autofocus` | Captura **facial** (selfie/liveness) y de **documento** (INE) | Núcleo del alta biométrica |
| `USE_BIOMETRIC`, `USE_FINGERPRINT` | Autenticación biométrica local (FIDO/UAF) | Daon ASM |
| `POST_NOTIFICATIONS` | Notificaciones (FCM) | Android 13+ |
| `com.google.android.c2dm.permission.RECEIVE` | Firebase Cloud Messaging | — |
| `AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`, `ACCESS_ADSERVICES_AD_ID` | ID de publicidad / attribution (Firebase/GMS) | Atención privacidad: una app del SAR solicitando AD_ID |
| `BIND_GET_INSTALL_REFERRER_SERVICE` | Install Referrer (atribución Play) | — |
| `RECEIVE_BOOT_COMPLETED` | Reprogramar WorkManager tras reinicio | — |
| `FOREGROUND_SERVICE` | Servicios en primer plano (WorkManager) | — |
| `VIBRATE`, `WAKE_LOCK` | UX / trabajo en segundo plano | — |
| `mx.com.procesar.aforemovil.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | Permiso propio nivel `signature` | Protege receivers dinámicos |

**No solicita**: contactos, SMS, micrófono (`RECORD_AUDIO`) pese a integrar Sensory (voz) — la
captura de voz, de usarse, no tiene permiso declarado en este manifiesto; conviene confirmar en
runtime. Tampoco almacenamiento externo amplio.

## Postura de seguridad declarada

| Atributo | Valor | Lectura |
|---|---|---|
| `android:allowBackup` | `false` | ✅ sin respaldo ADB/cloud del usuario |
| `android:usesCleartextTraffic` | `false` | ✅ bloquea HTTP en claro |
| `android:networkSecurityConfig` | `@xml/network_security_config` | Solo `debug-overrides` (sin pinning — doc 05) |
| `android:dataExtractionRules` | `@xml/data_extraction_rules` | Excluye todo de cloud-backup y device-transfer (doc 04) |
| `android:fullBackupContent` | `@xml/backup_rules` | Excluye sharedpref/db/file/root |
| `android:extractNativeLibs` | `false` | `.so` se cargan del APK comprimido (doc 06) |
| FileProvider `@xml/file_paths` | solo `cache-path images/` | ✅ acotado (contrasta con `root-path "/"` de jw/CFE) |

## Componentes exportados (superficie de ataque)

| Componente | `exported` | Filtro | Riesgo |
|---|---|---|---|
| `ui.main.MainActivity` | `true` | MAIN/LAUNCHER | Launcher (normal) |
| `preexistence.main.PreexistenceActivity` | `true` | VIEW + DEFAULT (sin esquema/dato) | Lanzable por intent explícito desde otras apps |
| `home.main.HomeActivity` | `true` | VIEW + DEFAULT (sin esquema/dato) | Idem; es la home autenticada |
| `ps.facial.ui.main.FacialActivity` | **`true`** | — | **Captura facial exportada**: revisar validación de entrada |
| `ps.facial.base.activity.IdxFacialCaptureActivity` | **`true`** | — | **Captura de identidad exportada** |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | `true` | c2dm RECEIVE | Protegido por permiso `c2dm.permission.SEND` |
| `androidx.work.impl.background.systemjob.SystemJobService` | `true` | — | Protegido `BIND_JOB_SERVICE` (normal) |
| `androidx...DiagnosticsReceiver`, `ProfileInstallReceiver` | `true` | — | Protegidos por `android.permission.DUMP` (normal) |

El resto de actividades de negocio (register, transfers, withdrawals, savings, nip, etc.) son
`exported="false"`. **No hay deep links con esquema/host propios** declarados (los filtros
VIEW+DEFAULT de Preexistence/Home no llevan `<data>`), por lo que no se expone un esquema
`aforemovil://` en el manifiesto base. Los enlaces a gob.mx/IMSS son de salida (doc 05).

### Observación sobre las actividades faciales exportadas
`FacialActivity` e `IdxFacialCaptureActivity` sin `permission` y con `exported="true"` pueden ser
invocadas por cualquier app instalada. **Confirmado estáticamente** (ver docs 02 y 03, decompilados
en `decompiled/`): sus `onCreate` **no validan sesión/token local** — `IdxFacialCaptureActivity`
solo hace `super.onCreate` + `clearFlags`, y `FacialActivity` cablea la pila de identidad (Dagger) e
inicializa FIDO antes de pintar la UI. La contención real recae en el **backend IdentityX** (las
operaciones de subida/evaluación inyectan credenciales de usuario y requieren contexto de
`CreateEvaluation`/`CreateIdCheck`). Queda para análisis dinámico si un `Intent` externo puede
disparar una captura/evaluación con un contexto controlable por el invocador.

## Módulos de negocio (Play Feature Delivery)

`meta-data com.android.dynamic.apk.fused.modules` enumera 27 módulos fusionados en el APK base:

```
about, appointments, base, calculators, complaints, faqs, goals, helponline, home,
marriage, minors, nip_activation, niprecovery, notifications, onlinesavings, preexistence,
profile, register, saveincash, savewithbenefits, savingsbytransfer, transactions, transfers,
transfers_cuo, unemployment, voluntary_savings_withdrawal
```

Cada uno corresponde a un `Activity` `<feature>.main.*Activity`. Su mapeo a operaciones del SAR
está en [07-operaciones-afore.md](07-operaciones-afore.md). `splits.required=true` indica entrega
por splits; el bundle incluye `config.armeabi_v7a` (nativo) y 18 splits de idioma.

## Providers y servicios relevantes

- `provider ...FileProvider` (`authorities .provider`, no exportado) → solo `cache/images/`.
- `service data.firebase.PushNotificationService` (FCM `MESSAGING_EVENT`).
- Daon FIDO: una docena de `Activity` `com.daon.fido.client.sdk.*` (register/auth/dereg UAF) y
  `com.daon.sdk.authenticator.capture.CaptureActivity`, `Camera2FrameProviderActivity`,
  `CameraXFrameProviderActivity` — todas **no exportadas** (ver doc 02/03).
