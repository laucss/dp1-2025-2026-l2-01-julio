export async function handlePlayerFight({
    attackerWins,
    fightAttacker,
    fightDefender,
    match,
    currentUser,
    moveLoserToRandomRoom,
    movePlayerToRoom,
    drawCardForWinner,
    notifyFightResolved,
    getRandomFreeRoom,
    normalizeRoomId,
    defenderRoomId,
    fetchConsumeAllActionPoints,
    setStealLoserPlayerId,
    setIsStealModalOpen
}) {

    const loserUser = attackerWins
        ? fightDefender.user
        : fightAttacker.user;

    const winnerUser = attackerWins
        ? fightAttacker.user
        : fightDefender.user;

    const winnerPlayer = match.players.find(
        p => p.user.id === winnerUser.id
    );

    const winnerRoomId = normalizeRoomId(
        winnerPlayer?.currentRoom?.id ||
        winnerPlayer?.roomId
    );

    const randomRoomId =
        getRandomFreeRoom(match, winnerRoomId);

    const moveResult =
        await moveLoserToRandomRoom(
            loserUser.id,
            randomRoomId
        );

    if (!moveResult) return;

    if (loserUser.id === match.currentTurnUserId) {
        await fetchConsumeAllActionPoints(loserUser.id);
    }

    if (attackerWins && defenderRoomId) {
        await movePlayerToRoom(
            fightAttacker.user.id,
            defenderRoomId
        );
    }

    const winnerPlayerId = attackerWins
        ? fightAttacker.id
        : fightDefender.id;

    const loserPlayerId = attackerWins
        ? fightDefender.id
        : fightAttacker.id;

    const drawSuccess =
        await drawCardForWinner(winnerPlayerId);

    if (!drawSuccess) return;

    await notifyFightResolved(
        winnerUser.id,
        winnerPlayerId,
        loserPlayerId
    );

    if (
        currentUser.id === winnerUser.id
    ) {
        setStealLoserPlayerId(loserPlayerId);
        setIsStealModalOpen(true);
    }
}

export async function handleNpcFight({
    attackerWins,
    fightAttacker,
    fightDefender,
    match,
    defenderRoomId,
    currentPlayer,
    handCards,
    bagCards,
    drawCardForWinner,
    movePlayerToRoom,
    moveLoserToRandomRoom,
    getRandomFreeRoom,
    fetchMatchAndPlayers,
    setDeck,
    setIsNpcLossModalOpen,
    matchId,
    jwt,
}) {
    try {

        if (attackerWins) {

            if (fightDefender?.isNiallCampbell) {

                const response = await fetch(
                    `/api/v1/matches/${matchId}/${fightAttacker.id}/playerWinsNiallCampbell`,
                    {
                        method: "POST",
                        headers: {
                            Authorization: `Bearer ${jwt}`,
                            Accept: "application/json",
                            "Content-Type": "application/json",
                        },
                    }
                );

                const data = await response.json();

                setDeck(data.deck);

            } else {

                await drawCardForWinner(fightAttacker.id);
                await drawCardForWinner(fightAttacker.id);

            }

            if (defenderRoomId) {

                await movePlayerToRoom(
                    fightAttacker.user.id,
                    defenderRoomId
                );

            }

            const botRandomRoomId =
                getRandomFreeRoom(match, defenderRoomId);

            await fetch(
                `/api/v1/matches/${matchId}/npc/location`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        npcId: fightDefender.id,
                        roomId: botRandomRoomId,
                    }),
                }
            );

            await fetchMatchAndPlayers();

            await fetch(
                `/api/v1/matches/${matchId}/npc-strength`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        npcId: fightDefender.id,
                        strength:
                            (fightDefender.strength || 1) + 1,
                    }),
                }
            );

        } else {

            const randomRoom =
                getRandomFreeRoom(match, defenderRoomId);

            await moveLoserToRandomRoom(
                currentPlayer.id,
                randomRoom
            );

            await fetch(
                `/api/v1/matches/${matchId}/player-strength`,
                {
                    method: "PUT",
                    headers: {
                        Authorization: `Bearer ${jwt}`,
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        userId: currentPlayer[0].id,
                        strength:
                            (currentPlayer[0].strength || 1) + 1,
                    }),
                }
            );

            if (
                handCards.length > 0 ||
                bagCards.length > 0
            ) {
                setIsNpcLossModalOpen(true);
            }

        }

    } catch (err) {

        console.error(err);

    }
}