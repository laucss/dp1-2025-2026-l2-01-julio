import React from 'react';
import '../../static/css/match/discardModal.css';

export default function FightModal({ isOpen, onClose, opponent, onResolve }) {
    if (!isOpen) return null;

    //TODO: Configurar el modal de pelea
    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content-wrapper" onClick={(e) => e.stopPropagation()}>
                <h2>¡Encuentro!</h2>
                <p>Has entrado en la misma habitación que <b>{opponent?.user?.username || 'otro jugador'}</b>.</p>
                <p>Elige quién gana la pelea:</p>
                <div style={{ display: 'flex', gap: '8px', justifyContent: 'flex-end' }}>
                    <button onClick={() => onResolve(true)}>Tú ganas</button>
                    <button onClick={() => onResolve(false)}>{opponent?.user?.username || 'Oponente'} gana</button>
                    <button onClick={onClose}>Cerrar</button>
                </div>
            </div>
        </div>
    );
}
