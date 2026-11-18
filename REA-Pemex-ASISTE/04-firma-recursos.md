# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de Pemex)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado **genérico `CN=Android, O=Google Inc.`**, no de Pemex: patrón de APK
re-empaquetado y re-firmado por el pipeline de redistribución de APKPure (mismo caso que CFE,
Tarjeta Rosa, Va-y-Ven, Licencia EDOMEX). **No verificable como el artefacto auténtico de
Pemex**; sirve para arquitectura, no para conclusiones de integridad. Para el binario auténtico:
descargar de Google Play con cuenta y comparar el certificado.

Ver `evidence/firma.txt` para el detalle de esquemas y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura

- APK ~61 MB. El peso lo aportan el runtime Flutter (`libflutter.so`/`libapp.so` en el split de
  arquitectura) y los assets de Flutter (imágenes en 4 densidades, fuentes).
- `assets/flutter_assets/.env` — el artefacto más informativo (endpoint + clave de telemetría,
  doc 02); el valor real de la clave se mantiene solo en `evidence/flutter.env` (no versionado).
- Splits presentes: `config.armeabi_v7a` (con `libapp.so`), densidad e idiomas.
