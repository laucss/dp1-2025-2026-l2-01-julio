import React, { useState } from 'react';
import './friends.css';
import { FaSearch, FaUser } from 'react-icons/fa';

function Friends() {
  const [friends, setFriends] = useState([
    { id: 1, name: 'Player1' },
    { id: 2, name: 'Player2' },
    { id: 3, name: 'Player3' },
  ]);

  const handleDelete = (id) => {
    setFriends(friends.filter(friend => friend.id !== id));
  };

  return (
    <div className="friends-page">
      <h1>Amigos</h1>

      {/* Botones principales */}
      <div className="friends-buttons">
        <button className="main-button">Enviar invitación</button>
        <button className="main-button">Invitaciones</button>
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
          <h2>Lista de Amigos ({friends.length})</h2>
        </div>

        <div className="friends-scroll">
          {friends.map(friend => (
            <div key={friend.id} className="friend-card">
              <FaUser className="friend-avatar" />
              <span className="friend-name">{friend.name}</span>
              <div className="friend-actions">
                <button className="play-btn">Jugar</button>
                <button
                  className="remove-btn"
                  onClick={() => handleDelete(friend.id)}
                >
                  Eliminar
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Friends;