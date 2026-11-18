# Hallazgos de seguridad y límites

Severidad cualitativa. Todo **estático**; no se ejecutó la app. En una app Veritran, la mayor
parte de la superficie real queda tras el muro (doc 02), así que esta sección documenta **lo
observable** y marca con honestidad lo que no.

## Observaciones (postura)

### O1 (positivo) — Pila de protección fuerte
RASP (`libtool-checker.so`), **SQLCipher** (DB local cifrada) y **Conscrypt** (TLS dedicado),
más biometría con anti-spoofing (doc 03–04). Coherente con banca; de las apps mejor protegidas
del acervo.

### O2 (positivo) — Firma auténtica del emisor
`O=Banco Nacional del Ejercito…` verifica → binario íntegro de Banjército (doc 05). Base de
análisis más confiable que las re-firmadas por APKPure.

### O3 (positivo / sofisticación) — Biometría multimodal on-device
Facial (liveness + anti-spoofing + impostor) y de voz (verificación de locutor) en
`assets/smma/model/` (doc 03). Autenticación avanzada para una app pública.

### O4 (neutral / superficie) — Permisos amplios
`CAMERA`, `RECORD_AUDIO`, `USE_FINGERPRINT`, `NFC`, `ACCESS_FINE_LOCATION`, `READ_CONTACTS`,
`READ_PHONE_STATE` (doc 01). Justificables por el flujo biométrico y antifraude, pero el **uso
exacto** de contactos y ubicación **no se puede verificar** desde el DEX → se declara como
superficie de permisos, no como abuso comprobado. No inventamos el uso.

## El muro (lo que NO se puede afirmar)

- **Endpoints / rutas de API:** no están en el DEX (sin cleartext ni dominios propios). Viven
  en el bundle Veritran interpretado por Duktape. **Desconocidos** estáticamente.
- **Lógica de transacciones, login, 2FA:** interpretada; no decompilable.
- **Manejo de llaves de SQLCipher y pinning TLS:** en la capa Veritran; no observable.
- **Comportamiento del RASP:** se confirma su presencia, no qué comprueba ni cómo reacciona.

### Vía de profundización (fuera de alcance aquí)
Análisis **dinámico** instrumentando el intérprete Duktape (hook a la evaluación del bundle)
tras **evadir el RASP** — requiere root/entorno controlado. Documentado como camino, no
ejecutado. Mismo veredicto que Banco del Bienestar.

## Conclusión

BanjeCel no es analizable a fondo estáticamente (es Veritran), pero **no es una caja negra
total**: se documentó con evidencia su arquitectura, su SDK biométrico (cara + voz con
anti-spoofing), su pila de protección (RASP + SQLCipher + Conscrypt) y su **firma auténtica**.
Lo que queda tras el muro se marca como límite, sin rellenar.
