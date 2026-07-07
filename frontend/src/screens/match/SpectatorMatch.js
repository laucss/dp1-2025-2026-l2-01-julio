import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import MatchBoardMap from "./components/MatchBoardMap";
import DeckSection from "./components/DeckSection";
import OtherPlayersPanel from './components/OthersPlayersSection';
import { getPlayerColor } from "./utils/playersUtil";
import FightModal from "./modals/FightModal";

export default function SpectatorMatch({ initialMatch, matchId, currentUser, jwt }) {
    const navigate = useNavigate();
    const [match, setMatch] = useState(initialMatch);
    const [stompClient, setStompClient] = useState(null);
    const [chatOpen, setChatOpen] = useState(false);
    const [otherPlayersBags, setOtherPlayersBags] = useState({});
    const [otherPlayersHands, setOtherPlayersHands] = useState({}); 
    const [isFightModalOpen, setIsFightModalOpen] = useState(false);
    const [fightAttacker, setFightAttacker] = useState(null);
    const [fightDefender, setFightDefender] = useState(null);
    const [pendingTargetRoom, setPendingTargetRoom] = useState(null);

    // 1. WebSocket para mantener al espectador al día de TODO lo que pasa
    // 1. WebSocket para mantener al espectador al día de TODO lo que pasa
    useEffect(() => {
        const client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: { 'Authorization': `Bearer ${jwt}` },
            // Dentro del useEffect del Client de stomp en SpectatorMatch.js
            onConnect: () => {
                setStompClient(client);
                
                // Tus suscripciones anteriores...
                client.subscribe(`/topic/match.${matchId}.turn`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.location`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.npc.location`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.cards`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.strength`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.actionPoints`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.end`, () => fetchMatchUpdate());

                // NUEVA SUSCRIPCIÓN DETALLADA PARA PELEAS
                client.subscribe(`/topic/match.${matchId}.fight`, async (msg) => {
                    const fightUpdate = JSON.parse(msg.body);
                    
                    if (fightUpdate.action === 'START') {
                        try {
                            // Pedimos una copia fresca de la partida para identificar los combatientes
                            const response = await fetch(`/api/v1/matches/${matchId}`, {
                                headers: { Authorization: `Bearer ${jwt}` },
                            });
                            
                            if (response.ok) {
                                const freshMatch = await response.json();
                                setMatch(freshMatch);
                                let attacker = null;
                                let defender = null;

                                // Mismo mapeo que PlayerMatch para identificar si es un jugador o un NPC
                                if (fightUpdate.isBot && (fightUpdate.attackerUsername === 'NPC' || fightUpdate.attackerUsername === 'Niall Campbell')) {
                                    attacker = freshMatch.npcs?.find(n => n.id === fightUpdate.attackerId);
                                    defender = freshMatch.players?.find(p => p.user.id === fightUpdate.defenderId);
                                } else if (fightUpdate.isBot && !(fightUpdate.attackerUsername === 'NPC' || fightUpdate.attackerUsername === 'Niall Campbell')) {
                                    attacker = freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                                    defender = freshMatch.npcs?.find(n => n.id === fightUpdate.defenderId);
                                } else {
                                    attacker = freshMatch.players?.find(p => p.user.id === fightUpdate.attackerId);
                                    defender = freshMatch.players?.find(p => p.user.id === fightUpdate.defenderId);
                                }
                                                        
                                if (attacker && defender) {
                                    setFightAttacker(attacker);
                                    setFightDefender(defender);
                                    setPendingTargetRoom(fightUpdate.roomId || fightUpdate.roomName);
                                    setIsFightModalOpen(true);
                                }
                            }
                        } catch (error) {
                            console.error('Error fetching data for spectator fight:', error);
                        }
                    } else if (fightUpdate.action === 'RESOLVE') {
                        // Cuando la pelea termina, cerramos el modal y refrescamos todo
                        setIsFightModalOpen(false);
                        setFightAttacker(null);
                        setFightDefender(null);
                        fetchMatchUpdate();
                    }
                });
            }
        });
        client.activate();
        return () => client.active && client.deactivate();
    }, [jwt, matchId]);

    const fetchMatchUpdate = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}`, {
                headers: { Authorization: `Bearer ${jwt}` }
            });
            
            if (response.ok) {
                const data = await response.json();
                setMatch(data);

                // Si hay jugadores, hacemos un fetch de sus cartas en segundo plano
                if (data.players && data.players.length > 0) {
                    const bags = {};
                    const hands = {};

                    for (const player of data.players) {
                        const cardsResponse = await fetch(`/api/v1/matches/${matchId}/${player.id}/getAllCards`, {
                            method: "GET",
                            headers: {
                                Authorization: `Bearer ${jwt}`,
                                Accept: 'application/json',
                                'Content-Type': 'application/json',
                            },
                        });
                        
                        if (cardsResponse.ok) {
                            const cardsData = await cardsResponse.json();
                            bags[player.id] = Array.isArray(cardsData.bag?.cards) ? cardsData.bag.cards : [];
                            hands[player.id] = Array.isArray(cardsData.hand?.cards) ? cardsData.hand.cards : [];
                        }
                    }
                    setOtherPlayersBags(bags);
                    setOtherPlayersHands(hands);
                }
            }
        } catch (error) {
            console.error("Error al actualizar los datos del espectador:", error);
        }
    };

    // 2. Polling de seguridad (exclusivo del espectador por si falla el WebSocket)
    useEffect(() => {
        const interval = setInterval(() => {
            fetchMatchUpdate();
        }, 4000);
        return () => clearInterval(interval);
    }, []);

    const stopSpectating = async () => {
        await fetch(`/api/v1/matches/${matchId}/StopSpectating`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${jwt}` },
        });
        navigate("/lobbies");
    }

    if (match?.status === "FINISHED") {
        return (
            <div className="match-ended">
                <div className="end-overlay">
                    <div className="end-text-box">
                        <h2>The match has ended</h2>
                        {match?.winner?.user && <p>Winner: {match.winner.user.username}</p>}
                        <button onClick={() => navigate(`/`)}>Return to main menu</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="match-container spectator-mode">
            <div className="match-board">
                <div className="player-and-decks-section">
                    <DeckSection deck={match.deck} match={match} currentUser={currentUser} canDraw={false} />
                </div>

                <MatchBoardMap
                    match={match}
                    currentUser={currentUser}
                    isSpectator={true}
                    playersList={match.players || []}
                    otherPlayersBags={otherPlayersBags}
                    move={() => {}} 
                />

                {/* NUEVO PANEL DE ESPECTADOR: Muestra a todos los jugadores en juego */}
                <div className="panel-players-and-buttons"> 
                    <OtherPlayersPanel
                        playersList={match.players || []} 
                        otherPlayersHands={otherPlayersHands}
                        otherPlayersBags={otherPlayersBags}
                        getPlayerColor={getPlayerColor}
                        players={match.players || []}
                        npcs={match.npcs || []}
                    />
                </div>

                <FightModal
                    isOpen={isFightModalOpen}
                    onClose={() => {
                        setIsFightModalOpen(false);
                        setFightDefender(null);
                        setFightAttacker(null);
                    }}
                    defender={fightDefender}
                    attacker={fightAttacker}
                    stompClient={stompClient}
                    bagCards={[]} 
                    matchData={match}
                    proposingUserId={null}
                    onVotingResultProcessed={() => {}}
                    onResolve={() => {}} 
                />
                
            </div>

            {/* Panel inferior limpio */}
            <div className="player-section">
                <div className="buttons-section" style={{ width: '100%', justifyContent: 'center' }}>
                    <div className="spectator-badge" style={{ marginRight: '20px', alignSelf: 'center', background: '#34495e', padding: '10px', borderRadius: '5px', color: '#fff' }}>
                        Modo Espectador 
                    </div>
                    <button className="leave-match-button" onClick={stopSpectating} style={{ background: '#e74c3c', color: 'white' }}>
                        Leave Match
                    </button>
                </div>
            </div>
        </div>
    );
}