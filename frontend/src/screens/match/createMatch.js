import React, { useState } from "react";
import tokenService from "../../services/token.service";
import { useNavigate } from 'react-router-dom';
import './createMatch.css';


const jwt = tokenService.getLocalAccessToken();

export default function CreateLobby() {
  const [name, setName] = useState("");
  const [maxPlayers, setMaxPlayers] = useState(4);
  const [isPrivate, setIsPrivate] = useState(false);
  const [message, setMessage] = useState("");

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
        body: JSON.stringify({
          name: name,
          maxPlayers: maxPlayers,
          isPrivate: isPrivate,
        }),
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
  <div className="lobby-creation-content">
    <h1>Crear Partida</h1>

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
        <label>
          <input
            type="checkbox"
            checked={isPrivate}
            onChange={(e) => setIsPrivate(e.target.checked)}
          />
          Partida privada
        </label>
      </div>

      <button type="submit">Crear</button>
    </form>

    {message && <p>{message}</p>}
  </div>
</div>
);

}

