import React from "react";
import { useParams } from "react-router-dom";
import '../../static/css/home/waitingRoom.css';
import { Button } from "reactstrap";

export default function WaitingRoom() {
  const {matchId} = useParams();

  return (
    <div className="waiting-room-background">
      <div className="waiting-room-overlay">
        <div className="waiting-room-box">
          <h1>Espere a que la partida comience...</h1>
          <p>Lobby ID: {matchId} </p>
        <Button
          color="danger"
          style={{ marginTop: "1.5rem", padding: "0.7rem 1.5rem", fontWeight: "bold" }}
        >
          Salir del Lobby
        </Button>

        </div>
      </div>
    </div>
  );
}
