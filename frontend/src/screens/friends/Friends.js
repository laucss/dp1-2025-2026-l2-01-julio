import React, { useEffect, useRef, useState } from 'react';
import { Card, CardText, Col, Input, Label, Row } from "reactstrap";
import './friends.css';
import { FaSearch, FaUser } from 'react-icons/fa';
import api from '../../services/api';
import tokenService from '../../services/token.service';
import useRequestStates from '../../hooks/useRequestStates';


const jwt = tokenService.getLocalAccessToken();


export default function Friends() {

  const [receiver, setReceiver] = useState("");
  const [errorMessage, setErrorMessage] = useState(null);

  const friendBoxRef = useRef(null);

  const { allFriends, getAndSetAllFriends } = useRequestStates(
    jwt,
    errorMessage,
    setErrorMessage
  );

  const { allSent, getAndSetSentRequests } = useRequestStates(
    jwt,
    errorMessage,
    setErrorMessage
  );

  const { allReceived, getAndSetReceivedRequests } = useRequestStates(
    jwt,
    errorMessage,
    setErrorMessage
  );

  // Modal states
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

  // Limpieza de estados previos
  setInviteError(null);
  setInviteSuccess(null);
  setInviteLoading(true);

  console.log("JWT:", tokenService.getLocalAccessToken());

  fetch(`/api/v1/friendRequests`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ receiverId: inviteUsername }),
  })
    .then(async (response) => {
      if (!response.ok) {
        const errText = await response.text();
        throw new Error(`Error ${response.status}: ${errText}`);
      }
      return response.json();
    })
    .then((json) => {
      if (json.message) {
        setInviteSuccess(json.message);
      } else {
        setInviteSuccess("Invitación enviada correctamente.");
      }
      getAndSetSentRequests(tokenService.getUser()?.id);
      setInviteUsername("");
    })
    .catch((error) => {
      console.error(error);
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
      if (json.message) {
        setErrorMessage(json.message);
      }
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
      if (json.message) {
        setErrorMessage(json.message);
      }
      const userId = tokenService.getUser?.()?.id;
      if (userId) {
        await getAndSetReceivedRequests(userId);
      }
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
      .catch((error) => {
        setErrorMessage(error.message);
      });
    getAndSetAllFriends(tokenService.getUser()?.id);
  };

  useEffect(() => {
    if (errorMessage) {
      if (errorMessage === "Unexpected end of JSON input") {
        setErrorMessage("You cannot send an invitation to yourself.");
      }
      const timer = setTimeout(() => {
        setErrorMessage(null);
      }, 2500);
      return () => clearTimeout(timer);
    }
  }, [errorMessage]);


  const friendList = allFriends?.map((f) => {
    const friend = f.sender.id == tokenService.getUser()?.id ? f.receiver : f.sender;

    return (
    <Card key={f.id}>
      <Row>
        <Col className="Column-friend">
          <Row>
            <Col>
              <CardText style={{ marginLeft: "-65%", paddingTop: "4%" }}>
                {friend.username}
              </CardText>
            </Col>
          </Row>
        </Col>
        <Row>
        <Col style={{ marginLeft: "70%" }}>
          <button
            className="request-button"
            style={{
              backgroundColor: "hsl(0, 70%, 55%)",
              color: "hsl(0, 80%, 25%)",
            }}
            value={f.id}
            onClick={handleDelete}
          >
            Delete
          </button>
        </Col>
        </Row>
      </Row>
    </Card>
    );
  });

  return (
    <div className="friends-page">
      <h1>Amigos</h1>

      {/* Botones principales */}
      <div className="friends-buttons">
        <button className="main-button" onClick={openInviteModal}>Enviar invitación</button>
        <button className="main-button" onClick={() => { setShowReceivedModal(true); getAndSetReceivedRequests(tokenService.getUser?.()?.id); }}>Invitaciones</button>
      </div>

      {/* Buscador */}
      <div className="friends-search">
        <FaSearch className="search-icon" />
        <input
          type="text"
          placeholder="Buscar por nombre de usuario"
          className="search-input"
        />
      </div>

      {/* Lista de amigos */}
      <div className="friends-list">
        <div className="friends-header">
          <h2>Lista de Amigos ({allFriends?.length || 0})</h2>
        </div>

        <div className="friends-scroll">
          {(!allFriends || allFriends.length === 0) ? (
            <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
              No tienes amigos ;(
            </div>
          ) : (
            allFriends.map(friend => (
              <div key={friend.id} className="friend-card">
                <FaUser className="friend-avatar" />
                <span className="friend-name">{friend.name}</span>
                <div className="friend-actions">
                  <button className="play-btn">Jugar</button>
                  <button
                    className="remove-btn"
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
      
      {/* Modal Invitación */}
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
            <button
              onClick={handleSubmit}
              disabled={inviteLoading}
              className="modal-send"
            >
              {inviteLoading ? 'Enviando...' : 'Enviar'}
            </button>
            <button 
              onClick={closeInviteModal} 
              className="modal-cancel"
            >
              Cancelar
            </button>
          </div>
        </div>
      </div>
      )}

      {/* Modal Solicitudes Recibidas */}
      {showReceivedModal && (
        <div className="modal-overlay">
          <div className="modal-card">
            <h3>Solicitudes Pendientes</h3>
            
            <div className="friends-scroll" style={{ maxHeight: '300px', overflowY: 'auto', marginBottom: '16px' }}>
              {(allReceived || []).map(request => (
                <Card key={request.id} className="friend-card" style={{ margin: '8px 0', padding: '12px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                      <FaUser className="friend-avatar" style={{ marginRight: '8px' }}/>
                      <span className="friend-name">{request.sender?.username}</span>
                    </div>
                    <div className="friend-actions" style={{ display: 'flex', gap: '8px' }}>
                      <button
                        className="request-button"
                        style={{ backgroundColor: "#23c483", color: "hsl(156, 80%, 25%)" }}
                        onClick={() => handleAccept(request.id)}
                      >
                        Aceptar
                      </button>
                      <button
                        className="request-button"
                        style={{ backgroundColor: "hsl(0, 70%, 55%)", color: "hsl(0, 80%, 25%)" }}
                        onClick={() => handleReject(request.id)}
                      >
                        Rechazar
                      </button>
                    </div>
                  </div>
                </Card>
              ))}
              {(!allReceived || allReceived.length === 0) && (
                <div style={{ textAlign: 'center', padding: '20px', color: '#666' }}>
                  No tienes solicitudes pendientes
                </div>
              )}
            </div>

            <div className="modal-buttons">
              <button 
                onClick={() => setShowReceivedModal(false)} 
                className="modal-cancel"
              >
                Cerrar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
