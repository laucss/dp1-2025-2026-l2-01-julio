import React from "react";

export default function DeckSection({
    numCardsDrawn,
    drawCard,
    canDraw,
    deck,
    match,
    currentUser,
}) {
    return (
        <>
            <div className="deck-row">
                <button
                    onClick={() => {
                        if (numCardsDrawn < 7) {
                            drawCard();
                        } else {
                            alert("No puedes robar más de 7 cartas");
                        }
                    }}
                    disabled={!canDraw}
                    style={{
                        border: "none",
                        background: "transparent",
                        padding: 0,
                        cursor: !canDraw ? "not-allowed" : "pointer",
                        opacity: !canDraw ? 0.4 : 1,
                        outline: "none",
                    }}
                >
                    <img
                        src="/backCard.png"
                        alt="Robar carta"
                        className="deck-pile"
                    />
                </button>

                <div className="discard-pile-section">
                    {deck.discardedCards.length > 0 ? (
                        <img
                            src={`/resources${
                                deck.discardedCards[
                                    deck.discardedCards.length - 1
                                ].frontImage
                            }`}
                            alt="Última carta descartada"
                            style={{ width: "150px", height: "auto" }}
                        />
                    ) : (
                        <div className="dicard-pile">
                            Empty
                        </div>
                    )}
                </div>
            </div>

            <div className="turn-banner">
                {match?.currentTurnUserId === currentUser?.id ? (
                    <div className="turn-message turn-mine">
                        ¡Es tu turno!
                    </div>
                ) : (
                    <div className="turn-message turn-others">
                        Turno de:{" "}
                        {match?.players?.find(
                            (p) => p.user.id === match.currentTurnUserId
                        )?.user.username || "Esperando..."}
                    </div>
                )}
            </div>
        </>
    );
}