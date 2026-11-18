# Firma y procedencia

## ⚠️ Re-firmado por APKPure (no auténtico del Ayuntamiento ni de Cepo)

```
Signer certificate DN: CN=Android, OU=Android, O=Google Inc., L=Mountain View, ST=California, C=US
```

Certificado genérico de APKPure (mismo patrón que CFE, Tarjeta Rosa, Pemex, etc.). **No
verificable como el artefacto auténtico publicado por el H. Ayuntamiento de Puebla ni por el
proveedor Cepo**: sirve para arquitectura, no para integridad. Para el binario auténtico,
descargar de Google Play con cuenta y comparar el certificado. Ver `evidence/firma.txt`.

> Nota: PairIP incluye verificación de licencia/integridad de Play; re-empaquetar y re-firmar
> (como hizo APKPure) normalmente **rompe** esa verificación en tiempo de ejecución — otra razón
> para tratar este binario como no-auténtico y no asumir su comportamiento en runtime.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura

- **173 ensamblados .NET** AOT (`evidence/dotnet-assemblies.txt`); el propio es `Cepo.CiudadanoMAUI`.
- Recursos de OneSignal (`res/raw/consumer_onesignal_keep.xml`, animaciones) y Lottie.
- Splits presentes: `config.arm64_v8a` (con las `.so` AOT), densidad (`hdpi`), idioma (`en`).
- PairIP: el DEX es el runtime/VM de PairIP (clases `com.pairip.*`).
