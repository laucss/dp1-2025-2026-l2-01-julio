import React from 'react';
import './UserStatusIndicator.css';

const statusColor = {
  ONLINE: '#4CAF50',
  OFFLINE: '#E53935',
  PLAYING: '#FF9800',
};

const statusText = {
  ONLINE: 'Online',
  OFFLINE: 'Offline',
  PLAYING: 'Jugando',
};

export default function UserStatusIndicator({ status }) {
  const normalized = (status || '').toUpperCase();
  return (
    <div className="user-status-indicator">
      <span
        className="status-dot"
        style={{ backgroundColor: statusColor[normalized] || '#999' }}
        title={statusText[normalized] || 'Desconocido'}
      />
    </div>
  );
}
