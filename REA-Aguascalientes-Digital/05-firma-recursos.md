# Firma y procedencia

## ✅ Firma AUTÉNTICA del Gobierno de Aguascalientes

```
Signer #1 certificate DN: CN=Ulises Mendoza, OU=OFMA, O=GOBAGS,
                          L=Aguascalientes, ST=Aguascalientes, C=MX
```

- `O=GOBAGS` = Gobierno del Estado de Aguascalientes; `OU=OFMA`. Es un **certificado del gobierno
  estatal** (firmado por su desarrollador/oficina), **no** el genérico `CN=Android, O=Google Inc.`
  de APKPure → la firma **original se conservó** y verifica. El binario es **íntegro respecto al
  emisor**: los defectos documentados (doc 03) están en el **código oficial del GEA**, no son
  artefactos de un redistribuidor.

Ver `evidence/firma.txt` para esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura

- Super-app grande (19 071 clases, 3 dex). Hilt + Room + Moshi + Retrofit; Firebase (FCM);
  ExoPlayer/streaming (ryta); Google Maps (mdt.mx).
- `.so` propias mínimas: `libd64d72.so` (nombre ofuscado; rol no confirmado),
  `libdatastore_shared_counter.so` (AndroidX DataStore), `libandroidx.graphics.path.so`.
- `res/xml/network_security_config.xml` — el recurso clave (doc 03).
