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
      const response = await fetch("/api/v1/lobbies", {
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

      <button 
        className="back-arrow-btn"
        onClick={() => navigate('/')}
        >
        ￩
    </button>
  <div className="creation-box">


  <div className="lobby-creation-content">
    <h1>Create Match</h1>

    <form onSubmit={handleCreate}>
      <div>
        <label>Name</label>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Match name"
          required
        />
      </div>

      <div>
        <label>Number of Players</label>
        <div className="player-count-container">
          <button className="circle-btn" type="button" onClick={() => setMaxPlayers((prev) => Math.max(3, prev - 1))}>-</button>
          <span>{maxPlayers}</span>
          <button className="circle-btn" type="button" onClick={() => setMaxPlayers((prev) => Math.min(6, prev + 1))}>+</button>
        </div>
      </div>

      <div>
        <label>Number of NPCs</label>
        <div className="player-count-container">
          <button className="circle-btn" type="button" onClick={() => setNumNpcs(prev => Math.max(3, prev - 1))}>-</button>
          <span>{numNpcs}</span>
          <button className= "circle-btn" type="button" onClick={() => setNumNpcs(prev => Math.min(8,prev + 1))}>+</button>
        </div>
      </div>

      


      <div>
        <label>
          <input
            type="checkbox"
            checked={isPrivate}
            onChange={(e) => setIsPrivate(e.target.checked)}
          />
          Private Match
        </label>
      </div>

    <div className="button-row" style={{ display: "flex", justifyContent: "center", gap: "15px", marginTop: "1rem" }}>
        <button type="submit">Create</button>
        <button type="cancel" onClick={() => navigate("/")}>Cancel</button>
    </div>      
    </form>

    {message && <p>{message}</p>}
  </div>
  </div>
</div>
</div>
);

}

