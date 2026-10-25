# Identidad, biometría y KYC

IMSS Digital integra **tres proveedores de verificación de identidad** más biometría local.
Es el stack KYC más grande del lote (CFE solo usa ThreatMetrix para scoring de fraude; aquí
hay verificación de identidad real con documentos y rostro).

## Proveedores (por frecuencia en el código C#)

| SDK | Refs en blob | Rol |
|---|---|---|
| **Mati / MetaMap** | **3,410** | Orquestador KYC dominante: flujo de verificación de identidad |
| **FaceTec** | 693 | **Liveness** facial 3D (prueba de vida), vía `facetec-preprod.firmaautografa.com` |
| **Acuant** | 63 (+AssureID 24, passlive, MedicScan) | Lectura/validación de documentos (INE, pasaporte) |

Confirmaciones adicionales:
- Clase nativa `com.facetec.sdk` en el DEX (binding del SDK FaceTec).
- Reglas de procesamiento de documentos en `assets/`:
  `api_process_fallback_rules_ine.json`, `api_process_fallback_rules_passport.json`.
- `CURP`, `INE`, `Biometri*` aparecen cientos de veces en el código.

## Documentos y señales que lee

- **INE** (credencial de elector) — OCR frontal/reverso + reglas de fallback.
- **Pasaporte** — incluyendo lectura **NFC** del chip (permiso `NFC`, doc 01).
- **CURP** — validación contra `www.gob.mx/curp`.
- **Rostro** — captura con `CAMERA` + liveness FaceTec (prueba de vida contra deepfakes/fotos).
- **Voz** — `RECORD_AUDIO` declarado (posible en algún flujo de FaceTec/soporte).

## Endpoints KYC

| Host | Proveedor |
|---|---|
| `https://facetec-preprod.firmaautografa.com` | FaceTec (vía integrador **firmaautografa.com**) |
| `https://eu.acas.acuant.net`, `eu.ozone.acuant.net`, `eu.frm.acuant.net`, `eu.assureid.acuant.net`, `eu.passlive.acuant.net`, `medicscan.acuant.net` | Acuant (región **EU**) |
| `bmt.imss.gob.mx/msbmt-enroll`, `.../media/selfie` | Enrolamiento biométrico propio de IMSS (BMT) |

## Observaciones

1. **`facetec-preprod`** en el binario de producción: el liveness apunta a un entorno
   **preproducción** del integrador `firmaautografa.com`, no a producción. Posible residuo o
   configuración incompleta (doc 03/08).
2. **Datos biométricos a terceros fuera de México**: Acuant procesa documentos en
   infraestructura **EU** (`eu.*.acuant.net`). El rostro/documentos de derechohabientes salen
   del país hacia proveedores privados — consideración de privacidad/soberanía de datos
   relevante para una institución pública. Debe cruzarse con el aviso de privacidad embebido
   (`API-App-IMSS-Digital.pdf`, referenciado en el código).
3. **Integrador local `firmaautografa.com`** actúa de fachada para FaceTec: la app no llama a
   FaceTec directo sino a través de ese proveedor de firma electrónica.
4. **Biometría propia de IMSS (BMT)**: además del KYC de terceros, IMSS tiene su propio
   enrolamiento (`msbmt-enroll`) con captura de selfie y validación OTP por SMS (doc 06).
