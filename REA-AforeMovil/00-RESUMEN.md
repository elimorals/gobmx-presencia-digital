# AforeMóvil — Ingeniería inversa (REA)

> Análisis **estático** del APK base del bundle XAPK `mx.com.procesar.aforemovil`.
> La app nunca se ejecuta. Herramienta: REA + JADX headless 0.7.1 + apktool 3.0.3.
> Fecha: 2026-10-24.

## Identidad del artefacto

| Campo | Valor |
|---|---|
| Archivo | `mx.com.procesar.aforemovil.apk` (111,809,524 bytes, APK base del XAPK) |
| SHA-256 (base) | `01b3f3fbb6ea7b47ca909cab96eaedda57e74dbc7023d2ed41ecb0208204efc6` |
| Bundle | XAPK v2, 20 APK (base + 19 splits), 141,727,045 bytes |
| Paquete | `mx.com.procesar.aforemovil` |
| App | AforeMóvil (oficial, Procesar / CONSAR — SAR) |
| Versión | 6.0.15 (versionCode 793) |
| minSdk / targetSdk | 24 / 36 (compileSdk 36 / Android 16) |
| Clases (top-level, JADX) | 17,984 · Recursos | 1,795 · Archivos decodificados | 1,849 |
| DEX | 28 (`classes.dex` … `classes28.dex`; base ≈9.1 MB + classes3 ≈9.6 MB) |
| Firma | **v2 + v3 verificadas**, `O=procesar`; Source Stamp de Google Play (ver doc 09) |
| Application | `com.procesar.aforemovil.BaseApp` |

## Qué es la app

AforeMóvil es el canal móvil del **SAR**. Permite al trabajador: registrarse / recuperar su
cuenta (**preexistencia**), consultar saldo y **movimientos**, **ahorrar** (en línea, en
efectivo, por domiciliación, con beneficios/puntos del gobierno), hacer **traspasos** entre
AFORE, solicitar **retiros** parciales (desempleo, matrimonio) y de **ahorro voluntario**,
gestionar **NIP**, **beneficiarios**, **menores**, **metas** de ahorro, **citas** y **quejas**.
Toda operación sensible se autoriza con biometría (**GestoPago** FIDO) y el alta exige
**prueba de vida facial + lectura de la identificación (INE)**.

## Pila tecnológica

| Capa | Tecnología | Evidencia |
|---|---|---|
| UI | Jetpack **Compose** + Material3 + Views; arquitectura MVVM (`UiState`, `UseCase/Result`) | temas `Theme.Material3.*`, clases `*UiState`, `usecase.Result` |
| DI | **Dagger/Hilt** (y wrappers propios) | `...facial.di.wrapper.DaggerWrapper`, singleton `V` |
| Base de datos | **Realm** (objetos) | `librealm-jni.so`, `RealmConfigs`, `RealmController*`, ~25 `realm.model.*` |
| Preferencias | Jetpack **DataStore** | `DataStoreManager.handleMigration`, `libdatastore_shared_counter.so` |
| Red | HTTP (OkHttp; `okhttp3.internal.platform.PlatformInitializer` en manifest) | endpoints en `strings.xml` (doc 05) |
| Identidad/biometría | **Daon IdentityX / xFace** (FIDO UAF, liveness) + **Sensory** (voz) | 9 `.so` Daon, `com.daon.*`, `com.sensory.*` (docs 02, 03) |
| Visión | **ML Kit** (OCR + barcode), **CameraX** / Camera2 | `com.google.mlkit.vision.DEPENDENCIES=ocr, barcode` |
| Documentos | Captura de identificación + **IBM FileNet** (almacén de imágenes) | `ps_take_documents`, `FilenetImagesStatus` |
| Push | **Firebase Cloud Messaging** | `PushNotificationService`, `FirebaseMessagingService` |
| Telemetría | **Firebase** (Crashlytics, Analytics, Sessions) + **Dynatrace** (APM) + **TrustX** (biometría) | `BaseApp.onCreate`, `com.dynatrace.*`, `trustx*.com` |
| Mapas | **Google Maps** (sucursales/citas) | `com.google.android.geo.API_KEY`, query a `com.google.android.apps.maps` |
| Permisos runtime | **Dexter** (Karumi) | `com.karumi.dexter.*`, `DexterActivity` |
| Entrega | **Play Feature Delivery** (27 módulos dinámicos) + SplitCompat | `com.android.dynamic.apk.fused.modules` |
| PDF | PDFBox-Android (tom_roush) | `assets/com/tom_roush/pdfbox/*` |

## Mapa de módulos (código propio)

| Paquete | Rol |
|---|---|
| `com.procesar.aforemovil.<feature>.main.*Activity` | 27 módulos de negocio (home, register, transfers, unemployment, marriage, onlinesavings, saveincash, savingsbytransfer, savewithbenefits, voluntary_savings_withdrawal, transactions, transfers_cuo, appointments, goals, minors, niprecovery, nip_activation, complaints, faqs, helponline, profile, notifications, about, preexistence, calculators) — ver doc 07 |
| `com.procesar.aforemovil.commons.data.*` | Capa de datos compartida: modelos (Afore, Saver, Beneficiary, Bank, GestoPago, Goal…), Realm (controllers, migraciones, modelos), validadores (CLABE, NIP, RFC, Amount), `UiState`, `UseCase/Result`, `UXUIConfig` (UI dirigida por servidor) |
| `com.procesar.aforemovil.ui.*` | Shell de UI: `MainActivity`, sesión (`SessionExpiredActivity`, `SessionTimeoutActivity`), `fsar` |
| `com.procesar.aforemovil.data.firebase.*` | `PushNotificationService` (FCM) |
| `mx.com.procesar.ps.facial.*` | Módulo de identidad facial (IDx): `FacialActivity`, `IdxFacialCaptureActivity`, OCR de documento, `FaceOverlayView`, `UserData` |
| `mx.com.procesar.ps_take_documents.*` | Captura de documentos (INE/IFE): `TakeDocumentsActivity` |
| `mx.com.procesar.ps.idxfacial.*` | Servicios/UI de IdentityX facial |

## Hallazgos principales

Ver la lista de 6 hallazgos en [README.md](README.md). En una línea cada uno:
1. Doble biometría de identidad (Daon xFace liveness + Sensory voz + ML Kit OCR del INE).
2. App transaccional del SAR (traspasos, retiros, ahorro, NIP) con autorización FIDO/GestoPago.
3. Endpoints en `strings.xml` (backend `api.esar.io`, no en el DEX).
4. Datos en reposo bien protegidos (backup/transfer excluyen todo; FileProvider acotado).
5. Sin certificate pinning (NSC solo con debug-overrides).
6. `FacialActivity`/`IdxFacialCaptureActivity`/`HomeActivity`/`PreexistenceActivity` exportadas.

## Limitaciones

Observaciones estáticas; no establecen comportamiento en runtime. Nombres ofuscados por R8
(clases de una letra) interpretados por símbolos y strings conservados. Los secretos de cliente
embebidos en librerías nativas (`libkeys.so`, `libidx_facial_keys.so`) no se extrajeron en este
pase.
