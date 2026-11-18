# 04 · Permisos, PII y almacenamiento

## PII manejada

App de pánico de violencia de género → datos de altísima sensibilidad:
- **Ubicación en tiempo real** (`ACCESS_FINE_LOCATION`) — el dato central de la alerta.
- **Contactos** (`READ_CONTACTS`/`WRITE_CONTACTS`) — contactos de confianza.
- **Cámara** (`CAMERA`) — evidencia fotográfica.
- Identidad de la víctima (según el formulario GeneXus del backend `/alerta/`).

## H3 — PII sensible sobre canal inseguro · CONFIRMADO · ALTA

Toda esa PII viaja al backend por **HTTP en claro** (ver doc 03). La combinación
"datos de víctima de violencia de género + ubicación + cleartext" eleva el riesgo: la exposición no
es solo de confidencialidad, sino de **seguridad física** de la usuaria si un tercero intercepta su
ubicación. Deriva directamente de H1.

## Almacenamiento local

No se realizó auditoría profunda de `SharedPreferences`/SQLite en este pase (prioridad fue la red).
GeneXus guarda estado de sesión y datos de la KB en `SharedPreferences` y una base local; conviene
revisar en un segundo pase si hay token/credencial en claro y si `allowBackup` está activo. **No
confirmado en este pase** — no se afirma hallazgo de almacenamiento sin evidencia.

## Push

FCM/C2DM estándar. El `C2D_MESSAGE` propio y los receivers de badge OEM son del template GeneXus.
