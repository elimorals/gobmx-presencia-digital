# Divulgación responsable — paquete de informes

**Borradores** de divulgación responsable de los hallazgos de seguridad críticos y altos del
análisis de ingeniería inversa (`REA-*/`). **No enviados.** Cada informe está listo para que el
investigador lo remita, por el canal oficial de la dependencia o proveedor afectado, firmado con
sus propios datos de contacto (marcados `[TUS DATOS]`).

## Principios de este paquete

- **Divulgación responsable, no pública:** notificar primero al afectado y darle plazo de
  corrección (sugerido **90 días naturales**) antes de cualquier publicación de detalle.
- **Sin armas:** los informes describen la **clase** de vulnerabilidad, su **ubicación** en el
  código y el **impacto**, con remediación. **No** incluyen un exploit funcional ni un
  procedimiento de extracción paso a paso.
- **Sin secretos en claro:** los valores de llaves/credenciales hallados **no** se incluyen; se
  ofrecen al afectado que lo solicite, por canal seguro.
- **Alcance ético:** análisis **estático** de un **artefacto público** (APK de tienda), sin
  atacar infraestructura en vivo, sin usar credenciales ajenas y sin extraer datos de usuarios.

## Informes

| # | App | Dependencia / Proveedor | Severidad | Archivo |
|---|---|---|---|---|
| 01 | ProcurApp | Fiscalía General del Estado de Guanajuato (FGE GTO) | **CRÍTICA** | [01-procurapp-fge-guanajuato.md](01-procurapp-fge-guanajuato.md) |
| 02 | Licencia Digital (EDOMEX + Jalisco) | **Cosmocolor** (proveedor) + SEMOV EDOMEX + Transporte Jalisco | **CRÍTICA (multi-estado)** | [02-cosmocolor-licencia-multiestado.md](02-cosmocolor-licencia-multiestado.md) |
| 03 | Mi Cuenta Infonavit | INFONAVIT | **CRÍTICA** | [03-infonavit.md](03-infonavit.md) |
| 04 | 911 CDMX | C5 / CAEPCCM (CDMX) | **ALTA** | [04-911-cdmx-c5.md](04-911-cdmx-c5.md) |
| 05 | Tesorería CDMX | Secretaría de Administración y Finanzas (SAF CDMX) | **ALTA** | [05-tesoreria-cdmx-saf.md](05-tesoreria-cdmx-saf.md) |
| 06 | FiREL | Consejo de la Judicatura Federal (CJF) + FielNet/IQSEC | **ALTA (a confirmar)** | [06-firel-cjf.md](06-firel-cjf.md) |
| 07 | Guía Pemex | Petróleos Mexicanos (Pemex) | **MEDIA-ALTA** | [07-guia-pemex.md](07-guia-pemex.md) |
| 08 | **Sistémico** — antipatrón TLS trust-all en el ecosistema gob.mx | CERT-MX (coordinador) + dependencias/proveedores | **CRÍTICA (sistémica)** | [08-sistemico-tls-trust-all-CERT.md](08-sistemico-tls-trust-all-CERT.md) |

> **El informe 08 es el recomendado para enviar primero.** Agrupa el patrón que se repite en los
> casos 01/02/03/06 y lo dirige a un CERT nacional: ataca la causa raíz del ecosistema, no 4 apps
> sueltas. Los informes individuales quedan como respaldo técnico por dependencia.

## Canales de contacto (orientativo)

El investigador elige y verifica el canal. Opciones habituales en México:
- **El contacto de seguridad / oficialía de la propia dependencia** (correo institucional,
  oficialía de partes, o unidad de ciberseguridad si tiene).
- **CERT-MX** (Guardia Nacional, Centro Nacional de Respuesta a Incidentes Cibernéticos) para
  dependencias federales.
- **UNAM-CERT** como coordinador de divulgación si el contacto directo no responde.
- Para los **proveedores** (Cosmocolor, FielNet/IQSEC, Cepo, GemesGroup, MyCampus): su correo de
  contacto/seguridad corporativo — suele ser la ruta más rápida porque el defecto es de su producto.

> Recomendación: documentar fecha y medio de cada notificación, conservar acuse, y fijar el plazo
> de 90 días desde el primer contacto efectivo.
