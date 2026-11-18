# Resumen ejecutivo — Valida INE-QR 2.1.0

| Campo | Valor |
|---|---|
| Nombre | Valida INE-QR |
| Package | `mx.ine.rfe.lectorqr` |
| Versión | 2.1.0 (versionCode 36) |
| Emisor | Instituto Nacional Electoral (INE) — Unidad Técnica de Servicios de Informática |
| Instalaciones | 1 M+ (Google Play), #14 del inventario gob.mx |
| minSdk / targetSdk | 24 / 36 |
| Pila | Nativa Android (Kotlin/Java), **sin** framework híbrido/Flutter/Unity |
| Motor de QR | SDK propietario **PersonalCode** (`com.personalcode.*`) con núcleo nativo |
| Red | **Ninguna** — sin permiso `INTERNET`; validación 100 % offline |
| Firma | **Auténtica del INE** (`O=INSTITUTO NACIONAL ELECTORAL`); Source Stamp genérico de Google por APKPure |
| Clases (JADX) | 1527 |

## Qué hace

Lee con la cámara el **código QR de la credencial para votar** del INE, lo decodifica y
valida **localmente**, y muestra los datos que codifica: CIC, CURP, OCR, sección electoral
y vigencia. Usa además **OCR (MLKit)** para leer el texto impreso de la credencial. No
requiere cuenta, login ni conexión.

## Arquitectura

- **UI (`mx.ine.rfe.*`)**: capa delgada — `SplashActivity` (launcher), `WelcomeActivity`,
  `TutorialActivity`, `HomeActivity`, `WaitingRoomActivity`, `ResultsActivity`,
  `ErrorActivity`, `LegalsActivity`. Solo presentación.
- **Motor (`com.personalcode.*`)**: el SDK de terceros hace la detección (CameraX +
  `CameraOverlay`/`DecoratedCameraPreview`) y el procesamiento del QR. El método central
  `Azf.pc(Activity, List<byte[]>, boolean): String` es **nativo** → la lógica real
  (decode, descifrado y/o verificación de firma del QR) vive en `libO0p.so`/`libl1I.so`.
- **Licencia (`assets/license.txt`)**: licencia firmada que habilita el SDK con una
  **cuota de escaneos** (`hasRemaining`/`getRemaining`) y una fecha asociada.
- **Catálogo (`assets/catalogo_municipios.txt`)**: tabla entidad/municipio del INE para
  resolver la ubicación codificada en la credencial.

## Postura de seguridad (resumen)

Buena para una app de este tipo: **sin red** (no puede exfiltrar lo escaneado), permisos
mínimos, anti-overlay, backup deshabilitado, componentes no exportados, firma auténtica
del emisor. El único "no verificable" es la criptografía de validación del QR, deliberadamente
escondida en el SDK nativo propietario.

Detalle por tema en los capítulos 01–07.
