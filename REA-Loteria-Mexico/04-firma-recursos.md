# Firma y procedencia

## Firma del proveedor español GemesGroup (original preservada)

```
Signer #1 certificate DN: CN=Jorge Alonso Nicolás, O=GemesGroup, L=Madrid, ST=Madrid, C=ES
Signer #1 SHA-256:        ca0942d45c2cb9f9cd5555ee50bf87baed3496bb9a60eb349ba5cdd8fc1fc640
```

- El certificado es de **GemesGroup** (Madrid, España), el **proveedor/desarrollador** que compila
  la app — **no** el genérico `CN=Android, O=Google Inc.` de APKPure → la firma **original se
  conservó** (APKPure redistribuyó el APK sin re-firmarlo). El binario es íntegro respecto a quien
  lo firmó.
- Matiz: el firmante identifica al **proveedor extranjero**, no a la Lotería Nacional ni a una
  dependencia mexicana. Es coherente con el carácter **white-label** de la app (doc 02): una
  plataforma comercial de lotería (también usada por ONCE/Santa Casa) firmada por su fabricante.

Ver `evidence/firma.txt`.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): APK único `loterianacional.mx@arm64-v8a.apk`.

## Recursos

- APK 9.4 MB, 645 recursos; app ligera (consulta + anuncios).
- SQLite para cupones guardados (`CuponDatabase`).
- Firebase (FCM) + AdServices.
