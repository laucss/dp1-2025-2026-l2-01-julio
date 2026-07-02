import { useNavigate } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import tokenService from "../../services/token.service";
import '../../static/css/match/listingLobbies.css';
import { Button, ButtonGroup, Table, Modal, ModalHeader, ModalBody, ModalFooter, Input } from "reactstrap";

const jwt = tokenService.getLocalAccessToken();

export default function JoinMatch() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [lobbies, setLobbies] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedTab, setSelectedTab] = useState("WAITING")
  const [showModal, setShowModal] = useState(false);  
  const [privateCode, setPrivateCode] = useState("");
  const [showFullMatchModal, setShowFullMatchModal] = useState(false);
  const [showStartedMatchModal, setShowStartedMatchModal] = useState(false);

  const navigate = useNavigate();

  const fetchLobbies = async (status, currentPage = 0) => {
    try {
      const response = await fetch(`/api/v1/lobbies?status=${status}&page=${currentPage}&size=10`, {
        headers: { Authorization: `Bearer ${jwt}` },
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text || 'Error loading lobbies');
      }

      const data = await response.json();
      // Expecting a Page response with `content` and `totalPages`
      setLobbies(Array.isArray(data.content) ? data.content : []);
      setTotalPages(typeof data.totalPages === 'number' ? data.totalPages : 0);
    } catch (error) {
      setMessage(error.message || 'Failed to fetch lobbies');
      setVisible(true);
    }
  };


  const handleJoin = async (match) => {
    try {
      const response = await fetch(`/api/v1/lobbies/${match.id}/join`, {
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
        let text = "";
        try { text = await response.text(); } catch {}
        const lower = (text || "").toLowerCase();
        if (response.status === 400 && (lower.includes("llena") || lower.includes("full"))) {
          setShowFullMatchModal(true);
        } else if (response.status === 400 && (lower.includes("comenzado") || lower.includes("empezado") || lower.includes("correct status"))) {
          setShowStartedMatchModal(true);
        } else {
          alert(" No se pudo unir al lobby: " + (text || "Error desconocido"));
        }
      }
    } catch (error) {
      alert(" Error al conectar con el servidor.");
    }
  }

    const handleSpectate = async (match) => {
    try {
      const response = await fetch(`/api/v1/matches/${match.id}/spectate`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: 'application/json',
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const data = await response.json()
        if (data.status === 'WAITING') {
          navigate(`/lobby/${match.id}`)
        } if (data.status === 'PLAYING')  {
          navigate(`/macthes/${match.id}`)
        } 
        
      } else {
        let text = "";
        try { text = await response.text(); } catch {}
          alert(" No se pudo unir a la partida: " + (text || "Error desconocido"));
        }
    } catch (error) {
        alert(" Error al conectar con el servidor.");
    }
  }

 
  const handleJoinPrivate = async () => {
    if (!privateCode) {
      alert("Introduce un código primero");
      return;
    }

    try {
      const response = await fetch(`/api/v1/lobbies/join/private?code=${privateCode}`, {
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
          {match.status === "WAITING" &&
            (match.players && match.players.length >= match.maxPlayers ? (
              <Button
                size="sm"
                color="danger"
                disabled
              >
                FULL
              </Button>
            ) : (
              <Button
                size="sm"
                color="success"
                aria-label={`join-${match.name}`}
                onClick={() => handleJoin(match)}
              >
                Join
              </Button>
            ))}

          <Button
            size="sm"
            color="info"
            aria-label={`spectate-${match.name}`}
            onClick={() => handleSpectate(match)}
          >
            Spectate
          </Button>
        </ButtonGroup>
      </td>
    </tr>
  ))

  useEffect(() => {
    fetchLobbies(selectedTab,page);
  }, [selectedTab,page]);

  return (
  <div className="admin-page-container">
    <div className="lobbies-overlay">
    <div className="lobbies-box">

      {/* Flecha de volver al inicio */}
      <button 
          className="back-arrow-btn"
          onClick={() => navigate('/')}
        >
          ￩
      </button>

      <ButtonGroup className="mb-3">
        <Button
          color={selectedTab === "WAITING" ? "primary" : "secondary"}
          onClick={() => {
            setSelectedTab("WAITING");
            setPage(0);
          }}
        >
          Lobbies
        </Button>

        <Button
          color={selectedTab === "PLAYING" ? "primary" : "secondary"}
          onClick={() => {
            setSelectedTab("PLAYING");
            setPage(0);
          }}
        >
          Ongoing Matches
        </Button>
      </ButtonGroup>

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

      <div style={{ display: 'flex', justifyContent: 'center', marginTop: 12 }}>
        <ButtonGroup className="pagination-group">
          <Button disabled={page === 0} onClick={() => setPage(page - 1)}>◀</Button>
          <Button disabled>{page + 1} / {totalPages}</Button>
          <Button disabled={page >= Math.max(0, totalPages - 1)} onClick={() => setPage(page + 1)}>▶</Button>
        </ButtonGroup>
      </div>
    </div>

    {/* Modal para lobby privado */}
    <Modal
      isOpen={showModal}
      toggle={() => setShowModal(false)}
      centered
      backdrop="static"
      className="join-private-modal"
    >
      <ModalHeader
        toggle={() => setShowModal(false)}
        className="join-private-modal-header"
      >
        Join Private Lobby
      </ModalHeader>

      <ModalBody className="join-private-modal-body">
        <p>Enter the private lobby code:</p>

        <Input
          className="join-private-input"
          type="text"
          value={privateCode}
          onChange={(e) => setPrivateCode(e.target.value)}
          placeholder="Example: AB12CD34"
        />
      </ModalBody>

      <ModalFooter className="join-private-modal-footer">
        <Button
          className="join-private-confirm-btn"
          onClick={handleJoinPrivate}
        >
          Join
        </Button>

        <Button
          className="join-private-cancel-btn"
          onClick={() => setShowModal(false)}
        >
          Cancel
        </Button>
      </ModalFooter>
    </Modal>

    {/* Modal para partida llena */}
    <Modal isOpen={showFullMatchModal} toggle={() => setShowFullMatchModal(false)} centered backdrop="static">
      <ModalBody className="text-center" style={{ padding: '40px 20px' }}>
        <p style={{ fontSize: '18px', marginBottom: '30px' }}>
          The match you want to join is already full
        </p>
        <Button 
          color="primary" 
          onClick={() => setShowFullMatchModal(false)}
          style={{ marginTop: '20px' }}
        >
          Close
        </Button>
      </ModalBody>
    </Modal>

    {/* Modal para partida ya comenzada */}
    <Modal isOpen={showStartedMatchModal} toggle={() => setShowStartedMatchModal(false)} centered backdrop="static">
      <ModalBody className="text-center" style={{ padding: '40px 20px' }}>
        <p style={{ fontSize: '18px', marginBottom: '30px' }}>
          The game you want to join has already started
        </p>
        <Button 
          color="primary" 
          onClick={() => setShowStartedMatchModal(false)}
          style={{ marginTop: '20px' }}
        >
          Close
        </Button>
      </ModalBody>
    </Modal>
    </div>
  </div>
  );
}
