import React, { useState } from "react";
import "../../../static/css/match/modals/returnedCardModal.css"

export default function ReturnedCardModal({ isOpen, onClose, card }) {
    if (!isOpen) return null;
    return (
        <div className="returned-start-dice-modal-overlay">
            <div className="returned-start-dice-modal-content">

                {card?.id ? (
                    <>
                    <h2 className="returned-start-dice-title">You have received this card</h2>
                    <div key={card.id}>
                        <img
                            src={`/resources${card.frontImage}`}
                            alt={`Carta ${card.letter}`}
                            className="returned-card"
                        />
                    </div>
                    </>
                ) : (
                    <h2 className="returned-start-dice-title">no recibes ninguna carta porque no hay ninguna en el monton de descartes</h2>
                )
                }
                <button onClick={onClose} className="returned-cancel-button">
                    Close
                </button>
                
            </div>
        </div>
    );
}

