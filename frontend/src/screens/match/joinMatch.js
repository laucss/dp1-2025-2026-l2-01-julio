import { useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import './listingLobbies.css';
import { Button, ButtonGroup, Table, Modal, ModalHeader, ModalBody, ModalFooter, Input } from "reactstrap";

const jwt = tokenService.getLocalAccessToken();

export default function JoinMatch() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [lobbies, setLobbies] = useFetchState(
    [],
    `/api/v1/matches/lobbies`,
    jwt,
    setMessage,
    setVisible
  );

  const [showModal, setShowModal] = useState(false);  
  const [privateCode, setPrivateCode] = useState(""); 

  const navigate = useNavigate();


  const handleJoin = async (match) => {
    try {
      const response = await fetch(`/api/v1/matches/lobbies/${match.id}/join`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        navigate(`/lobby/${match.id}`);
      } else {
        const errorText = await response.text();
        alert(" No se pudo unir al lobby: " + errorText);
      }
    } catch (error) {
      alert(" Error al conectar con el servidor.");
    }
  };

 
  const handleJoinPrivate = async () => {
    if (!privateCode) {
      alert("Introduce un código primero");
      return;
    }

    try {
      const response = await fetch(`/api/v1/matches/lobbies/join/private?code=${privateCode}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const match = await response.json();
        setShowModal(false);
        navigate(`/lobby/${match.id}`);
      } else {
        const errorText = await response.text();
        alert(" Código inválido o lobby no disponible: " + errorText);
      }
    } catch (error) {
      alert(" Error al conectar con el servidor.");
    }
  };


  const lobbiesList = lobbies.map((match) => (
    <tr key={match.id}>
      <td className="text-center">{match.name}</td>
      <td className="text-center">
        {match.players ? match.players.length : 0} / {match.maxPlayers}
      </td>
      <td className="text-center">
        <ButtonGroup>
          {match.players && match.players.length >= match.maxPlayers ? (
            <Button
              size="sm"
              color="danger" // rojo
              disabled       // no se puede hacer click
            >
              FULL
            </Button>
          ) : (
            <Button
              size="sm"
              color="success"
              aria-label={"join-" + match.name}
              onClick={() => handleJoin(match)}
            >
              Join
            </Button>
          )}
        </ButtonGroup>
      </td>
    </tr>
  ));

  return (
  <div className="admin-page-container">
    <div className="lobbies-overlay">
    <div className="lobbies-box">

                {/* Flecha de volver al inicio */}
        <button 
            className="back-arrow-btn"
            onClick={() => navigate('/')}
          >
            ←
      </button>

      <h1>Lobbies</h1>

      <Table aria-label="lobbies" className="mt-4">
        <thead>
          <tr>
            <th width="15%">Name</th>
            <th width="15%">Players</th>
            <th width="15%">Action</th>
          </tr>
        </thead>
        <tbody>{lobbiesList}</tbody>
      </Table>

      <Button
        className="join-private-btn"
        onClick={() => setShowModal(true)}
      >
        Join Private Lobby
      </Button>
    </div>

    {/* Modal para lobby privado */}
    <Modal isOpen={showModal} toggle={() => setShowModal(false)}>
      <ModalHeader toggle={() => setShowModal(false)}>Join Private Lobby</ModalHeader>
      <ModalBody>
        <p>Introduce el código del lobby privado:</p>
        <Input
          type="text"
          value={privateCode}
          onChange={(e) => setPrivateCode(e.target.value)}
          placeholder="Ej: AB12CD34"
        />
      </ModalBody>
      <ModalFooter>
        <Button color="success" onClick={handleJoinPrivate}>Join</Button>
        <Button color="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
      </ModalFooter>
    </Modal>
    </div>
  </div>
  );
}
