# Plan de Pruebas

**Asignatura:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Curso académico:** 2025/2026 
**Grupo/Equipo:** Extra Group 1  
**Nombre del proyecto:** Escape From Elba 
**Repositorio:** [<!-- URL del repo -->  ](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01)
**Integrantes (máx. 6):** <br>
Lucía Baltasar Muñoz (SBJ4592 / lucbalmun@alum.us.es)<br>
Nerea Camacho Perez (QFL3393 / nercamper@alum.us.es)<br>
Laura Cubero Sánchez (XNT3290 / laucubsan@alum.us.es)<br>


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
| Iniciar sesión | [UTB-AUTH-01: authenticateUserBadCredentialsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que el sistema devuelve un error cuando un usuario intenta iniciar sesión con credenciales incorrectas. | Implementada | Integración backend – Controlador |
| Iniciar sesión | [UTB-AUTH-02: authenticateUserSuccessTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un usuario puede iniciar sesión correctamente con credenciales válidas y se genera un JWT. | Implementada | Integración backend – Controlador |
| Iniciar sesión | [UTB-AUTH-03: validateTokenValidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un token JWT válido es aceptado por el sistema. | Implementada | Integración backend – Controlador |
| Iniciar sesión | [UTB-AUTH-04: validateTokenInvalidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que un token JWT inválido es procesado correctamente por el sistema. | Implementada | Integración backend – Controlador |
| Registrar usuario | [UTB-AUTH-05: registerUserUsernameExistsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que no se permite el registro si el nombre de usuario ya existe. | Implementada | Integración backend – Controlador |
| Registrar usuario | [UTB-AUTH-06: registerUserEmailExistsTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthControllerTests.java) | Verifica que no se permite el registro si el correo electrónico ya existe. | Implementada | Integración backend – Controlador |
| Registrar usuario | [UTB-AUTH-07: createUserWithAdminRoleSavesUser](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthServiceTests.java) | Verifica que al registrar un usuario con rol administrador se crea y guarda correctamente con la autoridad ADMIN y la contraseña cifrada. | Implementada | Unitaria backend – Servicio |
| Registrar usuario | [UTB-AUTH-08: createUserWithDefaultRoleSavesUserAsPlayer](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/auth/AuthServiceTests.java) | Verifica que al registrar un usuario sin rol administrador se le asigna por defecto el rol PLAYER y se guarda correctamente. | Implementada | Unitaria backend – Servicio |
| Gestión de cartas | [UTB-CARD-01: findAllWhenNoCardsReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el repositorio de cartas devuelve una lista vacía cuando no existen cartas almacenadas. | Implementada | Integración backend – Repositorio |
| Gestión de cartas | [UTB-CARD-02: findByIdNonExistentReturnsEmptyOptional](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que la búsqueda de una carta inexistente devuelve un Optional vacío. | Implementada | Integración backend – Repositorio |
| Gestión de cartas | [UTB-CARD-03: saveCardAndFindByIdReturnsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que una carta se guarda correctamente y puede recuperarse por su identificador. | Implementada | Integración backend – Repositorio |
| Gestión de cartas | [UTB-CARD-04: findAllReturnsAllSavedCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el repositorio devuelve todas las cartas almacenadas. | Implementada | Integración backend – Repositorio |
| Gestión de cartas | [UTB-CARD-05: cloneCardReturnsEqualButDifferentObject](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/CardRepositoryTests.java) | Verifica que el método de clonación de una carta devuelve un objeto distinto pero con los mismos atributos. | Implementada | Unitaria backend – Modelo |
| Validación de palabras | [UTB-DICT-01: containsWordReturnsTrueForExistingWord](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio identifica correctamente palabras existentes en el diccionario, independientemente del uso de mayúsculas o minúsculas. | Implementada | Unitaria backend – Servicio |
| Validación de palabras | [UTB-DICT-02: containsWordReturnsFalseForNonExistingWord](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio devuelve falso para palabras que no existen en el diccionario. | Implementada | Unitaria backend – Servicio |
| Identificación de armas | [UTB-DICT-03: isWeaponReturnsTrueForWeapon](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio identifica correctamente palabras correspondientes a armas, sin distinguir mayúsculas o minúsculas. | Implementada | Unitaria backend – Servicio |
| Identificación de armas | [UTB-DICT-04: isWeaponReturnsFalseForNonWeapon](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/card/DictionaryServiceTests.java) | Verifica que el servicio devuelve falso para palabras que no corresponden a armas. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-01: initializeDeckCreatesDeckAndStoresIt](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se inicializa correctamente una baraja para una partida, clonando las cartas y almacenándola como activa. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-02: findDeckByIdExistingDeckReturnsDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se recupera correctamente una baraja existente a partir de su identificador. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-03: findDeckByIdNotExistingReturnsEmptyDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al solicitar una baraja inexistente se devuelve una baraja vacía. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-04: deleteDeckInGameRemovesDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que una baraja activa se elimina correctamente del sistema. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-05: drawCardRemovesLastCardFromDeck](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al robar una carta esta se elimina del mazo y se devuelve correctamente. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-06: addCardToDiscardedPileAddsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que una carta se añade correctamente al montón de descartes, comprobando previamente su validez. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-07: getAndRemoveLastDiscardedCardReturnsAndRemovesCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se obtiene y elimina correctamente la última carta descartada cuando existe. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-08: getAndRemoveLastDiscardedCardEmptyReturnsNull](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que al intentar obtener una carta descartada cuando no existen se devuelve null. | Implementada | Unitaria backend – Servicio |
| HGestión de la baraja | [UTB-DECK-09: isEmptyReturnsTrueWhenNoCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que el sistema identifica correctamente una baraja vacía. | Implementada | Unitaria backend – Servicio |
| Gestión de la baraja | [UTB-DECK-10: drawInitialCardsFromDeckReturnsThreeCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/deck/DeckServiceTests.java) | Verifica que se roban correctamente las cartas iniciales de la baraja y se eliminan del mazo. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-01: createPlayerHandCreatesHandIfNotExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se crea correctamente la mano de un jugador cuando no existe previamente. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-02: createPlayerHandDoesNotOverrideExistingHand](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que no se sobrescribe una mano de jugador ya existente. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-03: deleteMatchHandsRemovesMatchEntry](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se eliminan todas las manos asociadas a una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-04: findPlayerHandReturnsHandIfExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se recupera correctamente la mano de un jugador existente. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-05: findPlayerHandReturnsEmptyHandIfNotExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se devuelve una mano vacía cuando no existe previamente. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-06: addCardToPlayerHandAddsCard](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que una carta se añade correctamente a la mano de un jugador. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-07: removeCardFromPlayerHandReturnsNullIfCardIsNull](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que no se elimina ninguna carta cuando la carta proporcionada es nula. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-08: removeCardFromPlayerHandRemovesCardByReference](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se elimina correctamente una carta de la mano utilizando la misma referencia. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-09: removeCardFromPlayerHandRemovesCardByLetter](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se elimina correctamente una carta de la mano comparando por su letra. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-10: addFewCardsToPlayerHandAddsMultipleCards](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que se añaden correctamente varias cartas a la mano de un jugador. | Implementada | Unitaria backend – Servicio |
| Gestión de la mano | [UTB-HAND-11: updateReplacesPlayerHand](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/cards/hand/HandServiceTests.java) | Verifica que la mano del jugador se actualiza correctamente a partir de un DTO. | Implementada | Unitaria backend – Servicio |
| Chat en partida | [UTB-CHAT-01: getMyChatEmptyTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que la consulta del chat del jugador devuelve una lista vacía cuando no hay mensajes. | Implementada | Unitaria backend – Controlador aislado |
| Chat en partida | [UTB-CHAT-02: createChatMessageValidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que se puede crear un mensaje de chat válido y se guarda correctamente a través del servicio. | Implementada | Unitaria backend – Controlador aislado |
| Chat en partida | [UTB-CHAT-03: createChatMessageInvalidTest](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatControllerTests.java) | Verifica que intentar crear un mensaje inválido (vacío) devuelve un error 400 y no llama al servicio. | Implementada | Unitaria backend – Controlador aislado |
| Chat en partida | [UTB-CHATREP-01: findByMatchIdNoChatsReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que al consultar chats de un match inexistente se devuelve lista vacía. | Implementada | Unitaria backend – Repositorio |
| Chat en partida | [UTB-CHATREP-02: findByMatchIdNoChatsParameterized](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que al consultar varios matchIds sin chats se devuelve lista vacía para cada uno. | Implementada | Unitaria backend – Repositorio |
| Chat en partida | [UTB-CHATREP-03: findByMatchIdSingleChatReturnsOneMessage](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que se devuelve correctamente un mensaje de chat asociado a un match y jugador. | Implementada | Unitaria backend – Repositorio |
| Chat en partida | [UTB-CHATREP-04: findByMatchIdMultipleChatsReturnsAllMessages](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatRepositoryTests.java) | Verifica que se devuelven todos los mensajes de un match con varios jugadores. | Implementada | Unitaria backend – Repositorio |
| Chat en partida | [UTB-CHATS-01: findChatOfMyGameNoUserReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que si no hay usuario autenticado, la función devuelve lista vacía. | Implementada | Unitaria backend – Servicio aislado |
| Chat en partida | [UTB-CHATS-02: findChatOfMyGameUserNotInMatchReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que si el usuario no participa en la partida, se devuelve lista vacía. | Implementada | Unitaria backend – Servicio aislado |
| Chat en partida | [UTB-CHATS-03: findChatOfMyGameHasMessagesReturnsSorted](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que los mensajes del chat de la partida se devuelven en orden cronológico. | Implementada | Unitaria backend – Servicio aislado |
| Chat en partida | [UTB-CHATS-04: createChatMessageNoUserThrows](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que intentar crear un mensaje sin usuario autenticado lanza excepción. | Implementada | Unitaria backend – Servicio aislado |
| Chat en partida | [UTB-CHATS-05: createChatMessageUserNotInMatchThrows](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que un usuario que no participa en la partida no puede crear mensaje (lanza excepción). | Implementada | Unitaria backend – Servicio aislado |
| Chat en partida | [UTB-CHATS-06: createChatMessageSuccess](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/chat/ChatServiceTests.java) | Verifica que un usuario válido puede crear un mensaje correctamente y se asocia al jugador. | Implementada | Unitaria backend – Servicio aislado |
| Gestión de NPCs | [UTB-NPC-01: findByIdNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que buscar un NPC por ID inexistente devuelve Optional.empty(). | Implementada | Unitaria backend – Repositorio |
| Gestión de NPCs | [UTB-NPC-02: findByIdReturnsNpc](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que se puede recuperar un NPC guardado correctamente por su ID. | Implementada | Unitaria backend – Repositorio |
| Gestión de NPCs | [UTB-NPC-03: findByIdAndMatchIdNonExistingReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que buscar un NPC por ID y Match inexistentes devuelve Optional.empty() | Implementada | Unitaria backend – Repositorio |
| Gestión de NPCs | [UTB-NPC-04: findByIdAndMatchIdReturnsNpc](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que se puede recuperar un NPC guardado por ID y su Match asociado correctamente. | Implementada | Unitaria backend – Repositorio |
| Gestión de NPCs | [UTB-NPC-05: findByIdAndMatchIdWithRandomIdsReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/npc/NpcRepositoryTests.java) | Verifica que IDs aleatorios no devuelven ningún NPC. | Implementada | Unitaria backend – Repositorio |
| Gestión de jugadores | [US-PLAYER-05: findByMatchIdAndUserIdReturnsOptional](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/player/PlayerServiceTests.java) | Verifica que findByMatchIdAndUserId(matchId, userId) devuelve un Optional<Player> correcto. | Implementada | Unit – Servicio |
| Gestión de usuarios | [UTB-USER-CTRL-06: shouldCreateUser](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/user/UserControllerTests.java) | Verifica que un administrador puede crear un nuevo usuario correctamente. | Implementada | Integración backend – Controlador |
| Gestión de usuarios | [UTB-USER-SERV-05: shouldFindUsersByUsername](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/user/UserServiceTests.java) | Verifica que se puede recuperar un usuario existente por su nombre de usuario. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-SERV-01: findAllRoomsReturnsEmptyListInitially](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomServiceTests.java) | Verifica que el sistema devuelve una lista vacía de salas cuando no existe ninguna registrada. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-SERV-02: findByNameNoRoomReturnsEmptyOptional](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomServiceTests.java) | Verifica que la búsqueda de una sala por nombre inexistente devuelve un resultado vacío. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-SERV-03: saveRoomAndRetrieveById](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomServiceTests.java) | Verifica que una sala se guarda correctamente y puede recuperarse por su identificador. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-SERV-04: saveRoomAndRetrieveByDices](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomServiceTests.java) | Verifica que una sala puede recuperarse correctamente a partir de la combinación de dados negro y blanco. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-SERV-05: findAllRoomsMultipleRoomsReturnsAll](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomServiceTests.java) | Verifica que el sistema devuelve todas las salas registradas cuando existen varias. | Implementada | Integración backend – Servicio |
| Gestión de salas | [UTB-ROOM-REP-01: findByNameNoRoomReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que la búsqueda de una sala por nombre inexistente devuelve un resultado vacío. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-REP-02: findByNameParameterized](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que la búsqueda parametrizada de salas por nombre inexistente devuelve un resultado vacío. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-REP-03: saveAndFindByNameReturnsRoom](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que una sala se guarda correctamente y puede recuperarse por su nombre. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-REP-04: findByDicesReturnsCorrectRoom](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que una sala se recupera correctamente a partir de la combinación de dados negro y blanco. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-REP-05: findAllReturnsAllRooms](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que el repositorio devuelve todas las salas almacenadas. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-REP-06: findByIdReturnsRoom](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomRepositoryTests.java) | Verifica que una sala puede recuperarse correctamente a partir de su identificador. | Implementada | Integración backend – Repositorio |
| Gestión de salas | [UTB-ROOM-CTRL-01: getAllRoomsWhenNoRoomsReturnsEmptyList](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomControllerTests.java) | Verifica que el controlador devuelve una lista vacía cuando no existen salas registradas. | Implementada | Integración backend – Controlador |
| Gestión de salas | [UTB-ROOM-CTRL-02: findByNameNoRoomsReturnsEmpty](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomControllerTests.java) | Verifica que la búsqueda de salas por nombre inexistente no devuelve resultados. | Implementada | Integración backend – Controlador |
| Gestión de salas | [UTB-ROOM-CTRL-03: createRoomAndRetrieveById](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomControllerTests.java) | Verifica que una sala creada puede recuperarse correctamente mediante su identificador desde el controlador. | Implementada | Integración backend – Controlador |
| Gestión de salas | [UTB-ROOM-CTRL-04: createRoomAndRetrieveAllRooms](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomControllerTests.java) | Verifica que el controlador devuelve correctamente todas las salas creadas. | Implementada | Integración backend – Controlador |
| Gestión de salas | [UTB-ROOM-CTRL-05: findRoomByDicesReturnsCorrectRoom](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/SBJ4592/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/room/RoomControllerTests.java) | Verifica que una sala se recupera correctamente a partir de la combinación de dados negro y blanco. | Implementada | Integración backend – Controlador |
| Notificaciones | [shouldSendInviteSuccessfully](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se puede enviar correctamente una invitación a otro usuario. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldThrowExceptionWhenInviteAlreadyExists](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se lanza una excepción si ya existe una invitación pendiente entre los mismos usuarios. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldGetPendingNotifications](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se recuperan correctamente las notificaciones pendientes de un usuario. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldGetNotificationBetweenUsers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se puede obtener una notificación específica entre dos usuarios para un match. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldGetNotificationById](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se puede obtener una notificación por su id. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldReturnEmptyWhenNotificationNotFound](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se devuelve vacío cuando no existe una notificación con el id indicado. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldAcceptInvite](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se puede aceptar correctamente una invitación pendiente. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldRejectInvite](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se puede rechazar correctamente una invitación pendiente. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldRejectOtherInvitesForMatch](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que se rechazan todas las demás invitaciones pendientes de un usuario para un match, excepto la indicada. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldReturnEmptyListWhenNoInvitesToReject](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationServiceTest.java) | Verifica que no se producen errores si no hay invitaciones pendientes que rechazar. | Implementada | Unitaria backend – Servicio |
| Notificaciones | [shouldFindPendingNotificationsByReceiverId](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se recuperan correctamente las notificaciones pendientes de un usuario por su id. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldReturnEmptyListWhenNoPendingNotifications](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se devuelve una lista vacía cuando un usuario no tiene notificaciones pendientes. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldFindNotificationBetweenSenderAndReceiver](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se puede obtener una notificación pendiente entre un remitente y un receptor. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldReturnEmptyWhenNoNotificationBetweenUsers](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se devuelve vacío cuando no hay notificación entre dos usuarios. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldFindNotificationWithMatchId](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se puede obtener una notificación pendiente entre dos usuarios para un match específico. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldReturnEmptyWhenNoNotificationWithMatchId](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se devuelve vacío cuando no existe notificación para un match específico entre dos usuarios. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | [shouldFindNotificationsForReceiverAndMatch](https://github.com/gii-is-DP1/dp1-2025-2026-l2-01/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/Escape_From_Elba/notifications/NotificationRepositoryTest.java) | Verifica que se pueden obtener todas las notificaciones pendientes de un usuario para un match específico. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldReturnEmptyListWhenNoNotificationsForMatch | Verifica que se devuelve una lista vacía cuando no hay notificaciones para un usuario en un match. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldReturnNotificationsWhenRepositoryHasData | Verifica que se recuperan notificaciones pendientes de un usuario cuando el repositorio tiene datos. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldReturnEmptyListWhenNoNotificationsExist | Verifica que se devuelve una lista vacía cuando no existen notificaciones para un usuario. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldFindNotificationById | Verifica que se puede recuperar una notificación específica entre remitente y receptor. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldReturnEmptyWhenSearchingNonExistentNotification | Verifica que se devuelve vacío cuando se busca una notificación que no existe entre dos usuarios. | Implementada | Unitaria backend – Repositorio |
| Notificaciones | shouldFindNotificationsByMatchId | Verifica que se pueden recuperar todas las notificaciones pendientes de un usuario para un match específico. | Implementada | Unitaria backend – Repositorio |
| Gestión de partidas | getMatchByIdNotFoundThrows | Verifica que se lanza excepción al buscar una partida inexistente por ID. | Implementada | Unit – Servicio |
| Gestión de partidas | getFinishedAndInProgressMatchesReturnsPage | Verifica que se obtienen partidas finalizadas y en progreso paginadas. | Implementada | Unit – Servicio |
| Gestión de partidas | getMatchesPlayedByUserCallsRepo | Verifica que se consultan las partidas jugadas por un usuario paginadas. | Implementada | Unit – Servicio |
| Gestión de partidas | getMatchesWonByUserCallsRepo | Verifica que se consultan las partidas ganadas por un usuario paginadas. | Implementada | Unit – Servicio |
| Gestión de partidas | endMatchSetsStatusAndWinner | Verifica que al finalizar una partida se establece el estado FINISHED y se asigna el ganador. | Implementada | Unit – Servicio |
| Gestión de partidas | deleteCallsRepoDeleteById | Verifica que al eliminar una partida se llama al repositorio para borrar por ID. | Implementada | Unit – Servicio |
| Gestión de partidas | getInProgressMatchesCallsRepo | Verifica que se obtienen las partidas en progreso. | Implementada | Unit – Servicio |
| Gestión de cartas | playerWinsNiallCampbellReturnsCardWhenPresentOrNull | Verifica que al ganar Niall Campbell se obtiene carta si existe o null. | Implementada | Unit – Servicio |
| Gestión de acción de jugadores | playerLosesAgaintsNonPlayerZeroesActionPointsAndMovesCardToDiscard | Verifica que al perder contra NPC se ponen AP a 0 y carta va al descarte. | Implementada | Unit – Servicio |
| Gestión de movimiento | moveLoserPlayerUpdatesRoomAndStrength | Verifica que el jugador perdedor se mueve de sala y su fuerza aumenta. | Implementada | Unit – Servicio |
| Gestión de movimiento | movePlayerToAdyacentRoomMovesAndConsumesActionPoint | Verifica que un jugador se mueve a sala adyacente y consume un punto de acción. | Implementada | Unit – Servicio |
| Gestión de movimiento | moveNpcToAdyacentRoomMovesNpcAndConsumesPlayerActionPoint | Verifica que un NPC se mueve y se decrementa el AP del jugador. | Implementada | Unit – Servicio |
| Gestión de movimiento | movePlayerByFormingRoomNameSucceedsWhenBagHasLetters | Verifica que un jugador se mueve formando nombre de sala si tiene cartas necesarias. | Implementada | Unit – Servicio |
| Intentos de escape | escapeAttemptSuccessAndFailurePaths | Verifica los caminos de éxito y fracaso de intento de escape y descarte de cartas. | Implementada | Unit – Servicio |
| Gestión de cartas | testDefaultConstructor | Verifica que el constructor por defecto inicializa todos los atributos a null. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testConstructorWithEntities | Verifica la creación correcta del DTO a partir de entidades de mano, bolsa y mazo. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testConstructorWithDTOs | Verifica la creación correcta del DTO a partir de otros DTOs. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEntityToDTOConversion | Verifica la conversión automática de entidades a DTOs durante la construcción del objeto. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetHand | Verifica el correcto almacenamiento y recuperación de la mano del jugador. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetBag | Verifica el correcto almacenamiento y recuperación de la bolsa de cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetDeck | Verifica el correcto almacenamiento y recuperación del mazo de cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetPlayerId | Verifica el correcto almacenamiento y recuperación del identificador del jugador. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testHandleNullValues | Verifica el comportamiento del DTO cuando sus atributos toman valores nulos. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateHand | Verifica la actualización correcta de la mano asociada al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateBag | Verifica la actualización correcta de la bolsa asociada al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateDeck | Verifica la actualización correcta del mazo asociado al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdatePlayerId | Verifica la actualización correcta del identificador del jugador. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testLargePlayerId | Verifica el manejo de identificadores de jugador de gran tamaño. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testHandWithCards | Verifica la correcta gestión de una mano que contiene cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testBagWithCards | Verifica la correcta gestión de una bolsa que contiene múltiples cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDeckWithCards | Verifica la correcta gestión de un mazo con varias cartas disponibles. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEmptyCollections | Verifica el comportamiento del DTO cuando todas las colecciones están vacías. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testIndependentInstancesWithEntities | Verifica la independencia entre distintas instancias construidas a partir de entidades. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testIndependentInstancesWithDTOs | Verifica la independencia entre distintas instancias construidas a partir de DTOs. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testZeroPlayerId | Verifica el manejo correcto del valor 0 como identificador de jugador. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDataIntegrityWithEntities | Verifica la integridad de los datos tras la conversión desde entidades. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDTOReferencePreservation | Verifica que las referencias de los DTOs se mantienen correctamente en el constructor correspondiente. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testMultiplePlayers | Verifica el correcto funcionamiento del DTO en escenarios con múltiples jugadores. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDefaultConstructor | Verifica que el constructor por defecto inicializa todos los atributos a null. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testConstructorWithCard | Verifica la correcta conversión de una entidad Card en un CardDTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetId | Verifica el correcto almacenamiento y recuperación del identificador de carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetFrontImage | Verifica el correcto almacenamiento y recuperación de la imagen frontal de la carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetBackImage | Verifica el correcto almacenamiento y recuperación de la imagen trasera de la carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetLetter | Verifica el correcto almacenamiento y recuperación de la letra de la carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testHandleNullValues | Verifica el manejo correcto de valores nulos en todos los atributos. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateId | Verifica la actualización correcta del identificador de la carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateFrontImage | Verifica la actualización correcta de la imagen frontal. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateBackImage | Verifica la actualización correcta de la imagen trasera. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateLetter | Verifica la actualización correcta de la letra asociada a la carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testLargeId | Verifica el manejo de identificadores de gran tamaño. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDifferentLetters | Verifica la correcta gestión de distintas letras válidas de carta. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDifferentImageFormats | Verifica la gestión de diferentes formatos y rutas de imágenes. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testIndependentInstances | Verifica la independencia entre distintas instancias de CardDTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testZeroId | Verifica el manejo correcto del valor 0 como identificador. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEmptyImagePaths | Verifica el manejo de rutas de imagen vacías. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEscapeLetters | Verifica la correcta representación de las letras que forman la palabra ESCAPE. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testConstructorWithAllParameters | Verifica la creación correcta de un resultado de robo de carta con todos sus elementos asociados. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testCardConversion | Verifica la conversión de una entidad Card a CardDTO durante la creación del resultado. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDeckConversion | Verifica la conversión correcta de un mazo en juego a su correspondiente DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testHandConversion | Verifica la conversión correcta de una mano de cartas a su correspondiente DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetCard | Verifica el correcto almacenamiento y recuperación de la carta robada. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetDeck | Verifica el correcto almacenamiento y recuperación del mazo asociado. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testSetAndGetHand | Verifica el correcto almacenamiento y recuperación de la mano asociada. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDifferentCardLetters | Verifica el comportamiento del DTO con distintas letras de cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testDeckWithCards | Verifica la correcta representación de mazos con múltiples cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testHandWithCards | Verifica la correcta representación de manos con cartas. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEmptyDeck | Verifica el comportamiento cuando el mazo se encuentra vacío. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testEmptyHand | Verifica el comportamiento cuando la mano se encuentra vacía. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testIndependentInstances | Verifica la independencia entre distintas instancias del DTO. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateCard | Verifica la actualización correcta de la carta robada tras la construcción del objeto. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateDeck | Verifica la actualización correcta del mazo asociado tras la construcción del objeto. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testUpdateHand | Verifica la actualización correcta de la mano asociada tras la construcción del objeto. | Implementada | Unitaria backend – DTO |
| Gestión de cartas | testCardDataIntegrity | Verifica que la información de la carta se mantiene íntegra tras el proceso de conversión. | Implementada | Unitaria backend – DTO |
| Gestión de armas | validateWeaponValidWeaponReturnsBonus | Verifica que una palabra reconocida como arma válida devuelve correctamente la bonificación correspondiente. | Implementada | Integración backend – Controlador |
| Gestión de armas | validateWeaponInvalidWeaponReturnsNoBonus | Verifica que una palabra que no corresponde a un arma no genera bonificación. | Implementada | Integración backend – Controlador |
| Gestión de armas | validateWeaponInvalidBodyReturnsBadRequest | Verifica que el controlador devuelve un error cuando el cuerpo de la petición es inválido. | Implementada | Integración backend – Controlador |
| Gestión de bolsas | testConstructorWithEmptyBag | Verifica la creación de un DTO de bolsa a partir de una bolsa vacía. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testConstructorWithCards | Verifica la conversión correcta de cartas desde una bolsa a su DTO correspondiente. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testConstructorWithPlayerId | Verifica que el identificador del jugador se almacena correctamente en el DTO. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testCardConversion | Verifica la conversión de entidades Card a objetos CardDTO. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testCardOrderPreservation | Verifica que el orden de las cartas se mantiene tras la conversión al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testLargeNumberOfCards | Verifica el comportamiento del DTO cuando la bolsa contiene un gran número de cartas. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testListNotReferenced | Verifica que el DTO crea una nueva colección y no reutiliza la referencia de la entidad original. | Implementada | Unitaria backend – DTO |
| Gestión de bolsas | testEscapeLettersInCards | Verifica la correcta gestión de cartas que contienen letras utilizadas en la mecánica de formación de palabras. | Implementada | Unitaria backend – DTO |
| Validación de palabras | wordFromCardsBuildsCorrectWord | Verifica que se construye correctamente una palabra a partir de una lista de cartas. | Implementada | Unitaria backend – Servicio |
| Validación de palabras | doesWordExistsLocalDictionary | Verifica que una palabra existente en el diccionario local es reconocida correctamente. | Implementada | Unitaria backend – Servicio |
| Validación de palabras | doesWordExistsExternalApiSuccess | Verifica que una palabra se considera válida cuando es encontrada mediante el servicio externo. | Implementada | Unitaria backend – Servicio |
| Validación de palabras | checkBagIsValidEmptyBagReturnsTrue | Verifica que una bolsa vacía se considera válida. | Implementada | Unitaria backend – Servicio |
| Validación de palabras | checkBagIsValidDelegatesToWordValidation | Verifica que la validación de la bolsa utiliza correctamente la validación de palabras. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | validateWeaponEmptyThrows | Verifica que se lanza una excepción cuando se intenta validar un arma vacía. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | isWeaponOnListReturnsTrue | Verifica que una palabra reconocida como arma es identificada correctamente. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | validateWeaponValidWeaponOnList | Verifica que un arma válida devuelve un resultado con estado VALID. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | validateWeaponRequiresVotingIfNotOnListButExists | Verifica que se inicia una votación cuando la palabra existe pero no está registrada como arma. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | validateWeaponInvalidIfNotOnListAndDoesNotExist | Verifica que una palabra inexistente es considerada un arma inválida. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | checkProposedWeaponExistsReturnsTrueIfWordExists | Verifica que una propuesta de arma existente es aceptada correctamente. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | checkProposedWeaponExistsReturnsFalseIfWordDoesNotExist | Verifica que una propuesta de arma inexistente es rechazada correctamente. | Implementada | Unitaria backend – Servicio |
| Gestión de bolsas | updateStoresNewBagInActivesBags | Verifica que una actualización de bolsa se almacena correctamente en la colección de bolsas activas. | Implementada | Unitaria backend – Servicio |
| Gestión de armas | testConstructorWithAllParameters | Verifica la creación correcta del DTO de validación de arma con todos sus atributos. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testSetAndGetWeapon | Verifica el almacenamiento y recuperación del nombre del arma. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testSetAndGetBonusValue | Verifica el almacenamiento y recuperación de la bonificación asociada al arma. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testSetAndGetStatus | Verifica el almacenamiento y recuperación del estado de validación del arma. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testAllValidationWeaponStatusValues | Verifica el comportamiento del DTO para todos los estados posibles de validación. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testConstructorWithNullValues | Verifica la correcta creación del DTO cuando todos los parámetros son nulos. | Implementada | Unitaria backend – DTO |
| Gestión de armas | testStatusIsEnum | Verifica que el estado almacenado pertenece al enumerado ValidationWeaponStatus. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testConstructorWithEmptyDeck | Verifica la creación de un DTO de mazo a partir de un mazo vacío. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testConstructorWithNotDiscardedCards | Verifica la conversión de cartas disponibles a su representación DTO. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testConstructorWithDiscardedCards | Verifica la conversión de cartas descartadas a su representación DTO. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testConstructorWithBothCardTypes | Verifica la correcta gestión simultánea de cartas disponibles y descartadas. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testCardConversion | Verifica la conversión completa de entidades Card a CardDTO dentro del mazo. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testCardOrderPreservation | Verifica que el orden de las cartas se mantiene tras la conversión al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testLargeNumberOfNotDiscardedCards | Verifica el comportamiento del DTO con un gran número de cartas disponibles. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testLargeNumberOfDiscardedCards | Verifica el comportamiento del DTO con un gran número de cartas descartadas. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testListsNotReferenced | Verifica que las colecciones generadas en el DTO no reutilizan las referencias originales. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testEscapeLettersInNotDiscardedCards | Verifica la correcta representación de las cartas asociadas a la mecánica de formación de palabras. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testCardsWithFullInformation | Verifica la conservación de toda la información de las cartas durante la conversión al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de mazos | testMoveCardFromNotDiscardedToDiscarded | Verifica la gestión del movimiento de cartas entre las listas de disponibles y descartadas. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testConstructorWithEmptyHand | Verifica la creación de un DTO de mano a partir de una mano vacía. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testConstructorWithSingleCard | Verifica la conversión correcta de una mano con una única carta a su representación DTO. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testConstructorWithMultipleCards | Verifica la conversión correcta de una mano con múltiples cartas. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testCardConversion | Verifica la conversión de entidades Card a CardDTO dentro de la mano. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testEscapeLettersInHand | Verifica la correcta gestión de cartas con letras utilizadas en la formación de palabras. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testCardOrderPreservation | Verifica que el orden de las cartas se mantiene tras la conversión al DTO. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testLargeNumberOfCards | Verifica el comportamiento del DTO con una gran cantidad de cartas. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testListNotReferenced | Verifica que la lista de cartas generada en el DTO no reutiliza la referencia original. | Implementada | Unitaria backend – DTO |
| Gestión de manos | testCardsWithFullInformation | Verifica que toda la información de las cartas se conserva correctamente durante la conversión. | Implementada | Unitaria backend – DTO |
| Chat de partida | testConstructorWithChatMessage | Verifica la conversión correcta de una entidad ChatMessage a ChatMessageDTO. | Implementada | Unitaria backend – DTO |
| Chat de partida | testConstructorWithNullChatMessage | Verifica el comportamiento del DTO cuando la entidad ChatMessage es nula. | Implementada | Unitaria backend – DTO |
| Chat de partida | testConstructorWithNullPlayer | Verifica la conversión de mensajes cuyo jugador asociado es nulo. | Implementada | Unitaria backend – DTO |
| Chat de partida | testConstructorWithNullMatch | Verifica la conversión de mensajes cuya partida asociada es nula. | Implementada | Unitaria backend – DTO |
| Chat de partida | testLongMessage | Verifica la gestión de mensajes largos dentro de los límites permitidos. | Implementada | Unitaria backend – DTO |
| Chat de partida | testMessageWithSpecialCharacters | Verifica la correcta gestión de mensajes con caracteres especiales. | Implementada | Unitaria backend – DTO |
| Chat de partida | testDifferentTimeValues | Verifica la gestión correcta de diferentes marcas temporales en los mensajes. | Implementada | Unitaria backend – DTO |
| Chat de partida | testMultilineMessage | Verifica la gestión correcta de mensajes multilínea. | Implementada | Unitaria backend – DTO |
| Chat de partida | testMessageWithSpaces | Verifica que los espacios iniciales y finales del mensaje se conservan correctamente. | Implementada | Unitaria backend – DTO |
| Sistema de combate | testResolveFight | Verifica la resolución de un combate y la devolución de la información resultante. | Implementada | Integración backend – Controlador |
| Sistema de combate | testStealCardFromPlayer | Verifica el robo de una carta a otro jugador tras finalizar un combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testPlayerLosesAgainstNpc | Verifica el procesamiento de la derrota de un jugador frente a un NPC. | Implementada | Integración backend – Controlador |
| Sistema de combate | testNotifyFight | Verifica el envío de notificaciones websocket de inicio o actualización de combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testNotifyFightDice | Verifica el envío de actualizaciones websocket de tiradas de dados durante un combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testNotifyDiceTotals | Verifica el envío de actualizaciones websocket con los totales de dados del combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testNotifyReadyState | Verifica la notificación del estado de preparación de los participantes del combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testNotifyFightWeapons | Verifica la notificación de armas utilizadas durante un combate. | Implementada | Integración backend – Controlador |
| Sistema de combate | testCheckAndTriggerChainFights | Verifica la comprobación y activación de combates encadenados pendientes. | Implementada | Integración backend – Controlador |
| Sistema de combate | testProcessFightResolutionMatchNotFound | Verifica que se lanza una excepción cuando se intenta resolver un combate de una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testNpcBeatsPlayerScenario | Verifica el escenario en el que un NPC derrota a un jugador. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerBeatsNormalNpc | Verifica el escenario en el que un jugador derrota a un NPC normal y obtiene la recompensa correspondiente. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerBeatsNiallCampbell | Verifica el escenario especial en el que un jugador derrota al NPC Niall Campbell. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerBeatsPlayer | Verifica el escenario de victoria de un jugador sobre otro jugador. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerStealFromPlayerHand | Verifica el robo de una carta desde la mano del jugador derrotado. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerStealFromPlayerBag | Verifica el robo de una carta desde la bolsa del jugador derrotado. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerStealFromPlayerInvalidLocation | Verifica que se lanza una excepción cuando se intenta robar desde una ubicación inválida. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerLosesAgainstNpcHand | Verifica la pérdida de una carta de la mano tras ser derrotado por un NPC. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerLosesAgainstNpcBag | Verifica la pérdida de una carta de la bolsa tras ser derrotado por un NPC. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testGetPossibleFightSafeArea | Verifica que no se generan combates en salas seguras. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testGetPossibleFightTwoPlayers | Verifica la generación de un combate pendiente cuando dos jugadores coinciden en una misma sala. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testLoserLoseActionPoints | Verifica la reducción de puntos de acción del perdedor y su notificación correspondiente. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testUpdatePlayerStatistics | Verifica la actualización de estadísticas del jugador tras una victoria. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | shouldNotifyWhenPendingFights | Verifica que se notifican los combates pendientes cuando existen enfrentamientos por resolver. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | shouldNotNotifyWhenNoPendingFights | Verifica que no se envían notificaciones cuando no existen combates pendientes. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | shouldThrowIfMatchDoesNotExistWhenChecKPendingFights | Verifica que se lanza una excepción al consultar combates pendientes de una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Sistema de combate | testPlayerBeatsNpcScenario | Verifica la construcción correcta del DTO de resolución de combate para una victoria de jugador sobre NPC. | Implementada | Unitaria backend – DTO |
| Sistema de combate | testNpcBeatsPlayerScenario | Verifica la construcción correcta del DTO de resolución de combate para una victoria de NPC sobre jugador. | Implementada | Unitaria backend – DTO |
| Sistema de combate | testPlayerBeatsPlayerScenario | Verifica la construcción correcta del DTO de resolución de combate para una victoria entre jugadores. | Implementada | Unitaria backend – DTO |
| Sistema de combate | testWeaponsUpdateDTOAndWeaponData | Verifica la gestión y serialización de la información de armas utilizada durante el combate. | Implementada | Unitaria backend – DTO |
| Gestión de amistades | shouldFindRequestPendingByIdNotPending | Verifica que una solicitud no pendiente no es recuperada como solicitud pendiente. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldFindRequestPendingByIdNotExist | Verifica que una solicitud inexistente no puede recuperarse como pendiente. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldFindAllRequestsByUserIdNoRequests | Verifica que un usuario sin solicitudes enviadas obtiene una lista vacía. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldFindAllRequestsForUserIdNoRequests | Verifica que un usuario sin solicitudes recibidas obtiene una lista vacía. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldFindAllFriendsByUserIdNone | Verifica que un usuario sin amistades aceptadas obtiene una lista vacía. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldFindPendingOrFriendsUsersNone | Verifica que no existe relación ni solicitud pendiente entre usuarios no relacionados. | Implementada | Integración backend – Repositorio |
| Gestión de amistades | shouldGetFriendsByUserId | Verifica la obtención de la lista de amistades aceptadas de un usuario. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldGetPendingRequestsByUserId | Verifica la obtención de solicitudes pendientes de un usuario. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldGetReceivedRequestsByUserId | Verifica la obtención de solicitudes recibidas por un usuario. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldCreateFriendRequest | Verifica la creación correcta de una solicitud de amistad. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldNotCreateFriendRequestToSelf | Verifica que un usuario no puede enviarse una solicitud a sí mismo. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldAcceptMyFriendRequest | Verifica la aceptación de una solicitud de amistad propia pendiente. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldNotAcceptOtherUsersRequest | Verifica que no se puede aceptar una solicitud ajena. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldRejectMyFriendRequest | Verifica el rechazo de una solicitud de amistad propia pendiente. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldNotRejectOtherUsersRequest | Verifica que no se puede rechazar una solicitud ajena. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldDeleteFriendAsSender | Verifica que el remitente original puede eliminar una amistad. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldDeleteFriendAsReceiver | Verifica que el destinatario original puede eliminar una amistad. | Implementada | Integración backend – Controlador |
| Gestión de amistades | shouldNotDeleteFriendNotMine | Verifica que no se puede eliminar una amistad ajena. | Implementada | Integración backend – Controlador |
| Gestión de invitaciones | shouldReturnInvitationsWhenRepositoryHasData | Verifica la recuperación de invitaciones pendientes para un usuario receptor. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldReturnEmptyListWhenNoInvitationsExist | Verifica que un usuario sin invitaciones pendientes obtiene una lista vacía. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldFindInvitationById | Verifica la búsqueda de una invitación pendiente entre remitente y destinatario. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldReturnEmptyWhenSearchingNonExistentInvitation | Verifica que una invitación inexistente no es recuperada por el repositorio. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldFindInvitationsByMatchId | Verifica la recuperación de invitaciones pendientes asociadas a una partida concreta. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldFindInvitationWithMatchId | Verifica la búsqueda de una invitación utilizando remitente, destinatario, partida y estado. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldReturnEmptyWhenNoInvitationWithMatchId | Verifica que no se obtiene ninguna invitación cuando la combinación de remitente, destinatario y partida no existe. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldFindInvitationsForReceiverAndMatch | Verifica la recuperación de invitaciones de un receptor asociadas a una partida específica. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldReturnEmptyListWhenNoInvitationsForMatch | Verifica que una consulta sin invitaciones asociadas devuelve una lista vacía. | Implementada | Integración backend – Repositorio |
| Gestión de invitaciones | shouldVerifyInvitationRepositoryMethods | Verifica la disponibilidad y correcto funcionamiento de los métodos de consulta principales del repositorio de invitaciones. | Implementada | Integración backend – Repositorio |
| Gestión de lobbies | testCreateLobby | Verifica la creación correcta de un lobby con los parámetros indicados por el jugador. | Implementada | Integración backend – Controlador |
| Gestión de lobbies | testJoinPublicLobby | Verifica la incorporación de un jugador a un lobby público existente. | Implementada | Integración backend – Controlador |
| Gestión de lobbies | testJoinPrivateLobby | Verifica la incorporación de un jugador a un lobby privado mediante código de acceso. | Implementada | Integración backend – Controlador |
| Gestión de lobbies | testLeaveLobby | Verifica la salida de un jugador de un lobby existente. | Implementada | Integración backend – Controlador |
| Gestión de lobbies | testStartMatch | Verifica el inicio de una partida desde el lobby. | Implementada | Integración backend – Controlador |
| Gestión de lobbies | getAllPublicLobbiesReturnsPage | Verifica la recuperación paginada de lobbies públicos en espera. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | getPrivateLobbyExists | Verifica la búsqueda de un lobby privado mediante su código de acceso. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | createLobbyNoUserThrowsUnauthorized | Verifica que no se puede crear un lobby sin usuario autenticado. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | createPublicLobbySuccess | Verifica la creación correcta de un lobby público. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | createPrivateLobbyGeneratesCode | Verifica que la creación de un lobby privado genera un código de acceso válido. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | joinLobbyNotFoundThrows | Verifica que se lanza una excepción al intentar unirse a un lobby inexistente. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | joinLobbySuccess | Verifica la incorporación correcta de un jugador a un lobby público y la notificación correspondiente. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | joinPrivateLobbySuccess | Verifica la incorporación correcta de un jugador a un lobby privado y la notificación correspondiente. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | leaveLobbyAsCreatorDeletesMatch | Verifica que al abandonar el lobby su creador se elimina la partida y las invitaciones asociadas. | Implementada | Unitaria backend – Servicio |
| Gestión de lobbies | leaveLobbyAsRegularPlayerRemovesPlayer | Verifica que un jugador no creador puede abandonar el lobby sin eliminar la partida. | Implementada | Unitaria backend – Servicio |
| Gestión del mapa | testConstructorWithAllFields (NpcPositionDTO) | Verifica la creación correcta de un DTO de posición de NPC con todos sus atributos inicializados. | Implementada | Unitaria backend – DTO |
| Gestión del mapa | testToString (NpcPositionDTO) | Verifica la representación textual de la posición de un NPC. | Implementada | Unitaria backend – DTO |
| Gestión del mapa | testEqualsAndHashCode (NpcPositionDTO) | Verifica la igualdad lógica y la consistencia del hash de posiciones de NPC equivalentes. | Implementada | Unitaria backend – DTO |
| Gestión del mapa | testConstructorWithAllFields (PlayerPositionDTO) | Verifica la creación correcta de un DTO de posición de jugador con todos sus atributos inicializados. | Implementada | Unitaria backend – DTO |
| Gestión del mapa | testToString (PlayerPositionDTO) | Verifica la representación textual de la posición de un jugador. | Implementada | Unitaria backend – DTO |
| Gestión del mapa | testEqualsAndHashCode (PlayerPositionDTO) | Verifica la igualdad lógica y la consistencia del hash de posiciones de jugador equivalentes. | Implementada | Unitaria backend – DTO |
| Gestión de partidas | testMoveToAdjacentRoomSuccess | Verifica el movimiento de un jugador a una sala adyacente. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testMoveNpcToRoomSuccess | Verifica el movimiento de un NPC a una sala determinada. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testMoveByLettersSuccess | Verifica el movimiento de un jugador mediante la formación del nombre de una sala con cartas. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testAttemptEscapeSuccess | Verifica un intento de escape correctamente procesado. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testSubmitDice | Verifica el registro de una tirada de dado para determinar el orden de turno. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testNextTurn | Verifica el avance al siguiente turno de la partida. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testEndMatch | Verifica la finalización de una partida indicando un ganador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetMatchById | Verifica la recuperación de una partida mediante su identificador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetPlayersByMatchId | Verifica la obtención de los jugadores asociados a una partida. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetWinnerByMatchId | Verifica la obtención del ganador de una partida finalizada. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testUserInMatch | Verifica la consulta de la partida en la que participa un usuario. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetAdjacencyMap | Verifica la obtención del mapa de adyacencias entre salas. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetActionPoints | Verifica la consulta de los puntos de acción de un jugador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testDrawCardFromDeck | Verifica el robo de cartas desde el mazo. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetAllCards | Verifica la consulta del estado completo de las cartas de un jugador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetAllGames | Verifica la recuperación del listado de partidas disponibles. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testGetMatchesFilters | Verifica el filtrado de partidas por estado. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testHistorialsByUser | Verifica la recuperación del historial de partidas de un usuario. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testFinishMatch | Verifica la finalización de una partida mediante endpoint específico. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testEndMatch_NullWinner | Verifica la finalización de una partida sin ganador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testLeaveMatch | Verifica el abandono de una partida por parte de un jugador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testDeleteGame | Verifica la eliminación de una partida. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testConfirmDiscardPhase | Verifica la confirmación de la fase de descarte. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testConsumeActionPoints | Verifica el consumo de puntos de acción de un jugador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testNotifyStrength | Verifica la notificación de actualización de fuerza de un jugador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testSpectateGame | Verifica la incorporación de un usuario como espectador. | Implementada | Integración backend – Controlador |
| Gestión de partidas | testStopSpectating | Verifica la salida de un espectador de una partida. | Implementada | Integración backend – Controlador |
| Gestión de partidas | saveAndFindById | Verifica el almacenamiento y recuperación de una partida. | Implementada | Integración backend – Repositorio |
| Gestión de partidas | findPrivateLobbyByCode | Verifica la búsqueda de un lobby privado mediante código. | Implementada | Integración backend – Repositorio |
| Gestión de partidas | isFullAndMinReached | Verifica el cálculo de límites mínimos y máximos de jugadores. | Implementada | Unitaria backend – Entidad |
| Gestión de partidas | findByName | Verifica la búsqueda de partidas por nombre. | Implementada | Integración backend – Repositorio |
| Gestión de partidas | findPrivateLobbies | Verifica la recuperación de lobbies privados en estado de espera. | Implementada | Integración backend – Repositorio |
| Gestión de partidas | testMovePlayerToAdyacentRoomSuccess | Verifica el movimiento correcto de un jugador a una sala adyacente. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMovePlayerToAdyacentRoomChangeMatchStatus | Verifica el cambio de fase al realizar un movimiento válido. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenPlayerNoCurrentRoomWhenMoveAdyacentRoom | Verifica que no es posible mover un jugador sin sala asignada. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMovePlayerToSameRoomThrowsException | Verifica que no se puede mover un jugador a la misma sala. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMoveNpcToRoomSuccess | Verifica el movimiento correcto de un NPC. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMoveNpcToRoomChangeMatchPhaseSuccess | Verifica el cambio de fase al mover un NPC. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenNpcNoCurrentRoomWhenMoveNPC | Verifica que un NPC debe estar asignado a una sala para moverse. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMoveNpcToSameRoomThrowsException | Verifica que un NPC no puede moverse a la misma sala. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMovePlayerByFormingRoomNameSuccess | Verifica el movimiento mediante formación de palabras. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMoveByFormingWordChangeMatchPhaseSuccess | Verifica el cambio de fase tras movimiento con palabras. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenNoBagCardsMoveByFormingWord | Verifica que no puede realizarse el movimiento sin cartas disponibles. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testMovePlayerByFormingRoomNameInsuficientLetters | Verifica que se requiere disponer de letras suficientes. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testEscapeAttemptSuccess | Verifica un intento de escape exitoso. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testEscapeAttemptFailure | Verifica un intento de escape fallido. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testEscapeAttemptNoActionPoints | Verifica que no puede intentarse escapar sin puntos de acción. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | testEscapeAttemptNotInTower | Verifica que solo se puede escapar desde una torre. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenPlayerDoesNotHaveRequiredWord | Verifica que el jugador debe poseer la palabra requerida para escapar. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | getAllMatchsReturnsList | Verifica la recuperación de todas las partidas almacenadas. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | getMatchByIdFound | Verifica la recuperación de una partida existente mediante identificador. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | getMatchsByNameReturnsList | Verifica la búsqueda de partidas por nombre. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | deleteInvokesRepository | Verifica la eliminación de una partida mediante su identificador. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | deleteMatchCardsCallsDeleteMethods | Verifica la eliminación de todos los elementos de cartas asociados a una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | submitDiceAndAssignOrderSetsDiceWhenNotAllRolled | Verifica el registro de la tirada de dado cuando aún no han tirado todos los jugadores. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | getMatchWinnerReturnsWinnerWhenFinished | Verifica la obtención del ganador de una partida finalizada. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | userInMatchReturnsValueFromRepo | Verifica la consulta de la partida en la que participa un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | saveCallsRepoAndReturnsSameInstance | Verifica el almacenamiento correcto de una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | consumeActionPointForUserDecrementsAndReturnsDTO | Verifica el consumo de un punto de acción de un jugador. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | consumeAllActionPointForUserSetsZeroAndReturnsDTO | Verifica el consumo total de puntos de acción de un jugador. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | consumeOneActionPointReducesAndReturnsDTO | Verifica la reducción de un único punto de acción. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | playerDrawsRewardCardNotifiesAndReturnsResult | Verifica el robo de una carta de recompensa y su notificación. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | submitDiceAndAssignOrderThrowsWhenPlayerNotFound | Verifica que no se puede registrar una tirada para un jugador inexistente. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | consumeActionPointForUserThrowsWhenPlayerNotFound | Verifica que se lanza una excepción cuando el jugador no existe. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | getMatchWinnerThrowsWhenMatchNotFinished | Verifica que no puede consultarse un ganador en una partida no finalizada. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldGetAllMatches | Verifica nuevamente la recuperación de todas las partidas. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnMatchesByName | Verifica nuevamente la búsqueda de partidas por nombre. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnRunningMatches | Verifica la recuperación de partidas en ejecución. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldSaveMatch | Verifica el guardado correcto de una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenMatchDoesNotExist | Verifica que se lanza una excepción al buscar una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldDeleteMatch | Verifica la eliminación de una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnUserInMatch | Verifica la obtención del identificador de la partida donde participa un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnMatchDTOWhenUserIsPlayer | Verifica la obtención de la vista de partida para un jugador participante. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnMatchDTOWhenUserIsSpectator | Verifica la obtención de la vista de partida para un espectador. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenMatchNotFound | Verifica que no puede obtenerse información de una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldCreatePlayerDTOForEveryPlayer | Verifica la generación de DTOs para todos los jugadores de la partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnFinishedAndInProgressMatches | Verifica la recuperación de partidas finalizadas y en progreso. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnInProgressMatches | Verifica la recuperación de partidas en progreso. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnFinishedMatches | Verifica la recuperación de partidas finalizadas. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnPlayedOrAbandonedMatches | Verifica la recuperación de partidas jugadas o abandonadas por un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnPlayedAndCreatedMatches | Verifica la recuperación de partidas jugadas y creadas por un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnWonMatches | Verifica la recuperación de partidas ganadas por un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldReturnAbandonedMatches | Verifica la recuperación de partidas abandonadas por un usuario. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldThrowWhenStartingUnknownMatch | Verifica que no puede iniciarse una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldSetStatusToPlaying | Verifica que una partida pasa a estado PLAYING al iniciarse. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldResetPlayers | Verifica el reinicio de parámetros de los jugadores al comenzar la partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldCreateNpcs | Verifica la creación de NPCs al iniciar una partida. | Implementada | Unitaria backend – Servicio |
| Gestión de partidas | shouldNotifyGameStarted | Verifica la emisión de notificaciones al comenzar una partida. | Implementada | Unitaria backend – Servicio |
| DTO Actualización de puntos de acción | testDefaultConstructor | Verifica la creación de un DTO de actualización de puntos de acción mediante constructor vacío. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testConstructorWithAllFields | Verifica la creación de un DTO con todos los campos inicializados. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testSetAndGetPlayerId | Verifica la asignación y recuperación del identificador del jugador. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testSetAndGetUserId | Verifica la asignación y recuperación del identificador del usuario. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testSetAndGetUsername | Verifica la asignación y recuperación del nombre de usuario. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testSetAndGetActionPoints | Verifica la asignación y recuperación de puntos de acción. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testSetAndGetTimestamp | Verifica la asignación y recuperación de la marca temporal. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testHandleNullValues | Verifica el manejo correcto de valores nulos. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testMultipleFieldChanges | Verifica modificaciones sucesivas de los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testToString | Verifica la representación textual del DTO. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testZeroActionPoints | Verifica el almacenamiento de cero puntos de acción. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testNegativeActionPoints | Verifica el almacenamiento de valores negativos de puntos de acción. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testLargeIdValues | Verifica el manejo de identificadores máximos. | Implementada | Unitaria backend – DTO |
| DTO Actualización de puntos de acción | testIndependentInstances | Verifica la independencia entre instancias distintas del DTO. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testDefaultConstructor | Verifica la creación vacía del DTO de actualización de cartas. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testConstructorWithAllFields | Verifica la creación del DTO con información completa de ganador y perdedor. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testSetAndGetMatchId | Verifica la asignación y recuperación del identificador de partida. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testSetAndGetWinner | Verifica la asignación y recuperación de las cartas del ganador. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testSetAndGetLoser | Verifica la asignación y recuperación de las cartas del perdedor. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testHandleNullValues | Verifica el manejo de valores nulos en el DTO. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testMultipleFieldChanges | Verifica cambios sucesivos en los datos de ganador y perdedor. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testIndependentInstances | Verifica la independencia entre dos DTO de actualización de cartas. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testLargeMatchId | Verifica identificadores de partida muy grandes. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testZeroMatchId | Verifica el uso de identificador de partida igual a cero. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testNegativeMatchId | Verifica el uso de identificadores negativos. | Implementada | Unitaria backend – DTO |
| DTO Actualización de cartas | testFightResultCardsUpdate | Verifica la actualización de cartas tras el resultado de un combate. | Implementada | Unitaria backend – DTO |
| Validación de entidades | shouldNotValidateWhenFirstNameEmpty | Verifica que una entidad Person no supera la validación cuando el campo firstName está vacío. | Implementada | Unitaria backend – Validación |
| Validación de entidades | shouldNotValidateWhenFirstNameEmpty | Verifica que se genera exactamente una violación de restricción al validar una entidad con el nombre vacío. | Implementada | Unitaria backend – Validación |
| Validación de entidades | shouldNotValidateWhenFirstNameEmpty | Verifica que la propiedad que provoca el error de validación es firstName. | Implementada | Unitaria backend – Validación |
| Validación de entidades | shouldNotValidateWhenFirstNameEmpty | Verifica que el mensaje de error asociado a la validación corresponde a «must not be empty». | Implementada | Unitaria backend – Validación 
| DTO de NPC | testNoArgsConstructorAndSetters | Verifica la creación de un NpcDTO mediante constructor vacío y la correcta asignación y recuperación de todos sus atributos mediante setters y getters. | Implementada | Unitaria backend – DTO |
| DTO de NPC | testConstructorFromNpcWithoutRoom | Verifica la construcción de un NpcDTO a partir de una entidad Npc sin sala asociada, comprobando que los datos se copian correctamente y que la sala permanece nula. | Implementada | Unitaria backend – DTO |
| DTO de NPC | testConstructorFromNpcWithRoom | Verifica la construcción de un NpcDTO a partir de una entidad Npc con sala asociada, comprobando que se copian correctamente los datos del NPC y de la sala. | Implementada | Unitaria backend – DTO |
| Repositorio de NPCs | saveNpcCreatesNpc | Verifica que el repositorio almacena correctamente un nuevo NPC y le asigna un identificador. | Implementada | Integración backend – Repositorio |
| Repositorio de NPCs | saveNpcUpdatesExistingNpc | Verifica que el repositorio actualiza correctamente los datos de un NPC previamente almacenado. | Implementada | Integración backend – Repositorio |
| Controlador de jugadores | findAllPlayersEmptyTest | Verifica que el endpoint de consulta de jugadores responde correctamente cuando no existen jugadores registrados. | Implementada | Integración backend – Controlador |
| Controlador de jugadores | findAllPlayersReturnsPlayersTest | Verifica que el endpoint de consulta de jugadores responde correctamente cuando existen jugadores registrados. | Implementada | Integración backend – Controlador |
| Controlador de jugadores | findAllByUserIdEmptyTest | Verifica que la consulta de jugadores asociados a un usuario devuelve una respuesta correcta cuando el usuario no tiene jugadores asociados. | Implementada | Integración backend – Controlador |
| Controlador de jugadores | findAllByUserIdReturnsPlayersTest | Verifica que la consulta de jugadores asociados a un usuario devuelve correctamente los jugadores encontrados. | Implementada | Integración backend – Controlador |
| Controlador de jugadores | getPlayersByMatchIdEmptyTest | Verifica que la consulta de jugadores de una partida responde correctamente cuando la partida no contiene jugadores. | Implementada | Integración backend – Controlador |
| Controlador de jugadores | getPlayersByMatchIdReturnsPlayersTest | Verifica que la consulta de jugadores de una partida devuelve correctamente la lista de jugadores asociados. | Implementada | Integración backend – Controlador |
| Repositorio de jugadores | findByUserIdNoPlayersReturnsEmptyList | Verifica que la búsqueda de jugadores asociados a un usuario sin partidas devuelve una lista vacía. | Implementada | Integración backend – Repositorio |
| Repositorio de jugadores | findByUserIdReturnsPlayers | Verifica que la búsqueda por identificador de usuario devuelve correctamente los jugadores asociados. | Implementada | Integración backend – Repositorio |
| Repositorio de jugadores | findByMatchAndUserNonExistingReturnsEmpty | Verifica que la búsqueda de un jugador mediante identificador de partida e identificador de usuario inexistentes devuelve un resultado vacío. | Implementada | Integración backend – Repositorio |
| Repositorio de jugadores | findByMatchAndUserReturnsPlayer | Verifica que la búsqueda mediante identificador de partida e identificador de usuario devuelve correctamente el jugador asociado. | Implementada | Integración backend – Repositorio |
| Repositorio de jugadores | findByMatchIdNoPlayersReturnsEmpty | Verifica que la búsqueda de jugadores de una partida sin participantes devuelve una lista vacía. | Implementada | Integración backend – Repositorio |
| Repositorio de jugadores | getTotalAccionPointsByUserReturnsSum | Verifica que el repositorio calcula correctamente la suma total de puntos de acción de todos los jugadores asociados a un usuario. | Implementada | Integración backend – Repositorio |
| Servicio de jugadores | findAllReturnsPlayers | Verifica que el servicio devuelve correctamente la lista de jugadores obtenida del repositorio. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | findByIdExistingReturnsPlayer | Verifica que la búsqueda por identificador devuelve el jugador cuando existe en el repositorio. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | findByIdNonExistingThrowsException | Verifica que se lanza una excepción cuando se solicita un jugador inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | findByUserIdReturnsPlayers | Verifica que la búsqueda por identificador de usuario devuelve los jugadores asociados. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | savePlayerReturnsSavedPlayer | Verifica que el guardado de un jugador devuelve la entidad almacenada. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | deleteByIdCallsRepository | Verifica que la eliminación de un jugador delega correctamente en el repositorio. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | getPlayersByMatchIdReturnsPlayers | Verifica que la consulta de jugadores de una partida devuelve los jugadores asociados. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | removePlayerActionPointSuccess | Verifica que se consume correctamente un punto de acción de un jugador perteneciente a la partida indicada y que los cambios se almacenan. | Implementada | Unitaria backend – Servicio |
| Servicio de jugadores | removePlayerActionPointNoPlayerDoesNothing | Verifica que no se realizan modificaciones ni persistencia cuando el jugador no existe. | Implementada | Unitaria backend – Servicio |
| DTO de jugador en partida | testDefaultConstructor | Verifica la creación de un PlayerInGameDTO mediante constructor vacío y la inicialización de todos sus atributos a null. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testConstructorWithPlayer | Verifica la creación de un PlayerInGameDTO a partir de una entidad Player copiando correctamente sus datos básicos y la información de la sala actual. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testConstructorWithPlayerAndGameData | Verifica la creación de un PlayerInGameDTO a partir de un jugador junto con la información de mano y bolsa de juego. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testConstructorWithPlayerWithoutRoom | Verifica la creación de un PlayerInGameDTO a partir de un jugador sin sala asociada. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetId | Verifica la asignación y recuperación del identificador del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetStrength | Verifica la asignación y recuperación del atributo de fuerza del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetActionPoints | Verifica la asignación y recuperación de los puntos de acción del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetUser | Verifica la asignación y recuperación del usuario asociado al jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetCurrentRoom | Verifica la asignación y recuperación de la sala actual del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetHand | Verifica la asignación y recuperación de la mano del jugador en la partida. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testSetAndGetBag | Verifica la asignación y recuperación de la bolsa del jugador en la partida. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testGettersAndSettersConsistency | Verifica la coherencia entre los métodos getter y setter de todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testCompletePlayerDataCopy | Verifica la copia completa de la información de un jugador al construir el DTO. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testMultipleModifications | Verifica que los atributos del DTO pueden modificarse varias veces manteniendo el valor más reciente. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testCurrentRoomNull | Verifica el manejo correcto de valores nulos para la sala actual. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testUserNull | Verifica el manejo correcto de valores nulos para el usuario asociado. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testHandNull | Verifica el manejo correcto de valores nulos para la mano del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testBagNull | Verifica el manejo correcto de valores nulos para la bolsa del jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testChangeCurrentRoomFromNullToRoom | Verifica la actualización de la sala actual desde un valor nulo a una sala válida. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testChangeCurrentRoomFromRoomToNull | Verifica la eliminación de la referencia a la sala actual estableciendo un valor nulo. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testIndependentInstances | Verifica la independencia entre distintas instancias de PlayerInGameDTO. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testStrengthValues | Verifica el almacenamiento de distintos valores de fuerza, incluyendo cero y valores elevados. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testActionPointsValues | Verifica el almacenamiento de distintos valores de puntos de acción, incluyendo cero y valores elevados. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testUserReference | Verifica que la referencia al usuario asociado se conserva correctamente al crear el DTO desde un Player. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testRoomDTOCreation | Verifica la creación automática de un RoomDTO a partir de la sala asociada al jugador. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testHandInGameDTOCreation | Verifica la creación automática de un HandInGameDTO a partir de la mano del jugador en partida. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testBagInGameDTOCreation | Verifica la creación automática de un BagInGameDTO a partir de la bolsa del jugador en partida. | Implementada | Unitaria backend – DTO |
| DTO de jugador en partida | testMultipleFieldChanges | Verifica la modificación simultánea de múltiples atributos del DTO y la correcta conservación de los nuevos valores. | Implementada | Unitaria backend – DTO |
| Controlador de logros | testFindAll | Verifica la recuperación de todos los logros registrados en el sistema. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAllEmpty | Verifica que la consulta de logros devuelve una lista vacía cuando no existen registros. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievement | Verifica la obtención de un logro existente mediante su identificador. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementNotFound | Verifica que la consulta de un logro inexistente devuelve un estado de recurso no encontrado. | Implementada | Integración backend – Controlador |
| Controlador de logros | testCreateAchievement | Verifica la creación de un logro con datos válidos. | Implementada | Integración backend – Controlador |
| Controlador de logros | testCreateAchievementNullName | Verifica la creación de un logro con una descripción alternativa válida. | Implementada | Integración backend – Controlador |
| Controlador de logros | testModifyAchievement | Verifica la modificación de la descripción de un logro existente. | Implementada | Integración backend – Controlador |
| Controlador de logros | testDeleteAchievement | Verifica la eliminación de un logro mediante su identificador. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementsByTierFacil | Verifica la recuperación de logros pertenecientes al nivel FACIL. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementsByTierIntermedio | Verifica que la búsqueda de logros del nivel INTERMEDIO devuelve una colección vacía cuando no existen resultados. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindMultipleAchievements | Verifica la recuperación simultánea de varios logros pertenecientes a distintos niveles de dificultad. | Implementada | Integración backend – Controlador |
| Controlador de logros | testDeleteNonExistentAchievement | Verifica la invocación de la eliminación de un logro inexistente. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAllAsAdmin | Verifica que un usuario administrador puede consultar el listado completo de logros. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementsAllTiers | Verifica la recuperación de logros pertenecientes a todos los niveles de dificultad disponibles. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementsDifferentMetrics | Verifica la recuperación de logros asociados a diferentes métricas estadísticas. | Implementada | Integración backend – Controlador |
| Controlador de logros | testCreateAchievementDifferentThresholds | Verifica la creación de un logro con un umbral de progreso personalizado. | Implementada | Integración backend – Controlador |
| Controlador de logros | testUpdateAchievementThreshold | Verifica la modificación del umbral requerido para obtener un logro. | Implementada | Integración backend – Controlador |
| Controlador de logros | testUpdateAchievementTier | Verifica la modificación del nivel de dificultad asociado a un logro. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementsByTierDificil | Verifica la recuperación de logros pertenecientes al nivel DIFICIL. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindMultipleAchievementsSameTier | Verifica la recuperación de múltiples logros asociados al mismo nivel de dificultad. | Implementada | Integración backend – Controlador |
| Controlador de logros | testCreateAchievementHighThreshold | Verifica la creación de un logro con un umbral elevado y nivel de dificultad alto. | Implementada | Integración backend – Controlador |
| Controlador de logros | testFindAchievementVerifyAllFields | Verifica que la respuesta de un logro contiene todos los campos esperados. | Implementada | Integración backend – Controlador |
| Controlador de logros | testDeleteMultipleAchievements | Verifica la eliminación consecutiva de varios logros. | Implementada | Integración backend – Controlador |
| Controlador de logros | testUpdateAchievementMetric | Verifica la modificación de la métrica asociada a un logro existente. | Implementada | Integración backend – Controlador |
| Controlador de logros | testCreateAchievementsAllMetrics | Verifica la creación de logros utilizando todos los tipos de métricas disponibles en el sistema. | Implementada | Integración backend – Controlador |
| Servicio de logros | testGetAchievements | Verifica la recuperación de todos los logros almacenados en el repositorio. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetById | Verifica la obtención de un logro existente mediante su identificador. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetByIdNotFound | Verifica que la búsqueda de un logro inexistente devuelve un resultado nulo. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testSaveAchievement | Verifica el almacenamiento correcto de un logro en el repositorio. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testDeleteAchievementById | Verifica la eliminación de un logro mediante su identificador. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedVictories | Verifica que un logro basado en victorias se desbloquea cuando el usuario supera el umbral requerido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedVictories | Verifica que un logro basado en victorias no se desbloquea cuando el usuario no alcanza el umbral requerido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedGamesPlayed | Verifica que un logro basado en partidas jugadas se desbloquea cuando se alcanza el umbral definido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedGamesPlayed | Verifica que un logro basado en partidas jugadas no se desbloquea cuando el progreso es insuficiente. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedTotalPlayTime | Verifica que un logro basado en tiempo total de juego se desbloquea cuando el tiempo acumulado supera el umbral. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedActionPoints | Verifica que un logro basado en puntos de acción obtenidos se desbloquea cuando se alcanza el valor requerido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetAchievementsByTierFacil | Verifica la recuperación de logros pertenecientes a la categoría FACIL. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetAchievementsByTierDificil | Verifica la recuperación de logros pertenecientes a la categoría DIFICIL cuando no existen resultados. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedAtBoundary | Verifica que un logro se desbloquea cuando el valor alcanzado coincide exactamente con el umbral definido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedJustBelowThreshold | Verifica que un logro no se desbloquea cuando el progreso queda justo por debajo del umbral. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetAchievementsEmpty | Verifica que la recuperación de logros devuelve una lista vacía cuando no existen registros. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetMultipleAchievements | Verifica la recuperación simultánea de múltiples logros almacenados. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testSaveMultipleAchievements | Verifica el almacenamiento consecutivo de varios logros. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testDeleteMultipleAchievements | Verifica la eliminación consecutiva de varios logros diferentes. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedRoomsVisited | Verifica que un logro basado en salas visitadas no se desbloquea cuando no se alcanza el umbral requerido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedRoomsVisited | Verifica que un logro basado en salas visitadas se desbloquea cuando se supera el umbral establecido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedBattlesWon | Verifica que un logro basado en combates ganados se desbloquea cuando se alcanza el progreso requerido. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedBattlesWon | Verifica que un logro basado en combates ganados no se desbloquea cuando el progreso es insuficiente. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedZeroThreshold | Verifica que un logro con umbral igual a cero se considera desbloqueado. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedVeryHighThreshold | Verifica que un logro con un umbral extremadamente elevado no se desbloquea cuando el usuario no alcanza dicho valor. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetAchievementsByMultipleTiers | Verifica la recuperación de logros pertenecientes a diferentes categorías de dificultad. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testSaveAchievementVerifyFields | Verifica que los atributos de un logro se conservan correctamente tras su almacenamiento. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementUnlockedExactThresholdPlayTime | Verifica que un logro basado en tiempo de juego se desbloquea exactamente al alcanzar su umbral. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedActionPoints | Verifica que un logro basado en puntos de acción no se desbloquea cuando el progreso queda por debajo del umbral. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testGetByIdMultipleIds | Verifica la obtención correcta de distintos logros mediante diferentes identificadores. | Implementada | Unitaria backend – Servicio |
| Servicio de logros | testIsAchievementNotUnlockedGamesPlayedBoundary | Verifica que un logro basado en partidas jugadas no se desbloquea cuando el valor obtenido queda justo por debajo del umbral. | Implementada | Unitaria backend – Servicio |
| DTO de estadísticas generales | testDefaultConstructor | Verifica la creación de un GeneralStatisticsDTO mediante constructor por defecto y la inicialización de todos los atributos a null. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetAveragePlayersPerMatch | Verifica la asignación y recuperación de la media de jugadores por partida. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetTotalMatchesPlayed | Verifica la asignación y recuperación del número total de partidas jugadas. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetTotalBattlesDisputed | Verifica la asignación y recuperación del número total de combates disputados. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetAverageRoomsVisitedPerMatch | Verifica la asignación y recuperación de la media de salas visitadas por partida. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetAverageMatchDuration | Verifica la asignación y recuperación de la duración media de las partidas. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetLongestMatchDuration | Verifica la asignación y recuperación de la duración máxima registrada de una partida. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetShortestMatchDuration | Verifica la asignación y recuperación de la duración mínima registrada de una partida. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testSetAndGetId | Verifica la asignación y recuperación del identificador heredado de BaseEntity. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testNullValues | Verifica el manejo correcto de valores nulos en todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testGettersAndSettersConsistency | Verifica la coherencia entre los métodos getter y setter de todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testMultipleModifications | Verifica que los atributos pueden modificarse varias veces conservando el último valor asignado. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testPreciseDecimalValues | Verifica el almacenamiento de valores decimales con precisión en los campos estadísticos. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testIntegerValuesInDoubleFields | Verifica el almacenamiento de valores enteros en atributos definidos como Double. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testZeroValues | Verifica el almacenamiento correcto de valores iguales a cero en todos los campos numéricos. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testLargeValues | Verifica el almacenamiento de valores numéricos elevados en todos los atributos estadísticos. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testNegativeValues | Verifica el almacenamiento de valores negativos en los distintos atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testIndependentInstances | Verifica la independencia entre distintas instancias de GeneralStatisticsDTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas generales | testFieldIndependence | Verifica que la modificación de un atributo no altera el valor del resto de campos del DTO. | Implementada | Unitaria backend – DTO |
| Controlador de estadísticas | testGetUserStatisticsMaxValues | Verifica la obtención de estadísticas de usuario con valores elevados en todos los indicadores. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetUserStatisticsAllZeros | Verifica la obtención de estadísticas de usuario cuando todos los indicadores tienen valor cero. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetUserStatisticsAsAdmin | Verifica que un usuario con rol ADMIN puede consultar las estadísticas de un usuario. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetGeneralStatisticsAsAdmin | Verifica que un usuario con rol ADMIN puede consultar las estadísticas generales de la aplicación. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetGeneralStatisticsHighValues | Verifica la obtención de estadísticas generales con valores excepcionalmente altos. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetUserStatisticsNullActionPoints | Verifica el tratamiento de valores nulos en puntos de acción, tiempo jugado y salas visitadas al generar estadísticas de usuario. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetUserStatisticsVerifyAllCalls | Verifica que el controlador invoca todos los métodos necesarios del servicio para construir las estadísticas de usuario. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetGeneralStatisticsVerifyAllCalls | Verifica que el controlador invoca todos los métodos necesarios del servicio para construir las estadísticas generales. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetGeneralStatisticsDecimalAverages | Verifica la obtención de estadísticas generales con valores decimales en las medias calculadas. | Implementada | Integración backend – Controlador |
| Controlador de estadísticas | testGetRankingEmpty | Verifica que la consulta del ranking devuelve correctamente una lista vacía cuando no existen jugadores clasificados. | Implementada | Integración backend – Controlador |
| Servicio de estadísticas | testGetTotalVictoriesByUserNull | Verifica que el servicio devuelve cero cuando el repositorio no proporciona un valor para las victorias de un usuario. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetBattlesWonByUserNull | Verifica que el servicio devuelve cero cuando no existe información sobre combates ganados por un usuario. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetTotalBattlesDisputedNull | Verifica que el servicio devuelve cero cuando no existe información sobre combates disputados globalmente. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetTotalAccionPointsByUserNull | Verifica el comportamiento del servicio cuando el total de puntos de acción de un usuario es nulo. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetMatchesPlayedByUserEmpty | Verifica que el número de partidas jugadas es cero cuando el usuario no tiene registros asociados. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetTotalTimePlayedByUserNullMatch | Verifica que el tiempo total jugado es cero cuando los jugadores no tienen partida asociada. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetTotalTimePlayedByUserNullTimes | Verifica que el tiempo total jugado es cero cuando las partidas carecen de fecha de inicio o fin. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetAverageRoomsVisitedPerMatchWithNull | Verifica el cálculo de la media de salas visitadas cuando algunos jugadores tienen valores nulos. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetAverageMatchDuration | Verifica el cálculo de la duración media de las partidas registradas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetAverageMatchDurationEmpty | Verifica que la duración media de las partidas es cero cuando no existen partidas registradas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetBattlesPlayedByUser | Verifica la obtención del número total de combates disputados por un usuario. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetBattlesPlayedByUserNull | Verifica que el servicio devuelve cero cuando no existe información sobre combates disputados por un usuario. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetMaxRoomsVisitedInMatch | Verifica la obtención del máximo número de salas visitadas por un usuario en una partida. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetLongestMatchDuration | Verifica la obtención de la duración de la partida más larga registrada. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetShortestMatchDuration | Verifica la obtención de la duración de la partida más corta registrada. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetShortestMatchDurationEmpty | Verifica que la duración mínima de partida es cero cuando no existen partidas registradas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetPlayerTypeAggressive | Verifica la clasificación de un usuario como jugador de tipo Aggressive según sus estadísticas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetPlayerTypeExplorer | Verifica la clasificación de un usuario como jugador de tipo Explorer según sus estadísticas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetPlayerTypeBalanced | Verifica la clasificación de un usuario como jugador de tipo Balanced según sus estadísticas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetWinRateByUser | Verifica el cálculo del porcentaje de victorias de un usuario respecto al total de partidas jugadas. | Implementada | Unitaria backend – Servicio |
| Servicio de estadísticas | testGetWinRateByUserZeroMatches | Verifica que el porcentaje de victorias es cero cuando el usuario no ha jugado ninguna partida. | Implementada | Unitaria backend – Servicio |
| DTO de estadísticas de usuario | testDefaultConstructor | Verifica la creación de un UserStatisticsDTO con los valores por defecto establecidos en sus atributos. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetTotalVictories | Verifica la asignación y recuperación del número total de victorias del usuario. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetMatchesPlayed | Verifica la asignación y recuperación del número de partidas jugadas. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetTotalTimePlayed | Verifica la asignación y recuperación del tiempo total de juego acumulado. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetTotalActionPoints | Verifica la asignación y recuperación de los puntos de acción acumulados. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetBattlesWon | Verifica la asignación y recuperación del número de combates ganados. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetRoomsVisited | Verifica la asignación y recuperación del número de salas visitadas. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetDoubleFields | Verifica la asignación y recuperación de todos los campos estadísticos de tipo Double. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetTotalBattlesPlayed | Verifica la asignación y recuperación del número total de combates disputados. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetMaxRoomsVisitedInMatch | Verifica la asignación y recuperación del máximo número de salas visitadas en una partida. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetPlayerType | Verifica la asignación y recuperación del tipo de jugador calculado. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testSetAndGetId | Verifica la asignación y recuperación del identificador heredado de BaseEntity. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testNullValues | Verifica el manejo correcto de valores nulos en todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testGettersAndSettersConsistency | Verifica la coherencia entre los métodos getter y setter de todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testMultipleModifications | Verifica que los atributos pueden modificarse varias veces conservando el último valor asignado. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testZeroValues | Verifica el almacenamiento correcto de valores iguales a cero en todos los campos estadísticos. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testLargeValues | Verifica el almacenamiento de valores elevados en todos los atributos estadísticos del DTO. | Implementada | Unitaria backend – DTO |
| DTO de estadísticas de usuario | testIndependentInstances | Verifica la independencia entre distintas instancias de UserStatisticsDTO. | Implementada | Unitaria backend – DTO |
| Repositorio de autoridades | shouldSaveAndFindByIdAuthority | Verifica que una autoridad puede almacenarse correctamente y recuperarse posteriormente mediante su identificador. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldFindByNameAuthority | Verifica que una autoridad puede recuperarse correctamente mediante su nombre completo. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldFindByNamePartialMatch | Verifica que la búsqueda por nombre permite localizar una autoridad mediante una coincidencia parcial del texto. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldReturnEmptyWhenAuthoritiesByNameNotFound | Verifica que la búsqueda por nombre devuelve un resultado vacío cuando la autoridad no existe. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldFindAllAuthorities | Verifica que el repositorio recupera correctamente el conjunto de autoridades almacenadas. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldReturnEmptyWhenByIdNotFound | Verifica que la búsqueda por identificador devuelve un resultado vacío cuando la autoridad no existe. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldUpdateAuthority | Verifica que una autoridad existente puede actualizarse correctamente y que los cambios quedan persistidos. | Implementada | Integración backend – Repositorio |
| Repositorio de autoridades | shouldDeleteAuthority | Verifica que una autoridad puede eliminarse correctamente del repositorio y deja de estar disponible para futuras consultas. | Implementada | Integración backend – Repositorio |
| Servicio de autoridades | shouldFindAllAuthorities | Verifica la recuperación de todas las autoridades registradas en el sistema. | Implementada | Unitaria backend – Servicio |
| Servicio de autoridades | shouldFindAuthoritiesByAuthority | Verifica la búsqueda de una autoridad existente mediante su nombre. | Implementada | Unitaria backend – Servicio |
| Servicio de autoridades | shouldNotFindAuthoritiesByIncorrectAuthority | Verifica que se lanza una excepción cuando se busca una autoridad inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de autoridades | shouldInsertAuthorities | Verifica la creación y almacenamiento de una nueva autoridad, comprobando la asignación de identificador y el incremento del número total de registros. | Implementada | Unitaria backend – Servicio |
| DTO resumido de partidas | testConstructorWithFullData | Verifica la creación de un MiniMatchDTO a partir de una partida con estado y jugadores, copiando correctamente toda la información. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testConstructorWithNullStatus | Verifica la creación de un MiniMatchDTO cuando la partida no tiene estado asociado. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testConstructorWithNullPlayers | Verifica la creación de un MiniMatchDTO cuando la partida no tiene jugadores asociados. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testConstructorWithEmptyPlayersList | Verifica la creación de un MiniMatchDTO cuando la partida contiene una lista vacía de jugadores. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testMatchPlayerDTOConstructor | Verifica la creación de un MatchPlayerDTO a partir de un jugador y la copia correcta de sus datos. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testMatchPlayerDTOWithNullUser | Verifica la creación de un MatchPlayerDTO cuando el jugador no tiene usuario asociado. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testSetId | Verifica la modificación y recuperación del identificador de la partida en el DTO. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testSetStatus | Verifica la modificación y recuperación del estado de la partida en el DTO. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testSetPlayers | Verifica la modificación y recuperación de la lista de jugadores asociada al DTO. | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testDifferentMatchStatuses | Verifica el manejo correcto de los distintos estados posibles de una partida (WAITING, PLAYING y FINISHED). | Implementada | Unitaria backend – DTO |
| DTO resumido de partidas | testMatchPlayerDTOSetters | Verifica el funcionamiento de los métodos setter y getter de MatchPlayerDTO. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testConstructorWithMatch | Verifica la creación de un MiniUserDTO a partir de un usuario y una partida asociada, copiando correctamente todos los datos. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testConstructorWithoutMatch | Verifica la creación de un MiniUserDTO a partir de un usuario sin partida asociada. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testDefaultConstructor | Verifica la creación de un MiniUserDTO mediante constructor vacío con todos los atributos inicializados a null. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testConstructorWithNullStatus | Verifica que un usuario sin estado asociado se representa con el estado por defecto OFFLINE. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testConstructorWithNullMatch | Verifica la creación de un MiniUserDTO cuando la partida asociada es nula. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testSetUsername | Verifica la modificación y recuperación del nombre de usuario. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testSetAvatar | Verifica la modificación y recuperación del avatar del usuario. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testSetStatus | Verifica la modificación y recuperación del estado del usuario. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testSetMatch | Verifica la modificación y recuperación de la partida asociada al usuario. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testDifferentUserStatuses | Verifica el manejo correcto de los distintos estados posibles de un usuario (ONLINE, OFFLINE y PLAYING). | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testGetMatch | Verifica la recuperación correcta de la información de la partida asociada al usuario. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testGettersAndSettersConsistency | Verifica la coherencia entre los métodos getter y setter de todos los atributos del DTO. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testWithNullAvatar | Verifica la creación de un DTO cuando el usuario no tiene avatar asociado. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testWithoutUserId | Verifica la creación de un DTO a partir de un usuario sin identificador asignado. | Implementada | Unitaria backend – DTO |
| DTO resumido de usuario | testCompleteFieldCopy | Verifica la copia completa y correcta de todos los atributos de un usuario al construir el DTO. | Implementada | Unitaria backend – DTO |
| Controlador de usuarios | shouldFindAll | Verifica que el endpoint devuelve correctamente la lista completa de usuarios registrados. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldFindAllWithAuthority | Verifica que el endpoint filtra correctamente los usuarios por autoridad. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldFindAllAuths | Verifica que el endpoint devuelve correctamente la lista de autoridades disponibles. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldReturnUser | Verifica que la consulta de un usuario por identificador devuelve la información correcta cuando existe. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldReturnNotFoundUser | Verifica que la consulta de un usuario inexistente devuelve un estado de recurso no encontrado. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldUpdateUser | Verifica la actualización correcta de los datos de un usuario existente. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldReturnNotFoundUpdateUser | Verifica que la actualización de un usuario inexistente devuelve un estado de recurso no encontrado. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldDeleteOtherUser | Verifica que un usuario administrador puede eliminar correctamente otro usuario. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldNotDeleteLoggedUser | Verifica que un usuario no puede eliminar su propia cuenta cuando la operación está restringida. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldSetUserOnline | Verifica que el estado del usuario autenticado puede actualizarse correctamente a ONLINE. | Implementada | Integración backend – Controlador |
| Controlador de usuarios | shouldSetUserOffline | Verifica que el estado del usuario autenticado puede actualizarse correctamente a OFFLINE. | Implementada | Integración backend – Controlador |
| Repositorio de usuarios | saveAndFindByIdReturnsUser | Verifica que un usuario puede almacenarse correctamente y recuperarse posteriormente mediante su identificador. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | findByUsernameReturnsUser | Verifica que la búsqueda por nombre de usuario devuelve correctamente el usuario existente. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | existsByUsernameReturnsTrue | Verifica que el repositorio confirma la existencia de usuarios cuando el nombre de usuario está registrado. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | existsByEmailReturnsTrue | Verifica que el repositorio detecta correctamente la existencia de un correo electrónico registrado y devuelve falso para uno inexistente. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | findAllByAuthorityReturnsOnlyMatchingUsers | Verifica que la búsqueda por autoridad devuelve únicamente los usuarios asociados a la autoridad indicada. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | findAllReturnsAllUsers | Verifica que el repositorio recupera correctamente todos los usuarios almacenados. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | findByUsernameReturnsEmpty | Verifica que la búsqueda por nombre de usuario devuelve un resultado vacío cuando el usuario no existe. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | existsByUsernameReturnsFalse | Verifica que el repositorio indica correctamente que un nombre de usuario no existe en la base de datos. | Implementada | Integración backend – Repositorio |
| Repositorio de usuarios | findByIdReturnsEmpty | Verifica que la búsqueda por identificador devuelve un resultado vacío cuando el usuario no existe. | Implementada | Integración backend – Repositorio |
| Servicio de usuarios | shouldFindCurrentUser | Verifica la recuperación del usuario actualmente autenticado cuando existe en el sistema. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotFindCorrectCurrentUser | Verifica que se lanza una excepción cuando el usuario autenticado no existe en el sistema. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotFindAuthenticated | Verifica que se lanza una excepción cuando no existe ningún usuario autenticado. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldFindAllUsers | Verifica la recuperación de todos los usuarios registrados en el sistema. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldFindUsersByAuthority | Verifica la recuperación de usuarios filtrados por autoridad o rol. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotFindUserByIncorrectUsername | Verifica que se lanza una excepción al buscar un usuario mediante un nombre inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldFindSingleUser | Verifica la recuperación de un usuario existente mediante su identificador. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotFindSingleUserWithBadID | Verifica que se lanza una excepción al buscar un usuario mediante un identificador inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldExistUser | Verifica que el servicio detecta correctamente la existencia de un nombre de usuario registrado. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotExistUser | Verifica que el servicio indica correctamente que un nombre de usuario no existe. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldExistEmail | Verifica que el servicio detecta correctamente la existencia de una dirección de correo registrada. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotExistEmail | Verifica que el servicio indica correctamente que una dirección de correo no existe. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldUpdateUser | Verifica la actualización completa de los datos de un usuario existente y su persistencia en el sistema. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldNotUpdateUserWithInvalidId | Verifica que se lanza una excepción cuando se intenta actualizar un usuario inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldSaveUser | Verifica el almacenamiento de un nuevo usuario y el incremento del número total de registros. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldDeleteUser | Verifica la eliminación de un usuario existente y su posterior inaccesibilidad desde el servicio. | Implementada | Unitaria backend – Servicio |
| Servicio de usuarios | shouldInsertUser | Verifica la creación de un nuevo usuario comprobando la asignación de identificador y el aumento del número de usuarios registrados. | Implementada | Unitaria backend – Servicio |
| Utilidades de validación (Checkers) | checkGameStatus_IncorrectStatus_ThrowsGameIsNotALobbyException | Verifica que se lanza una excepción cuando el estado de la partida no coincide con el estado esperado. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkGameIsNotPlaying_NotPlaying_DoesNotThrow | Verifica que no se produce ninguna excepción cuando la partida no está en estado PLAYING. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkGameIsNotPlaying_IsPlaying_ThrowsAlreadyPlayingException | Verifica que se lanza una excepción cuando se intenta realizar una operación sobre una partida que ya está en juego. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkNumberOfPlayers_LobbyHasSpace_DoesNotThrow | Verifica que una sala con plazas disponibles supera correctamente la validación de capacidad. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkNumberOfPlayers_LobbyIsFull_ThrowsLobbyIsFullException | Verifica que se lanza una excepción cuando se intenta acceder a una sala que ha alcanzado su capacidad máxima. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkPlayerAlreadyInALobby_NotInLobby_DoesNotThrow | Verifica que un usuario que no pertenece a ninguna sala puede continuar el proceso sin restricciones. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkPlayerIsInTheGame_InGame_DoesNotThrow | Verifica que la validación se supera correctamente cuando el jugador pertenece a la partida indicada. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkPlayerIsInTheGame_NotInGame_ThrowsPlayerNotInTheGame | Verifica que se lanza una excepción cuando el jugador no forma parte de la partida especificada. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCanSpectateGame_PublicGame_DoesNotThrow | Verifica que un usuario puede acceder como espectador a una partida pública. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCanSpectateGame_PrivateGameAndFriendOfAll_DoesNotThrow | Verifica que un usuario puede acceder como espectador a una partida privada cuando cumple las condiciones de amistad requeridas. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCanSpectateGame_PrivateGameAndNotFriendOfAll_ThrowsGameIsNotPublicException | Verifica que se lanza una excepción cuando un usuario intenta acceder a una partida privada sin autorización. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCardExists_NullCardOrNullId_ThrowsIllegalArgumentException | Verifica que se lanza una excepción cuando se valida una carta nula o sin identificador. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCardExists_CardNotFoundInRepo_ThrowsResourceNotFoundException | Verifica que se lanza una excepción cuando la carta indicada no existe en el repositorio. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCardExists_CardFound_DoesNotThrow | Verifica que una carta existente supera correctamente la validación. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkWordIsValid_True_DoesNotThrow | Verifica que una palabra válida supera correctamente la validación. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkWordIsValid_False_ThrowsBagNotValidException | Verifica que se lanza una excepción cuando la palabra formada no es válida. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCardsDrawnInTurn_ValidDrawn_DoesNotThrow | Verifica que un jugador no excede el límite permitido de cartas robadas durante el turno. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkCardsDrawnInTurn_LimitExceeded_ThrowsMoreThan7CardsDrawnException | Verifica que se lanza una excepción cuando un jugador supera el número máximo de cartas robadas por turno. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | chechPlayerExists_NullResult_ThrowsResourceNotFoundException | Verifica que se lanza una excepción cuando el jugador solicitado no existe. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | chechPlayerExists_Exists_DoesNotThrow | Verifica que la validación se supera correctamente cuando el jugador existe. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkRoomIsAdyacent_IsAdjacent_DoesNotThrow | Verifica que el movimiento entre salas adyacentes es considerado válido. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkRoomIsAdyacent_NotAdjacent_ThrowsInvalidMovementException | Verifica que se lanza una excepción cuando se intenta mover a una sala no adyacente. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkPlayerHasActionPoints_HasPoints_DoesNotThrow | Verifica que un jugador con puntos de acción disponibles supera correctamente la validación. | Implementada | Unitaria backend – Utilidad |
| Utilidades de validación (Checkers) | checkPlayerHasActionPoints_NoPoints_ThrowsNoActionPointsException | Verifica que se lanza una excepción cuando un jugador no dispone de puntos de acción. | Implementada | Unitaria backend – Utilidad |
| Controlador de votaciones | testGetAllVotingSuccess | Verifica la recuperación correcta de las votaciones asociadas a una partida existente. | Implementada | Integración backend – Controlador |
| Controlador de votaciones | testGetAllVotingNotFound | Verifica que la consulta de votaciones devuelve un estado de recurso no encontrado cuando la partida no existe o no tiene votaciones asociadas. | Implementada | Integración backend – Controlador |
| Controlador de votaciones | testSubmitVoteSuccess | Verifica el registro correcto de un voto válido en una votación activa. | Implementada | Integración backend – Controlador |
| Controlador de votaciones | testSubmitVoteAlreadyVotedThrows | Verifica que se devuelve un conflicto cuando un jugador intenta votar más de una vez en la misma votación. | Implementada | Integración backend – Controlador |
| Controlador de votaciones | testSubmitVoteMoreVotesThanPlayersThrows | Verifica que se devuelve un error cuando se intenta registrar más votos de los permitidos por el número de jugadores participantes. | Implementada | Integración backend – Controlador |
| Controlador de votaciones | testSubmitVoteNoPendingVotingThrows | Verifica que se devuelve un estado de recurso no encontrado cuando se intenta votar en una partida sin votación pendiente. | Implementada | Integración backend – Controlador |
| Repositorio de votaciones | findByIdReturnsVoting | Verifica que una votación almacenada puede recuperarse correctamente mediante su identificador. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findAllReturnsAllVotings | Verifica que el repositorio recupera correctamente todas las votaciones almacenadas. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findPendingVotingByMatchIdNonExistingReturnsEmpty | Verifica que la búsqueda de una votación pendiente devuelve un resultado vacío cuando la partida no existe. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findPendingVotingByMatchIdReturnsVoting | Verifica que se recupera correctamente la votación pendiente asociada a una partida existente. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findPendingVotingByMatchIdIgnoresNonPending | Verifica que las votaciones cuyo estado no es PENDING no son devueltas en la búsqueda de votaciones pendientes. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findByMatchIdReturnsAllMatchVotings | Verifica que se recuperan todas las votaciones asociadas a una misma partida. | Implementada | Integración backend – Repositorio |
| Repositorio de votaciones | findByMatchIdWithRandomIdsReturnsEmpty | Verifica que la búsqueda por identificador de partida devuelve una colección vacía cuando no existen votaciones asociadas. | Implementada | Integración backend – Repositorio |
| Servicio de votaciones | getVotingsByMatchIdNoVotingsThrows | Verifica que se lanza una excepción cuando se solicitan las votaciones de una partida que no tiene votaciones asociadas. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | getVotingsByMatchIdReturnsList | Verifica la recuperación correcta de las votaciones asociadas a una partida existente. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | startVotingMatchNotFoundThrows | Verifica que se lanza una excepción cuando se intenta iniciar una votación en una partida inexistente. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | startVotingSuccess | Verifica la creación correcta de una votación, el cambio de estado de la partida a VOTING y la generación del DTO resultante. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | submitVoteNoPendingVotingThrows | Verifica que se lanza una excepción cuando se intenta votar sin existir una votación pendiente en la partida. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | submitVotePlayerNotInVotingThrows | Verifica que se lanza una excepción cuando el jugador que emite el voto no participa en la votación. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | submitVoteAlreadyVotedThrows | Verifica que se lanza una excepción cuando un jugador intenta votar más de una vez en la misma votación. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | submitVoteMoreVotesThanPlayersThrows | Verifica que se lanza una excepción cuando el número de votos supera el número de jugadores participantes. | Implementada | Unitaria backend – Servicio |
| Servicio de votaciones | submitVoteFinishesVoting | Verifica que una votación finaliza correctamente cuando se reciben todos los votos, actualizando el resultado y devolviendo la partida al estado PLAYING. | Implementada | Unitaria backend – Servicio |

## 6. Criterios de Aceptación

- Todas las pruebas unitarias deben pasar con éxito antes de la entrega final del proyecto.
- La cobertura de código debe ser al menos del 70%.
- No debe haber fallos críticos en las pruebas de integración y en la funcionalidad.

## 7. Conclusión

Este plan de pruebas establece la estructura y los criterios para asegurar la calidad del software desarrollado. Es responsabilidad del equipo de desarrollo y pruebas seguir este plan para garantizar la entrega de un producto funcional y libre de errores.
