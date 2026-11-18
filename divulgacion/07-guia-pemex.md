# Informe de divulgación responsable — Guía Pemex

**Para:** Petróleos Mexicanos (Pemex) — área de tecnología/seguridad de la información (y
proveedor del desarrollo de la app).
**De:** [TUS DATOS]
**Fecha:** [FECHA DE ENVÍO]
**Severidad:** **MEDIA-ALTA**
**Estado:** divulgación privada; plazo de corrección sugerido 90 días.

## Resumen ejecutivo

La app oficial **Guía Pemex** incluye un archivo `.env` empaquetado dentro de los assets de la
app, con la **API key del backend** y claves de servicios de mapas (Google Maps / TomTom) en texto
plano. Cualquiera puede extraerlas de una copia del APK y usarlas contra el backend o consumir
cuota de los servicios de mapas a cargo de Pemex.

## App afectada

- **Nombre:** Guía Pemex
- **Package:** `mx.gob.pemex.guiapemex` (confirmar contra el canal oficial).
- **Pila:** Flutter. El secreto vive en `assets/flutter_assets/.env`. SHA-256: ver `hashes.sha256`.

## Detalle técnico

- **CWE-798 / CWE-312: Use of Hard-coded Credentials / Cleartext Storage.**
  - `assets/.env` contiene `APP_SERVICES_API_KEY` (API key del backend de la app) y claves de
    Google Maps / TomTom, todas en claro.
  - Los assets de Flutter **no están cifrados**: `unzip` del APK los expone directamente.

> Los valores concretos de las claves **no** se incluyen en este informe; se entregan al afectado
> por canal seguro si lo solicita.

## Impacto

- **Abuso de la API key del backend:** llamadas no autorizadas a los servicios de la app con la
  identidad de la app.
- **Consumo de cuota / cargos** en Google Maps / TomTom a cargo de Pemex por el uso de las claves
  por terceros.

## Alcance y método (ética)

Análisis **estático** de un APK público; no se usó ninguna de las claves ni se atacó el backend.

## Remediación recomendada

1. **Rotar** la `APP_SERVICES_API_KEY` y las claves de mapas.
2. No empaquetar `.env` en el cliente; el secreto del backend debe vivir en el servidor (la app
   debería autenticarse, no portar la llave del servicio).
3. Restringir las claves de mapas por package/firma de app (restricción de Android) y por API.

## Divulgación

Por haber **secretos activos**, priorizar la rotación. Acuse solicitado; plazo sugerido 90 días.
