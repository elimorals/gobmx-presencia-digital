# 04 — Criptografía y almacenamiento

## Endpoints y llaves cifrados en `config.properties`

`assets/config.properties` (copia en [`evidence/config.properties`](evidence/config.properties)) **no
guarda las URLs en claro**: las guarda como texto cifrado base64. Extracto estructural (valores
redactados aquí; están completos solo en `evidence/`, gitignored):

```
# Direcciones de los servidores
BASE_PRE            = <cifrado>
BASE_PRE_DEV_PRUEBAS= <cifrado>
BASE_PRE_DEV_TECNO  = <cifrado>
BASE_PRE_DESA_MOVIL = <cifrado>        # comentarios: "Cifrado ECB" / "Cifrado CBC"
BASE_PRE_SEGURIDAD  = <cifrado>
BASE_PRE_C3         = <cifrado>
PRODUCTION          = <cifrado>
DEVELOPMENT         = <cifrado>
# KEYS twitter
CONSUMER_K          = <cifrado>
CONSUMER_SK         = <cifrado>
```

Los **comentarios del propio archivo** indican el esquema: líneas marcadas *"Cifrado ECB"* y
*"Cifrado CBC"* (se dejó en CBC la versión activa de varias). Es decir, el equipo **cifra sus
endpoints y las llaves de Twitter con AES** y los embebe en el APK.

### Por qué es seguridad por oscuridad (hallazgo P2)

Para descifrarlos en runtime, **la llave de descifrado tiene que estar en el binario** (o derivarse
de algo en él). El DEX contiene la maquinaria AES (`javax.crypto.spec.SecretKeySpec`,
`IvParameterSpec`, `Cipher` con `AES/ECB/NoPadding` y PBKDF — ver abajo). Por tanto, **cualquiera con
el APK puede recuperar la llave y descifrar los endpoints y las llaves de Twitter**: el cifrado
ofusca, no protege. Es el mismo patrón que CFE (endpoints ocultos por ofuscación), aquí con cifrado
simétrico reversible desde el cliente.

> No se descifraron los valores: documentar el mecanismo y que la llave es recuperable es el
> hallazgo; publicar los endpoints/llaves descifrados no aporta y expondría secretos de terceros.

## Criptografía observada en el DEX

| Indicador (strings) | Lectura |
|---|---|
| `AES/ECB/NoPadding` | **Modo débil**: ECB no oculta patrones de bloque; NoPadding es frágil |
| `javax.crypto.spec.SecretKeySpec`, `IvParameterSpec` | Clave/IV construidos en código (clave simétrica en cliente) |
| `OpenSSLPBKDF`, `PBEPBKDF1`, `PBEPBKDF2` | Derivación de clave por contraseña (PBKDF) |

**`AES/ECB/NoPadding` es un hallazgo de cripto débil (P2).** ECB revela igualdades de bloque en el
texto cifrado; combinado con una llave embebida, el esquema protege poco. (El uso exacto —si ECB
cifra los endpoints, o un dato menor— no se aisló al cuerpo de método; se reporta la presencia del
modo débil en el binario, no la cadena completa, para no inventar la ruta.)

## `getLlave`: llave desde el servidor

La API expone `getLlave() → GetLlaveResponse` (doc 03). Sugiere que, además de la llave local, el
servidor entrega una llave (de sesión o para cifrar el paquete antes de subirlo). El contenido y uso
exacto de esa llave no se trazó a cuerpo de método; se documenta su existencia.

## Almacenamiento local

- **Realm** (`librealm-jni.so`) — base de datos local nativa. Guarda el estado de la captación
  (apoyos pendientes de subir, perfil, etc.). Realm soporta cifrado con clave de 64 bytes; **no se
  confirmó** si esta app habilita el cifrado de Realm (requeriría trazar la apertura de la DB al
  código; queda como pregunta abierta, no como veredicto).
- `SharedPreferences` para configuración/sesión (uso estándar, no auditado campo por campo).
- `allowBackup=false` (doc 01) evita respaldo adb de estos datos.
