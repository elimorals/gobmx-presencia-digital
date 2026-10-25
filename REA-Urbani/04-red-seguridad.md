# 04 — Red y seguridad de transporte

## Configuración de seguridad de red

`res/xml/network_security_config.xml` (decodificado):

```xml
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
    <debug-overrides>
        <trust-anchors>
            <certificates src="user" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
```

Lectura precisa:

- El manifest trae `android:usesCleartextTraffic="true"`, **pero** el `network_security_config`
  pone `base-config cleartextTrafficPermitted="false"`. El NSC gana sobre el atributo del manifest…
  **para la pila de red de Android (Java/OkHttp)**.
- **⚠️ Corrección verificada en runtime (doc 11, D7):** el NSC **NO gobierna el tráfico de Flutter**.
  Flutter hace sus conexiones con su propio stack (BoringSSL del engine), ajeno al
  `network_security_config` de Android. En vivo, la app llamó
  `http://media-manager-prod.urbani.io/campaigns/userCampaigns` y recibió **200 en claro**, sin
  ningún error `CLEARTEXT_NOT_PERMITTED`. Es decir: **la app sí transmite tráfico HTTP sin cifrar**
  (al menos campañas/publicidad), pese a lo que el NSC sugiere. El "cleartext bloqueado" es cierto
  para la pila Android, **falso para el tráfico real de la app**.
- `debug-overrides` confía en CA de usuario **solo en builds depurables** (`android:debuggable`).
  En release no aplica. Es el patrón estándar de Flutter.
- Trust anchors = **solo CAs del sistema**. **No hay `<pin-set>`** → **no hay certificate pinning a
  nivel de plataforma**.

## ¿Hay pinning en la app?

**No se puede afirmar ni negar con evidencia estática.** Flutter suele hacer el pinning (si lo hay)
en **Dart** (p. ej. `SecurityContext`/`badCertificateCallback` o un `HttpClient` con fingerprints),
y el Dart está **cifrado** (doc 03). Lo honesto: a nivel plataforma **no hay pinning**; si existe
en Dart, no es observable aquí. Un MITM con una CA de sistema instalada en un dispositivo
propio/rooteado sería el camino para comprobarlo (pase dinámico).

## Endpoints del backend

**Estáticamente no se recuperaron** (Dart cifrado, doc 03; `libapp.so` sin URLs legibles). Pero el
**pase dinámico** (doc 11) los capturó del logcat en vivo — la app loguea sus llamadas de red en
release. Backend real (todo **HTTPS**, respuestas 200 observadas):

```
https://static-cdd-urbani-app-prod.urbani.io/api/v1/configs/cities/{MTY,NA}   config por ciudad (server-driven UI)
https://app-static-prod.urbani.io/app/v2/support/url
https://auth-prod.urbani.io/app/data/service/states                           geocercas de ciudades
https://cdn.urbani.mx/splash/monterrey_v1.mp4                                  CDN de assets
```

Endpoints **autenticados** por nombre (omitidos sin sesión): `messages`, `mobility`, `payments`,
`urbicoins`. **El backend es `urbani.io` (operador privado), no gob.mx** — ver doc 11 (D4).

Además, los servicios **Firebase/Google** (recursos, doc 07):

```
firebase_database_url = https://proyecto-ruta-f8800.firebaseio.com
google_storage_bucket = proyecto-ruta-f8800.appspot.com
```

(El uso concreto de Firebase RTDB/Storage lo decide el Dart; en runtime se vio **Remote Config**
activo para feature flags, no se confirmó uso de RTDB.)

## Postura de red

| Control | Estado |
|---|---|
| Cleartext (pila Android) | NSC `false` (bloqueado para OkHttp/HttpURLConnection) |
| Cleartext (tráfico real Flutter) | **NO bloqueado** — HTTP 200 a `media-manager` en vivo (doc 11, D7) |
| Trust anchors | Solo sistema |
| Cert pinning (plataforma) | **Ausente** (sin `pin-set`) |
| Cert pinning (Dart) | No observado (la mayoría del tráfico fue HTTPS a system CAs; sin pin aparente) |
| Endpoints de negocio | **Recuperados en runtime** (doc 11): ~24 rutas en 6 subdominios `urbani.io` + `urbiaccess.io` |
| Storage de objetos | **Cloudflare R2** (doc 11, D10) |
| Firebase RTDB / Storage | `proyecto-ruta-f8800` (en claro, doc 07); en runtime se vio Remote Config |
