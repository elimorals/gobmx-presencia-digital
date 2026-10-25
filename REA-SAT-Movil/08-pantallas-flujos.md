# Pantallas y flujos funcionales

Al ser Flutter, no hay una Activity por pantalla (solo `MainActivity`): las pantallas son widgets
Dart y la navegación es GetX. El inventario de **funciones** se reconstruye de los endpoints (doc
04), los hosts embebidos y las cadenas de `libapp.so`. Muchos trámites se abren en **WebView**
(`flutter_inappwebview`) apuntando al portal del SAT; otros son nativos en Flutter.

## Trámites y servicios identificados

| Servicio | Evidencia | Modo |
|---|---|---|
| **Inicio de sesión / CIEC** | OAuth2 ROPC (doc 03) | Nativo Flutter |
| **SAT ID** (verificación de identidad) | `chiikul.sat.gob.mx`, cámara+video, TFLite (doc 06) | Nativo + ML |
| **e.firma portable** (firmado) | `/dwh/efirma/`, `/dwh/digitalcertificate/`, PointyCastle (doc 05) | Nativo |
| **Declaración Anual PF** | `anualpf.clouda.sat.gob.mx` | WebView |
| **Opinión de cumplimiento** | ruta `/opinionc/`, cadena `OPINIONC` | API + PDF |
| **Mi información** (datos del contribuyente) | `/dwh/miinformacion/` | API |
| **Citas** | `citas.sat.gob.mx` | WebView |
| **Chat / orientación** | `chat.sat.gob.mx` | WebView |
| **Denuncias** | `aplicacionesc.mat.sat.gob.mx/.../denunciaSatMovil.aspx` | WebView (ASP.NET) |
| **Verificación de CFDI / carta porte** | `verificacfdi.facturaelectronica.sat.gob.mx/verificaccp/` | WebView |
| **Validador de QR** | `siat.sat.gob.mx/app/qr/.../validadorqr.jsf` + escáner ZXing | Nativo (escáner) + WebView |
| **Portales de servicios/declaraciones** | `pstcdypisr.clouda`, `ptscdecprov.clouda` | WebView |
| **Expediente / medios de contacto** | `wwwmat.sat.gob.mx/iniciar-expediente/medios_contacto/` | WebView |
| **Videos educativos** | YouTube Data API + `youtube_player_iframe` | Nativo |
| **Eventos / telemetría** | `/satmovil/v1/events/` | API (background) |

## Módulos Dart confirmados (2º pase, doc 10)

El árbol `package:sat_movil_2` recuperado de `libapp.so` confirma y **amplía** la lista anterior con
funciones que no se veían solo por endpoints. Servicios propios (`lib/services/`):

- **Constancia de Situación Fiscal (CSF)** — `information_from_csf_service`
- **Buzón tributario / mensajes** — `message_service`, `notifications/mailbox_notification`
- **Régimen fiscal** — `regime_service`
- **Obligaciones** — `obligations_service`
- **Actividad económica** — `economical_activity_service`
- **Sellos** (digitales/CFDI) — `stamps_service`
- **Domicilio fiscal** — `address_service`
- **Certificados de e.firma** — `certificates_service`
- **Identificación / SAT ID** — `identification_service`, flujo `pages/password_process_flow/` (17 pantallas)

El subsistema `services/notifications/` tiene un mapeador por dominio (address, calendar,
certificates, contact, economical_activity, events, mailbox, message, obligations, regime, stamps,
identification) + `notifications_counter`: la app sincroniza cada dominio y notifica localmente los
cambios. Lista completa en `10-extraccion-profunda.md` y `analisis/dart-tree-sat_movil_2.txt`.

## Patrón arquitectónico de UI

- **Híbrido nativo + WebView.** El núcleo de identidad (login, SAT ID, e.firma, QR) es Flutter
  nativo porque necesita cámara, cripto y biometría. Los trámites "de portal" (declaración anual,
  citas, chat, CFDI, denuncias) se sirven en WebView apuntando a los sitios existentes del SAT,
  varios construidos en **ASP.NET** (`.aspx`, `__VIEWSTATE`) y **JSF** (`.jsf`). Es un patrón común
  en gobierno: envolver portales legacy en una app móvil.
- **Generación de documentos.** Syncfusion PDF + pdfx + printing + share_plus → la app genera y
  comparte acuses/comprobantes (p. ej. opinión de cumplimiento en PDF).
- **Servicio en background.** `flutter_background_service` + `/events/` sugiere envío de eventos y
  posiblemente avisos; reinicia al boot (doc 01).

## Superficie de contenido embebido

- **Azure Blob** (`wu1agsprosta001.blob.core.windows.net`): hosting de contenido/recursos (banners,
  catálogos, quizá actualizaciones de config) — fuera del dominio `sat.gob.mx`.
- **WebView `flutter_inappwebview`**: trae incluso el juego t-rex offline y `web_support.js`. Es el
  mismo componente para todos los portales; su configuración (JS habilitado, manejo de certificados,
  dominios permitidos) es relevante y **no se trazó** — pendiente de dinámica.

## Límite

El mapeo servicio→pantalla es por endpoint y cadena, no por flujo de navegación. El orden real, qué
pantallas son WebView vs nativas en cada caso, y los parámetros de cada request están en Dart AOT.
