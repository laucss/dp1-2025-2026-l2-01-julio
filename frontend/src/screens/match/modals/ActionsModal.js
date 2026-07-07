import React from "react";
import { toast } from "react-toastify";
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

    const handleAttemptEscape = () => {
        if (!canAttemptEscape) {
            toast.error('Conditions not met to attempt escape.');
            return;
        }

        onAttemptEscape && onAttemptEscape();
        onClose();
    }


    return (
        <div className="actions-modal-overlay">
            <div className="actions-modal-content">
                <h2>Actions</h2>

                <button onClick={move}>Move to adjacent room</button>
                <button onClick={moveByLetters}>Move to room with your word</button>
                <button onClick={() => { onMoveNpcRequested && onMoveNpcRequested(); onClose(); }}>Move to an NPC</button>
                <button
                    onClick={handleAttemptEscape}
                    className={!canAttemptEscape ? 'actions-escape-disabled' : ''}
                    title={!canAttemptEscape ? 'Conditions not met to attempt escape' : ''}
                >
                    Attempt Escape
                </button>

                <button className="actions-close-btn" onClick={onClose}>Close</button>
            </div>
        </div>
    );
}
