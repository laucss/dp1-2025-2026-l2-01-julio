import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import tokenService from "../../services/token.service";
import getIdFromUrl from '../../util/getIdFromUrl';
import PlayerMatch from "./PlayerMatch";
import SpectatorMatch from "./SpectatorMatch";

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function Match() {
    const matchId = getIdFromUrl(2);
    const navigate = useNavigate();
    const [match, setMatch] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchMatchInitial = async () => {
        try {
            const response = await fetch(`/api/v1/matches/${matchId}`, {
                headers: { Authorization: `Bearer ${jwt}`, 'Content-Type': 'application/json' }
            });
            if (response.ok) {
                const data = await response.json();
                setMatch(data);
            } else {
                navigate('/');
            }
        } catch (error) {
            console.error("Error cargando la partida:", error);
            navigate('/');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchMatchInitial();
    }, [matchId]);

    if (loading) return <div>Cargando partida...</div>;
    if (!match) return <div>No se encontró la partida.</div>;

    // Determinamos el rol una sola vez en el nivel superior
    const isSpectator = match.spectators?.some(s => s.id === currentUser.id);

    return isSpectator 
        ? <SpectatorMatch initialMatch={match} matchId={matchId} currentUser={currentUser} jwt={jwt} />
        : <PlayerMatch initialMatch={match} matchId={matchId} currentUser={currentUser} jwt={jwt} />;
}