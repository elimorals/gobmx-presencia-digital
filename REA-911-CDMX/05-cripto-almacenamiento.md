# 05 — Criptografía y almacenamiento local

## Veredicto: sin cifrado de datos en reposo

Toda la información del usuario se guarda en **SharedPreferences planas**, administradas por
`com.desarrollo.c5.app_066_android_2.utils.App066PreferenceManager` (campo
`android.content.SharedPreferences sharedPreferences`, archivo `SHARED_PREFERENCES_FILE`).
No se usa `EncryptedSharedPreferences`, ni el Android Keystore, ni SQLCipher.

### Evidencia en código (cita literal)

`setBloodType` escribe el tipo de sangre sin ninguna transformación
(`decompiled/App066PreferenceManager.storage.java`):

```java
public void setBloodType(String str) {
    this.sharedPreferences.edit().putString("bloodType", str).apply();
}
```

El **mismo patrón** `putString(clave, valor).apply()` (sin cifrar) se repite en los 30+
setters de la clase (83 métodos en total). Lo que queda en claro en el almacenamiento de
la app incluye:

- **Identidad / PII:** `UserName`, `UserFirstSurname`, `UserSecondSurname`, `UserMail`,
  `UserPhoneNumber`, `UserDateBirth`, `UserStreet`, `UserStreetNumber`, `UserSuburb`,
  `UserZipCode`, `UserDelegation`, `UserCompanyMobile`, `PhoneNumber`.
- **Salud:** `bloodType` (`setBloodType`/`getBloodType`), `BloodTypeType`.
- **Credenciales/tokens:** `ActivationCode` (el OTP de registro), `ChatToken` (token del
  chat Locatel), `TokenFirebase`, `DeviceKey`, `UUID`.
- **Estado operativo:** `LastLocation` (última posición GPS), `IncidenteActivo`,
  `IncidenteFollowMe`, `InEmergency`, `FollowMeActive`.

### Agravante: respaldo habilitado

`android:allowBackup="true"` con `fullBackupContent="false"` (sin reglas de exclusión)
significa que el sistema respalda el `shared_prefs/` por defecto. En un dispositivo con
respaldo a la nube o por adb, **la PII, el tipo de sangre y los tokens son extraíbles del
respaldo** sin root. Ver doc 08, hallazgo 3.

## Criptografía de aplicación

No se observan usos de criptografía propia de la app (cifrado de payloads, firma de
peticiones, derivación de llaves). La app confía la confidencialidad al transporte — que,
siendo HTTP (doc 03), **no la provee**. No hay por tanto ni cifrado en tránsito ni en reposo
para los datos sensibles.

## SQLite

Las solicitudes de la red de emergencia se guardan en SQLite plano vía
`utils.database.DB_Manager` y `dao.SolicitudesRedEmergenciaDaoSqlite`. No hay dependencias
de SQLCipher ni indicios de cifrado de la base; se asume base en claro en el sandbox de la
app (no verificado dinámicamente).

> **Límite:** "en claro" se afirma por el código de los setters y la ausencia de
> dependencias de cifrado; no se extrajo el archivo `shared_prefs` en ejecución (análisis
> estático).
