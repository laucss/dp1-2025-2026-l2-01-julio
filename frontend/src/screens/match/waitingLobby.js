import React, {useState, useEffect, useRef} from "react";
import { useParams, useNavigate } from "react-router-dom";
import useFetchState from "../../util/useFetchState";
import '../../static/css/home/waitingRoom.css';
import { Button, Table } from "reactstrap";
import tokenService from "../../services/token.service";

const jwt = tokenService.getLocalAccessToken();

export default function WaitingRoom() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [countdown, setCountdown] = useState(null);
  const countdownRef = useRef(null);
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

  const playerUsernames = lobby.players ? lobby.players.map((player) => (
    <tr key={player.user.id}>
      <td className="text-center">{player.user.username}</td>
    </tr>
  )) : [];

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
