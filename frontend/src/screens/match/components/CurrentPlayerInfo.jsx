import React from "react";
import "../../../static/css/match/currentPlayerInfo.css"

export default function CurrentPlayerInfo({
    currentPlayer,
    actionPoints,
    strength,
    getPlayerColor,
    players,
    match,
    currentUser
}) {
    // Calculamos si es el turno del jugador actual
    const isMyTurn = match?.currentTurnUserId === currentUser?.id;
    
    // Obtenemos el nombre del jugador que tiene el turno
    const currentTurnUsername = match?.players?.find(
        (p) => p.user?.id === match.currentTurnUserId
    )?.user?.username || "Esperando...";

    return (
        <div className="current-player-panel-container">
            <div className="player-avatar-and-username">
                {/* IZQUIERDA: Avatar */}
                <div
                    className="player-avatar-container"
                    style={{
                        border: `4px solid ${getPlayerColor(players || [], currentPlayer?.id)}`,
                    }}
                >
                    <img
                        src={currentPlayer?.user?.avatar ? currentPlayer.user.avatar : "/Avatar_default.png"}
                        alt={`${currentPlayer?.user?.username || 'Default'} avatar`}
                        className="current-avatar-img"
                    />
                </div>
                <p className="player-username">
                    {currentPlayer?.user?.username}
                </p>
            </div>

            {/* DERECHA: Datos del Jugador */}
            <div className="player-data-column">
                

                {/* Sección de Puntos */}
                <div className="points-section-row">
                    <div className="action-points-card">
                        <h1>{actionPoints}</h1>
                        <p>Action points</p>
                    </div>

                    <div className="action-points-card">
                        <h1>{strength}</h1>
                        <p>Strength</p>
                    </div>
                </div>

                {/* DEBAJO DE LOS PUNTOS: Banner de Turno */}
                <div className="panel-turn-container">
                    {isMyTurn ? (
                        <div className="panel-turn-message turn-mine">
                            ¡Es tu turno!
                        </div>
                    ) : (
                        <div className="panel-turn-message turn-others">
                            Turno de: {currentTurnUsername}
                        </div>
                    )}
                </div>
            </div>

        </div>
    );
}