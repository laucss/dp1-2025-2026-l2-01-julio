import React, {useState, useEffect, useRef} from "react";
import { useParams, useNavigate } from "react-router-dom";
import useFetchState from "../../util/useFetchState";
import '../../static/css/home/waitingRoom.css';
import { Button, Table } from "reactstrap";
import tokenService from "../../services/token.service";

const jwt = tokenService.getLocalAccessToken();
const currentUser = tokenService.getUser();

export default function WaitingRoom() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [countdown, setCountdown] = useState(null);
  const countdownRef = useRef(null);
  const userId = tokenService.getUser().id;

  const [lobby, setLobby] = useFetchState(
      [],
      `/api/v1/matches/lobbies/${matchId}`,
      jwt,
      setMessage,
      setVisible
    );



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


const startGame = async () => {
  try {
    const response = await fetch(`/api/v1/matches/lobbies/${matchId}/start`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Error al iniciar la partida");
    }

    // Opcional: si el endpoint devuelve el match actualizado
    const match = await response.json();

    // Redirigir a la pantalla de juego
    navigate(`/match/${matchId}`);
  } catch (error) {
    alert("No se pudo iniciar la partida: " + error.message);
  }
};
  

  const playerUsernames = lobby.players ? lobby.players.map((player) => (
    <tr key={player.user.id}>
      <td> {player.user.avatar ? <img src={player.user.avatar} alt={`${player.user.username}'s avatar`} className="player-avatar" /> : "/Avatar_default.png"}{player.user.username}</td>
    </tr>
  )) : [];

  const isCreator = currentUser && lobby.creatorId === currentUser.id;
  const canStart = lobby.players && lobby.players.length >= lobby.minPlayers;

  return (
    <div className="waiting-room-background">
      <div className="waiting-room-overlay">
        <div className="waiting-room-box">
          <h1>Espere a que la partida comience...</h1>
          <p>Lobby ID: {matchId}</p>
          <Table aria-label="lobbies" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Jugadores en el Lobby</th>
              </tr>
            </thead>
            <tbody>
            {playerUsernames}
            </tbody>
          </Table>

          {lobby.isPrivate && <div>
            <h5> Código de la partida: {lobby.code} </h5>
          </div>
          }
          

          {isCreator && (
            <Button
              color="success"
              disabled={!canStart} // deshabilitado si no hay suficientes jugadores
              style={{ marginTop: "1.5rem", padding: "0.7rem 1.5rem", fontWeight: "bold" }}
              onClick={startGame}
            >
              Comenzar Partida
            </Button>
          )}



          {countdown !== null && (
            <div style={{ marginTop: '1rem', textAlign: 'center' }}>
              <h4>La partida comienza en: {countdown} s</h4>
            </div>
          )}

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
