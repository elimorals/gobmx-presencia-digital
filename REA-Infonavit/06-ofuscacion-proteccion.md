# 06 · Ofuscación y protección en runtime — H5 (Media)

## Sin ofuscación de código

El código decompila con nombres **en claro**: `com.infonavit.data.RetrofitService`,
`getUnsafeOkHttpClient`, `CryptoHelper`, `EnviromentRetrofit`, `BiometricCryptoManager`, campos
`PASSWORD_PAYMENT`, etc. No hay renombrado R8/ProGuard agresivo ni cadenas cifradas a nivel de
clase. Esto facilitó localizar los hallazgos H1–H4 directamente por nombre.

Contraste con otras apps gob.mx analizadas: CFE Contigo trae DexGuard + packer nativo
(`libb1bd.so`, entropía 1.0) y AforeMóvil trae R8 + ocultamiento de llaves nativo. Mi Cuenta
Infonavit **no** tiene ninguna de esas protecciones.

## Sin packer / sin librerías nativas de protección

Librerías nativas en `lib/<abi>/` (ver doc 09): solo `libmodpdfium.so`, `libjniPdfium.so`,
`libmodft2.so` (FreeType), `libmodpng.so` (todas de PDFium/render de PDF),
`libdatastore_shared_counter.so` (Jetpack DataStore) y `libc++_shared.so`. **No hay** `.so` de
packer, DexGuard, Promon, Appdome ni similar. El DEX no está empaquetado ni cifrado.

## Sin anti-tamper / anti-root / anti-instrumentación

Búsqueda de marcadores `rootbeer|dexguard|frida|xposed|SafetyNet|PlayIntegrity|isRooted|emulator`
sobre `smali*/com/infonavit`: sin coincidencias significativas (un único fichero,
`ui/login/PasswordFragment`, casa el patrón de forma incidental, sin lógica de detección de root).

Conclusión: la app **no** detecta dispositivos rooteados, no verifica integridad con SafetyNet /
Play Integrity, no detecta Frida/Xposed ni emuladores, y no valida su propia firma en runtime. No
hay barrera contra reempaquetado, hooking ni ejecución en entorno controlado.

> Sí existe el módulo `safetydetect` de **Huawei HMS** declarado en el manifiesto, pero es el SDK
> de Huawei disponible; no se halló código de INFONAVIT que invoque una verificación de integridad
> y actúe sobre su resultado. No se afirma que haya o no haya un gate real: no hay evidencia
> estática de que se use para bloquear la app.

## Impacto

La ausencia combinada de ofuscación + anti-tamper + anti-root hace trivial: (a) leer las
credenciales y el bypass TLS (como en este análisis), (b) reempaquetar la app con cambios, (c)
instrumentarla con Frida para observar/alterar su lógica. Para una app financiera de gobierno es
una carencia relevante, aunque de severidad menor que H1/H2 por sí sola.

## Recomendación

Activar R8 con ofuscación, considerar detección de root/emulador y attestation (Play Integrity),
y mover secretos fuera del binario (doc 04). La prioridad sigue siendo H1 y H2; el endurecimiento
anti-tamper es defensa en profundidad.
