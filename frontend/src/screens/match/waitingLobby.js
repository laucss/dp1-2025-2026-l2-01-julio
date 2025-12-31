import React, { useState, useEffect, useRef } from "react";
import { FaUserPlus } from "react-icons/fa";
import { useParams, useNavigate } from "react-router-dom";
import useFetchState from "../../util/useFetchState";
import "../../static/css/home/waitingRoom.css";
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

  const [showFriendsModal, setShowFriendsModal] = useState(false);
  const [onlineFriends, setOnlineFriends] = useState([]);

  const [lobby, setLobby] = useFetchState(
    [],
    `/api/v1/matches/lobbies/${matchId}`,
    jwt,
    setMessage,
    setVisible
  );

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
          let amigo, amigoStatus;
          if (f.sender?.id === currentUserId) {
            amigo = f.receiver;
            amigoStatus = f.receiver?.status;
          } else {
            amigo = f.sender;
            amigoStatus = f.sender?.status;
          }
          return {
            ...f,
            displayName: amigo?.username,
            friendStatus: amigoStatus,
            avatar: amigo?.avatar
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
    await fetch(`/api/v1/matches/lobbies/${matchId}/leave`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    navigate("/lobbies");
  };

  const startGame = async () => {
    await fetch(`/api/v1/matches/lobbies/${matchId}/start`, {
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

            <h1>Espere a que la partida comience...</h1>
            <p>Lobby ID: {matchId}</p>

            <Table className="mt-4">
              <thead>
                <tr>
                  <th className="text-center">Jugadores en el Lobby</th>
                </tr>
              </thead>
              <tbody>
                {lobby.players?.map(p => (
                  <tr key={p.user.id}>
                    <td>{p.user.username}</td>
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
                Comenzar Partida
              </Button>
            )}

            <Button color="danger" onClick={leaveLobby}>
              Salir del Lobby
            </Button>
          </div>
        </div>
      </div>

      {showFriendsModal && (
        <OnlineFriendsModal
          friends={onlineFriends}
          onClose={handleCloseFriendsModal}
        />
      )}
    </>
  );
}

function OnlineFriendsModal({ friends, onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-card fixed-size-modal">
        <h2>Amigos Online</h2>
        <div className="friends-list-scroll">
          {friends.length === 0 ? (
            <p className="no-friends">No hay amigos online</p>
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
                <button className="invite-btn">Invitar</button>
              </div>
            ))
          )}
        </div>
        <button className="close-btn" onClick={onClose}>
          Cerrar
        </button>
      </div>
    </div>
  );
}