# Datos y PII manejados

La app es de **procuración de justicia**: concentra datos personales de víctimas y denunciantes
de la categoría más sensible. Inferido de los módulos, modelos y permisos (doc 01–02); el
contenido exacto de cada request vive en el servidor (no se tocó).

| Dato / flujo | Módulo | Sensibilidad |
|---|---|---|
| Identidad del denunciante (nombre, perfil, contacto) | `registro`/`perfil`, todos | Alta |
| Relato de la denuncia + **evidencia fotográfica** (con EXIF/geolocalización, `ACCESS_MEDIA_LOCATION`) | `denuncia*` | Muy alta |
| **Denuncia por violencia de género** (víctima, agresor, hechos) | `denuncia_genero` | **Crítica** |
| **Ubicación en tiempo real + contactos de pánico** | `panico` | **Crítica** (seguridad física) |
| **Perfil genético / ADN** (solicitud, muestras, citas) | `perfil_genetico` | **Crítica** |
| Persona desaparecida (datos del reporte) | `persona_desaparecida` | Muy alta |
| Reporte de extorsión | `extorcion` | Alta |
| Cartas de no-antecedentes por **folio** | `antecedentes` | Alta (dato legal) |
| Vehículo robado (placas, VIN, propietario) | `vehiculo_robado` | Media-Alta |
| Credenciales (login, recuperar contraseña) | `registro` | Alta |

## Por qué importa con el hallazgo del doc 03

Todos estos datos se envían a los backends de la FGE. Las rutas que usan
`RetrofitClientInstancePROD` y `RetrofitClientInstanceGenetica` lo hacen **sin validar el
certificado del servidor** (doc 03). Un MITM puede capturar o manipular:
- la **identidad y el relato** de una víctima de violencia de género,
- la **ubicación de un botón de pánico** (riesgo directo a la integridad física),
- las **citas y solicitudes de perfil genético** (ADN).

Es el conjunto de datos donde una fuga/MITM tiene el **mayor daño potencial** del acervo gob.mx
analizado: no es dinero ni un trámite, es la seguridad de personas en situación de riesgo.

## Antecedentes (cartas por folio) — nota

El módulo `antecedentes` descarga cartas por **folio** (`CartasFolioRepository`). Como en SEG
(certificados por `{folio}`), **si** los folios fueran predecibles y el endpoint no exigiera
autorización ligada al folio, habría superficie de IDOR. **No verificable estáticamente** (no se
tocó el endpoint); se deja como pregunta a revisar del lado del servidor, sin afirmarlo.
