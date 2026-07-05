import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Client } from '@stomp/stompjs';
import '../../static/css/match/Match.css';
import MatchBoardMap from "./components/MatchBoardMap";
import DeckSection from "./components/DeckSection";
import ChatBox from "./chatBox";
import { FaComments } from "react-icons/fa";

export default function SpectatorMatch({ initialMatch, matchId, currentUser, jwt }) {
    const navigate = useNavigate();
    const [match, setMatch] = useState(initialMatch);
    const [stompClient, setStompClient] = useState(null);
    const [chatOpen, setChatOpen] = useState(false);
    const [otherPlayersBags, setOtherPlayersBags] = useState({});

    // 1. WebSocket para mantener al espectador al día de TODO lo que pasa
    useEffect(() => {
        const client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: { 'Authorization': `Bearer ${jwt}` },
            onConnect: () => {
                setStompClient(client);
                // El espectador se suscribe a los cambios de turno para refrescar la info completa
                client.subscribe(`/topic/match.${matchId}.turn`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.location`, () => fetchMatchUpdate());
                client.subscribe(`/topic/match.${matchId}.npc.location`, () => fetchMatchUpdate());
            }
        });
        client.activate();
        return () => client.active && client.deactivate();
    }, [jwt, matchId]);

    const fetchMatchUpdate = async () => {
        const response = await fetch(`/api/v1/matches/${matchId}`, {
            headers: { Authorization: `Bearer ${jwt}` }
        });
        if (response.ok) {
            const data = await response.json();
            setMatch(data);
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
                    {/* El deck section se renderiza estático o deshabilitado */}
                    <DeckSection deck={match.deck} match={match} currentUser={currentUser} canDraw={false} />
                </div>

                <MatchBoardMap
                    match={match}
                    currentUser={currentUser}
                    isSpectator={true}
                    playersList={match.players || []}
                    otherPlayersBags={otherPlayersBags}
                    move={() => {}} // No hace nada si un espectador clickea el mapa
                />
            </div>

            {/* Panel inferior limpio: solo botón de salir y chat */}
            <div className="player-section">
                <div className="buttons-section" style={{ width: '100%', justifyContent: 'center' }}>
                    <div className="spectator-badge" style={{ marginRight: '20px', alignSelf: 'center', background: '#34495e', padding: '10px', borderRadius: '5px', color: '#fff' }}>
                        Modo Espectador 👀
                    </div>
                    <button className="leave-match-button" onClick={stopSpectating} style={{ background: '#e74c3c', color: 'white' }}>
                        Leave Match
                    </button>
                </div>
            </div>
        </div>
    );
}