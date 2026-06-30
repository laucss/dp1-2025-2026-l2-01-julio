import React, { useState, useEffect, useRef } from "react";
import { fetchInviteStatuses } from "../../util/fetchInviteStatuses";
import { FaUserPlus } from "react-icons/fa";
import { useParams, useNavigate } from "react-router-dom";
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import useFetchState from "../../util/useFetchState";
import "../../static/css/home/waitingRoom.css";
import { Button, Table } from "reactstrap";
import tokenService from "../../services/token.service";

export default function WaitingRoom() {
  const { matchId } = useParams();
  const navigate = useNavigate();
  const jwt = tokenService.getLocalAccessToken();
  const currentUser = tokenService.getUser();

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [countdown, setCountdown] = useState(null);
  const countdownRef = useRef(null);

  const [showFriendsModal, setShowFriendsModal] = useState(false);
  const [onlineFriends, setOnlineFriends] = useState([]);
  const [stompClient, setStompClient] = useState(null);

  const [lobby, setLobby] = useFetchState(
    [],
    `/api/v1/lobbies/${matchId}`,
    jwt,
    setMessage,
    setVisible
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
        setMessage(`${update.username} joinned the lobby`);
        setVisible(true);
      } else if (update.action === 'LEAVE') {
        setMessage(`${update.username} left the lobby`);
        setVisible(true);
      } else if (update.action === 'START') {
        navigate(`/match/${matchId}`);
      } else if (update.action === 'DELETED') {
        setMessage(`The lobby was closed by the creator`);
        setVisible(true);
        setTimeout(() => navigate('/lobbies'), 2000);
      }
    });

    return () => subscription.unsubscribe();
  }, [stompClient, matchId]);

  const fetchOnlineFriends = async () => {
    try {
      const userId = tokenService.getUser()?.id;
      const res = await fetch(`/api/v1/friendRequests/${userId}`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });
      const result = await res.json();
      console.log('Respuesta backend amigos:', result);
      let friendsArray = [];
      if (Array.isArray(result)) {
        friendsArray = result;
      } else if (Array.isArray(result.data)) {
        friendsArray = result.data;
      } else if (result.data && Array.isArray(result.data.content)) {
        friendsArray = result.data.content;
      } else if (result.data && Array.isArray(result.data.data)) {
        friendsArray = result.data.data;
      }
      const currentUserId = tokenService.getUser()?.id;
      friendsArray.forEach(f => {
        console.log('Amigo:', f);
      });
      const allFriends = friendsArray
        .map(f => {
          let amigo, amigoStatus, amigoId;
          if (f.sender?.id === currentUserId) {
            amigo = f.receiver;
            amigoStatus = f.receiver?.status;
            amigoId = f.receiver?.id;
          } else {
            amigo = f.sender;
            amigoStatus = f.sender?.status;
            amigoId = f.sender?.id;
          }
          return {
            ...f,
            displayName: amigo?.username,
            friendStatus: amigoStatus,
            avatar: amigo?.avatar,
            userId: amigoId
          };
        });
      console.log('Todos los amigos:', allFriends);
      setOnlineFriends(allFriends);
    } catch (e) {
      console.error('Error obteniendo amigos online:', e);
      setOnlineFriends([]);
    }
  };

  const handleOpenFriendsModal = () => {
    fetchOnlineFriends();
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
          friends={onlineFriends}
          onClose={handleCloseFriendsModal}
          lobby={lobby}
        />
      )}
    </>
  );

function OnlineFriendsModal({ friends, onClose, lobby }) {
  const [inviteStatus, setInviteStatus] = useState({});
  const matchId = window.location.pathname.split("/").pop();
  const currentUser = tokenService.getUser();
  const jwt = tokenService.getLocalAccessToken();
  const players = lobby && lobby.players ? lobby.players : [];

  useEffect(() => {
    let mounted = true;
    async function syncStatuses() {
      if (friends.length > 0 && currentUser && jwt) {
        const statuses = await fetchInviteStatuses(friends, matchId, jwt, currentUser.id);
        if (mounted) setInviteStatus(statuses);
      }
    }
    syncStatuses();
    return () => { mounted = false; };
  }, [friends, matchId, jwt, currentUser]);

  const handleInvite = async (friend) => {
    setInviteStatus(s => ({ ...s, [friend.id]: "loading" }));
    try {
      const res = await fetch("/api/v1/notifications/invite", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${jwt}`,
        },
        body: JSON.stringify({
          senderId: currentUser.id,
          receiverId: friend.userId,
          matchId: matchId,
        }),
      });
      if (res.ok) {
        setInviteStatus(s => ({ ...s, [friend.id]: "success" }));
      } else {
        setInviteStatus(s => ({ ...s, [friend.id]: "error" }));
      }
    } catch {
      setInviteStatus(s => ({ ...s, [friend.id]: "error" }));
    }
  };

  
  const isFriendInLobby = (friend) => {
    return players.some(p => p.user && p.user.id === friend.userId);
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card enlarged-modal">
        <h2>Online Friends</h2>
        <div className="friends-list-scroll">
          {friends.length === 0 ? (
            <p className="no-friends">No online friends</p>
          ) : (
            friends.map(f => (
              <div key={f.id} className="friend-mini-container">
                <div className="friend-avatar-name">
                  {f.avatar ? (
                    <img src={f.avatar} alt="avatar" className="friend-avatar" />
                  ) : (
                    <div className="friend-avatar-placeholder" />
                  )}
                  <span className="friend-name">{f.displayName}</span>
                </div>
                {isFriendInLobby(f) ? (
                  <span className="waiting-invite-text-small">Joined</span>
                ) : (
                  inviteStatus[f.id] === "success" ? (
                    <span className="waiting-invite-text-small">Waiting...</span>
                  ) : (
                    <button className="invite-btn" onClick={() => handleInvite(f)} disabled={inviteStatus[f.id] === "loading"}>
                      {inviteStatus[f.id] === "loading" ? "Sending..." : "Invite"}
                    </button>
                  )
                )}
                {inviteStatus[f.id] === "error" && <span className="invite-error">Error inviting</span>}
              </div>
            ))
          )}
        </div>
        <button className="close-btn" onClick={onClose}>
          Close
        </button>
      </div>
    </div>
  );
}
}