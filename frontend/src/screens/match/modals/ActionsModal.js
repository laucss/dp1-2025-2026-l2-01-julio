import React from "react";
import "../../../static/css/match/modals/actionsModal.css";

export default function ActionsModal({ isOpen, onClose, moveToAdyacent, moveToRoomWithWord, onMoveNpcRequested, onAttemptEscape, canAttemptEscape }) {
    if (!isOpen) return null;

    const move = () => {
        moveToAdyacent()
        onClose()
    }

    const moveByLetters = () => {
        moveToRoomWithWord()
        onClose()
    }


    return (
        <div className="actions-modal-overlay">
            <div className="actions-modal-content">
                <h2>Acciones</h2>

                <button onClick={move}>Mover a habitación adyacente</button>
                <button onClick={moveByLetters}>Mover a habitación con tu palabra</button>
                <button onClick={() => { onMoveNpcRequested && onMoveNpcRequested(); onClose(); }}>Mover a un NPC</button>
                <button
                    onClick={() => { if (canAttemptEscape) { onAttemptEscape && onAttemptEscape(); onClose(); } }}
                    disabled={!canAttemptEscape}
                    title={!canAttemptEscape ? 'Debes estar en una de las cuatro torres para intentar escapar' : ''}
                >
                    Intentar escapar
                </button>

                <button className="actions-close-btn" onClick={onClose}>Cerrar</button>
            </div>
        </div>
    );
}
