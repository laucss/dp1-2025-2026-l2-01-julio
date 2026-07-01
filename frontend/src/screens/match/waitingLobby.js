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

import { toast } from "react-toastify";


export default function WaitingRoom() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();

  const [showFriendsModal, setShowFriendsModal] = useState(false);
  const [stompClient, setStompClient] = useState(null);

  const [lobby, setLobby] = useFetchState(
    [],
    `/api/v1/lobbies/${matchId}`,
    jwt
  );

  // Inicializar conexión WebSocket para el lobby
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: { 'Authorization': `Bearer ${jwt}` },
      onConnect: () => setStompClient(client)
    });

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
  };

  const startGame = async () => {
    await fetch(`/api/v1/lobbies/${matchId}/start`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    navigate(`/match/${matchId}`);
  };

  const isCreator = currentUser && lobby.creatorId === currentUser.id;
  const canStart = lobby.players && lobby.players.length >= lobby.minPlayers;

  return (
    <>
      <div className="waiting-room-background">
        <div className="waiting-room-overlay">
          <div className="waiting-room-box">
            <button
              onClick={handleOpenFriendsModal}
              className="invite-friends-btn"
              title="Invitar amigos"
            >
              <FaUserPlus />
            </button>

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

            {isCreator && (
              <Button
                color="success"
                disabled={!canStart}
                onClick={startGame}
              >
                Start Match
              </Button>
            )}

            <Button color="danger" onClick={leaveLobby}>
              Leave Lobby
            </Button>
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