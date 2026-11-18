# Arquitectura — lector nativo y catálogo federado

## Pila

- **Nativa Android (Kotlin/Java).** 1 `classes.dex`; paquete propio
  `gob.mx.conaliteg.bibliotecadigital` con subpaquetes `adapters/`, `model/`, `views/`
  (patrón MVC). Sin Flutter (`flutter_assets`), sin Unity (`il2cpp`), sin .NET (`assemblies`),
  sin bundle web (`assets/www`) → es una app Android convencional, **decompilable**.
- Firebase Analytics (`app-measurement.com`) para telemetría de uso.

## Función

Lector/agregador de **biblioteca digital** de la CONALITEG: libros de texto gratuitos y
contenidos educativos. El catálogo **federa** fuentes propias y de terceros (doc 00):

- **Propias CONALITEG:** `api`, `middleware`, `libros`, `secundaria`, `milesdehistorias`
  (todas `*.conaliteg.gob.mx`).
- **Editoriales / universidades:** FCE, UPN, UNAM, Grupo SM, Trillas, Cervantes Virtual,
  Academia.org, Fonoteca Nacional (dominios listados en el NSC, doc 03).

La lógica es legible en el DEX (MVC), pero no se decompiló clase por clase porque el valor del
análisis está en la **configuración de red** (doc 03), que es donde aparece la debilidad. Se
documenta la arquitectura y los backends; el recorrido función por función de la UI de lectura
no aporta hallazgos de seguridad adicionales.
