# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico de CONALITEG)

```
Signer #1 certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, etc.). **No
verificable como el artefacto auténtico de la CONALITEG**: vale para arquitectura, no para
integridad. Para el binario auténtico, descargar de Google Play con cuenta y comparar el
certificado. Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos

- APK pequeño (~5.7 MB): es un lector; el contenido (libros) se descarga de los backends, no se
  empaqueta.
- `res/xml/network_security_config.xml` — el recurso clave (doc 03), conservado en `evidence/`.
- Firebase Analytics (`app-measurement.com`) para telemetría de uso.
