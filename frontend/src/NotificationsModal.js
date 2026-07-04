import React, { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import tokenService from "./services/token.service";
import "./static/css/appnavbar/notificationsModal.css";

export default function NotificationsModal({ isOpen, onClose }) {
    const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorModalMessage, setErrorModalMessage] = useState("");
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();
  const pollingRef = useRef(null);

  useEffect(() => {
    if (!isOpen || !user) return;
    let cancelled = false;

    const fetchOnce = async () => {
      try {
        const controller = new AbortController();
        setLoading(true);
        const res = await fetch(`/api/v1/notifications`, {
          headers: { Authorization: `Bearer ${jwt}` },
          signal: controller.signal,
        });
        if (!res.ok) {
          const txt = await res.text().catch(() => "");
          throw new Error(txt || `HTTP ${res.status}`);
        }
        const data = await res.json();
        if (!cancelled) setNotifications(Array.isArray(data) ? data : []);
      } catch (e) {
        if (!cancelled) setError("Error al cargar notificaciones");
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    // Primera carga inmediata
    fetchOnce();
    // Polling ligero cada 5s sólo mientras el panel está abierto
    pollingRef.current = setInterval(fetchOnce, 5000);

    return () => {
      cancelled = true;
      if (pollingRef.current) clearInterval(pollingRef.current);
      pollingRef.current = null;
    };
  }, [isOpen, user?.id, jwt]);

  const handleAccept = async (id) => {
    setLoading(true);

    const res = await fetch(`/api/v1/invitations/${id}/accept`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });

    if (res.ok) {
      const invitation = await res.json();

      console.log(invitation);
      console.log(invitation.match);

      const matchId = invitation.match;

      setNotifications(n => n.filter(notif => notif.id !== id));
      setError(null);

      if (matchId) {
        window.location.href = `/lobby/${matchId}`;
      } else {
        setError("Match ID not found in the invitation.");
        setErrorModalMessage("Match ID not found in the invitation.");
        setShowErrorModal(true);
      }
    }

    setLoading(false);
  };

  const handleReject = async (id) => {
    setLoading(true);
    const res = await fetch(`/api/v1/invitations/${id}/reject`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    if (res.ok) {
      setNotifications(n => n.filter(notif => notif.id !== id));
    } else {
      setError("No se pudo rechazar la invitación");
    }
    setLoading(false);
  };

  const handleAcceptFriendRequest = async (requestId) => {
    try {
      await fetch(`api/v1/friendRequests/accept`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestId),
      });

    } catch (error) {
      // setErrorMessage(error.message);
    }
  }

  const handleRejectFriendRequest = async (requestId)  =>{
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
      
    } catch (error) {
      // setErrorMessage(error.message);
    }
  }

  if (!isOpen) return null;
  if (!user) {
    return (
      <div className="notifications-modal-overlay">
        <div className="notifications-modal-card">
          <h2>Notifications</h2>
          <p className="notif-error">You must be logged in to view your notifications.</p>
          <button className="notif-close-btn" onClick={onClose}>Close</button>
        </div>
      </div>
    );
  }

  const getNotificationText = (notification) => {
    switch (notification.type) {
      case "FRIEND_REQUEST":
        return (
          <>
            <b>{notification.sender.username}</b> has sent you a friend request.
          </>
        );
      case "MATCH_INVITATION_AS_PLAYER":
        return (
          <>
            <b>{notification.sender.username}</b> has invited you to join a match as a player.
          </>
        );
      case "MATCH_INVITATION_AS_SPECTATOR":
        return (
          <>
            <b>{notification.sender.username}</b> has invited you to spectate a match.
          </>
        );
      default:
        return "Unknown notification.";
    }
  }

  return (
    <div className="notifications-modal-overlay">
      <div className="notifications-modal-card">
        <h2>Notifications</h2>
        {/* No mostrar mensaje de cargando */}
        {/* Mensaje de error oculto intencionadamente */}
        <div className="notifications-list-scroll">
          {notifications.length === 0 ? (
            <p>You have no pending invitations</p>
          ) : (
            notifications.map(n => (
              <div key={n.id} className="notification-item">
                <span>{getNotificationText(n)}</span>
                <div className="notif-actions">
                  <button className="notif-accept" onClick={() => n.type === 'FRIEND_REQUEST' ? handleAcceptFriendRequest(n.id) : handleAccept(n.id)}>Accept</button>
                  <button className="notif-reject" onClick={() => n.type === 'FRIEND_REQUEST' ? handleRejectFriendRequest(n.id) : handleReject(n.id)}>Reject</button>
                </div>
              </div>
            ))
          )}
        </div>
        <div className="notif-close-btn-container">
          <button className="notif-close-btn" onClick={onClose}>Close</button>
        </div>
      </div>

      {showErrorModal && (
        <div className="notif-error-overlay">
          <div className="notif-error-card">
            <p>{errorModalMessage}</p>
            <button className="notif-error-close" onClick={() => setShowErrorModal(false)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
}
