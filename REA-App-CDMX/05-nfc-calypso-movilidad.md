# 05 — NFC: tarjeta de transporte Calypso (HCE)

La función técnicamente más compleja de App CDMX: el teléfono **emula una tarjeta Calypso**
de Movilidad Integrada mediante **Host Card Emulation (HCE)**, usando el SDK comercial
**Spirtech**. Es la pieza que justifica los permisos `NFC` y la feature
`android.hardware.nfc.hce`.

## Qué es Calypso / Spirtech

- **Calypso** es el estándar internacional de tarjetas de transporte sin contacto (el mismo
  de la tarjeta de Movilidad Integrada / "Tu Llave" / tarjeta "Mi" de la CDMX).
- **Spirtech** es el proveedor del stack que permite llevar esa tarjeta al teléfono. En el
  bundle aparecen tres familias de paquetes Spirtech y una librería nativa dedicada.

## Evidencia estática

Librería nativa:

```
lib/armeabi-v7a/libcalypsoHceNatives.so   (84,012 bytes)
```

Componentes en el manifiesto (todos deben estar `exported` para recibir eventos NFC/sistema):

```
com.spirtech.android.hce.calypso.contactless.ContactlessService   (service)   ← servicio HCE
com.spirtech.android.hce.calypso.scheduling.SyncJobService        (service)
com.spirtech.android.hce.calypso.scheduling.BootReceiver          (receiver)  ← re-arma tras boot
com.spirtech.android.hce.calypso.multiapp.MultiAppProvider        (provider)
com.spirtech.android.hce.calypso.multiapp.MultiAppReceiver        (receiver)
ccm.spirtech.calypsocardmanager.front.nfcdiscovery.android.AndroidNfcReceiver (receiver)
```

Provider de gestión de tarjeta:

```
mx.gob.cdmx.adip.apps.com.spirtech.surpass.ma.provider   ← Spirtech SURPASS (Mobile Application)
```

Paquetes en DEX:

```
Lcom/spirtech/android...   (9 refs)
Lcom/spirtech/ybo...       (4 refs)   ← "ybo" = billetera/validación de transporte
```

Metadatos NFC: `@xml/apduservice` (declara los AIDs — Application IDs — que el teléfono
responde cuando se acerca a un validador/torniquete) y permiso propio
`DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` para receivers dinámicos.

## Arquitectura inferida

1. **Aprovisionamiento.** `SURPASS` (provider `surpass.ma.provider`) descarga/instala la
   tarjeta Calypso del usuario (tras login Llave y recarga, docs 04/06).
2. **Emulación.** Al acercar el teléfono a un torniquete, Android dirige las APDU al AID
   declarado en `apduservice.xml`; `ContactlessService` las atiende y la lógica criptográfica
   Calypso corre en `libcalypsoHceNatives.so` (clave de seguridad del sector transporte).
3. **Sincronización.** `SyncJobService` + `BootReceiver` mantienen saldo/estado sincronizados
   con el backend y re-arman el servicio tras reinicio (de ahí `RECEIVE_BOOT_COMPLETED`).
4. **Multi-app.** `MultiAppProvider/Receiver` soportan varias instancias/perfiles de tarjeta
   en el mismo dispositivo.

## Notas de seguridad (estáticas)

- La **seguridad real vive en nativo** (`libcalypsoHceNatives.so`) y en el elemento de
  seguridad del esquema Calypso, no en Java. El análisis estático del DEX no recupera las
  claves; confirmarlas requeriría instrumentación dinámica + hardware NFC, fuera de alcance.
- **Superficie exportada amplia.** Seis componentes Spirtech exportados son necesarios para
  NFC, pero amplían la superficie: merece revisarse si `ContactlessService` o
  `MultiAppProvider` validan el llamador (deberían, al estar protegidos por el modelo HCE,
  pero es el punto a auditar dinámicamente).
- **Dependencia de proveedor.** Todo el transporte digital de la CDMX en esta app depende
  del stack propietario Spirtech; es un activo crítico de terceros.
