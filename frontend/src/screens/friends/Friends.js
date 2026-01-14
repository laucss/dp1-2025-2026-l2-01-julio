import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './friends.css';
import { FaSearch, FaUser, FaEye, FaGamepad, FaTrash } from 'react-icons/fa';
import UserStatusIndicator from '../../components/UserStatusIndicator';
import tokenService from '../../services/token.service';
import useRequestStates from '../../hooks/useRequestStates';
import { Client } from '@stomp/stompjs';

const jwt = tokenService.getLocalAccessToken();

export default function Friends() {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [stompClient, setStompClient] = useState(null);
  
  const {
    allFriends,
    allSent,
    allReceived,
    getAndSetAllFriends,
    getAndSetSentRequests,
    getAndSetReceivedRequests,
    addFriendToState,
  } = useRequestStates(jwt, errorMessage, setErrorMessage);

  const [showInviteModal, setShowInviteModal] = useState(false);
  const [showReceivedModal, setShowReceivedModal] = useState(false);
  const [inviteUsername, setInviteUsername] = useState('');
  const [inviteLoading, setInviteLoading] = useState(false);
  const [inviteError, setInviteError] = useState(null);
  const [inviteSuccess, setInviteSuccess] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  // Conectar a WebSocket al montar el componente
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: { 'Authorization': `Bearer ${jwt}` },
      onConnect: () => setStompClient(client),
    });

    client.activate();
    return () => client.active && client.deactivate();
  }, [jwt]);

  const openInviteModal = () => {
    setInviteUsername('');
    setInviteError(null);
    setInviteSuccess(null);
    setShowInviteModal(true);
  };

  const closeInviteModal = () => {
    setShowInviteModal(false);
  };

  function handleSubmit(event) {
  event.preventDefault();

  setInviteError(null);
  setInviteSuccess(null);
  setInviteLoading(true);

  const currentUsername = tokenService.getUser?.()?.username;
  if (inviteUsername.trim().toLowerCase() === currentUsername?.toLowerCase()) {
    setInviteError("You can not send an invitation to yourself.");
    setInviteLoading(false);
    return;
  }

  fetch(`/api/v1/friendRequests/${inviteUsername}`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: tokenService.getUser()?.id,
  })
    .then(async (response) => {
      if (!response.ok) {
        const errText = await response.text();
        let errObj;
        try {
          errObj = JSON.parse(errText);
        } catch {
          errObj = null;
        }

        const errorMessage = errObj?.message || errText;

        if (response.status === 404) {
          if (errorMessage.includes("User not found")) {
            throw new Error("User does not exist.");
          }
          throw new Error(errorMessage);
        }

        if (response.status === 400) {
          throw new Error(errorMessage || "The specified user does not exist or the request is invalid.");
        }

        if (errObj && errObj.message?.includes("Entity Friend request already created.")) {
          let customError = "A request with this user already exists.";
          if (errObj.status === "ACCEPTED") {
            customError = "The request has already been accepted.";
          } else if (errObj.status === "PENDING") {
            customError = "The request is already pending.";
          }
          throw new Error(customError);
        } else {
          throw new Error(errorMessage);
        }
      }
      return response.json();
    })
    .then((json) => {
      setInviteSuccess(json.message || "Invitation sent successfully.");
      getAndSetSentRequests(tokenService.getUser()?.id);
      setInviteUsername("");
    })
    .catch((error) => {
      setInviteError(error.message);
    })
    .finally(() => {
      setInviteLoading(false);
    });
}

  async function handleAccept(requestId) {
    try {
      const response = await fetch(`api/v1/friendRequests/accept`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestId),
      });

  const json = await response.json();
      if (json.message) setErrorMessage(json.message);

    const userId = tokenService.getUser?.()?.id;
      if (json && json.id) {
        addFriendToState(json);
      }
      if (userId) {
        await getAndSetAllFriends(userId);
        await getAndSetReceivedRequests(userId);
      }
    } catch (error) {
      setErrorMessage(error.message);
    }
  }

  async function handleReject(requestId) {
    try {
      const response = await fetch(`api/v1/friendRequests/reject`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestId),
      });

      const json = await response.json();
      if (json.message) setErrorMessage(json.message);

      const userId = tokenService.getUser?.()?.id;
      if (userId) await getAndSetReceivedRequests(userId);
    } catch (error) {
      setErrorMessage(error.message);
    }
  }

  const openDeleteModal = (friend) => {
    const currentUserId = tokenService.getUser?.()?.id;
    const name = friend?.sender?.id === currentUserId
      ? friend?.receiver?.username
      : friend?.sender?.username;
    setDeleteTarget({ id: friend.id, name });
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
    setDeleteTarget(null);
  };

  const confirmDelete = async () => {
    if (!deleteTarget) return;
    try {
      const response = await fetch(`api/v1/friendRequests`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(deleteTarget.id),
      });
      let json = null;
      try {
        json = await response.json();
      } catch (e) {
      }

      if (json && json.message) setErrorMessage(json.message);

      const userId = tokenService.getUser?.()?.id;
      if (userId) await getAndSetAllFriends(userId);
    } catch (error) {
      setErrorMessage(error.message);
    } finally {
      closeDeleteModal();
    }
  };

  useEffect(() => {
    if (errorMessage) {
      if (errorMessage === "Unexpected end of JSON input") {
        setErrorMessage("You cannot send an invitation to yourself.");
      }
      const timer = setTimeout(() => setErrorMessage(null), 2500);
      return () => clearTimeout(timer);
    }
  }, [errorMessage]);

  // Suscripción a nuevas solicitudes recibidas
  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const userId = tokenService.getUser?.()?.id;
    if (!userId) return;

    const subscription = stompClient.subscribe(
      `/topic/user.${userId}.friendRequests`,
      (message) => {
        console.log('New friend request received:', message.body);
        getAndSetReceivedRequests(userId);
      }
    );

    return () => subscription.unsubscribe();
  }, [stompClient]);

  // Suscripción a actualizaciones de solicitudes
  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const userId = tokenService.getUser?.()?.id;
    if (!userId) return;

    const subscription = stompClient.subscribe(
      `/topic/user.${userId}.friendRequests.update`,
      (message) => {
        console.log('Friend request updated:', message.body);
        getAndSetReceivedRequests(userId);
      }
    );

    return () => subscription.unsubscribe();
  }, [stompClient]);

  // Suscripción a aceptaciones de solicitudes
  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const userId = tokenService.getUser?.()?.id;
    if (!userId) return;

    const subscription = stompClient.subscribe(
      `/topic/user.${userId}.friendRequests.accepted`,
      (message) => {
        console.log('Your request was accepted:', message.body);
        getAndSetAllFriends(userId);
        getAndSetSentRequests(userId);
      }
    );

    return () => subscription.unsubscribe();
  }, [stompClient]);

  // Suscripción a rechazos de solicitudes
  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const userId = tokenService.getUser?.()?.id;
    if (!userId) return;

    const subscription = stompClient.subscribe(
      `/topic/user.${userId}.friendRequests.rejected`,
      (message) => {
        console.log('Your request was rejected:', message.body);
        getAndSetSentRequests(userId);
      }
    );

    return () => subscription.unsubscribe();
  }, [stompClient]);

  // Suscripción a eliminación de amigos
  useEffect(() => {
    if (!stompClient || !stompClient.active) return;

    const userId = tokenService.getUser?.()?.id;
    if (!userId) return;

    const subscription = stompClient.subscribe(
      `/topic/user.${userId}.friendRequests.deleted`,
      (message) => {
        console.log('A friendship was deleted:', message.body);
        getAndSetAllFriends(userId);
        getAndSetReceivedRequests(userId);
        getAndSetSentRequests(userId);
      }
    );

    return () => subscription.unsubscribe();
  }, [stompClient]);

  // Cargar datos iniciales
  useEffect(() => {
    const userId = tokenService.getUser?.()?.id;
    if (userId) {
      getAndSetAllFriends(userId);
      getAndSetSentRequests(userId);
      getAndSetReceivedRequests(userId);
    }
  }, []);

  // Función para verificar si el usuario actual es amigo de todos los jugadores de una partida
  const isUserFriendOfAllPlayers = (matchPlayers, otherUserData) => {
    if (!matchPlayers || matchPlayers.length === 0) {
      return false;
    }
    
    const currentUserId = tokenService.getUser?.()?.id;
    if (!currentUserId) return false;

    // Para cada jugador de la partida, verificar si es el usuario actual o si es amigo
    return matchPlayers.every(player => {
      const playerId = player.userId || player.user?.id || player.id;
      
      // Si es el propio usuario, no necesita ser amigo de sí mismo
      if (playerId === currentUserId) return true;
      
      // Verificar si este jugador está en la lista de amigos
      return allFriends.some(friend => {
        const friendUserId = friend.sender?.id === currentUserId 
          ? friend.receiver?.id 
          : friend.sender?.id;
        return friendUserId === playerId;
      });
    });
  };

  return (
    <div className="friends-page">

      <div className="friends-container">

      <h1>Friends</h1>

      <div className="friends-buttons">
        <button className="main-button" onClick={openInviteModal}>
          Send Invitation
        </button>
        <button
          className="main-button"
          onClick={() => {
            setShowReceivedModal(true);
            getAndSetReceivedRequests(tokenService.getUser?.()?.id);
          }}
        >
          Invitations
        </button>
      </div>

      <div className="friends-search">
        <FaSearch className="search-icon" />
        <input
          type="text"
          placeholder="Search by username ..."
          className="search-input"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      <div className="friends-list">
        <div className="friends-header">
          <h2>Friends List</h2>
        </div>

        <div className="friends-scroll">
          {(!allFriends || allFriends.length === 0) ? (
            <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
              You have no friends :(
            </div>
          ) : (() => {
            const currentUserId = tokenService.getUser?.()?.id;
            const friendsWithName = (allFriends || []).map(f => ({
              ...f,
              displayName: (f.sender?.id === currentUserId) ? f.receiver?.username : f.sender?.username,
            }));

            const q = searchQuery?.trim().toLowerCase();
            const filtered = q ? friendsWithName.filter(f => (f.displayName || '').toLowerCase().includes(q)) : friendsWithName;

            if (filtered.length === 0) {
              return (
                <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
                  No friends found matching your search
                </div>
              );
            }

            return filtered.map(friend => (
              <div key={friend.id} className="friend-card">
                <FaUser className="friend-avatar" />
                {/* Mostrar el status del amigo, no del objeto friendRequest */}
                <UserStatusIndicator status={
                  (friend.sender?.username === friend.displayName ? friend.sender?.status : friend.receiver?.status) || friend.status
                } />
                <span className="friend-name">
                  {friend.displayName}
                </span>
                <div className="friend-actions">
                  {(() => {
                    const currentUsername = tokenService.getUser?.()?.username;
                    const otherUser = friend.sender?.username !== currentUsername ? friend.sender : friend.receiver;
                    
                    // Verificar si el amigo está en una partida PLAYING
                    if (otherUser?.match && otherUser.match.status === 'PLAYING') {
                      // Verificar si el usuario actual es amigo de TODOS los jugadores de la partida
                      const matchPlayers = otherUser.match.players || [];
                      const canSpectate = isUserFriendOfAllPlayers(matchPlayers, otherUser);
                      
                      if (canSpectate) {
                        return (
                          <button
                            className="play-btn"
                            title="Visualizar partida"
                            onClick={() => {
                              navigate(`/match/${otherUser.match.id}`, { state: { spectator: true } });
                            }}
                          >
                            <FaEye style={{ marginRight: 4 }} /> Spectate
                          </button>
                        );
                      }
                    }
                    return null;
                  })()}
                  {friend.status === 'ONLINE' && (
                    <button className="play-btn" title="Jugar">
                      <FaGamepad style={{ marginRight: 4 }} /> Play
                    </button>
                  )}
                  <button
                    className="remove-btn"
                    onClick={() => openDeleteModal(friend)}
                    title="Eliminar amigo"
                  >
                    <FaTrash style={{ marginRight: 4 }} /> Delete
                  </button>
                </div>
              </div>
            ));
          })()}
        </div>
      </div>

      </div> {/* end .friends-container */}

      {showInviteModal && (
        <div className="modal-overlay">
          <div className="modal-card">
            <h3>Send Invitation</h3>

            <input
              type="text"
              placeholder="Username"
              value={inviteUsername}
              onChange={(e) => setInviteUsername(e.target.value)}
              className="modal-input"
            />

            {inviteError && <div className="modal-error">{inviteError}</div>}
            {inviteSuccess && <div className="modal-success">{inviteSuccess}</div>}

            <div className="modal-buttons">
              <button onClick={handleSubmit} disabled={inviteLoading} className="modal-send">
                {inviteLoading ? 'Sending...' : 'Send'}
              </button>
              <button onClick={closeInviteModal} className="modal-cancel">
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {showReceivedModal && (
        <div className="modal-overlay">
          <div className="modal-card">
            <h3>Pending Requests</h3>

            <div className="friends-scroll" style={{ maxHeight: '300px', overflowY: 'auto', marginBottom: '16px' }}>
              {(allReceived || []).map(request => (
                <div key={request.id} className="request-card">
                  <div className="request-user">
                    <FaUser className="icon" />
                    <span>{request.sender?.username}</span>
                  </div>

                  <div className="request-actions">
                    <button className="request-btn request-accept" onClick={() => handleAccept(request.id)}>
                      Accept
                    </button>

                    <button className="request-btn request-reject" onClick={() => handleReject(request.id)}>
                      Reject
                    </button>
                  </div>
                </div>
              ))}

              {(!allReceived || allReceived.length === 0) && (
                <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
                  No pending requests
                </div>
              )}
            </div>

            <div className="modal-buttons">
              <button onClick={() => setShowReceivedModal(false)} className="modal-cancel">
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {showDeleteModal && (
        <div className="modal-overlay">
          <div className="modal-card">

            <p className="modal-confirm-text">
              Are you sure you want to delete "{deleteTarget?.name}" as a friend?
            </p>

            <div className="modal-buttons">
              <button onClick={confirmDelete} className="modal-delete">
                Delete
              </button>
              <button onClick={closeDeleteModal} className="modal-cancel">
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
