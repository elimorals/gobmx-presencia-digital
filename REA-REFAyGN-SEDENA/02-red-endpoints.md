# 02 · Red y endpoints

Los endpoints se extrajeron de **strings** de `libapp.so` (Dart AOT); no hay capa de red en
bytecode legible (muro Flutter).

## Backend

| Endpoint | Protocolo | Dueño | Nota |
|----------|-----------|-------|------|
| `https://reefam.sedena.gob.mx/` | **HTTPS** | SEDENA (oficial) | API principal |
| `https://reefam.sedena.gob.mx/uploads/` | **HTTPS** | SEDENA | subida/descarga de archivos (PDF) |

**Todo el tráfico de backend es HTTPS a un dominio `gob.mx` oficial.** No se halló ningún endpoint
`http://` de backend, ni IP interna/privada, ni subdominio de prueba (`-qa`/`-dev`/`sandbox`) — a
diferencia de varias apps del corpus (911-CDMX, SAT, IMSS).

## Enlaces sociales (benignos, informativos)

`facebook.com/SEDENAmxOficial`, `instagram.com/sedenamx`, `x.com/sedenamx`, `youtube.com/...`,
`tiktok.com`, `threads.net` — enlaces de redes sociales oficiales, no son endpoints de API.

## Secretos

- **Sin** API keys en `libapp.so` (barrido de `AIza…`, `AKIA…`, `sk_…`, JWT: cero).
- **Sin** assets sensibles en el APK (no hay `.env`, `config.json`, `.pem`, `.p12`, keystore).
- **Sin** Firebase / google-services.json.

> Caveat: el muro Dart AOT impide confirmar la **ausencia total** de secretos (podrían estar
> ofuscados en el snapshot); lo que se afirma es que no aparecen en claro en strings ni en assets.
