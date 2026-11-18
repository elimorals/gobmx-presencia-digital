# 05 · Hallazgos

## 1. `.env` con `APP_KEY` junto a config cifrada — ALTA / A-CONFIRMAR (CWE-312/798)

`assets/flutter_assets/.env` empaca un secreto `APP_KEY` (217 chars) junto a los endpoints API
cifrados (`API_EVENTOS`, `API_USUARIOS` y sus variantes `_PRUEBAS`). Si `APP_KEY` es la clave de
descifrado de esos blobs, el cifrado no protege nada (llave y ciphertext en el mismo archivo del
cliente). No confirmable por el muro Dart (`libapp.so`). **Remediación:** mover el secreto al
servidor; la app no debe portar la llave que descifra su propia config.

## 2. Endpoints de prueba en producción — MEDIA (CWE-489)

El `.env` incluye flag `PRUEBAS` y endpoints `API_EVENTOS_PRUEBAS` / `API_PRUEBAS` empacados en el
release. Entorno de prueba expuesto en el binario de producción. **Remediación:** no empacar config
de prueba en el build de producción.

## 3. Firebase API key en recursos — BAJA (CWE-312)

`AIzaSy…` embebida (estándar en apps Firebase; restringible por firma de app y por API). Credencial
en el binario. **Remediación:** restringir la key por package+firma y por API en la consola de GCP.

## 4. (Positivo) TLS/cleartext sano — AUSENTE-SANO

Sin `usesCleartextTraffic`, targetSdk 36 (cleartext bloqueado por defecto), sin NSC permisivo. No
repite el defecto de 911-CDMX. Único pendiente: el cliente Dart (muro), a confirmar con dinámico.

## Proveniencia

APK re-firmado por APKPure (`CN=Android, O=Google Inc.`) → la firma no prueba autenticidad; cruzar
con el canal oficial del C5 Jalisco antes de cualquier divulgación.

## Qué quedó tras el muro Dart (`libapp.so` AOT)

- Si `APP_KEY` descifra los `API_*` (hallazgo 1).
- El host real de la API propia (endpoints van cifrados).
- Si el cliente HTTP de Dart valida el certificado TLS.
- La lógica real del botón de pánico (qué/cómo/cuándo envía ubicación y contactos).

Todo lo anterior requiere **análisis dinámico** (Frida/reflutter con dispositivo Android).
