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
  const color = statusColor[normalized] || '#999';
  return (
    <div className="user-status-indicator">
      <span
        className="status-dot"
        style={{ backgroundColor: color }}
        title={statusText[normalized] || 'Desconocido'}
      />
      <span className="status-label" style={{ color }}>
        {statusText[normalized] || 'Desconocido'}
      </span>
    </div>
  );
}
