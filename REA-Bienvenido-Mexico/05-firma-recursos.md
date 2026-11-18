# Firma y procedencia

## ✅ Firma AUTÉNTICA del SAT

```
Signer #1 certificate DN: CN=Angel Fernandez, OU=AGCTI, O=SAT, L=Mexico, ST=DF, C=52
```

- El certificado identifica al **SAT** (`O=SAT`, `OU=AGCTI` = Administración General de
  Comunicaciones y Tecnologías de la Información del SAT), firmado por un responsable nombrado.
  **No** es el genérico `CN=Android, O=Google Inc.` de APKPure → APKPure redistribuyó el **APK
  original sin re-firmarlo**. Es el **artefacto auténtico** del SAT, base de análisis fuerte.
- (`C=52` es el código numérico de México en vez de `MX` — detalle del certificado, no afecta la
  validez.)

Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): APK `com.tcs.programapaisano@arm64-v8a.apk`.

## Recursos

- 968 recursos, 1 268 clases. App de contenido (texto/imágenes del Programa Paisano).
- `android:largeHeap="true"` (maneja imágenes/contenido grande).
- Desarrollador: **TCS (Tata Consultancy Services)** — se infiere del package `com.tcs.*`.
- Base de código **legacy** (support.v4, Apache HTTP legacy, doc 01).
