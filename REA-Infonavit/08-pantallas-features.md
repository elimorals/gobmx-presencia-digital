# 08 · Pantallas y features de negocio

Mapa derivado de los subpaquetes de `com.infonavit.micuentainfonavit.ui.*` (y actividades del
manifiesto). Es el inventario de funcionalidades que la app ofrece al derechohabiente. No se
ejecutó la app; la función de cada módulo se infiere del nombre del paquete y del manifiesto.

## Entrada y sesión

| Módulo | Qué hace (inferido) |
|---|---|
| `ui.login` (`SplashActivity`→`LoginV3Activity`) | splash, login NSS/CURP + contraseña |
| `ui.registro.v2` (`RegistroActivity`, exported, deeplink) | alta de cuenta; recibe deeplinks `…/registro` |
| `ui.recuperar_contrasena.v2` (`RestablecerContrasenaActivity`, exported, deeplink) | recuperación de contraseña |
| `ui.biometric` | login biométrico (huella/rostro) con KeyStore (doc 05) |
| `ui.avisoprivacidad` (`TermConditionsActivity`, `AvisoPrivacidadActivity`) | términos y aviso de privacidad |
| `ui.aviso` | avisos/mensajería in-app |

## Ahorro, crédito y saldo (core)

| Módulo | Qué hace |
|---|---|
| `ui.resumen_ahorros` / `resumen_ahorros_v2` | resumen de subcuenta de vivienda |
| `ui.savings` / `saving_v2` | ahorro / saldo detallado |
| `ui.movements` | movimientos de la subcuenta |
| `ui.aportaciones_extraordinarias` | aportaciones voluntarias/extraordinarias |
| `ui.reestructuras` | reestructura de crédito (modelo `ReestructuraResponse`) |
| `ui.mejoravit` | línea de crédito Mejoravit (mejora de vivienda) |
| `ui.vivienda_bienestar` | programa Vivienda para el Bienestar |
| `ui.calculadora_uma` | calculadora (UMA / montos) |

## Trámites y atención

| Módulo | Qué hace |
|---|---|
| `ui.aclaracion_pagos` | aclaración de pagos (usa servicio de pago, doc 04) |
| `ui.citas` | agenda de citas (CESI / Centro de Servicio) |
| `ui.oficinas` (`OficinasContainerActivity`) + `ui.location` | localizador de oficinas (Maps/Places + GPS) |
| `ui.constancia` | generación de constancias (PDF, PDFium/iText) |
| `ui.seguimiento_caso` | seguimiento de casos (modelo `BusquedaCasosResponse`) |
| `ui.correccion_datos` / `ui.actualizar_datos` | corrección/actualización de datos de contacto |
| `ui.profile` | perfil del derechohabiente (`DatosPrincipales`, `UserDataResponse`) |

## Seguridad y token

| Módulo | Qué hace |
|---|---|
| `ui.token_infonavit` | generación de token de la app |
| `ui.nip_infonatel` | NIP para Infonatel (atención telefónica) |
| `ui.bitacora_seguridad` | bitácora de seguridad (eventos de la cuenta) |
| `ui.trazas` | trazas/auditoría interna |

## Otros

`ui.encuesta_satisfaccion` (encuestas), `ui.pdf_view` (visor PDF con PDFium), `ui.amount_view`,
`ui.controls`/`foundations.controls`/`foundations.adapters` (componentes UI), `usecases` (lógica
de negocio), `databinding` (Android Data Binding).

## Datos personales en juego

Por los modelos (`com.infonavit.domain.models.*`): NSS, CURP, RFC, datos de contacto, crédito,
relaciones laborales, saldos y movimientos, casos de atención. Todo ello viaja por el cliente sin
validación TLS (doc 03) y algunos trámites usan las credenciales de servicio embebidas (doc 04),
lo que eleva el impacto de H1/H2 sobre PII financiera y de identidad.
