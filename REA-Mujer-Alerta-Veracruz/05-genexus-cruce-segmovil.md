# 05 · Cruce de proveedor — GeneXus (vs SEG-Móvil)

## Pregunta

¿El defecto de esta app es del **producto GeneXus** compartido con SEG-Móvil
(`REA-SEG-Movil`, también `com.artech.*`)?

## Respuesta: NO. El defecto es del despliegue, no del framework.

| Dimensión | Veracruz Mujer Alerta | SEG-Móvil (Guanajuato) |
|-----------|------------------------|-------------------------|
| Framework | GeneXus (`com.artech.*`) | GeneXus (`com.artech.*`) |
| Firma | `O=Artech` (vendor, Uruguay) | (ver REA-SEG-Movil) |
| Backend | `http://alertagenero.sspver.gob.mx/alerta/` | `https://*.seg.guanajuato.gob.mx` + Azure AD B2C |
| TLS | **cleartext HTTP** (NSC lo permite) | **HTTPS** |
| Trust-all | ausente | ausente |

- **El runtime GeneXus es el mismo**, pero SEG-Móvil habla HTTPS y esta app habla HTTP. La URL del
  servidor es un parámetro de la KB/despliegue, no algo que GeneXus imponga.
- GeneXus **no** mete el antipatrón trust-all (a diferencia del snippet endémico de
  Infonavit/Cosmocolor). En ninguna de las dos apps GeneXus se halló `getUnsafeOkHttpClient`.

## Conclusión para el mapa de proveedores

GeneXus **no** es un vector de defecto compartido. El cleartext de esta app es un **error de
configuración de despliegue de SSP Veracruz**, de la misma clase (CWE-319) que 911-CDMX, no un
defecto de producto multi-estado como Cosmocolor. Clasificación en el mapa: clúster D (cleartext),
no clúster A (trust-all).
