import React, { useState, useEffect, useRef } from "react";
import "../../../static/css/match/components/otherPlayersSection.css";

export default function OtherPlayersPanel({
    playersList,
    otherPlayersBags,
    otherPlayersHands,
    getPlayerColor,
    players,
    npcs
}) {
    const [selectedEntity, setSelectedEntity] = useState(null);
    const detailPanelRef = useRef(null);
    const scrollContainerRef = useRef(null);

    const handleEntityClick = (id, type) => {
        if (selectedEntity && selectedEntity.id === id && selectedEntity.type === type) {
            setSelectedEntity(null);
        } else {
            setSelectedEntity({ id, type });
        }
    };

    // Captura la rueda del ratón y desplaza horizontalmente el contenedor de avatares
    const handleWheelScroll = (e) => {
        if (scrollContainerRef.current) {
            // Detiene el scroll vertical de la página principal mientras el ratón esté aquí encima
            e.preventDefault();
            e.stopPropagation(); 
            scrollContainerRef.current.scrollLeft += e.deltaY;
        }
    };

    // Detectar clics fuera para cerrar el panel
    useEffect(() => {
        function handleClickOutside(event) {
            if (detailPanelRef.current && !detailPanelRef.current.contains(event.target)) {
                if (!event.target.closest('.player-avatar-circle-card')) {
                    setSelectedEntity(null);
                }
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, [selectedEntity]);

    let inspectedData = null;
    if (selectedEntity) {
        if (selectedEntity.type === 'player') {
            inspectedData = playersList.find(p => p.id === selectedEntity.id);
        } else if (selectedEntity.type === 'npc') {
            inspectedData = npcs.find(n => n.id === selectedEntity.id);
        }
    }

    return (
        <div className="other-players-section">
            {/* Lista horizontal de avatares */}
            <div 
                className="players-avatars-horizontal-list" 
                ref={scrollContainerRef}
                onWheel={handleWheelScroll}
            >
                {/* Render de Jugadores */}
                {playersList.map((p) => {
                    const isSelected = selectedEntity?.type === 'player' && selectedEntity?.id === p.id;
                    return (
                        <div 
                            key={`player-${p.id}`} 
                            className={`player-avatar-circle-card ${isSelected ? 'active-selection' : ''}`}
                            onClick={() => handleEntityClick(p.id, 'player')}
                        >
                            <div
                                style={{
                                    borderRadius: "50%",
                                    border: `4px solid ${getPlayerColor(players || [], p.id)}`,
                                    display: "inline-block",
                                    padding: "3px",
                                    flexShrink: 0,
                                }}
                            >
                                <img
                                    src={p.user?.avatar || "/Avatar_default.png"}
                                    alt={`${p.user?.username} avatar`}
                                    className="player-avatar-img"
                                />
                            </div>
                            <p className="player-username-short">
                                {p.user?.username}
                            </p>
                        </div>
                    );
                })}

                {/* Render de NPCs */}
                {npcs?.map((npc, index) => {
                    const isSelected = selectedEntity?.type === 'npc' && selectedEntity?.id === npc.id;
                    const borderNpcColor = npc.isNiallCampbell ? "#f23f3f" : "#666666"; 
                    const npcName = npc.isNiallCampbell ? "Niall C." : `NPC ${index + 1}`;
                    const npcAvatar = "/Avatar_default.png"; // Cambia por tu imagen correspondiente de NPC

                    return (
                        <div 
                            key={`npc-${npc.id || index}`} 
                            className={`player-avatar-circle-card ${isSelected ? 'active-selection' : ''}`}
                            onClick={() => handleEntityClick(npc.id, 'npc')}
                        >
                            <div
                                style={{
                                    borderRadius: "50%",
                                    border: `4px solid ${borderNpcColor}`,
                                    display: "inline-block",
                                    padding: "3px",
                                    flexShrink: 0,
                                }}
                            >
                                <img
                                    src={npcAvatar}
                                    alt={npcName}
                                    className="player-avatar-img"
                                />
                            </div>
                            <p className="player-username-short">
                                {npcName}
                            </p>
                        </div>
                    );
                })}
            </div>

            {/* Panel de Detalle Expandido */}
            {selectedEntity && inspectedData && (
                <div className="player-detail-panel-expanded" ref={detailPanelRef}>
                    <div className="detail-header">
                        <div className="detail-header-left">
                            <span 
                                className="detail-status-dot" 
                                style={{ 
                                    backgroundColor: selectedEntity.type === 'player' 
                                        ? getPlayerColor(players || [], inspectedData.id) 
                                        : (inspectedData.isNiallCampbell ? "#ff0000" : "#666666")
                                }}
                            />
                            <h4>
                                {selectedEntity.type === 'player' 
                                    ?  `${inspectedData.user?.username}` 
                                    : `${inspectedData.isNiallCampbell ? 'Niall Campbell' : `NPC ${inspectedData.id}`}`
                                }
                            </h4>
                        </div>
                        
                        <div className="detail-header-strength" title="Fuerza">
                            <span className="strength-icon">⚔️</span> 
                            <span className="strength-value">{inspectedData.strength}</span>
                        </div>
                    </div>
                    
                    {selectedEntity.type === 'player' ? (
                        <div className="detail-grids-container">
                            {/* Bolsa */}
                            <div className="detail-section-box">
                                <h5>Bolsa</h5>
                                <div className="cards-display-zone">
                                    {otherPlayersBags[inspectedData.id] && otherPlayersBags[inspectedData.id].length > 0 ? (
                                        <div className="bag-cards-container">
                                            {otherPlayersBags[inspectedData.id].map((carta, index) => (
                                                <img
                                                    key={index}
                                                    src={`/resources${carta.frontImage}`}
                                                    alt={`Carta ${carta.letter}`}
                                                    className="inspected-player-card"
                                                    title={carta.letter}
                                                />
                                            ))}
                                        </div>
                                    ) : (
                                        <p className="empty-zone-text">Bolsa vacía</p>
                                    )}
                                </div>
                            </div>

                            {/* Mano */}
                            <div className="detail-section-box">
                                <h5>Mano</h5>
                                <div className="cards-display-zone">
                                    {otherPlayersHands[inspectedData.id] && otherPlayersHands[inspectedData.id].length > 0 ? (
                                        <div className="bag-cards-container">
                                            {otherPlayersHands[inspectedData.id].map((carta, index) => (
                                                <img
                                                    key={index}
                                                    src={`/resources${carta.frontImage}`}
                                                    alt={`Carta ${carta.letter}`}
                                                    className="inspected-player-card"
                                                    title={carta.letter}
                                                />
                                            ))}
                                        </div>
                                    ) : (
                                        <p className="empty-zone-text">Sin cartas en mano</p>
                                    )}
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="npc-detail-container">
                            <p className="npc-info-text">
                                📍 <strong>Ubicación:</strong> Habitación {inspectedData.room?.name || `ID: ${inspectedData.room?.id || 'Desconocida'}`}
                            </p>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}