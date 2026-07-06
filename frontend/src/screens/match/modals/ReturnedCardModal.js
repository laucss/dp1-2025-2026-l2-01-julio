import React, { useState } from "react";
import '../../../static/css/match/modals/startDiceModal.css';
import tokenService from '../../../services/token.service';
import getIdFromUrl from '../../../util/getIdFromUrl';

export default function ReturnedCardModal({ isOpen, onClose, card }) {
    const jwt = tokenService.getLocalAccessToken();
    const currentUser = tokenService.getUser();
    const matchId = getIdFromUrl(2);

    return (
        <div className="start-dice-modal-overlay">
            <div className="start-dice-modal-content">

                {card?.id ? (
                    <>
                    <h2 className="start-dice-title">You have received this card</h2>
                    <div key={card.id}>
                        <img
                            src={`/resources${card.frontImage}`}
                            alt={`Carta ${card.letter}`}
                            className="card"
                        />
                    </div>
                    </>
                ) : (
                    <h2 className="start-dice-title">no recibes ninguna carta porque no hay ninguna en el monton de descartes</h2>
                )
                }
                
            </div>
        </div>
    );
}

