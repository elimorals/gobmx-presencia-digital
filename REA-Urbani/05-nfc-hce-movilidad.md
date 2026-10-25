# 05 — NFC HCE: tarjeta de transporte virtual

Urbani emula una **tarjeta de transporte por NFC** usando **Host Card Emulation (HCE)** vía el
plugin Flutter `nfc_host_card_emulation`. El teléfono se presenta como tarjeta ante los validadores
de abordaje.

## Servicio APDU

Manifest:

```
service io.flutter.plugins.nfc_host_card_emulation.AndroidHceService
  exported=true  permission=android.permission.BIND_NFC_SERVICE   (solo el sistema puede ligarlo)
  intent-filter: android.nfc.cardemulation.action.HOST_APDU_SERVICE
  meta-data: android.nfc.cardemulation.host_apdu_service = @xml/apduservice
```

`res/xml/apduservice.xml` (decodificado):

```xml
<host-apdu-service android:requireDeviceUnlock="false" ...>
  <aid-group android:priority="100" android:category="other">
    <aid-filter android:name="A000000396" />
    <aid-filter android:name="A000000062" />
    <aid-filter android:name="F22334455667" />
    <aid-filter android:name="F22334455B33" />
    <aid-filter android:name="F2987A45BC11" />
  </aid-group>
</host-apdu-service>
```

Descripciones del servicio (`strings.xml`):

```
servicedesc    = "Servicio para RD300"
aiddescription = "AID para RD300"
```

**RD300** es el modelo de **validador/lector** de transporte ante el que la tarjeta HCE se presenta.

## AIDs (valores observados, sin atribuir emisor)

| AID | Tipo | Nota |
|---|---|---|
| `A000000396` | RID registrado (prefijo `A0`) | emisor **no atribuido** sin dataset de AIDs |
| `A000000062` | RID registrado (prefijo `A0`) | emisor **no atribuido** |
| `F22334455667` | **propietario/nacional** (prefijo `F`) | AID propio del sistema de transporte |
| `F22334455B33` | **propietario/nacional** (prefijo `F`) | AID propio |
| `F2987A45BC11` | **propietario/nacional** (prefijo `F`) | AID propio |

Los AID con prefijo `F` son de categoría **propietaria** (no RID de autoridad de registro): son los
AID del sistema de tarjeta de Urbani/MiRuta. **No afirmo** qué entidad emite los `A0*` (haría falta
cruzar contra el registro ISO/IEC 7816-5, fuera de este pase). No se recuperó el **protocolo APDU**
(comandos, cripto de la tarjeta) porque la implementación vive en Dart cifrado (doc 03).

## `requireDeviceUnlock="false"` — nota de seguridad

La emulación responde **sin desbloquear el teléfono**. Es habitual en transporte (abordar rápido),
pero implica que la tarjeta virtual puede ser leída por cualquier lector NFC con el AID correcto
estando el dispositivo bloqueado. El riesgo real depende de qué datos exponga el APDU y de la cripto
de la tarjeta — ambos **no recuperados** (Dart cifrado). Se documenta la configuración, no un
impacto confirmado.

## Lectura de tarjeta física (modo reader)

Además de emular (HCE), la app **lee tarjetas NFC físicas**. `res/xml/nfc_tech_filter.xml`:

```xml
<tech-list>
    <tech>android.nfc.tech.IsoDep</tech>       <!-- APDU (tarjeta con chip / transporte) -->
    <tech>android.nfc.tech.NfcA</tech>         <!-- ISO 14443-A -->
    <tech>android.nfc.tech.MifareClassic</tech><!-- tarjetas Mifare Classic -->
</tech-list>
```

`IsoDep` + `MifareClassic` son las tecnologías típicas de tarjetas de transporte. La app funciona en
los dos sentidos: **lee** una tarjeta física (p. ej. para consultar/recargar saldo de una tarjeta
existente) y **emula** la tarjeta virtual por HCE. La lógica de lectura (comandos, cripto) vive en
Dart cifrado y no se recuperó.

## Soporte BLE

El manifest marca `android.hardware.bluetooth_le` como **requerido** (`required="true"`) y pide
BLUETOOTH_SCAN/ADVERTISE/CONNECT. Es compatible con lectura de validadores o balizas por BLE además
de NFC, pero el uso concreto está en Dart y no se confirmó.
