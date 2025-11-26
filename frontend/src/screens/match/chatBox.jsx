import React, { useEffect, useRef, useState } from "react";
import { ChatApi } from "../../services/chatApi";
import "./chat.css";

export default function ChatBox({ matchId }) {
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [error, setError] = useState(null);
  const chatEndRef = useRef(null);

  // Obtener mensajes del chat
  const fetchMessages = async () => {
    if (!matchId) return;

    try {
      const data = await ChatApi.getMyChat(matchId);
      setMessages(data);
      setError(null);
    } catch (err) {
      console.error(err);
      setError("Error cargando mensajes.");
    }
  };

  // Enviar mensaje
  const sendMessage = async () => {
    if (!text.trim()) return;

    try {
      await ChatApi.sendMessage(text, matchId);
      setText("");
      await fetchMessages(); // refrescar mensajes
    } catch (err) {
      console.error(err);
      setError("Error enviando mensaje.");
    }
  };

  // Polling: actualizar chat cada 2 segundos
  useEffect(() => {
    if (!matchId) return;

    fetchMessages();
    const interval = setInterval(fetchMessages, 2000);
    return () => clearInterval(interval);
  }, [matchId]);

  // Scroll automático al último mensaje
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  // Enviar mensaje con Enter
  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-messages">
        {messages.length === 0 && <div className="chat-empty">No hay mensajes aún</div>}
        {messages.map((msg) => (
          <div key={msg.id} className="chat-message">
            <b>{msg.playerUsername || msg.playerId}:</b> {msg.message}
          </div>
        ))}
        <div ref={chatEndRef}></div>
      </div>

      {error && <div className="chat-error">{error}</div>}

      <textarea
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Escribe un mensaje..."
        className="chat-input"
      />

      <button onClick={sendMessage} className="chat-send-button">
        Enviar
      </button>
    </div>
  );
}
