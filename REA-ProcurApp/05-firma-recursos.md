# Firma y procedencia

## Firma AUTÉNTICA de la PGJ/FGE Guanajuato

```
Signer #1 certificate DN: CN=ProcurApp,
  OU=Dirección General de Análisis y Tecnologías de Información,
  O=Procuraduría General de Justicia del Estado de Guanajuato,
  L=Guanajuato, ST=Guanajuato, C=MX
```

- El certificado identifica al **emisor real** (Procuraduría/Fiscalía del Estado de Guanajuato,
  su Dirección General de Análisis y TI). La firma verifica → **binario íntegro del emisor**, no
  re-firmado por APKPure.
- **Consecuencia:** el hallazgo crítico (TLS desactivado, doc 03) está en el **código auténtico
  publicado por la propia Fiscalía**, no en un re-empaquetado de un redistribuidor. Es una
  debilidad real del artefacto oficial.

Ver `evidence/firma.txt` para esquemas (v1/v2/v3) y Source Stamp.

## Hashes

Ver [`hashes.sha256`](hashes.sha256): XAPK (bundle descargado) + APK base analizado.

## Recursos

- Nativa Kotlin/Java, 2 dex, ~697 clases propias **sin ofuscar** → totalmente auditable.
- Retrofit + Gson (red), Room (persistencia local), Firebase (FCM/Analytics).
- El artefacto más relevante: `decompiled/UnsafeOkHttpClient.java` (cuerpo + xref) — la prueba
  del hallazgo crítico.

## Nota de divulgación

Por la severidad (TLS desactivado) y el tipo de datos (víctimas de delito, violencia de género,
ADN, pánico), este hallazgo debería comunicarse de forma **responsable** a la FGE Guanajuato /
su Dirección General de Análisis y TI antes de cualquier difusión pública del detalle técnico.
Este análisis es estático, sobre un binario público, sin tocar infraestructura en vivo.
