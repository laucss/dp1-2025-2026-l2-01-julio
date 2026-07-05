import { toast } from "react-toastify"

/**
 * RESUELVE UN COMBATE JUGADOR VS JUGADOR
 * Envía el resultado al backend y gestiona el modal visual local de robo de cartas.
 */
export async function handlePlayerFight({
    matchId,
    jwt,
    attackerWins,
    fightAttacker,
    fightDefender,
    defenderRoomId,
    currentUser,
    setStealLoserPlayerId,
    setIsStealModalOpen
}) {
    try {
        const response = await fetch(`/api/v1/fights/${matchId}/fight/resolve`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                matchId: matchId,
                attackerId: fightAttacker.id,
                defenderId: fightDefender.id,
                isNpcFight: false,
                isNpcAttacker: false,
                attackerWins: attackerWins,
                defenderRoomId: defenderRoomId
            }),
        });

        if (!response.ok) throw new Error("Error resolviendo combate JvJ en el servidor");

        // Fase interactiva visual en React: abrir modal de robo si el usuario actual ganó
        const winnerUser = attackerWins ? fightAttacker.user : fightDefender.user;
        const loserPlayerId = attackerWins ? fightDefender.id : fightAttacker.id;

        console.log('currentUser', currentUser.id)
        console.log('winnerUser', winnerUser.id)
        if (currentUser.id === winnerUser.id) {
            console.log('ENTRENDO EN BEATS PLAYER', currentUser.id === winnerUser.id)
            setStealLoserPlayerId(loserPlayerId);
            setIsStealModalOpen(true);
        }

    } catch (err) {
        console.error("Error en handlePlayerFight:", err);
    }
}

/**
 * RESUELVE UN COMBATE CONTRA UN NPC (Humano vs Bot o Bot vs Humano)
 * Envía el DTO unificado al servidor y gestiona el modal visual de descarte si el humano pierde.
 */
export async function handleNpcFight({
    matchId,
    jwt,
    attackerWins,
    fightAttacker,
    fightDefender,
    npcIsAttacker,
    defenderRoomId,
    handCards,
    bagCards,
    setIsNpcLossModalOpen
}) {
    console.log('attacker',fightAttacker)
    try {
        // Mapeamos los IDs tal cual llegan desde la interfaz (manteniendo el rol de atacante/defensor original)
        const response = await fetch(`/api/v1/fights/${matchId}/fight/resolve`, {
            method: "PUT",
            headers: {
                Authorization: `Bearer ${jwt}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                matchId: matchId,
                attackerId: fightAttacker.id,
                defenderId: fightDefender.id,
                isNpcFight: true,
                isNpcAttacker: npcIsAttacker,
                attackerWins: attackerWins,
                defenderRoomId: defenderRoomId
            }),
        });

        if (!response.ok) throw new Error("Error resolviendo la pelea NPC en el servidor");

        const data = await response.json()
        console.log('card returned', data)

        // Fase interactiva visual en React: si el humano pierde y tiene cartas, abrir descarte
        const humanWins = npcIsAttacker ? !attackerWins : attackerWins;

        if (data.fightResultType === 'PLAYER_BEATS_NIALL' && humanWins) {
            toast.info('no recibes ninguna carta porque no hay ninguna en el monton de descartes')
        }

        if (!humanWins && (handCards.length > 0 || bagCards.length > 0)) {
            setIsNpcLossModalOpen(true);
        } else if (!humanWins && (handCards.length === 0 && bagCards.length === 0) && data.fightResultType === 'NPC_BEATS_PLAYER'){
            toast.info("you've lost but because you dont have any card, you cannot discard")
        }

    } catch (err) {
        console.error("Error en handleNpcFight:", err);
    }
}