# 06 — Firma, procedencia y recursos

## Firma del APK

`apksigner verify --print-certs` sobre el APK base:

```
Signer #1 certificate DN: CN=Saul Espinosa Ruiz
Signer #1 SHA-256: 735193f7c905468b0d686a38bed8c879a8c331568ce7f467ebd74e973d57ee0d
Verificado con esquemas v1 (JAR) + v2 + v3 : true
Número de firmantes: 1

Source Stamp Signer DN: CN=Android, OU=Android, O=Google Inc., …
Source Stamp SHA-256: 3257d599a49d2c961a471ca9843f59d341a405884583fc087df4237b733bbd6d
```

### Lectura de procedencia

- **La firma de la app está preservada y es la original del desarrollador**: `CN=Saul Espinosa Ruiz`
  (un certificado de **persona física**, solo `CN`, sin `O`/`OU` de organización). Verifica con los tres
  esquemas → el binario no fue re-firmado por el redistribuidor en su firma principal.
- Hay además un **Source Stamp genérico de `Google Inc.`**, señal de que el APK pasó por el pipeline de
  **APKPure** (mismo patrón documentado en IMSS Digital). El Source Stamp no altera la firma principal,
  pero marca que la copia analizada proviene de un redistribuidor, no directamente de Play.

### Implicación (hallazgo, doc 07)

Una app oficial de la **Secretaría de Seguridad Ciudadana** con ~1 M de instalaciones está firmada por
una **cuenta de desarrollador individual** (`Saul Espinosa Ruiz`), no por un certificado institucional
de la SSC/GCDMX. Esto significa que la clave que controla las actualizaciones y la identidad de la app
es personal: su pérdida/compromiso o la rotación de la persona son un riesgo de cadena de suministro
para una app de seguridad pública. Es un hecho de la firma, no una afirmación sobre la persona.

## Recursos

- `assets/flutter_assets/` — fuentes propias de la SSC (`MiPolicia/AsuntosInternos`,
  `PoliciaCibernetica`, `Transito`), Montserrat/OpenSans/Roboto, graphics, json, y el `cert/` (doc 04).
- Split por idioma (`config.<lang>.apk`, 20 idiomas) y densidad (`xxxhdpi`, `xxhdpi`).
- `hashes.sha256`: APK base `5092a09a…`, split arm64 `587a892e…`.
