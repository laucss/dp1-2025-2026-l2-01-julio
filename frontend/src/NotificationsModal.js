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
        const res = await fetch(`/api/v1/notifications?receiverId=${user.id}`, {
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
    const res = await fetch(`/api/v1/notifications/${id}/accept`, {
      method: "POST",
      headers: { Authorization: `Bearer ${jwt}` },
    });
    if (res.ok) {
      // Obtener el matchId de la notificación aceptada
      const notif = notifications.find(notif => notif.id === id);
      setNotifications(n => n.filter(notif => notif.id !== id));
      setError(null);
      // Compatibilidad máxima: buscar cualquier campo de id de lobby
      const lobbyId = notif?.matchId || notif?.lobbyId || notif?.idLobby || notif?.match?.id;
      console.log('Notificación aceptada:', notif);
      console.log('Id de lobby detectado:', lobbyId);
      if (lobbyId) {
        window.location.href = `/lobby/${lobbyId}`;
      } else {
        setError('No se encontró el id de la partida en la invitación.');
        setErrorModalMessage('No se encontró el id de la partida en la invitación.');
        setShowErrorModal(true);
      }
    } else {
      let text = "";
      try { text = await res.text(); } catch {}
      const lower = (text || "").toLowerCase();
      const message = lower.includes("comenzado") || lower.includes("empezado")
        ? "La partida a la que te quieres unir ya ha comenzado."
        : lower.includes("llena") || lower.includes("full")
          ? "La partida a la que quieres unirte está llena."
          : "No se pudo aceptar la invitación";
      setError(message);
      setErrorModalMessage(message);
      setShowErrorModal(true);
    }
    setLoading(false);
  };

  const handleReject = async (id) => {
    setLoading(true);
    const res = await fetch(`/api/v1/notifications/${id}/reject`, {
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

  if (!isOpen) return null;
  if (!user) {
    return (
      <div className="notifications-modal-overlay">
        <div className="notifications-modal-card">
          <h2>Notificaciones</h2>
          <p className="notif-error">Debes iniciar sesión para ver tus notificaciones.</p>
          <button className="notif-close-btn" onClick={onClose}>Cerrar</button>
        </div>
      </div>
    );
  }

  return (
    <div className="notifications-modal-overlay">
      <div className="notifications-modal-card">
        <h2>Notificaciones</h2>
        {/* No mostrar mensaje de cargando */}
        {/* Mensaje de error oculto intencionadamente */}
        <div className="notifications-list-scroll">
          {notifications.length === 0 ? (
            <p>No tienes invitaciones pendientes</p>
          ) : (
            notifications.map(n => (
              <div key={n.id} className="notification-item">
                <span>El jugador <b>{n.sender.username}</b> te ha invitado a jugar una partida.</span>
                <div className="notif-actions">
                  <button className="notif-accept" onClick={() => handleAccept(n.id)}>Aceptar</button>
                  <button className="notif-reject" onClick={() => handleReject(n.id)}>Rechazar</button>
                </div>
              </div>
            ))
          )}
        </div>
        <div className="notif-close-btn-container">
          <button className="notif-close-btn" onClick={onClose}>Cerrar</button>
        </div>
      </div>

      {showErrorModal && (
        <div className="notif-error-overlay">
          <div className="notif-error-card">
            <p>{errorModalMessage}</p>
            <button className="notif-error-close" onClick={() => setShowErrorModal(false)}>Cerrar</button>
          </div>
        </div>
      )}
    </div>
  );
}
