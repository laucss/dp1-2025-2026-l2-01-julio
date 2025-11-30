import React from "react";
import "./actionsModal.css";

export default function ActionsModal({ isOpen, onClose }) {
    if (!isOpen) return null;

    return (
        <div className="actions-modal-overlay">
            <div className="actions-modal-content">
                <h2>Acciones</h2>

                <button>Mover a habitación adyacente</button>
                <button>Mover a habitación con tu palabra</button>
                <button>Mover a un jugador</button>
                <button>Intentar escapar</button>

                <button className="close-btn" onClick={onClose}>Cerrar</button>
            </div>
        </div>
    );
}
