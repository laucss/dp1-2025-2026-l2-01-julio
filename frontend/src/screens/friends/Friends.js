import React, { useEffect, useState } from 'react';
import './friends.css';
import { FaSearch, FaUser, FaEye, FaGamepad, FaTrash } from 'react-icons/fa';
import UserStatusIndicator from '../../components/UserStatusIndicator';
import tokenService from '../../services/token.service';
import useRequestStates from '../../hooks/useRequestStates';

const jwt = tokenService.getLocalAccessToken();

export default function Friends() {
  const [errorMessage, setErrorMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  
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
    setInviteError("No puedes enviar una invitación a ti mismo.");
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
            throw new Error("El usuario no existe.");
          }
          throw new Error(errorMessage);
        }

        if (response.status === 400) {
          throw new Error(errorMessage || "El usuario especificado no existe o la solicitud es inválida.");
        }

        if (errObj && errObj.message?.includes("Entity Friend request already created.")) {
          let customError = "Ya existe una solicitud con este usuario.";

          if (errObj.status === "ACCEPTED") {
            customError = "La solicitud ya fue aceptada.";
          } else if (errObj.status === "PENDING") {
            customError = "La solicitud ya está pendiente.";
          }
          throw new Error(customError);
        } else {
          throw new Error(errorMessage);
        }
      }
      return response.json();
    })
    .then((json) => {
      setInviteSuccess(json.message || "Invitación enviada correctamente.");
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

  useEffect(() => {
    const userId = tokenService.getUser?.()?.id;
    if (userId) {
      getAndSetAllFriends(userId);
      getAndSetSentRequests(userId);
      getAndSetReceivedRequests(userId);
    }
  }, []);

  return (
    <div className="friends-page">

      <div className="friends-container">

      <h1>Amigos</h1>

      <div className="friends-buttons">
        <button className="main-button" onClick={openInviteModal}>
          Enviar invitación
        </button>
        <button
          className="main-button"
          onClick={() => {
            setShowReceivedModal(true);
            getAndSetReceivedRequests(tokenService.getUser?.()?.id);
          }}
        >
          Invitaciones
        </button>
      </div>

      <div className="friends-search">
        <FaSearch className="search-icon" />
        <input
          type="text"
          placeholder="Buscar por nombre de usuario"
          className="search-input"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      <div className="friends-list">
        <div className="friends-header">
          <h2>Lista de Amigos</h2>
        </div>

        <div className="friends-scroll">
          {(!allFriends || allFriends.length === 0) ? (
            <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
              No tienes amigos ;(
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
                  No se han encontrado amigos que coincidan
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
                  {friend.status === 'PLAYING' ? (
                    <button className="play-btn" title="Visualizar partida">
                      <FaEye style={{ marginRight: 4 }} /> Visualizar
                    </button>
                  ) : friend.status === 'ONLINE' ? (
                    <button className="play-btn" title="Jugar">
                      <FaGamepad style={{ marginRight: 4 }} /> Jugar
                    </button>
                  ) : null}
                  <button
                    className="remove-btn"
                    onClick={() => openDeleteModal(friend)}
                    title="Eliminar amigo"
                  >
                    <FaTrash style={{ marginRight: 4 }} /> Eliminar
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
            <h3>Enviar invitación</h3>

            <input
              type="text"
              placeholder="Nombre de usuario"
              value={inviteUsername}
              onChange={(e) => setInviteUsername(e.target.value)}
              className="modal-input"
            />

            {inviteError && <div className="modal-error">{inviteError}</div>}
            {inviteSuccess && <div className="modal-success">{inviteSuccess}</div>}

            <div className="modal-buttons">
              <button onClick={handleSubmit} disabled={inviteLoading} className="modal-send">
                {inviteLoading ? 'Enviando...' : 'Enviar'}
              </button>
              <button onClick={closeInviteModal} className="modal-cancel">
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}

      {showReceivedModal && (
        <div className="modal-overlay">
          <div className="modal-card">
            <h3>Solicitudes Pendientes</h3>

            <div className="friends-scroll" style={{ maxHeight: '300px', overflowY: 'auto', marginBottom: '16px' }}>
              {(allReceived || []).map(request => (
                <div key={request.id} className="request-card">
                  <div className="request-user">
                    <FaUser className="icon" />
                    <span>{request.sender?.username}</span>
                  </div>

                  <div className="request-actions">
                    <button className="request-btn request-accept" onClick={() => handleAccept(request.id)}>
                      Aceptar
                    </button>

                    <button className="request-btn request-reject" onClick={() => handleReject(request.id)}>
                      Rechazar
                    </button>
                  </div>
                </div>
              ))}

              {(!allReceived || allReceived.length === 0) && (
                <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
                  No tienes solicitudes pendientes
                </div>
              )}
            </div>

            <div className="modal-buttons">
              <button onClick={() => setShowReceivedModal(false)} className="modal-cancel">
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}

      {showDeleteModal && (
        <div className="modal-overlay">
          <div className="modal-card">

            <p className="modal-confirm-text">
              ¿Estás seguro de que quieres eliminar a "{deleteTarget?.name}" como amigo?
            </p>

            <div className="modal-buttons">
              <button onClick={confirmDelete} className="modal-delete">
                Eliminar
              </button>
              <button onClick={closeDeleteModal} className="modal-cancel">
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
