# 07 — Operaciones del SAR (módulos de negocio)

AforeMóvil se construye con **Play Feature Delivery**: 27 módulos fusionados
(`com.android.dynamic.apk.fused.modules`), cada uno con un `Activity`
`com.procesar.aforemovil.<módulo>.main.*Activity`. Mapa a operaciones del SAR:

## Acceso y cuenta

| Módulo | Activity | Operación |
|---|---|---|
| `preexistence` | `PreexistenceActivity` *(exportada)* | Verificar si el trabajador ya tiene cuenta/registro |
| `register` | `RegisterActivity` | **Alta** / registro (dispara captura biométrica, doc 03) |
| `home` | `HomeActivity` *(exportada)* | Pantalla principal autenticada (saldo, accesos) |
| `profile` | `ProfileActivity` | Perfil, datos personales, beneficiarios |
| `nip_activation` | `NipActivationActivity` | Activar NIP |
| `niprecovery` | `NipRecoveryActivity` | Recuperar NIP |

## Consulta

| Módulo | Activity | Operación |
|---|---|---|
| `transactions` | `TransactionsActivity` | **Movimientos** / estado de cuenta |
| `calculators` | `CalculatorsActivity` | Calculadoras de ahorro/retiro (enlaza a CONSAR) |
| `goals` | `GoalsActivity` | **Metas** de ahorro (“La Aventura de mi Vida”) |
| `notifications` | `NotificationsActivity` | Notificaciones |

## Ahorro (aportaciones voluntarias)

| Módulo | Activity | Operación |
|---|---|---|
| `onlinesavings` | `OnlineSavingsActivity` | Ahorro **en línea** (cargo/tarjeta) |
| `saveincash` | `SaveInCashActivity` | Ahorro **en efectivo** (tiendas/convenios) |
| `savingsbytransfer` | `SavingsByTransferActivity` | Ahorro por **domiciliación/transferencia** |
| `savewithbenefits` | `SaveWithBenefitsActivity` | Ahorro **con beneficios** (p. ej. incentivos) |

## Traspasos

| Módulo | Activity | Operación |
|---|---|---|
| `transfers` | `TransfersActivity` | **Traspaso** de AFORE |
| `transfers_cuo` | `TransfersCuoActivity` | Traspaso **CUO** (Cuenta Única de... / variante operativa) |

## Retiros

| Módulo | Activity | Operación |
|---|---|---|
| `unemployment` | `UnemploymentWithdrawalActivity` | **Retiro por desempleo** (parcial) |
| `marriage` | `MarriageWithdrawalActivity` | **Retiro por gastos de matrimonio** |
| `voluntary_savings_withdrawal` | `VoluntarySavingsWithdrawalActivity` | **Retiro de ahorro voluntario** |

## Servicios y soporte

| Módulo | Activity | Operación |
|---|---|---|
| `appointments` | `AppointmentsActivity` | **Citas** (centros de atención; usa Maps/ubicación) |
| `minors` | `MinorsActivity` | Cuentas de **menores** |
| `complaints` | `ComplaintsActivity` | **Quejas** |
| `faqs` | `FaqsActivity` | Preguntas frecuentes (Realm `FaqRealm`/`FaqSection`) |
| `helponline` | `HelpOnlineActivity` | Ayuda en línea |
| `about` | `AboutActivity` | Acerca de |
| `calculators` | — | (ver Consulta) |

## Autorización de operaciones

Las operaciones que mueven dinero (ahorro, traspasos, retiros) se **autorizan biométricamente** vía
**GestoPago/FIDO** (doc 02): la app crea una autorización (`createGestoPagoAuthorizationList`,
persistida en Realm) que el usuario firma con el authenticator UAF (`DisplayTransactionActivity`
muestra el detalle). Validaciones de entrada: `ClabeError` (CLABE), `AmountError` (montos),
`RfcError` (RFC), `NipError` (NIP).

## Modelo de dominio de apoyo (`commons.data.model`)

`Afore`, `Saver` (ahorrador), `Beneficiary`/`BeneficiaryRelationship`, `Bank`, `Branch` (sucursal),
`Country`, `Address`, `EconomicActivity`, `Employment`, `Periodicity`, `Scholarship`,
`DocumentProcedure`, `MasterCatalog` (catálogos del SAR), `UXUIConfig`/`UXUIDetails`/`Navigation`
(UI y navegación **dirigidas por servidor**).

> La UI dirigida por servidor (`UXUIConfig`) permite a Procesar reconfigurar pantallas/navegación
> sin publicar nueva versión; su contenido se cachea en Realm.
