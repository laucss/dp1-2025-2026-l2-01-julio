import React, { useState, useEffect, useRef } from "react";
import { FaUserPlus } from "react-icons/fa";
import { useParams, useNavigate } from "react-router-dom";
import { Client } from '@stomp/stompjs';
import "../../static/css/home/waitingRoom.css";
import { Button, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import OnlineFriendsModal from "./modals/OnlineFriendsModal";
import { FaRegEye, FaRegCopy } from "react-icons/fa";

import { toast } from "react-toastify";


export default function WaitingRoom() {
  const { matchId } = useParams()
  const navigate = useNavigate()
  const jwt = tokenService.getLocalAccessToken()
  const currentUser = tokenService.getUser()
  const [lobby, setLobby] = useState({})
  const [copiedCode, setCopiedCode] = useState(false)

  const [showFriendsModal, setShowFriendsModal] = useState(false)
  const [stompClient, setStompClient] = useState(null)

  const [showSpectators, setShowSpectators] = useState(false)

  useEffect(() => {
    const fetchLobby = async () => {
      const res = await fetch(`/api/v1/lobbies/${matchId}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });

      const data = await res.json();
      console.log("Fetched lobby data:", data);
      setLobby(data);
    };

    fetchLobby();
  }, [matchId, jwt]);

  // Inicializar conexión WebSocket para el lobby
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: { 'Authorization': `Bearer ${jwt}` },
      onConnect: () => setStompClient(client)
    })

    client.activate();
    return () => client.active && client.deactivate();
  }, [jwt]);

  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const subscription = stompClient.subscribe(`/topic/lobby.${matchId}.updates`, (msg) => {
      const update = JSON.parse(msg.body);
      
      setLobby(prevLobby => {
        const nextLobby = { ...prevLobby };

        // Si el mensaje trae jugadores, actualiza la lista de jugadores
        if (update.players) {
          nextLobby.players = update.players.map(p => ({
            user: { id: p.userId, username: p.username, avatar: p.avatar }
          }));
        }

        if (update.spectators) {
          nextLobby.spectators = update.spectators.map(s => ({
            id: s.id,
            username: s.username,
            avatar: s.avatar
          }));
        }

        return nextLobby;
      });

      // Control de alertas visuales (Toasts y navegación)
      if (update.action === 'JOIN') {
        if (update.username !== currentUser?.username) {
          toast.info(`${update.username} has joined the lobby!`);
        }
      } else if (update.action === 'LEAVE') {
        if (update.username !== currentUser?.username) {
          toast.info(`${update.username} left the lobby`);
        }
      } else if (update.action === 'START') {
        navigate(`/match/${matchId}`);
      } else if (update.action === 'DELETED') {
        if (update.username !== currentUser?.username) {
          toast.info(`The lobby was closed by the creator`);
        }
        setTimeout(() => navigate('/lobbies'), 2000);
      } 
      // Notificaciones reactivas opcionales cuando entra o sale un espectador
      else if (update.action === 'SPECTATOR_JOIN') {
        toast.info(`${update.username} started spectating`);
      } else if (update.action === 'SPECTATOR_LEAVE') {
        toast.info(`${update.username} stopped spectating`);
      }
    });

    return () => subscription.unsubscribe();
  }, [stompClient, matchId]);

  

  const handleOpenFriendsModal = () => {
    setShowFriendsModal(true);
  };

  const handleCopyCode = async () => {
    if (!lobby.code) return;

    try {
      await navigator.clipboard.writeText(lobby.code);
      setCopiedCode(true);
      window.setTimeout(() => setCopiedCode(false), 1500);
    } catch (error) {
      toast.error("Could not copy the lobby code.");
    }
  };

  const handleCloseFriendsModal = () => {
    setShowFriendsModal(false);
  };

  useEffect(() => {
    document.body.style.overflow = showFriendsModal ? "hidden" : "";
    return () => (document.body.style.overflow = "");
  }, [showFriendsModal]);



  const leaveLobby = async () => {
    await fetch(`/api/v1/lobbies/${matchId}/leave`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    navigate("/lobbies");
  }

  
  const stopSpectating = async () => {
    await fetch(`/api/v1/matches/${matchId}/StopSpectating`, {
      method: "DELETE",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    navigate("/lobbies");
  }

  const startGame = async () => {
    await fetch(`/api/v1/lobbies/${matchId}/start`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    navigate(`/match/${matchId}`);
  }

  const isCreator = currentUser && lobby.creatorId === currentUser.id
  const canStart = lobby.players && lobby.players.length >= lobby.minPlayers
  const isSpectator = lobby.spectators?.some(s => s.id === currentUser.id)

return (
  <>
    <div className="waiting-room-background">
      <div className="waiting-room-overlay">
        {/* Contenedor flex principal que alineará la caja y los espectadores en horizontal */}
        <div className="waiting-room-layout">
          
          {/* Caja Principal del Lobby */}
          <div className="waiting-room-box">

            {/* CONTENEDORES DE LA ESQUINA SUPERIOR DERECHA (PUESTOS EN VERTICAL) */}
            <div className="right-controls-wrapper">
              
              {/* 1. BOTÓN DE ESPECTADORES (OJO) - VA ARRIBA */}
              <div className="spectators-button-wrapper">
                <button
                  onClick={() => setShowSpectators(prev => !prev)}
                  className="spectators-btn"
                  title="Ver espectadores"
                >
                  <FaRegEye />
                </button>
                {/* Insignia roja sobre el botón del ojo */}
                {lobby.spectators?.length > 0 && (
                  <span className="spectators-count-badge">
                    {lobby.spectators.length}
                  </span>
                )}
              </div>

              {/* 2. BOTÓN DE INVITAR AMIGOS - VA ABAJO */}
              {!isSpectator && (
                <div className="invite-button-wrapper">
                  <button
                    onClick={handleOpenFriendsModal}
                    className="invite-friends-btn"
                    title="Invitar amigos"
                  >
                    <FaUserPlus />
                  </button>
                </div>
              )}

            </div>
            
            <h1>Wait for the match to start...</h1>

            {lobby.isPrivate && lobby.code && (
              <div
                className="private-lobby-code"
                style={{
                  marginBottom: "16px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  gap: "12px",
                  flexWrap: "wrap",
                }}
              >
                <div
                  style={{
                    border: "2px solid #5ec3f6",
                    borderRadius: "0",
                    padding: "10px 16px",
                    background: "rgb(255, 255, 255)",
                    color: "#000000",
                    fontWeight: 700,
                    fontSize: "16px",
                    lineHeight: "1",
                    letterSpacing: "0.08em",
                    textTransform: "uppercase",
                  }}
                >
                  Code: {lobby.code}
                </div>
                <Button
                  type="button"
                  onClick={handleCopyCode}
                  style={{
                    backgroundColor: "#f2f2f2",
                    border: "2px solid #5ec3f6",
                    color: "#222",
                    padding: "10px 16px",
                    fontSize: "16px",
                    lineHeight: "1",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                  aria-label="Copy lobby code"
                  title="Copy lobby code"
                >
                  {copiedCode ? <span style={{ fontWeight: 700 }}>Copied</span> : <FaRegCopy />}
                </Button>
              </div>
            )}

            <Table className="mt-4">
              <thead>
                <tr>
                  <th className="text-center">Players in the Lobby</th>
                </tr>
              </thead>
              <tbody>
                {lobby.players?.map(p => (
                  <tr key={p.user.id}>
                    <td>
                      <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "10px" }}>
                        {p.user.avatar ? (
                          <img
                            src={p.user.avatar}
                            alt={p.user.username || "avatar"}
                            style={{ width: "36px", height: "36px", borderRadius: "50%", objectFit: "cover", border: "2px solid #e6e6e6" }}
                          />
                        ) : (
                          <div
                            style={{
                              width: "36px",
                              height: "36px",
                              borderRadius: "50%",
                              backgroundColor: "#ececec",
                              display: "flex",
                              alignItems: "center",
                              justifyContent: "center",
                              fontWeight: 700,
                              color: "#555"
                            }}
                          >
                            {(p.user.username || "?").charAt(0).toUpperCase()}
                          </div>
                        )}
                        <span>{p.user.username}</span>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>

            {isSpectator ? (
              <Button color="danger" onClick={stopSpectating}>
                Stop spectating
              </Button>
            ) : isCreator ? (
              <>
                {/* Agregado color="success" para forzar la clase .btn-success */}
                <Button 
                  color="success"
                  disabled={!canStart}
                  onClick={startGame}
                  className="me-2"
                >
                  Start Match
                </Button>

                <Button color="danger" onClick={leaveLobby}>
                  Cancel match
                </Button>
              </>
            ) : (
              <Button color="danger" onClick={leaveLobby}>
                Leave Lobby
              </Button>
            )}
     
          </div> {/* Fin de waiting-room-box */}

          {/* PANEL DE ESPECTADORES -> FUERA de la caja principal para que funcione left: calc(100% + 20px) */}
          {showSpectators && (
            <div className="spectators-panel">
              <h3>Spectators</h3>
              <div className="spectators-list">
                {(lobby.spectators?.length ?? 0) === 0 ? (
                  <p>No spectators</p>
                ) : (
                  lobby.spectators.map(s => (
                    <div key={s.id} className="spectator-item">
                      <img
                        src={s.avatar}
                        alt={s.username || "avatar"}
                        style={{ width: "36px", height: "36px", borderRadius: "50%", objectFit: "cover", border: "2px solid #e6e6e6" }}
                      />
                      <span>{s.username}</span>
                    </div>
                  ))
                )}
              </div>
            </div>
          )}

        </div> 
      </div>
    </div>

    {showFriendsModal && (
      <OnlineFriendsModal
        onClose={handleCloseFriendsModal}
        lobby={lobby}
      />
    )}
  </>
);

}