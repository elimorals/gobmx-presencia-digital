# Manifiesto — permisos y componentes

Fuente: `AndroidManifest.xml` decodificado. versionName 1.25, compileSdk 36.

## Permisos

App de lectura/biblioteca → permisos mínimos: `INTERNET`, `ACCESS_NETWORK_STATE`,
notificaciones (FCM) y almacenamiento para cachear/descargar libros. No pide cámara, micrófono,
ubicación, contactos ni teléfono. Higiene de permisos **buena** (coherente con un lector).

> El detalle exacto de permisos está en `AndroidManifest.xml`; la app no declara permisos
> peligrosos ni `signature|privileged`.

## Componentes

- Estructura MVC nativa propia: `gob.mx.conaliteg.bibliotecadigital.{adapters,model,views}`
  (confirmado en el DEX). Activities de lectura/listado.
- Firebase (FCM + Analytics, `app-measurement.com`) estándar.

## Flag clave: `networkSecurityConfig`

A diferencia de la mayoría del lote, CONALITEG **sí** declara un
`android:networkSecurityConfig` por recurso (`res/xml/network_security_config.xml`), y **ahí
está el problema**: habilita cleartext y confía en CAs de usuario (doc 03). El manifiesto apunta
a esa config; el análisis de red se centra en ella.
