import { useNavigate } from 'react-router-dom';
import React, { useEffect, useState } from 'react';
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import { Button, ButtonGroup, Table } from "reactstrap";

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

  const navigate = useNavigate();

  // 👉 Función para unirse a un lobby
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
        // Redirige a la sala de espera de ese lobby
        navigate(`/lobby/${match.id}`);
      } else {
        const errorText = await response.text();
        console.error("Error joining lobby:", errorText);
        alert("No se pudo unir al lobby.");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Error al conectar con el servidor.");
    }
  };

  const lobbiesList = lobbies.map((match) => {
    return (
      <tr key={match.id}>
        <td className="text-center">{match.name}</td>
        <td className="text-center">
          {match.players ? match.players.length : 0} / {match.maxPlayers}
        </td>
        <td className="text-center">
          <ButtonGroup>
            <Button
              size="sm"
              color="success"
              aria-label={"join-" + match.name}
              onClick={() => handleJoin(match)}
            >
              Join
            </Button>
          </ButtonGroup>
        </td>
      </tr>
    );
  });

  return (
    <div>
      <div className="admin-page-container">
        <h1 className="text-center">Lobbies</h1>
        <div>
          <Table aria-label="lobbies" className="mt-4">
            <thead>
              <tr>
                <th width="15%" className="text-center">Name</th>
                <th width="15%" className="text-center">Players</th>
                <th width="15%" className="text-center">Action</th>
              </tr>
            </thead>
            <tbody>{lobbiesList}</tbody>
          </Table>
        </div>
        <Button color="primary">
          Join Private Lobby
        </Button>
      </div>
    </div>
  );
}
