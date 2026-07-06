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
                    {deck.discardedCards?.length > 0 ? (
                        <img
                            src={`/resources${
                                deck.discardedCards[
                                    deck.discardedCards?.length - 1
                                ].frontImage
                            }`}
                            alt="Última carta descartada"
                            style={{ width: "150px", height: "190px" , border: "2px dashed #ccc" , borderRadius: "8px"}}
                        />
                    ) : (
                        <div className="dicard-pile">
                            Empty
                        </div>
                    )}
                </div>
            </div>

        </>
    );
}