# Firma y procedencia

## ✅ Firma AUTÉNTICA de Banjército (binario íntegro del emisor)

A diferencia de la mayoría del lote (re-firmadas por APKPure con el genérico `CN=Android`), el
APK de BanjeCel conserva la **firma original del emisor** y verifica:

```
Signer #1 certificate DN:  CN=Banjercito, OU=Informatica,
                           O=Banco Nacional del Ejercito Fuerza Aerea y Armada,
                           L=Mexico, ST=DF, C=MX
Signer #1 SHA-256:         3a9d50d475d41d4279d679f325d74e1376577e1e1f8a9de999d213de5a0fd43f
```

- El certificado identifica al **emisor real** (Banjército / Banco Nacional del Ejército). La
  firma verifica → APKPure redistribuyó el **APK original sin alterarlo**. Es, por tanto, el
  **artefacto auténtico** publicado por el banco — la base más fuerte posible para un análisis
  (más que CFE, cuyo binario estaba re-firmado).
- Esto también corrige por contraste la lección de **Banco del Bienestar**, cuyo APK del acervo
  resultó ser un Aptoide mal nombrado; aquí el package y la firma coinciden con el emisor.

## Hashes

Ver [`hashes.sha256`](hashes.sha256):

```
<sha256>  mx.com.banjercito.model.common.VTUserApplicationBJCTMB@arm64-v8a.apk
```

## Recursos / estructura

- APK 141 MB: el peso lo aportan los **modelos biométricos** (`assets/smma/model/`, ~15 MB solo
  los faciales) y las **4 ABIs** de las `.so` nativas de Veritran (`libduktape`, `libsmma`,
  `libtool-checker`, `libsqlcipher`, `libconscrypt`, RenderScript, Yoga).
- Único assets de nivel superior aparte de los modelos: `assets/dexopt`.
- versionName 7.10.3.50500, compileSdk 34.
