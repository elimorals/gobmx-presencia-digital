# 02 · Red, TLS y hallazgos — eSIT Móvil Hidalgo

## Backends

| Host | Uso | Protocolo |
|------|-----|-----------|
| `atributariaw1.hidalgo.gob.mx` | API tributaria | HTTPS |
| `portaltributario.hidalgo.gob.mx` | portal / aviso de privacidad | HTTPS |

Extraídos del bundle Hermes (`strings`). No se halló ningún backend por `http://` en claro (los
`http://` presentes son de React dev, p. ej. `fb.me/use-check-prop-types`).

## TLS (H1 — AUSENTE-SANO)

- Sin `usesCleartextTraffic`, sin NSC, targetSdk 34 → **cleartext bloqueado por defecto**.
- No se halló trust-all: ni `getUnsafeOkHttpClient`, ni `checkServerTrusted` vacío, ni
  `HostnameVerifier` permisivo (es RN, no Retrofit; la red va por `fetch`/networking de RN).
- **Conclusión:** postura TLS correcta. No repite el defecto de 911-CDMX (cleartext) ni el de
  Infonavit/Cosmocolor (trust-all). Clasifica como **contraejemplo sano**.

> Matiz honesto: la **validación TLS efectiva en runtime** (p. ej. si algún `fetch` deshabilita la
> verificación, o pinning) vive en el bytecode Hermes y **no** se confirmó estáticamente. "Sano" =
> no se halló defecto, no "probado seguro".

## Secretos (H3/muro)

- No se hallaron API keys en claro (`AIza…`, `sk_live`, `pk_live`, `Bearer …`) en el bundle.
- El empaquetado Hermes glutina strings; valores de secretos/tokens no son confirmables sin
  `hermes-dec` o instrumentación dinámica. **A-CONFIRMAR**, sin afirmar ausencia total.

## Permisos (H2/H4)

- `DUMP` (privilegiado, inerte) y `RECORD_AUDIO` (amplio) → higiene de permisos mejorable, sin
  impacto directo demostrado. Ver [01-manifiesto.md](01-manifiesto.md).

## Proveniencia

Re-firmada por APKPure (`CN=Android, O=Google Inc.`, cert `880cdb17…`). El **bytecode no cambia**
al re-firmar, así que el análisis de código es válido; la **firma no prueba autenticidad** — cruzar
el sha256 contra el APK del canal oficial de Hidalgo antes de cualquier divulgación.
