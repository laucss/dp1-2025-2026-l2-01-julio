import React, { useEffect, useRef, useState } from 'react';
import { Card, CardText, Col, Row } from "reactstrap";
import './friends.css';
import { FaSearch, FaUser } from 'react-icons/fa';
import tokenService from '../../services/token.service';
import useRequestStates from '../../hooks/useRequestStates';

const jwt = tokenService.getLocalAccessToken();

export default function Friends() {

  const [errorMessage, setErrorMessage] = useState(null);

  const { allFriends, getAndSetAllFriends } = useRequestStates(jwt, errorMessage, setErrorMessage);
  const { allSent, getAndSetSentRequests } = useRequestStates(jwt, errorMessage, setErrorMessage);
  const { allReceived, getAndSetReceivedRequests } = useRequestStates(jwt, errorMessage, setErrorMessage);

  const [showInviteModal, setShowInviteModal] = useState(false);
  const [showReceivedModal, setShowReceivedModal] = useState(false);
  const [inviteUsername, setInviteUsername] = useState('');
  const [inviteLoading, setInviteLoading] = useState(false);
  const [inviteError, setInviteError] = useState(null);
  const [inviteSuccess, setInviteSuccess] = useState(null);

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

        if (errObj && errObj.message?.includes("Entity Friend request already created.")) {
          let customError = "Ya existe una solicitud con este usuario.";

          if (errObj.status === "ACCEPTED") {
            customError = "La solicitud ya fue aceptada.";
          } else if (errObj.status === "PENDING") {
            customError = "La solicitud ya está pendiente.";
          }
          throw new Error(customError);
        } else {
          throw new Error(`Error ${response.status}: ${errText}`);
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

  const handleDelete = (event) => {
    event.preventDefault();

    fetch(`api/v1/friendRequests`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(event.target.value),
    })
      .then((response) => response.json())
      .catch((error) => setErrorMessage(error.message));

    getAndSetAllFriends(tokenService.getUser()?.id);
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

  return (
    <div className="friends-page">
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
        <input type="text" placeholder="Buscar por nombre de usuario" className="search-input" />
      </div>

      <div className="friends-list">
        <div className="friends-header">
          <h2>Lista de Amigos ({allFriends?.length || 0})</h2>
        </div>

        <div className="friends-scroll">
          {!allFriends || allFriends.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
              No tienes amigos ;(
            </div>
          ) : (
            allFriends.map(friend => (
              <div key={friend.id} className="friend-card">
                <FaUser className="friend-avatar" />

                <span className="friend-name">
                  {friend.sender.id === tokenService.getUser()?.id
                    ? friend.receiver.username
                    : friend.sender.username}
                </span>

                <div className="friend-actions">
                  <button className="play-btn">Jugar</button>

                  <button
                    className="remove-btn"
                    value={friend.id}
                    onClick={handleDelete}
                  >
                    Eliminar
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

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
    </div>
  );
}
