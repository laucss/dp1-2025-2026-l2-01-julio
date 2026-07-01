import React from "react";

export default function OtherPlayersPanel({
    playersList,
    otherPlayersBags,
    getPlayerColor,
    players
}) {
    return (
        <div className="other-players-section">
            <div className="players-avatars-section">

                {playersList.map((p) => (

                    <div key={p.user.id} className="player-avatar-card">

                        <div className="player-bag-display">

                            {otherPlayersBags[p.id] &&
                            otherPlayersBags[p.id].length > 0 ? (

                                <div className="bag-cards-container">

                                    {otherPlayersBags[p.id].map((carta, index) => (

                                        <img
                                            key={index}
                                            src={`/resources${carta.frontImage}`}
                                            alt={`Carta ${carta.letter}`}
                                            className="player-bag-card"
                                            title={carta.letter}
                                        />

                                    ))}

                                </div>

                            ) : (

                                <p className="empty-bag">
                                    Empty Bag
                                </p>

                            )}

                        </div>

                        <div className="player-info-row">

                            <div
                                style={{
                                    borderRadius: "50%",
                                    border: `4px solid ${getPlayerColor(players || [], p.id)}`,
                                    display: "inline-block",
                                    padding: "3px",
                                    flexShrink: 0,
                                }}
                            >

                                {p.user.avatar ? (

                                    <img
                                        src={p.user.avatar}
                                        alt={`${p.user.username} avatar`}
                                        className="player-avatar-img"
                                        style={{ borderRadius: "50%" }}
                                    />

                                ) : (

                                    <img
                                        src="/Avatar_default.png"
                                        alt="Default avatar"
                                        className="player-avatar-img"
                                        style={{ borderRadius: "50%" }}
                                    />

                                )}

                            </div>

                            <p className="player-username">
                                {p.user.username}
                            </p>

                        </div>

                    </div>

                ))}

            </div>
        </div>
    );
}