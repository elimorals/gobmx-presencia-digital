# 02 · Red y endpoints

Red con Retrofit2 + OkHttp3. Hosts extraídos de los DEX (strings):

## Backends propios (HTTPS)

- `https://descargacultura.unam.mx/` — API/contenido principal (y `/covers/`, `/resources/docs/…`).
- `https://www.unam.mx/`, `https://www.unamenlinea.unam.mx/` — enlaces institucionales.

## Terceros (HTTPS, estándar)

- Firebase: `firebase-settings.crashlytics.com`, `app-measurement.com` (Analytics/Crashlytics).
- Redes sociales (enlaces): Twitter/X, Facebook, Instagram, YouTube de DescargaCultura.

## Cleartext

- **Único `http://`: `http://localhost/`** — artefacto de desarrollo (loopback), inerte: no apunta
  a ningún backend real. Ver [03-red-tls.md](03-red-tls.md).

Todos los backends reales van por **HTTPS**. No hay IPs internas RFC1918 ni endpoints `-qa`/`-dev`
de producción embebidos.
