import React, { useState } from "react";
import tokenService from "./services/token.service";
import "./static/css/appnavbar/notificationsModal.css";

export default function NotificationsModal({ isOpen, onClose, notifications, refreshNotifications }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [errorModalMessage, setErrorModalMessage] = useState("");
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();

  const handleAccept = async (id) => {
    setLoading(true);
    try {
      const res = await fetch(`/api/v1/invitations/${id}/accept`, {
        method: "POST",
        headers: { Authorization: `Bearer ${jwt}` },
      });

      if (res.ok) {
        const invitation = await res.json();
        const matchId = invitation.match;
        setError(null);

        console.log('invitation', invitation)
        // Refrescar estado global
        await refreshNotifications();

        if (matchId) {
          window.location.href = `/lobby/${matchId}`;
        } else {
          setError("Match ID not found in the invitation.");
          setErrorModalMessage("Match ID not found in the invitation.");
          setShowErrorModal(true);
        }
      }
    } catch (e) {
      setError("Error al aceptar la invitación");
    } finally {
      setLoading(false);
    }
  };

  const handleReject = async (id) => {
    setLoading(true);
    try {
      const res = await fetch(`/api/v1/invitations/${id}/reject`, {
        method: "POST",
        headers: { Authorization: `Bearer ${jwt}` },
      });
      if (res.ok) {
        await refreshNotifications();
      } else {
        setError("No se pudo rechazar la invitación");
      }
    } catch (e) {
      setError("Error en la petición");
    } finally {
      setLoading(false);
    }
  };

  const handleAcceptFriendRequest = async (requestId) => {
    setLoading(true);
    try {
      const res = await fetch(`api/v1/friendRequests/accept`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestId),
      });
      if (res.ok) {
        await refreshNotifications();
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleRejectFriendRequest = async (requestId) => {
    setLoading(true);
    try {
      const res = await fetch(`api/v1/friendRequests/reject`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestId),
      });
      if (res.ok) {
        await refreshNotifications();
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

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
            <b>{notification.sender?.username || "Someone"}</b> has sent you a friend request.
          </>
        );
      case "MATCH_INVITATION_AS_PLAYER":
        return (
          <>
            <b>{notification.sender?.username || "Someone"}</b> has invited you to join a match as a player.
          </>
        );
      case "MATCH_INVITATION_AS_SPECTATOR":
        return (
          <>
            <b>{notification.sender?.username || "Someone"}</b> has invited you to spectate a match.
          </>
        );
      default:
        return "Unknown notification.";
    }
  };

  return (
    <div className="notifications-modal-overlay">
      <div className="notifications-modal-card">
        <h2>Notifications</h2>
        <div className="notifications-list-scroll">
          {!notifications || notifications.length === 0 ? (
            <p>You have no pending invitations</p>
          ) : (
            notifications.map(n => (
              <div key={n.id} className="notification-item">
                <span>{getNotificationText(n)}</span>
                <div className="notif-actions">
                  <button 
                    disabled={loading} 
                    className="notif-accept" 
                    onClick={() => n.type === 'FRIEND_REQUEST' ? handleAcceptFriendRequest(n.id) : handleAccept(n.id)}
                  >
                    Accept
                  </button>
                  <button 
                    disabled={loading} 
                    className="notif-reject" 
                    onClick={() => n.type === 'FRIEND_REQUEST' ? handleRejectFriendRequest(n.id) : handleReject(n.id)}
                  >
                    Reject
                  </button>
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