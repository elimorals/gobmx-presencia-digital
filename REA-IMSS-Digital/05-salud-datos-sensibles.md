# Datos de salud y sensibles

IMSS Digital no es una app de consulta de citas: da acceso móvil al **expediente clínico
electrónico** y a módulos de salud altamente sensibles. Esto eleva el impacto de cualquier
debilidad en autenticación/transporte (doc 06/08).

## Expediente clínico — `cds.imss.gob.mx` (Centro de Datos de Salud)

| Microservicio | Dato |
|---|---|
| `mscds-notas/v{1,2}/notasMedicas` | **Notas médicas** (consultas) |
| `mscds-resultados/v3/resultado/laboratorio/detalle[/pruebas]` | **Resultados de laboratorio** |
| `mscds-somatometria/v3/…` | Peso, talla, signos (somatometría) |
| `mscds-derechohabiente/v4/derechohabiente` | Datos del derechohabiente |
| `msnssa-app-movil/…/asegurado`, `/incapacidades/6meses`, `/validaAsegurado` | Estado de aseguramiento, incapacidades |

## Derechohabiencia — `canalmovil.imss.gob.mx`

- `mscmov-derechohabiencia-rest/.../consultaVigencia` — **vigencia de derechos**.
- `.../obtenerConstancia…` — constancia de vigencia (documento oficial).
- `mscmov-cmsrevista`, `mscmsamid-cms` — revista/contenidos del CMS.

## Pensionados (RJP) — `siap.imss.gob.mx`

- `api/rjp/personal/Consultar` — datos del pensionado.
- `api/rjp/supervivencia[/consultar]` — **prueba de supervivencia** (fe de vida) — crítico:
  determina continuidad del pago de pensión.
- `api/rjp/pertenencia/validar`, `api/rjp/correo/actualizar`.
- `.../aceptaciones/privacidad-integral` — aceptación de aviso de privacidad.

## Maternidad / incapacidades — `tspi.imss.gob.mx`, `cds-qa`

- `maternidad-web-movil/incapacidad/registro`, `licencia-140-bis-web` (cuidado de hijos con
  cáncer — Ley 140 Bis).
- `cds-qa.apps.ocp…/msnssa-app-movil/maternidad/{incapacidades,validaIncapacidad,actualiza/folioIncapacidad}` (**en QA**).

## Detección de violencia familiar — `vfam.imss.gob.mx` / `msvfam-*`

Módulo de alta sensibilidad:
- `msv-cuestionario/.../deteccion-violencia/{iniciar,enviarRespuestas,obtener}` — cuestionarios.
- `msv-cuestionario/.../grado-violencia/…` — evaluación de grado.
- `msv-evaluacion-directorio/.../directorio/{centroAtencionVictima,entidadesConCentrosVictimas}` —
  directorio de **centros de atención a víctimas**.
- `msv-usuario/.../usuario/{alta,agregarContacto}`.

## VPH (papiloma) — `msvph-consulta-…`

- `msvph-consulta/v1/vph/{resultado/historico,detalleresultado,validanss}` — **resultados de
  pruebas de VPH** por NSS.

## COVID-19

- `testcoronavirus.imss.gob.mx`, `covid-calculadora.imss.gob.mx`,
  `infofamiliarcovid.imss.gob.mx`, `msnssa-app-movil/covid19/bancosNSSA`.

## Por qué importa

Esta app concentra, bajo una sola sesión móvil: **expediente clínico, resultados de
laboratorio, estado de pensión, incapacidades, datos de violencia familiar y VPH**. Es
información médica y personal del nivel más sensible. Que el **login base** se ofrezca por
**HTTP** (doc 03) y que el flag global permita cleartext (doc 01) compromete la cadena: si el
token de sesión se obtiene sin TLS, todo lo anterior queda expuesto a un atacante en red
(doc 08). El tráfico de los módulos sensibles (vfam, vph, cds) sí es HTTPS, pero depende del
token emitido en el login.
