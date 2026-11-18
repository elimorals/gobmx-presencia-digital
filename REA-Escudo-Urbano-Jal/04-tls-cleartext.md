# 04 · TLS y cleartext

**Resultado: SANO.** Esta app **no** repite el defecto de 911-CDMX.

- **`usesCleartextTraffic`:** no declarado en el manifest.
- **targetSdk 36:** con targetSdk ≥ 28 el sistema **bloquea cleartext por defecto**; sin un NSC que
  lo reabra, el tráfico HTTP en claro no está permitido.
- **network_security_config:** no se halló un NSC permisivo (solo `res/xml/image_share_filepaths.xml`
  y `res/xml/splits0.xml`, no relacionados con TLS).
- **Trust-all:** no aplica buscar `getUnsafeOkHttpClient`/`ServiceGenerator` en el bytecode Java —
  el cliente HTTP real es el de Dart (`dart:io`/`package:http`/`dio`) dentro de `libapp.so`. Por el
  muro AOT **no se pudo inspeccionar** si el cliente Dart desactiva la validación del certificado.
  Se marca **A-CONFIRMAR** para el canal Dart, pero **no hay** señal estática de trust-all en la
  capa Java/manifest.

**Conclusión:** en el eje manifest/Java la postura TLS es correcta (AUSENTE-SANO). La única duda
abierta es el cliente Dart, que requiere dinámico. No se asume defecto sin evidencia.
