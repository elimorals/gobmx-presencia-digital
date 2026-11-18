# Lote 5 — banda 50k–100k instalaciones (licencias, pánico y tier medio)

**Fecha:** 2026-11-18
**Alcance:** 26 apps descargadas de la banda 50k–100k (2 no disponibles en APKPure: Mérida Móvil,
Mi IFT). **9 con RE a fondo** (carpeta `REA-*/` propia); **17 con triage** (package + cleartext +
firma + endpoints + veredicto). Método estático; verificación de package antes de documentar.

## Objetivo del lote

Extender dos hallazgos sistémicos con más muestras:
1. **Cruce de proveedores de licencia de conducir** — ¿el trust-all de Cosmocolor es del producto o
   de "las apps de licencia" en general?
2. **Hipótesis pánico→cleartext** — ¿las apps de pánico/emergencia heredan el cleartext?

## 1. Cruce de 5 vendors de licencia (RE a fondo) — CONCLUSIÓN

**El defecto TLS trust-all es EXCLUSIVO del producto Cosmocolor.** 4 vendors ajenos, limpios.

| Vendor | App / Estado | Pila | Backend | Trust-all | Veredicto |
|--------|--------------|------|---------|-----------|-----------|
| **Cosmocolor** | Chiapas (`...movapp.chp`) | nativa | `licenciamanejo.com` | **A-CONFIRMAR** (builder R8; primitivas presentes, residuo EDOMEX) | **3er estado Cosmocolor confirmado** |
| ZTK | Guanajuato | Flutter | `licenciadigitalgtoapp.appspot.com` (Firebase) | 🟢 ausente | sano |
| licdig | Chihuahua | Capacitor | `licdigital.sspechih.gob.mx` | 🟢 ausente | sano (config permisiva, backend HTTPS) |
| validatulicencia | Oaxaca | Flutter | `oaxaca.validatulicencia.mx` | 🟢 ausente | sano |
| DC Soluciones | Tlaxcala | Capacitor/Vue | `applic.smyt.tlaxcala.gob.mx` (OpenPay) | 🟢 ausente | sano; "global" ≠ multi-estado |

→ **Clúster B (Cosmocolor) sube de 2 a 3 estados** (EDOMEX ✓, Jalisco ✓, Chiapas a-confirmar).
Carpetas: `REA-Licencia-{Chiapas,Guanajuato,Chihuahua,Oaxaca,Tlaxcala}/`.

## 2. Apps de pánico/emergencia (RE a fondo) — HIPÓTESIS REFUTADA

**"Pánico → cleartext" NO es un patrón general.** De las apps de pánico de los lotes 4+5, solo
**Veracruz Mujer Alerta** (CRÍTICA) y las **911** usan cleartext; el resto están sanas. El cleartext
es **por despliegue de dependencia, no inherente al tipo de app**.

| App | Dependencia | Firma | Veredicto |
|-----|-------------|-------|-----------|
| Llamada Segura Hidalgo | C5i Hidalgo | **auténtica** | 🟢 sano (HTTPS, sin trust-all) |
| GN Reporta | Guardia Nacional/SSPC | re-firmada | 🟢 sano (cleartext=true **no usado**; backend HTTPS) |
| Mujeres Seguras | SEGOB Sonora | re-firmada | 🟢 sano (SendLocation http **comentado**); 🟡 endpoint `/mstest2` en prod |
| Alerta Ciudadana Veracruz | C4 Veracruz | dev individual | 🟢 sano (100% HTTPS) |

**Corolario "patrón Veracruz":** SSP (Mujer Alerta) + SEV = cleartext; C4 (Alerta Ciudadana) = sano
→ es por dependencia, no política estatal. Carpetas: `REA-{Llamada-Segura-Hidalgo,GN-Reporta,
Mujeres-Seguras-Sonora,Alerta-Veracruz}/`.

## 3. Tier medio (triage, no RE a fondo)

`usesCleartextTraffic` leído del manifest; **un flag `true` NO implica uso real** (GN Reporta lo
probó: flag true, backend HTTPS). Firma: casi todas re-firmadas por APKPure salvo las marcadas.

| App | Package | cleartext | Firma | Backend / nota |
|-----|---------|-----------|-------|----------------|
| SVH Morelos (finanzas) | `mx.onbit.morelos` | true | APKPure | `hacienda.morelos.gob.mx` + vendor `voixty.mx` |
| Salud Digital Chihuahua | `com.ssch.saluddigital` | true | APKPure | `saluddigital.ssch.gob.mx` |
| SAPAL (agua) | `mx.gob.sapal.sapalmovil` | n/a | APKPure | `services.sapal.gob.mx` |
| SIGO GTO / TaxiGto | `mx.hts.TaxiGtoUsuario` | true | APKPure | `taxi.guanajuato.gob.mx` |
| PTATConnect (STPS) | `com.stps.ptatmovil` | true | APKPure | `ptatconect.stps.gob.mx` |
| PJEdomex (judicial) | `com.pjedomex.pjedomex` | true | APKPure | pendiente endpoint (xapk) |
| Koox Contigo (Campeche) | `mx.gob.campeche.kooxcontigo` | n/a | — | base no extraído |
| Guadalajara App | `com.gdlmun.appcity` | true | APKPure | Capacitor (URLs en www/) |
| Gobierno Municipal SLP | `com.gobiernomunicipalslp` | true | APKPure | Capacitor |
| Marca el Cambio (Mpio Chihuahua) | `com.municipiodechihuahua.mp` | n/a | **auténtica Mpio Chihuahua** | trámites municipales |
| Feria San Marcos (Ags) | `mx.gob.aguascalientes.fnsm` | n/a | **auténtica Ags** | turismo |
| México en Cifras | `mx.org.inegi.MexicoCifras` | n/a | **auténtica INEGI** | datos públicos |
| Binnibus (Oaxaca) | `mx.gob.oaxaca.ios.binnibus` | n/a | APKPure | transporte |
| Hermosillo Gob | `mx.gob.hermosillo.hermosillogob` | n/a | APKPure | trámites |
| Tarjeta Joven (IMJUVE) | `mx.gob.imjuventud.app.imjuveapp` | n/a | APKPure | — |
| Correos México | `com.flyhighware.mailtracker` | n/a | APKPure | seguimiento (no-oficial) |
| APPrende INE Proceso Local | `mx.ine.app.apprendeine` | n/a | APKPure | elecciones |

**Candidatos a profundizar (cleartext=true + sensible):** SVH Morelos (hacienda), Salud Digital
Chihuahua (salud), PJEdomex (judicial), SIGO GTO, PTATConnect — requieren RE a fondo para confirmar
si el cleartext se usa (como GN Reporta, el flag puede ser vestigial).

## No descargables

Mérida Móvil (`app.meridamovil.com`), Mi IFT (`com.ift.comparador`) — no están en APKPure.
