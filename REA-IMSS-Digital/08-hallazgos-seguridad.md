# Hallazgos de seguridad (priorizados)

Observaciones **estáticas** sobre el binario. No se probó ningún endpoint ni se ejecutó la app;
"explotabilidad" es potencial, no verificada. Severidad es juicio del analista.

## Alta

### H1 — Login y servicios por HTTP sin TLS
- **Qué**: `android:usesCleartextTraffic="true"` + endpoints `http://`, incluido
  `http://siap.imss.gob.mx/ServiciosWebIMSSDigital/api/auth/login` y servicios de pensionados
  (RJP) y ChécaT.
- **Impacto**: credenciales y/o token de sesión capturables por un atacante en red (Wi-Fi
  pública, MITM). Con ese token se accede a los microservicios HTTPS de salud (expediente,
  laboratorio, VPH, violencia familiar).
- **Evidencia**: `AndroidManifest` (flag), `decompiled/endpoints.txt` (rutas http).
- **Arreglo**: forzar HTTPS en todos los endpoints, `usesCleartextTraffic="false"` + 
  `networkSecurityConfig` restrictivo, HSTS del lado servidor.

### H2 — Exposición potencial de datos de salud altamente sensibles
- **Qué**: la app da acceso a expediente clínico, resultados de laboratorio, VPH, incapacidades
  y **detección de violencia familiar** (doc 05), todo bajo una sesión cuyo login base puede ir
  por HTTP (H1).
- **Impacto**: fuga de datos médicos y de víctimas de violencia si se compromete el token.
- **Arreglo**: cerrar H1; además, pinning de certificado y expiración corta de token.

## Media

### H3 — Entornos QA/preprod embebidos en producción
- `cds-qa.apps.ocp.imss.gob.mx`, `mscmov-version-cmov-qa.apps.qaocp.imss.gob.mx`,
  `facetec-preprod.firmaautografa.com`.
- **Impacto**: los entornos no productivos suelen tener menos controles; su presencia en el
  release invita a probarlos y amplía superficie. El liveness en `preprod` puede significar
  verificación biométrica contra un entorno no endurecido.
- **Arreglo**: eliminar hosts no productivos del build de release; separar config por flavor.

### H4 — IP pública hardcodeada
- `https://201.149.45.42/Chat_Yo_Decido/`.
- **Impacto**: imposibilita rotación/pinning por dominio; residuo frágil.
- **Arreglo**: usar dominio con certificado gestionado.

### H5 — Datos biométricos y de identidad a terceros (algunos fuera de MX)
- Rostro y documentos (INE/pasaporte) procesados por Mati, FaceTec (vía `firmaautografa.com`) y
  **Acuant en infraestructura EU** (doc 04).
- **Impacto**: privacidad / soberanía de datos de derechohabientes. No es un bug técnico sino
  una decisión de arquitectura a auditar contra el aviso de privacidad y la normativa.

## Baja / higiene

### H6 — Permisos de sistema no concedibles declarados
- `MOUNT_FORMAT_FILESYSTEMS`, `MOUNT_UNMOUNT_FILESYSTEMS`, `INJECT_EVENTS`,
  `MODIFY_PHONE_STATE`, `ACCESS_MOCK_LOCATION` (doc 01).
- **Confirmado dinámicamente**: instalada en un dispositivo real (Android 16, sin root),
  `dumpsys package st.android.imsspublico` **no lista ninguno** de estos permisos — el sistema
  los descarta por su nivel de protección `signature|privileged`. Es decir: están declarados en
  el manifiesto pero el OS **ni los registra** para la app.
- **Impacto**: nulo en capacidad (confirmado: el sistema los ignora), pero mala higiene y ruido
  en auditorías. **No** implica que la app los use.
- **Arreglo**: depurar el `AndroidManifest` (probablemente heredados de plantilla).

### H7 — Sin ofuscación de código
- Xamarin sin protección de IL: endpoints, lógica y nombres de SDK quedan legibles tras
  descomprimir el blob (doc 02). Contrasta con CFE (DexGuard).
- **Impacto**: facilita el análisis/abuso del backend. 
- **Arreglo**: ofuscar IL (p.ej. con un protector .NET) y, sobre todo, no depender de la
  opacidad — reforzar autorización del lado servidor.

## Nota de procedencia

Binario descargado de APKPure. El certificado de app es `L=Mexico` (propio), pero el Source
Stamp es el genérico de Google (señal de re-proceso). No verificable como idéntico al de Play
sin comparar contra el canal oficial (doc 09). Los hallazgos de arquitectura (endpoints,
permisos, flags) provienen del contenido y son robustos frente a esa incertidumbre; cualquier
conclusión que dependa de bytes exactos debería confirmarse sobre el APK oficial.
