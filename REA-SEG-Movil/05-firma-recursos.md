# Firma y procedencia

## Firma de plataforma GeneXus (Artech) — original preservada

```
Signer #1 certificate DN: CN=Artech Genexus, OU=Artech Consultores, O=Artech,
                          L=Montevideo, ST=Montevideo, C=uy
```

- El certificado es de **Artech** (la empresa de GeneXus, Montevideo, Uruguay), **no** el
  genérico `CN=Android, O=Google Inc.` de APKPure. Es decir, la firma **original** del build
  GeneXus se **conservó** (APKPure redistribuyó el APK sin re-firmarlo).
- Matiz: la firma identifica a la **plataforma/empresa generadora (GeneXus/Artech)**, no a la
  SEG directamente. Es común en apps GeneXus firmadas con el certificado del proveedor/integrador.
  Verifica la integridad del artefacto tal como lo firmó quien lo compiló, pero el "emisor" del
  certificado es el proveedor, no la dependencia. (Contrasta con BanjeCel, firmada por el propio
  Banjército, o con las re-firmadas por APKPure.)

Ver `evidence/firma.txt` para el detalle de esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos / estructura

- Splits presentes: `config.armeabi_v7a`, densidad (`xhdpi`), idiomas (`ar`, `en`, `iw`, `ru`).
  **No** se descargó el split de arquitectura que contendría los assemblies .NET (doc 02).
- `assets/appsettings.json` — configuración central (backends + Azure B2C), el artefacto más
  informativo (doc 03–04).
- ExoPlayer embebido (reproducción de video de cursos).
