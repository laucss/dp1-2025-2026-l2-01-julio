import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import '../../static/css/home/waitingRoom.css';
import { Button } from "reactstrap";
import tokenService from "../../services/token.service";

export default function WaitingRoom() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const jwt = tokenService.getLocalAccessToken();

  const leaveLobby = async () => {
    try {
      const response = await fetch(`/api/v1/matches/lobbies/${matchId}/leave`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Error al salir del lobby");
      }

      navigate("/lobbies");
    } catch (error) {
      alert("No se pudo salir del lobby: " + error.message);
    }
  };

  return (
    <div className="waiting-room-background">
      <div className="waiting-room-overlay">
        <div className="waiting-room-box">
          <h1>Espere a que la partida comience...</h1>
          <p>Lobby ID: {matchId}</p>

          <Button
            color="danger"
            onClick={leaveLobby}           
            style={{ marginTop: "1.5rem", padding: "0.7rem 1.5rem", fontWeight: "bold" }}
          >
            Salir del Lobby
          </Button>
        </div>
      </div>
    </div>
  );
}
