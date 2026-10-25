# 10 — Firma del APK y recursos

## Firma

El APK base trae firma **v1 (JAR)**: `META-INF/BNDLTOOL.{RSA,SF,MF}` (alias `BNDLTOOL`,
el nombre que usa `bundletool`). Además lleva un **source stamp** de Google Play
(`stamp-cert-sha256`, `com.android.stamp.type=STAMP_TYPE_DISTRIBUTION_APK`), confirmando
distribución por Play. Certificado completo en [`evidence/signer-cert.txt`](evidence/signer-cert.txt).

### Certificado firmante

| Campo | Valor |
|---|---|
| Propietario (Subject) | `CN=Banco de México, OU=Banco de México, O=Banco de México, L=Ciudad de México, ST=Distrito Federal, C=MX` |
| Emisor (Issuer) | *idéntico* → **autofirmado** |
| Número de serie | `50abcbcd` |
| Válido | **2012-11-20 → 2040-04-07** (≈28 años) |
| Clave | **RSA 2048 bits** |
| Algoritmo de firma | **SHA1withRSA** — keytool lo marca **(débil)** |
| Versión X.509 | 3 |
| SHA-256 (cert) | `7A:0B:F5:EE:93:62:23:87:91:C0:9A:01:C1:F6:F8:38:83:7B:C5:21:DD:6E:1B:D6:0D:E3:7C:9C:0E:D6:F1:03` |
| SHA-1 (cert) | `E4:99:3D:24:27:61:DB:47:04:A0:AA:64:41:41:6B:7D:2A:1D:42:7E` |

**Hallazgo:** el certificado está **autofirmado por el Banco de México** (identidad
coherente con el publicador) y es de larga vida (emitido en 2012). Su **algoritmo de
autofirma es SHA1withRSA**, hoy considerado **débil**. Matiz importante: en la firma de
APK, el algoritmo de la **autofirma del certificado** no es lo mismo que el digest del
**esquema de firma v2/v3** del APK (que usa SHA-256); la clave RSA-2048 sigue siendo
adecuada. Aun así, un certificado nuevo debería emitirse con `SHA256withRSA`.

**No verificado:** la presencia/validez de los esquemas **v2/v3** (APK Signing Block)
requiere `apksigner`, **no disponible** en este entorno. Sólo se verificó la firma v1 y se
leyó el certificado. No se afirma el resultado de `apksigner verify` sin ejecutarlo.

## Recursos

| Métrica | Valor |
|---|---|
| Recursos (JADX) | **126** |
| `resources.arsc` | presente (tabla de recursos compilada) |
| `res/xml/splits0.xml` | metadato de splits (AXML binario) — declara el split por ABI |
| Iconos | `@mipmap/app_icon`, `@mipmap/app_icon_round` (mdpi→xxxhdpi + anydpi-v26 adaptativo) |
| `app_name` | `@string/app_name` → "BilletesMx" |

El APK es un **split de Play** (base + `config.arm64_v8a`); `com.android.vending.splits`
y `requiredSplitTypes="base__abi"` lo confirman. La huella de recursos es pequeña (126):
el contenido pesado son los assets Unity (ver [07](07-assets-unity.md)), no recursos
Android.
