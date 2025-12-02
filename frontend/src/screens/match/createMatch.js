import React, { useState } from "react";
import tokenService from "../../services/token.service";
import { useNavigate } from 'react-router-dom';
import '../../static/css/match/createMatch.css';


const jwt = tokenService.getLocalAccessToken();

export default function CreateLobby() {
  const [name, setName] = useState("");
  const [maxPlayers, setMaxPlayers] = useState(4);
  const [isPrivate, setIsPrivate] = useState(false);
  const [message, setMessage] = useState("");
  const [numNpcs, setNumNpcs] = useState(3);


  const navigate = useNavigate();

  const handleCreate = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("/api/v1/matches/lobbies", {
        method: "POST",
        headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({name,maxPlayers,numNpcs,isPrivate}),
      });

      if (!response.ok) {
        throw new Error("Error al crear la partida: " + response.status);
      }

      // Si tu endpoint devuelve el objeto del lobby creado
      const newLobby = await response.json();

      // Redirige al lobby recién creado
      navigate(`/lobby/${newLobby.id}`);

    } catch (error) {
      console.error(error);
      alert("No se pudo crear la partida");
    }
  };
  return (
  <div className="center-box">
    <div className="lobbies-overlay">
  <div className="lobbies-box">
  <div className="lobby-creation-content">
    <h1 style={{color:"black"}}>Crear Partida</h1>

    <form onSubmit={handleCreate}>
      <div>
        <label>Nombre</label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Nombre de la partida"
          required
        />
      </div>

      <div>
        <label>Número de jugadores</label>
        <div className="player-count-container">
          <button type="button" onClick={() => setMaxPlayers((prev) => Math.max(3, prev - 1))}>↓</button>
          <span>{maxPlayers}</span>
          <button type="button" onClick={() => setMaxPlayers((prev) => Math.min(6, prev + 1))}>↑</button>
        </div>
      </div>

      <div>
        <label>Número de NPCs</label>
        <div className="npc-count-container">
          <button type="button" onClick={() => setNumNpcs(prev => Math.max(3, prev - 1))}>-</button>
          <span>{numNpcs}</span>
          <button type="button" onClick={() => setNumNpcs(prev => Math.min(8,prev + 1))}>+</button>
        </div>
      </div>

      


      <div>
        <label>
          <input
            type="checkbox"
            checked={isPrivate}
            onChange={(e) => setIsPrivate(e.target.checked)}
          />
          Partida privada
        </label>
      </div>

    <div className="button-row" style={{ display: "flex", justifyContent: "center", gap: "15px", marginTop: "1rem" }}>
        <button type="submit">Crear</button>
        <button type="cancel" onClick={() => navigate("/")}>Cancelar</button>
    </div>      
    </form>

    {message && <p>{message}</p>}
  </div>
  </div>
</div>
</div>
);

}

