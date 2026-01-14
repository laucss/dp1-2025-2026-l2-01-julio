# Plan de Pruebas

**Asignatura:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Curso académico:** 2025/2026 
**Grupo/Equipo:** L2-01  
**Nombre del proyecto:** Escape From Elba 
**Repositorio:** [<!-- URL del repo -->  ](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01)
**Integrantes (máx. 6):** 
Lucía Baltasar Muñoz (SBJ4592 / lucbalmun@alum.us.es)<br>
Nerea Camacho Perez (QFL3393 / nercamper@alum.us.es)<br>
Laura Cubero Sánchez (XNT3290 / laucubsan@alum.us.es)<br>
Alberto Pardina Miñón (QSS7721 / albparmin@alum.us.es)<br>
Marco Visentin Lopez (CYB6650 / marvislop@alum.us.es)<br>
Emilio Diaz Arcenegui (FSS8078 / emidiaarc@alum.us.es)<br>



## 1. Introducción

Este documento describe el plan de pruebas para el proyecto Escape From Elba desarrollado en el marco de la asignatura Diseño y Pruebas 1 por el grupo L2-01. El objetivo del plan de pruebas es garantizar que el software desarrollado cumple con los requisitos especificados en las historias de usuario y que se han realizado las pruebas necesarias para validar su funcionamiento.

## 2. Alcance

El alcance de este plan de pruebas incluye:

- Pruebas unitarias.
  - Pruebas unitarias de backend incluyendo pruebas servicios o repositorios
  - Pruebas unitarias de frontend: pruebas de las funciones javascript creadas en frontend.
  - Pruebas unitarias de interfaz de usuario. Usan la interfaz de  usuario de nuestros componentes frontend.
- Pruebas de integración.  En nuestro caso principalmente son pruebas de controladores que también se ejecutarán mediante JUnit.

## 3. Estrategia de Pruebas

### 3.1 Tipos de Pruebas

#### 3.1.1 Pruebas Unitarias
Las pruebas unitarias se realizarán para verificar el correcto funcionamiento de los componentes individuales del software. Se utilizarán herramientas de automatización de pruebas como **JUnit** en backend y jest en frontend.

#### 3.1.2 Pruebas de Integración
Las pruebas de integración se enfocarán en evaluar la interacción entre los distintos módulos o componentes del sistema, nosotros las realizaremos a nivel de API, probando nuestros controladores Spring.

## 4. Herramientas y Entorno de Pruebas

### 4.1 Herramientas
- **Maven**: Gestión de dependencias y ejecución de las pruebas.
- **JUnit**: Framework de pruebas unitarias.
- **Jacoco**: Generación de informes de cobertura de código. Si se ejecuta el comando de maven install, se copiará el informe de cobertura a la subcarpeta del repositorio /docs/deliverables/D3/coverage (puede visualizarse pulsando en el fichero index.html de dicho directorio).
- **Allure**: Generación de informes de estado de las últimas ejecuciones de las pruebas. Permite agrupar las pruebas por módulo/épica y feature. Si se ejecuta el comando de maven install, se copiará el informe de estado a la subcarpeta del repositorio /docs/deliverables/D3/status (puede visualizarse pulsando en el fichero index.html de dicho directorio).
- **Jest**: Framework para pruebas unitarias en javascript.
- **React-test**: Librería para la creación de pruebas unitarias de componentes React.

### 4.2 Entorno de Pruebas
Las pruebas se ejecutarán en el entorno de desarrollo y, eventualmente, en el entorno de pruebas del servidor de integración continua.

## 5. Planificación de Pruebas
### 5.1 Estado y trazadibilidad de Pruebas por Módulo y Épica

El informe de estado de las pruebas (con trazabilidad de éstas hacia los módulos y las épicas/historias de usaurio) se encuentra [aquí](
https://gii-is-dp1.github.io/group-project-seed/deliverables/D3/status/#behaviors).

### 5.2 Cobertura de Pruebas

El informe de cobertura de pruebas se puede consultar [aquí](
https://gii-is-dp1.github.io/group-project-seed/deliverables/D3/coverage/).



*Nota importante para el alumno*: A la hora de entregar el proyecto, debes modificar la url para que esté asociada al respositorio concreto de tu proyecto. Date cuenta de que ahora mismo apunta al repositorio _gii-is-DP1/group-project-seed_.


| Historia de Usuario | Prueba | Descripción | Estado |Tipo |
|---------------------|--------|-------------|--------|--------|

| HU-01: Iniciar sesión | [UTB-AUTH-01: authenticateUserBadCredentialsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que el sistema devuelve un error cuando un usuario intenta iniciar sesión con credenciales incorrectas. | Implementada | Integración backend – Controlador |
| HU-01: Iniciar sesión | [UTB-AUTH-02: authenticateUserSuccessTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un usuario puede iniciar sesión correctamente con credenciales válidas y se genera un JWT. | Implementada | Integración backend – Controlador |
| HU-01: Iniciar sesión | [UTB-AUTH-03: validateTokenValidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un token JWT válido es aceptado por el sistema. | Implementada | Integración backend – Controlador |
| HU-01: Iniciar sesión | [UTB-AUTH-04: validateTokenInvalidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un token JWT inválido es procesado correctamente por el sistema. | Implementada | Integración backend – Controlador |
| HU-02: Registrar usuario | [UTB-AUTH-05: registerUserUsernameExistsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que no se permite el registro si el nombre de usuario ya existe. | Implementada | Integración backend – Controlador |
| HU-02: Registrar usuario | [UTB-AUTH-06: registerUserEmailExistsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que no se permite el registro si el correo electrónico ya existe. | Implementada | Integración backend – Controlador |

| HU-02: Registrar usuario | [UTB-AUTH-07: createUserWithAdminRoleSavesUser](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthServiceTests.java) | Verifica que al registrar un usuario con rol administrador se crea y guarda correctamente con la autoridad ADMIN y la contraseña cifrada. | Implementada | Unitaria backend – Servicio |
| HU-02: Registrar usuario | [UTB-AUTH-08: createUserWithDefaultRoleSavesUserAsPlayer](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthServiceTests.java) | Verifica que al registrar un usuario sin rol administrador se le asigna por defecto el rol PLAYER y se guarda correctamente. | Implementada | Unitaria backend – Servicio |

| HU-XX: Gestión de cartas | [UTB-CARD-01: findAllWhenNoCardsReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el repositorio de cartas devuelve una lista vacía cuando no existen cartas almacenadas. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de cartas | [UTB-CARD-02: findByIdNonExistentReturnsEmptyOptional](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que la búsqueda de una carta inexistente devuelve un Optional vacío. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de cartas | [UTB-CARD-03: saveCardAndFindByIdReturnsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que una carta se guarda correctamente y puede recuperarse por su identificador. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de cartas | [UTB-CARD-04: findAllReturnsAllSavedCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el repositorio devuelve todas las cartas almacenadas. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de cartas | [UTB-CARD-05: cloneCardReturnsEqualButDifferentObject](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el método de clonación de una carta devuelve un objeto distinto pero con los mismos atributos. | Implementada | Unitaria backend – Modelo |

| HU-XX: Validación de palabras | [UTB-DICT-01: containsWordReturnsTrueForExistingWord](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio identifica correctamente palabras existentes en el diccionario, independientemente del uso de mayúsculas o minúsculas. | Implementada | Unitaria backend – Servicio |
| HU-XX: Validación de palabras | [UTB-DICT-02: containsWordReturnsFalseForNonExistingWord](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio devuelve falso para palabras que no existen en el diccionario. | Implementada | Unitaria backend – Servicio |
| HU-XX: Identificación de armas | [UTB-DICT-03: isWeaponReturnsTrueForWeapon](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio identifica correctamente palabras correspondientes a armas, sin distinguir mayúsculas o minúsculas. | Implementada | Unitaria backend – Servicio |
| HU-XX: Identificación de armas | [UTB-DICT-04: isWeaponReturnsFalseForNonWeapon](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio devuelve falso para palabras que no corresponden a armas. | Implementada | Unitaria backend – Servicio |


| HU-XX: Gestión de la baraja | [UTB-DECK-01: initializeDeckCreatesDeckAndStoresIt](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se inicializa correctamente una baraja para una partida, clonando las cartas y almacenándola como activa. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-02: findDeckByIdExistingDeckReturnsDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se recupera correctamente una baraja existente a partir de su identificador. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-03: findDeckByIdNotExistingReturnsEmptyDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al solicitar una baraja inexistente se devuelve una baraja vacía. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-04: deleteDeckInGameRemovesDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que una baraja activa se elimina correctamente del sistema. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-05: drawCardRemovesLastCardFromDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al robar una carta esta se elimina del mazo y se devuelve correctamente. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-06: addCardToDiscardedPileAddsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que una carta se añade correctamente al montón de descartes, comprobando previamente su validez. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-07: getAndRemoveLastDiscardedCardReturnsAndRemovesCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se obtiene y elimina correctamente la última carta descartada cuando existe. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-08: getAndRemoveLastDiscardedCardEmptyReturnsNull](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al intentar obtener una carta descartada cuando no existen se devuelve null. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-09: isEmptyReturnsTrueWhenNoCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que el sistema identifica correctamente una baraja vacía. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la baraja | [UTB-DECK-10: drawInitialCardsFromDeckReturnsThreeCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se roban correctamente las cartas iniciales de la baraja y se eliminan del mazo. | Implementada | Unitaria backend – Servicio |


| HU-XX: Gestión de la mano | [UTB-HAND-01: createPlayerHandCreatesHandIfNotExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se crea correctamente la mano de un jugador cuando no existe previamente. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-02: createPlayerHandDoesNotOverrideExistingHand](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que no se sobrescribe una mano de jugador ya existente. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-03: deleteMatchHandsRemovesMatchEntry](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se eliminan todas las manos asociadas a una partida. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-04: findPlayerHandReturnsHandIfExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se recupera correctamente la mano de un jugador existente. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-05: findPlayerHandReturnsEmptyHandIfNotExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se devuelve una mano vacía cuando no existe previamente. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-06: addCardToPlayerHandAddsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que una carta se añade correctamente a la mano de un jugador. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-07: removeCardFromPlayerHandReturnsNullIfCardIsNull](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que no se elimina ninguna carta cuando la carta proporcionada es nula. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-08: removeCardFromPlayerHandRemovesCardByReference](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se elimina correctamente una carta de la mano utilizando la misma referencia. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-09: removeCardFromPlayerHandRemovesCardByLetter](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se elimina correctamente una carta de la mano comparando por su letra. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-10: addFewCardsToPlayerHandAddsMultipleCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se añaden correctamente varias cartas a la mano de un jugador. | Implementada | Unitaria backend – Servicio |
| HU-XX: Gestión de la mano | [UTB-HAND-11: updateReplacesPlayerHand](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que la mano del jugador se actualiza correctamente a partir de un DTO. | Implementada | Unitaria backend – Servicio |


| HU-XX: Chat en partida | [UTB-CHAT-01: getMyChatEmptyTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que la consulta del chat del jugador devuelve una lista vacía cuando no hay mensajes. | Implementada | Unitaria backend – Controlador aislado |
| HU-XX: Chat en partida | [UTB-CHAT-02: createChatMessageValidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que se puede crear un mensaje de chat válido y se guarda correctamente a través del servicio. | Implementada | Unitaria backend – Controlador aislado |
| HU-XX: Chat en partida | [UTB-CHAT-03: createChatMessageInvalidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que intentar crear un mensaje inválido (vacío) devuelve un error 400 y no llama al servicio. | Implementada | Unitaria backend – Controlador aislado |


| HU-XX: Chat en partida | [UTB-CHATREP-01: findByMatchIdNoChatsReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que al consultar chats de un match inexistente se devuelve lista vacía. | Implementada | Unitaria backend – Repositorio |
| HU-XX: Chat en partida | [UTB-CHATREP-02: findByMatchIdNoChatsParameterized](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que al consultar varios matchIds sin chats se devuelve lista vacía para cada uno. | Implementada | Unitaria backend – Repositorio |
| HU-XX: Chat en partida | [UTB-CHATREP-03: findByMatchIdSingleChatReturnsOneMessage](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que se devuelve correctamente un mensaje de chat asociado a un match y jugador. | Implementada | Unitaria backend – Repositorio |
| HU-XX: Chat en partida | [UTB-CHATREP-04: findByMatchIdMultipleChatsReturnsAllMessages](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que se devuelven todos los mensajes de un match con varios jugadores. | Implementada | Unitaria backend – Repositorio |


| HU-XX: Chat en partida | [UTB-CHATS-01: findChatOfMyGameNoUserReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que si no hay usuario autenticado, la función devuelve lista vacía. | Implementada | Unitaria backend – Servicio aislado |
| HU-XX: Chat en partida | [UTB-CHATS-02: findChatOfMyGameUserNotInMatchReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que si el usuario no participa en la partida, se devuelve lista vacía. | Implementada | Unitaria backend – Servicio aislado |
| HU-XX: Chat en partida | [UTB-CHATS-03: findChatOfMyGameHasMessagesReturnsSorted](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que los mensajes del chat de la partida se devuelven en orden cronológico. | Implementada | Unitaria backend – Servicio aislado |
| HU-XX: Chat en partida | [UTB-CHATS-04: createChatMessageNoUserThrows](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que intentar crear un mensaje sin usuario autenticado lanza excepción. | Implementada | Unitaria backend – Servicio aislado |
| HU-XX: Chat en partida | [UTB-CHATS-05: createChatMessageUserNotInMatchThrows](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que un usuario que no participa en la partida no puede crear mensaje (lanza excepción). | Implementada | Unitaria backend – Servicio aislado |
| HU-XX: Chat en partida | [UTB-CHATS-06: createChatMessageSuccess](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que un usuario válido puede crear un mensaje correctamente y se asocia al jugador. | Implementada | Unitaria backend – Servicio aislado |


| HU-XX: Gestión de NPCs | [UTB-NPC-01: findByIdNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que buscar un NPC por ID inexistente devuelve Optional.empty(). | Implementada | Unitaria backend – Repositorio |
| HU-XX: Gestión de NPCs | [UTB-NPC-02: findByIdReturnsNpc](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que se puede recuperar un NPC guardado correctamente por su ID. | Implementada | Unitaria backend – Repositorio |
| HU-XX: Gestión de NPCs | [UTB-NPC-03: findByIdAndMatchIdNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que buscar un NPC por ID y Match inexistentes devuelve Optional.empty() | Implementada | Unitaria backend – Repositorio |
| HU-XX: Gestión de NPCs | [UTB-NPC-04: findByIdAndMatchIdReturnsNpc](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que se puede recuperar un NPC guardado por ID y su Match asociado correctamente. | Implementada | Unitaria backend – Repositorio |
| HU-XX: Gestión de NPCs | [UTB-NPC-05: findByIdAndMatchIdWithRandomIdsReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que IDs aleatorios no devuelven ningún NPC. | Implementada | Unitaria backend – Repositorio |


| HU-XX: Gestión de jugadores | [ITB-PLAYER-01: findAllPlayersEmptyTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players devuelve lista vacía si no hay jugadores. | Implementada | Integración backend – Controlador |
| HU-XX: Gestión de jugadores | [ITB-PLAYER-02: findAllPlayersReturnsPlayersTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players devuelve la lista de jugadores existente. | Implementada | Integración backend – Controlador |
| HU-XX: Gestión de jugadores | [ITB-PLAYER-03: findAllByUserIdEmptyTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players/users/{userId} devuelve lista vacía si el usuario no tiene jugadores. | Implementada | Integración backend – Controlador |
| HU-XX: Gestión de jugadores | [ITB-PLAYER-04: findAllByUserIdReturnsPlayersTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players/users/{userId} devuelve la lista de jugadores del usuario. | Implementada | Integración backend – Controlador |
| HU-XX: Gestión de jugadores | [ITB-PLAYER-05: getPlayersByMatchIdEmptyTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players/matches/{matchId} devuelve lista vacía si no hay jugadores en la partida. | Implementada | Integración backend – Controlador |
| HU-XX: Gestión de jugadores | [ITB-PLAYER-06: getPlayersByMatchIdReturnsPlayersTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerControllerTests.java) | Verifica que la petición GET /players/matches/{matchId} devuelve la lista de jugadores de la partida. | Implementada | Integración backend – Controlador |


| HU-XX: Gestión de jugadores | [IR-PLAYER-01: findByIdNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar un jugador por un ID inexistente devuelve Optional.empty(). | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-02: findByUserIdNoPlayersReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar jugadores por un usuario sin jugadores devuelve lista vacía. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-03: findByUserIdReturnsPlayers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar jugadores por un usuario con jugadores devuelve la lista correcta. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-04: findByMatchAndUserNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar jugador por partida y usuario inexistentes devuelve Optional.empty(). | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-05: findByMatchAndUserReturnsPlayer](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar jugador por partida y usuario existentes devuelve el jugador correcto. | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-06: findByMatchIdNoPlayersReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que buscar jugadores por partida inexistente devuelve lista vacía (parametrizado). | Implementada | Integración backend – Repositorio |
| HU-XX: Gestión de jugadores | [IR-PLAYER-07: getTotalAccionPointsByUserReturnsSum](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerRepositoryTests.java) | Verifica que sumar los puntos de acción de todos los jugadores de un usuario devuelve el valor correcto. | Implementada | Integración backend – Repositorio |


| HU-XX: Gestión de jugadores | [US-PLAYER-01: findAllReturnsPlayers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findAll() devuelve la lista de jugadores del repositorio. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-02: findByIdExistingReturnsPlayer](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findById(id) devuelve el jugador si existe. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-03: findByIdNonExistingThrowsException](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findById(id) lanza ResourceNotFoundException si el jugador no existe. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-04: findByUserIdReturnsPlayers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findByUserId(userId) devuelve la lista de jugadores de un usuario. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-05: findByMatchIdAndUserIdReturnsOptional](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findByMatchIdAndUserId(matchId, userId) devuelve un Optional<Player> correcto. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-06: savePlayerReturnsSavedPlayer](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que save(player) guarda y devuelve el jugador. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-07: deleteByIdCallsRepository](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que deleteById(id) llama al repositorio correctamente. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-08: getPlayersByMatchIdReturnsPlayers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que getPlayersByMatchId(matchId) devuelve los jugadores de una partida. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-09: removePlayerActionPointSuccess](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que removePlayerActionPoint(matchId, playerId) decrementa correctamente los puntos de acción. | Implementada | Unit – Servicio |
| HU-XX: Gestión de jugadores | [US-PLAYER-10: removePlayerActionPointNoPlayerDoesNothing](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que removePlayerActionPoint() no hace nada si el jugador no existe. | Implementada | Unit – Servicio |


## 6. Criterios de Aceptación

- Todas las pruebas unitarias deben pasar con éxito antes de la entrega final del proyecto.
- La cobertura de código debe ser al menos del 70%.
- No debe haber fallos críticos en las pruebas de integración y en la funcionalidad.

## 7. Conclusión

Este plan de pruebas establece la estructura y los criterios para asegurar la calidad del software desarrollado. Es responsabilidad del equipo de desarrollo y pruebas seguir este plan para garantizar la entrega de un producto funcional y libre de errores.
