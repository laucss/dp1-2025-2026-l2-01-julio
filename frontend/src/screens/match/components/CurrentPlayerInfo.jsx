import React from "react";

export default function CurrentPlayerInfo({
    currentPlayer,
    actionPoints,
    strength,
    getPlayerColor,
    players
}) {

    return (
        <div className="current-player-info">
            <div
                style={{
                    borderRadius: "50%",
                    border: `4px solid ${getPlayerColor(players || [], currentPlayer?.id)}`,
                    display: "flex",
                    flexShrink: 0,
                    boxSizing: "border-box",
                    alignItems: "center",
                    justifyContent: "center",
                }}
            >
                {currentPlayer?.user?.avatar ? (
                    <img
                        src={currentPlayer.user.avatar}
                        alt={`${currentPlayer.user.username} avatar`}
                        className="current-avatar-img"
                        style={{ borderRadius: "50%" }}
                    />
                ) : (
                    <img
                        src="/Avatar_default.png"
                        alt="Default avatar"
                        className="current-avatar-img"
                        style={{ borderRadius: "50%" }}
                    />
                )}
            </div>

            <p className="player-username">
                {currentPlayer?.user?.username}
            </p>

            <div className="points-section">

                <div className="action-points">
                    <h1>{actionPoints}</h1>
                    <p>Action points</p>
                </div>

                <div className="action-points">
                    <h1>{strength}</h1>
                    <p>Strength</p>
                </div>

            </div>

        </div>
    );
}