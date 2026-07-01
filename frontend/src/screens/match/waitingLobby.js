import React, { useState, useEffect, useRef } from "react";
import { FaUserPlus } from "react-icons/fa";
import { useParams, useNavigate } from "react-router-dom";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import useFetchState from "../../util/useFetchState";
import "../../static/css/home/waitingRoom.css";
import { Button, Table } from "reactstrap";
import tokenService from "../../services/token.service";
import OnlineFriendsModal from "./OnlineFriendsModal";
import { FaRegEye } from "react-icons/fa";

import { toast } from "react-toastify";


export default function WaitingRoom() {
  const { matchId } = useParams()
  const navigate = useNavigate()
  const jwt = tokenService.getLocalAccessToken()
  const currentUser = tokenService.getUser()
  const [lobby, setLobby] = useState({})

  const [showFriendsModal, setShowFriendsModal] = useState(false)
  const [stompClient, setStompClient] = useState(null)

  const [showSpectators, setShowSpectators] = useState(false)

  useEffect(() => {
    const fetchLobby = async () => {
      const res = await fetch(`/api/v1/lobbies/${matchId}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });

      const data = await res.json();
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
      
      setLobby(prevLobby => ({
        ...prevLobby,
        players: update.players.map(p => ({
          user: {
            id: p.userId,
            username: p.username,
            avatar: p.avatar
          }
        }))
      }));

      if (update.action === 'JOIN') {
        toast.info(`${update.username} joinned the lobby`);
      } else if (update.action === 'LEAVE') {
        toast.info(`${update.username} left the lobby`);
      } else if (update.action === 'START') {
        navigate(`/match/${matchId}`);
      } else if (update.action === 'DELETED') {
        toast.info(`The lobby was closed by the creator`);
        setTimeout(() => navigate('/lobbies'), 2000);
      }
    });

    return () => subscription.unsubscribe();
  }, [stompClient, matchId]);

  

  const handleOpenFriendsModal = () => {
    setShowFriendsModal(true);
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
              <button
                onClick={handleOpenFriendsModal}
                className="invite-friends-btn"
                title="Invitar amigos"
              >
                <FaUserPlus />
              </button>

              <div className="spectators-button-wrapper">
                <button
                  onClick={() => setShowSpectators(prev => !prev)}
                  className="spectators-btn"
                  title="Ver espectadores"
                >
                  <FaRegEye />
                </button>

              </div>
              
              <h1>Wait for the match to start...</h1>

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


              
            </div>

            {/* Panel de Espectadores (Ahora está al mismo nivel que waiting-room-box) */}
            {showSpectators && (
              <div className="spectators-panel">
                <h3>Spectators</h3>
                <div className="spectators-list">
                  {(lobby.spectators?.length ?? 0) === 0 ? (
                    <p>No spectators</p>
                  ) : (
                    lobby.spectators.map(s => (
                      <div key={s.id} className="spectator-item">
                        {/* Puedes añadir avatares aquí si los tienes de manera similar a los players */}
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
  )

}