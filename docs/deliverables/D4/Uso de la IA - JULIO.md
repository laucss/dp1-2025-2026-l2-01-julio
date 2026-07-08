# Documentación del Uso de IA en el Proyecto
**Asignatura:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla) 

**Curso académico:** 2025-2026

**Grupo/Equipo:** L2-01

**Nombre del proyecto:** Escape From Elba

**Repositorio:** https://github.com/laucss/dp1-2025-2026-l2-01-julio

**Integrantes (máx. 6):**

Nerea Camacho Pérez (QFL3393 / nercamper@alum.us.es)<br>
Laura Cubero Sánchez (XNT3290 / laucubsan@alum.us.es)<br>
Lucía Baltasar Muñoz (SBJ4592 / lucbalmun@alum.us.es)<br>

## 1. Introducción

Este documento describe el uso que se ha echo de la IA en el proyecto. El objetivo es ser transparentes sobre el uso de IA realizado. Como recordatorio, al alumnado incluimos un resumen de lo indicado en el Syllabus de la asignatura:

### Declaración de Política y Compromiso

> **Principios guía generales (resumen):**  
> - **Dimensión Cognitiva:** El trabajo con IA **no** debe reducir su capacidad de pensar con claridad; úsela para **facilitar**—no obstaculizar—el aprendizaje.  
> - **Dimensión Ética :** La utilización de IA debe ser **transparente** y **alineada** con la integridad académica.

**Normas específicas de la asignatura:**
- ✅ **IA para código:** Se permite usar tecnología generativa para **completar o generar ejemplos de código** en las tareas, pero **debe citarse explícitamente** la procedencia del mismo. Así mismo el alumno debe **entender** y poder **modificar bajo demanda** cualquier código entregado, siendo el responsable de cualquier comportamiento del código que ha conmitado, ante el profesor y sus compañeros. Recuerde: **Usted es responsable** de dicho código.
- ❌ **IA para narrativa:** Salvo indicación en contrario, **no** se permite usar IA generativa para **redactar narrativa** de las entregas. Se puede usar como **recurso** durante el proceso, **no** para **responder por usted** a los ejercicios.

**Marco ético US:** Consulte y cumpla lo indicado en **Guías de Ética e IA** de la US: https://guiasbus.us.es/ia/etica

**Rellenar este documento es Obligatorio:** La **documentación del uso de IA** es un **entregable** del proyecto.

## Resumen por Sprint 
### Sprint de Julio — Resumen de uso de IA

Usos registrados: 3

Ámbitos principales: Refactorizaciones, correciones de errores generales, elaboración de css. 

Valor aportado: Resolución de errores y dudas junto con el aprendizaje aportado.

Riesgos relevantes y mitigaciones: Información incorrecta o poco precisa que suponga errores o exposición de datos.

Lecciones aprendidas: Utilización de la IA como herramienta complementaria para orientarnos en la resolución de problemas y extracción de datos útiles para el desarrollo de nuestro proyecto de manera que se asegure el aprendizaje.

Checklist de cumplimiento de uso ético de la IA del sprint de julio:

- [x] Toda interacción significativa está en el Registro Detallado con enlace a conversación.

- [x] No se usó IA para narrativa (o hay autorización documentada).

- [x] Toda pieza aceptada fue comprendida y verificada por humanos (tests/revisión).

- [x] Citas/Atribuciones incluidas cuando corresponde.

- [x] Se usó la IA sin dar datos personales/sensibles que puedieran quedar expuestos a herramientas externas.

## Registro detallado de uso de AI por Sprint


### Sprint de julio registro detallado de uso de IA por sprint

| # | Fecha y hora | Sprint | Integrante(s) | **Herramienta & versión** | **Acceso** | **Enlace a conversación / Prompt** | **Finalidad** | **Artefactos afectados** | **Verificación humana** | **Riesgos & mitigaciones** | **Resultado** |
|---:|--------------|:-----:|---------------|----------------------------|------------|------------------------------------|---------------|---------------------------|--------------------------|-----------------------------|---------------|
| 1.1 | 29/06/2026 | 1 | Laura Cubero Sánchez | Gemini 3.5 Flash | Web | https://gemini.google.com/u/2/app/f0b2ffaa14728428?is_sa=1&is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&utm_source=sem&utm_source=google&utm_medium=paid-media&utm_medium=cpc&utm_campaign=bkws&utm_campaign=2024esES_gemfeb&pt=9008&mt=8&ct=p-growth-sem-bkws&gclsrc=aw.ds&gad_source=1&gad_campaignid=20437330500&gbraid=0AAAAApk5Bhl-0m5k_34IbMyPo008nGJMz&gclid=Cj0KCQiA_8TJBhDNARIsAPX5qxQCdbQOXvpPO8PXRBnXU6FxJKwJ-hnAfiV75MHR2mNZVN7ScGhsmEAaAhavEALw_wcB&pageId=none  |Arreglo de fallo: delete user siendo administrador (Depuración / Diagnóstico) | UserService.java | Comparación con repositorios del hall of fame | Sin riesgos ni mitigaciones | Aceptado |

| 1.2 | 30/06/2026 | 1 | Laura Cubero Sánchez | Gemini 3.5 Flash | Web | https://gemini.google.com/u/2/app/18cd840cb4774817?is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&utm_source=google&utm_medium=cpc&utm_campaign=2024esES_gemfeb&pt=9008&mt=8&ct=p-growth-sem-bkws&gclsrc=aw.ds&gad_source=1&gad_campaignid=20437330500&gbraid=0AAAAApk5Bhl-0m5k_34IbMyPo008nGJMz&gclid=Cj0KCQiA_8TJBhDNARIsAPX5qxQCdbQOXvpPO8PXRBnXU6FxJKwJ-hnAfiV75MHR2mNZVN7ScGhsmEAaAhavEALw_wcB&pageId=none  | Decisión de cómo implementar spectator (Idea/Exploración) | Match.java, MatchService.java, MatchController.java, PlayerMatch.js, Spectator.js, Match.js | Comparación con repositorios del hall of fame | Sin riesgos ni mitigaciones | Aceptado parcialmente, se aceptó aquella que se consideró más apropiada dada la situación del proyecto en dicho momento |

| 1.3 | 07/07/2026 | 1 | Laura Cubero Sánchez | Gemini 3.5 Flash | Web | https://gemini.google.com/u/2/app/b306e6e3749a65d3?is_sa=1&is_sa=1&android-min-version=301356232&ios-min-version=322.0&campaign_id=bkws&utm_source=sem&utm_source=google&utm_medium=paid-media&utm_medium=cpc&utm_campaign=bkws&utm_campaign=2024esES_gemfeb&pt=9008&mt=8&ct=p-growth-sem-bkws&gclsrc=aw.ds&gad_source=1&gad_campaignid=20437330500&gbraid=0AAAAApk5Bhl-0m5k_34IbMyPo008nGJMz&gclid=Cj0KCQiA_8TJBhDNARIsAPX5qxQCdbQOXvpPO8PXRBnXU6FxJKwJ-hnAfiV75MHR2mNZVN7ScGhsmEAaAhavEALw_wcB&pageId=none  | Error concadenación de peleas en una partida (Refactorización, Generación de Código funcional) | MatchService.java, MatchController.java, PlayerMatch.js, ActionsService.java, ActionsController.java| Se preguntaba por ideas u opciones pensadas por el alunmno y se aceptaba la más conveniente ante todo el contexto del sistema | Se realizaron muchas refactorizaciones de modulos muy grandes y formas de llevar a cabo muchas funciones y acciones, podía romperse lo que ya funcionaba | Aceptado parcialmente, se aceptó aquella que se consideró más apropiada dada la situación del proyecto en dicho momento |


| 1.4 | 08/07/2026 | 1 | Lucía Baltasar Muñoz | ChatGPT 5.5 | Web | Conversación de ChatGPT | Diagnóstico de un error `Failed to load ApplicationContext` en un test de Spring Boot (Depuración / Diagnóstico) | `FriendRequestRepositoryTest.java` y posibles clases relacionadas (`FriendRequest`, repositorios, entidades y configuración de Spring) | Se proporcionó el stacktrace del error y se verificó que el mensaje mostrado correspondía únicamente al síntoma, identificando la necesidad de revisar la excepción raíz (`Caused by`) para localizar el origen real del fallo. | Existía el riesgo de diagnosticar incorrectamente el problema al tratarse de un error genérico. Se mitigó indicando cómo localizar la excepción raíz y las causas más habituales (creación de beans, configuración JPA, conexión con la base de datos o mapeo de entidades). | Aceptado como guía de diagnóstico para continuar la depuración del problema. |



## Conclusiones finales sobre el uso de la IA en el proyecto
Se ha utilizado la IA, para valorar qué opciones habían dadas las circunstancias concretas dadas y cuáles se consideraban más prácticas. Ha ayudado a detectar y solventar muchos fallos y errores, sobre todo en la interpretación de los mensajes de errror. En general resulta muy útil para conocer diferentes formas de abordar un problema, con sus inconvenientes y beneficios. Sin embargo también da respuestas muy erroneas que a veces puede provocar la pérdida considerable de tiempo al no ir con el enfoque correcto, ya que es normal pues no se les puede proporcionar el contexto global.


## Anexo A) Inventario de Herramientas de IA
|Herramienta|Versión/Modelo|Proveedor|Acceso (web/plugin/API)| Licencia/Plan | Observaciones|
|-----------|--------------|---------|-----------------------|---------------|--------------|
|Gemini|Gemini 1.5 Pro / Flash|Google|Web|Version aportada por la universidad|<!-- ... -->|

## Anexo B) Glosario de Finalidades

Idea/Exploración · Generación de Código funcional · Depuración / Diagnóstico · Generación de pruebas (unitarias/integración/e2e) · Diseño técnico · Documentación técnica (no narrativa) ·  Refactorización.