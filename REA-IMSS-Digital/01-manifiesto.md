# Manifiesto y superficie de ataque

Manifiesto decodificado: `AndroidManifest.xml`. 14 activities, 15 services, 7 receivers,
7 providers. Sin deep links ni App Links de negocio (no hay `android:scheme` propios).

## Permisos (27) — incluye permisos de sistema no concedibles

| Grupo | Permisos |
|---|---|
| Red | `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` |
| Ubicación | `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, **`ACCESS_MOCK_LOCATION`** |
| Cámara/micro | `CAMERA`, `RECORD_AUDIO` (captura de documentos y rostro — KYC) |
| Identidad/teléfono | `READ_PHONE_STATE`, `GET_ACCOUNTS`, **`MODIFY_PHONE_STATE`** |
| Biometría | `USE_BIOMETRIC`, `USE_FINGERPRINT` |
| Almacenamiento | `READ/WRITE_EXTERNAL_STORAGE`, `STORAGE` |
| Calendario | `READ_CALENDAR`, `WRITE_CALENDAR` (agendado de citas) |
| Sensores | `ACTIVITY_RECOGNITION` |
| NFC | `NFC` (lectura de chip de pasaporte/credencial) |
| Sistema | `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED`, `POST_NOTIFICATIONS` |
| **Privilegiados (⚠)** | **`MOUNT_FORMAT_FILESYSTEMS`, `MOUNT_UNMOUNT_FILESYSTEMS`, `INJECT_EVENTS`** |

### ⚠ Permisos de sistema imposibles para una app de tienda

`MOUNT_FORMAT_FILESYSTEMS`, `MOUNT_UNMOUNT_FILESYSTEMS`, `INJECT_EVENTS`, `MODIFY_PHONE_STATE`
y `ACCESS_MOCK_LOCATION` tienen nivel de protección **`signature|privileged`/`development`**:
el sistema **no los concede** a una app instalada desde Play (solo a apps firmadas con la
clave de plataforma o preinstaladas). Declararlos no da la capacidad —el sistema los ignora—
pero es **mala higiene**: amplían el manifiesto declarado, confunden auditorías y suelen venir
de copiar un `AndroidManifest` plantilla sin depurar. No implican que la app pueda formatear
el FS o inyectar eventos; sí que nadie limpió la lista de permisos.

## Flags de aplicación

- **`android:usesCleartextTraffic="true"`** → la app **permite HTTP en claro** a nivel global.
  Combinado con endpoints `http://` (doc 03), es el hallazgo de seguridad más relevante.
- `android:allowBackup="false"` — no respalda datos (correcto para datos de salud).
- Sin `networkSecurityConfig` propio → con cleartext=true, **todo dominio** puede ir por HTTP.

## Componentes exportados (superficie de ataque)

| Componente | Tipo | Nota |
|---|---|---|
| `crc642176304cdb761b92.Splash` | activity | LAUNCHER (nombre `crc64…` = binding Xamarin) |
| `com.google.firebase.iid.FirebaseInstanceIdReceiver` | receiver | FCM (protegido por permiso) |
| `androidx.profileinstaller.ProfileInstallReceiver` | receiver | Protegido por `DUMP` (shell/sistema) |

Superficie externa **acotada**: solo el Splash (launcher) y receivers de SDK. No hay
Activities de negocio exportadas ni deep links manipulables. El riesgo no está en la
superficie de intents, sino en la **capa de red** (doc 03/08).

## Nota sobre Xamarin

Los nombres de componente `crc64<hex>.<Clase>` son generados por **Xamarin.Android** (hash
CRC64 del namespace .NET para el binding Java↔C#). Confirman que las Activities reales están
implementadas en C# dentro de `assemblies.blob`, no en el DEX (doc 02).
