# 04 — Botón de auxilio, Sígueme (ubicación) y ficha de salud

Los tres subsistemas que manejan los datos más sensibles. Toda la red de estos flujos es
HTTP en claro (doc 03); aquí se documenta qué datos tocan y por qué código pasan.

## Botón de auxilio (911)

- UI en `fragments.BotonAuxiloFragment`; la pantalla de alerta activa es `activities.AlertActivity`.
- Al oprimirlo, la app crea un incidente contra `ExternalIncidentService/CreateIncidente`
  (constante en `DataRegister`) con la ubicación y, según los endpoints de `attachImage`,
  puede **adjuntar una foto** (permiso `CAMERA`).
- El estado del incidente activo se persiste en SharedPreferences
  (`App066PreferenceManager.setIncidenteActivo(...)`, `setInEmergency(true)`), de modo que
  la app sabe que hay una emergencia en curso tras reabrir.

## Sígueme (ubicación en tiempo real)

- `services.LocationService` + `services.SiguemeService` (ambos **exportados**, doc 01)
  publican la ubicación del usuario hacia su **red de emergencia** (contactos guardianes).
- Endpoints: `WsExternalIncident/Service1.svc/Followme` y `GetlocationthismovilApp` (doc 03).
- Estado en SharedPreferences: `setFollowMeActive(boolean)`, `setIncidenteFollowMe(Long)`,
  y la última posición conocida en `setLastLocation(LatLng)` / `getLastLocation()`.
- La última ubicación se guarda **en claro** en SharedPreferences (doc 05), además de
  viajar por HTTP.

## Red de emergencia (contactos guardianes)

- Se arma leyendo los **contactos** del teléfono (permiso `READ_CONTACTS`) y gestionando
  solicitudes de vínculo guardián.
- El bus de eventos usa acciones propias enrutadas por `NavigationActivity`:
  `SOLICITUD_PENDIENTE`, `SOLICITUD_ACEPTADA`, `CONTACTO_BORRADO`, `SUPERVISION` (doc 01),
  alimentadas por FCM (`NotificationService`) y persistidas en SQLite
  (`dao.SolicitudesRedEmergenciaDaoSqlite`).
- Endpoints: `RegistroExterno.svc/AlertEmergencyRed`, `findDeviceRed`, `regPendienteAceptar`,
  `DeleteContactoRed`, `cambioEstatusContacto`, `Synchronizes.svc/SincronizaRed`.

## Ficha de salud (datos médicos)

- UI en `fragments.FichaDeSaludFragment`, validada por `utils.validator.FichaSaludValidator`,
  empaquetada por `utils.packager.Ficha_Salud_Reg` / `Insurance_Data` / `Contacto_Emergencia`.
- Campos: **tipo de sangre**, seguros médicos, contactos de emergencia.
- El tipo de sangre se guarda localmente en SharedPreferences (doc 05) y se sincroniza al
  servidor por HTTP (`ExternalHealthCard.svc/registryHealthCard`,
  `Synchronizes.svc/SincronizaFichaSalud`).

## Denuncia de extorsión (089)

- `fragments.ExtorsionLlamadaFragment` + `receivers.PhonecallReceiver` (escucha `PHONE_STATE`).
- Valida el número contra `WsExternal089/Numero_Extorsion.svc/validarNumero` (HTTP).
- Estado en SharedPreferences: `setLlamadaExtorsion(boolean)`.

## Resumen de PII en circulación

| Dato | Origen | Local (SharedPreferences) | Red |
|---|---|---|---|
| Nombre completo, apellidos, correo, teléfono, fecha de nacimiento | registro | sí, en claro | HTTP `setRegister`/`SincronizaPersona` |
| Domicilio (calle, número, colonia, CP, delegación) | registro | sí, en claro | HTTP |
| **Tipo de sangre, seguros, contactos de emergencia** | ficha de salud | sí (tipo de sangre) | HTTP `registryHealthCard` |
| Contactos del teléfono | red de emergencia | SQLite | HTTP `AlertEmergencyRed` |
| Ubicación en tiempo real | GPS | última posición en claro | HTTP `Followme` |
| Foto del incidente | cámara | — | HTTP `attachImage` |
| OTP de registro | SMS/servidor | código de activación en claro | HTTP `GetBackCode` |
