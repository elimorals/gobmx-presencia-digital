# Resumen — Licencia Digital EDOMEX

## Identidad

| Campo | Valor |
|---|---|
| App | Licencia Digital EDOMEX (licencia de conducir digital) |
| Dependencia | Secretaría de Movilidad del Estado de México (SEMOV) |
| Proveedor | **Cosmocolor** (backend en `*.licenciamanejo.com`, dominio privado) |
| Package | `mx.com.cosmocolor.movapp` |
| versionName | 16 (`platformBuildVersionName=16`) |
| Ranking | #23 por instalaciones del inventario gob.mx (500 K+) |
| Firma | **Re-firmada por APKPure** (`CN=Android, O=Google Inc.`) — no auténtica |

## Pila

Android **nativo Kotlin/Java** (6 `classes.dex`, 22 181 clases, **0 librerías nativas**).
Arquitectura moderna: **Hilt** (DI), **Retrofit + OkHttp** (red), **Room sobre SQLCipher**
(persistencia cifrada), **Jetpack Compose + databinding** (UI), **RxJava** (en los interactores
de infracciones). `assets/PublicSuffixDatabase.list` confirma OkHttp.

Hay **dos pilas de red** conviviendo: la de Hilt (`di.NetworkModule`, validación TLS estándar)
y una singleton legada (`webservice.ServiceGenerator`, **validación TLS desactivada**) que es la
que transporta los flujos de negocio. Ver doc 02 y 04.

## Funcionalidad

Alta/enrolamiento (lectura **NFC** de la licencia física + **Regula** para documento + **Face
API** para rostro + **OTP por SMS**), consulta y renovación de licencia, **pago con tarjeta
dentro de la app**, generación del **QR de la licencia (TOTP + firma electrónica)**, consulta de
**infracciones**, citas, noticias y catálogos.

## Hallazgos (7)

Críticos y relevantes en el README y el doc 09. En una frase: **el almacenamiento local es
sólido (SQLCipher con llave aleatoria, mejor que la licencia federal), pero el transporte es
grave** — la ruta de red activa desactiva toda validación TLS y por ahí viajan licencia, pago
(tarjeta/CVV), OTP y biometría. Además hay un endpoint de impresión en `http://` claro y restos
de código de Chiapas (white-label multi-estado).

## Método y límites

Estático con REA (JADX 0.7.1) + apktool; afirmaciones citando clase/método del smali
decompilado (`decompiled/`). No se ejecutó la app ni se llamó a endpoints. El *seed* del TOTP,
el aprovisionamiento de la firma electrónica y el proveedor de pagos no se resolvieron del todo
y se marcan como límites.
