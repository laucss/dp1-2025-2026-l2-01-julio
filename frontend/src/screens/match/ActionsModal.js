import React from "react";
import "../../static/css/match/actionsModal.css";

export default function ActionsModal({ isOpen, onClose, moveToAdyacent, onMoveNpcRequested }) {
    if (!isOpen) return null;

    const move = () => {
        moveToAdyacent()
        onClose()
    }

    return (
        <div className="actions-modal-overlay">
            <div className="actions-modal-content">
                <h2>Acciones</h2>

                <button onClick={move}>Mover a habitación adyacente</button>
                <button>Mover a habitación con tu palabra</button>
                <button onClick={() => { onMoveNpcRequested && onMoveNpcRequested(); onClose(); }}>Mover a un NPC</button>
                <button>Intentar escapar</button>

                <button className="close-btn" onClick={onClose}>Cerrar</button>
            </div>
        </div>
    );
}
