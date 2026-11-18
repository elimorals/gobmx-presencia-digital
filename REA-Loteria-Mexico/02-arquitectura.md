# Arquitectura — nativa y white-label GemesGroup

## Pila

- **Nativa Android (Kotlin/Java).** 3 `classes*.dex`, 13 954 clases totales, **22 propias** bajo
  `loterianacional.mx.*`. Sin `flutter_assets`/`libapp.so` (Flutter), sin `assemblies` (.NET), sin
  `il2cpp` (Unity), sin `assets/www` (híbrida), sin `libduktape` (Veritran). APK 9.4 MB.
- Patrón clásico: `Activity` + `AsyncTask` (`*Task`) + `Parser` + `Database` (SQLite para cupones).

## White-label de GemesGroup (proveedor español)

La firma es de **GemesGroup (Madrid, España)** y el código revela que es un **producto
multi-país**: además de los endpoints mexicanos, el smali contiene los de **ONCE (España,
`juegosonce.es`)** y **Santa Casa (Portugal, `jogossantacasa.pt`)** (doc 03). Es decir, Lotenal
usa una plantilla comercial de app de lotería a la que se le conectaron las fuentes mexicanas.

## Módulos propios

| Clase | Rol |
|---|---|
| `CuponActivity`, `CheckCupon`, `CheckCuponTask`, `CuponUtil` | verificar un billete (¿ganó?) |
| `data.SorteoBean`, `data.SorteosParser` | modelo y parseo de resultados de sorteos |
| `data.CuponBean`, `data.CuponDatabase`, `data.CuponParser` | cupones guardados (SQLite) |
| `util.CargarHistoricoTask` | historial de sorteos |
| `util.CheckNewVersionAvalaibleTask` | chequeo de nueva versión (Dropbox/OneDrive, doc 03) |
| `util.RefreshAdIntervalTask` | anuncios |
| `util.SSLHelper` | helper TLS trust-all (**inerte**, doc 03) |
| `RemoteConfig`, `Prefs`, `alarmChecker`, `StartAtBootServiceReceiver` | config/alarma |

## Sin pagos (confirmado)

No existe clase de pago, pasarela (Stripe/MercadoPago/Banorte/etc.) ni permiso asociado. La app
es de **consulta** (resultados + verificación de cupón) con anuncios, no de compra de billetes. El
código no se recorrió clase por clase más allá de lo relevante a seguridad (el resto es UI/listas
y no aporta hallazgos sobre lo ya observado).
