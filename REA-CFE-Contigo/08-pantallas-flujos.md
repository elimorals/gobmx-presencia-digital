# Pantallas y flujos (trámites cubiertos)

31 Activities declaradas. Todas las de negocio son `exported="false"` (solo se llega tras el
Splash/login). El inventario de `ui.*` y los DTOs asociados dibujan los trámites que la app
resuelve.

## Mapa de pantallas → función

| Activity (`mx.com.cfe.cfecontigo.ui.*`) | Función | DTOs asociados |
|---|---|---|
| `login.SplashActivity` | Arranque (LAUNCHER) | — |
| `login.MaintenanceActivity` | Pantalla de mantenimiento (backend caído) | — |
| `login.LoginActivity` | Login (usuario/contraseña + captcha) | `AuthResponse`, `Captcha*` |
| `register.RegisterActivity` | Alta de cuenta | `AccountRequest` |
| `recover_password.RecoverPasswordActivity` | Recuperar contraseña | `Otp*Dto` |
| `password.PasswordActivity` | Cambio de contraseña | — |
| `tutorial.TutorialActivity` | Onboarding | — |
| `dashboard.DashboardActivity` | Inicio; mapa de CAC (`LocationsFragment`, `RippleRingsView`) | `CentroCACDto`, `CacDto` |
| `services.ServicesActivity` | Servicios/contratos ligados | `ServiceInfoResponse`, `Servicio` |
| `reciept.ReceiptActivity` | Recibos / comprobantes | `Factura`, `FacturaServicio`, `Voucher` |
| `cards.CardsActivity` | Tarjetas de pago | `CardDto`, `CardsDto`, `CardBinDto` |
| `simulator.SimulatorActivity` | Simulador de consumo (`ConsumptionGaugeView`) | `SimulatorResponse`, `Parameters` |
| `paperless.PaperlessActivity` | Factura electrónica / sin papel | `RegimenFiscalDto`, `UsoCfdiDto` |
| `contratacion.ContratacionActivity` | Alta de servicio nuevo + firma (`SignatureView`) | `ContractRequest`, `DatosContratanteDto`, `DatosFiscalesDto`, `DomicilioInstalacionDto`, `IdentificacionDto`, `FirmaDto`, `ComprobanteDomicilioDto` |
| `how_to_contract.HowToContractActivity` | Guía de contratación | `RequisitoTramiteDto` |
| `reports.ReportsAddActivity` | Reporte de falla / "no luz" | `SeguimientoNoLuz`, `SituacionNoLuzDto`, `OrdenConexion` |
| `anticorrupcion.AnticorrupcionActivity` | **Canal de denuncia de corrupción** | `DenunciaRequest`, `QuejaTrabajador*Dto` |
| `profile.ProfileActivity` | Perfil del usuario | `ProfileRequest`, `GoogleProfile` |
| `messages.MessagesActivity` | Mensajes / avisos | `MessageDto`, `MessagesDto` |
| `privacy.PrivacyActivity` | Aviso de privacidad | `assets/avisoprivacidad.md` |
| `terms.TermsActivity` | Términos y condiciones | — |

## Trámites que resuelve (resumen)

1. **Consulta y pago de recibo** (recibos, facturas, pago con tarjeta tokenizada/puntos).
2. **Contratación de servicio nuevo** (datos del contratante, fiscales, domicilio de
   instalación, identificación, comprobante de domicilio, **firma digital** en pantalla).
3. **Agendado de citas en CAC** (`AgendarCitaRequestDto`, `DisponibilidadResponseDto`,
   `HorarioDisponibleDto`, `ReagendarCitaRequestDto`, `BloquearHorarioRequestDto`).
4. **Reporte de fallas / "no hay luz"** con seguimiento.
5. **Denuncia de corrupción** (canal anticorrupción integrado).
6. **Simulador de consumo** y **facturación electrónica** (CFDI: régimen fiscal, uso CFDI).

## Componentes de UI propios notables

- `contratacion.SignatureView` — captura de firma manuscrita (contratación).
- `simulator.ConsumptionGaugeView` — gauge de consumo kWh.
- `dashboard.RippleRingsView` — animación del mapa de CAC.
- `markdown.MarkdownView` + `assets/markdown.css` — render de aviso de privacidad/términos en Markdown.

## Multi-contrato

`AutorizacionesDto`, `ServiceResponse`, `Servicio` y la relación cuenta↔servicios sugieren que
un usuario puede administrar **varios servicios/contratos** (casa, negocio) bajo una sola
cuenta, cada uno con sus recibos.
