import React, { useEffect, useRef, useState } from "react";
import { ChatApi } from "../../services/chatApi";
import "../../static/css/match/chat.css";

export default function ChatBox({ matchId }) {
   console.log("MATCH ID RECIBIDO:", matchId);

  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [error, setError] = useState(null);
  const chatEndRef = useRef(null);

  //Obtener mensajes del chat
  const fetchMessages = async () => {
    if (!matchId) return;

    try {
      const data = await ChatApi.getMyChat(matchId);

      //Asegurar que el array es válido
      setMessages(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      console.error(err);
      setError("Error cargando mensajes.");
    }
  };
  

  //Enviar mensaje
  const sendMessage = async () => {
    console.log("text:", text);
        console.log("matchId:", matchId);

    
    if (!text.trim()) return;

    try {
      await ChatApi.sendMessage(text, matchId);
      setText("");
      fetchMessages(); // refrescar mensajes
    } catch (err) {
      console.error(err);
      setError("Error enviando mensaje.");
    }
  };

  //Polling cada 2 segundos
  useEffect(() => {
    if (!matchId) return;

    fetchMessages();
    const interval = setInterval(fetchMessages, 2000);
    return () => clearInterval(interval);
  }, [matchId]);

  //Scroll automático hasta el último mensaje
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  //Enviar mensaje con Enter
  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="chat-container">
      
      <div className="chat-messages">
        {messages.length === 0 && (
          <div className="chat-empty">There is no messages yet</div>
        )}

        {messages.map((msg, i) => (
          <div key={msg.id || i} className="chat-message">
            <b>{msg.playerUsername ?? "Player"}:</b> {msg.message}
            <span className="chat-time">
              {msg.time
                ? ` ${new Date(msg.time).toLocaleTimeString([], {
                    hour: "2-digit",
                    minute: "2-digit",
                  })}`
                : ""}
            </span>
          </div>
        ))}

        <div ref={chatEndRef}></div>
      </div>

      {error && <div className="chat-error">{error}</div>}

      <textarea
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Write a message..."
        className="chat-input"
      />

      <button onClick={sendMessage} className="chat-send-button">
        Send
      </button>
    </div>
  );
}
