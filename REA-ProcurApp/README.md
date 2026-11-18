# Ingeniería inversa de ProcurApp 2.3.5 — índice

Análisis **estático** del APK `mx.gob.pgjguanajuato.procurapp` con REA (JADX headless). La app
**nunca se ejecutó**. Es la app oficial de la **Fiscalía General del Estado de Guanajuato**
(antes PGJ) — denuncias ciudadanas, botón de pánico, emergencias, perfil genético y más.
**#40 por instalaciones** del inventario gob.mx (≈100 K). Firma **auténtica** de la PGJ/FGE.

> **CRÍTICO — validación TLS desactivada en producción (clase `UnsafeOkHttpClient`).** La app
> trae una clase `utils.UnsafeOkHttpClient.getUnsafeOkHttpClient()` con un `TrustManager` que
> **acepta cualquier certificado** (`checkServerTrusted` vacío) y un `hostnameVerifier` que
> **siempre devuelve `true`** — el mismo patrón crítico de Infonavit y Licencia EDOMEX. El xref
> en smali confirma que lo usan **`RetrofitClientInstancePROD` (el cliente de PRODUCCIÓN)**,
> **`RetrofitClientInstanceGenetica`** (perfil genético/ADN) y **`RetrofitClientInstanceWS_CITAS`**
> (citas). Es decir: el tráfico de denuncias, pánico y datos genéticos de una **Fiscalía** va por
> HTTPS pero **sin validar el certificado** → un MITM puede leer o alterar la PII más sensible
> imaginable (identidad de víctimas, denuncias de género, ubicación de pánico, citas de ADN).

## Documentos

| # | Archivo | Tema |
|---|---|---|
| 00 | [00-RESUMEN.md](00-RESUMEN.md) | Identidad, hallazgo crítico, módulos |
| 01 | [01-manifiesto.md](01-manifiesto.md) | Permisos sensibles (ubicación, cámara, contactos) |
| 02 | [02-modulos-superficie.md](02-modulos-superficie.md) | 20+ módulos: denuncia/género/pánico/genético/… |
| 03 | [03-red-tls-mitm.md](03-red-tls-mitm.md) | **`UnsafeOkHttpClient` — TLS desactivado en PROD/Genética/Citas** |
| 04 | [04-datos-pii.md](04-datos-pii.md) | Qué PII sensible maneja cada módulo |
| 05 | [05-firma-recursos.md](05-firma-recursos.md) | Firma auténtica de la FGE/PGJ |
| 06 | [06-hallazgos-seguridad.md](06-hallazgos-seguridad.md) | Hallazgos priorizados |

## Artefactos

`AndroidManifest.xml` (versionado), `hashes.sha256` (versionado),
`decompiled/UnsafeOkHttpClient.java` (cuerpo + xref, no versionado), `evidence/firma.txt`.

## 5 hallazgos principales

1. **(CRÍTICO) Validación TLS desactivada en el cliente de PRODUCCIÓN.** `UnsafeOkHttpClient`
   (trust-all + hostnameVerifier `true`, código citado en doc 03) lo usan
   **`RetrofitClientInstancePROD`**, **`RetrofitClientInstanceGenetica`** y
   **`RetrofitClientInstanceWS_CITAS`** (xref smali verificado). MITM total sobre el tráfico de
   esas rutas — en una app que transporta denuncias, pánico y datos genéticos (doc 03).
2. **(Alto) Superficie de datos extremadamente sensibles.** Módulos de **denuncia** (general,
   casa-habitación, **género**/violencia de género), **extorsión**, **emergencias**, **pánico**
   (+ contactos de pánico), **perfil genético** (ADN, citas), **persona desaparecida**,
   **antecedentes** (cartas por folio), **vehículo robado** — todo PII de víctimas/denunciantes
   (doc 02, 04).
3. **(Medio) Backends de la FGE.** `https://portal.fgeguanajuato.gob.mx/` y
   `https://webservice.fgeguanajuato.gob.mx/` (doc 03). HTTPS, pero inútil en las rutas que usan
   el cliente inseguro.
4. **Código NO ofuscado.** ~697 clases propias legibles (MVVM: model/network/persistence/view/
   viewmodel, Retrofit + Room + Gson). Transparente para auditoría — y para un atacante (doc 02).
5. **Firma auténtica de la PGJ/FGE** (`O=Procuraduría General de Justicia del Estado de
   Guanajuato`, verifica) → binario íntegro del emisor; el hallazgo TLS es del código real de la
   Fiscalía, no de un re-empaquetado (doc 05).

## Alcance y límites

Análisis **original** de una app pública que se posee, con fines de investigación de seguridad
responsable. **No redistribuye** el APK. Observaciones **estáticas**: no se ejecutó la app, no
se enviaron denuncias ni se llamó a ningún endpoint. El hallazgo crítico (TLS desactivado) está
confirmado por el **código decompilado** y el **xref en smali**, no por strings. Las rutas exactas
de cada API bajo los backends y el contenido de los request no se enumeraron (no se tocó el
servidor). Dada la severidad y el tipo de datos (víctimas de delito), este hallazgo amerita
**divulgación responsable** a la FGE Guanajuato.
