import api from "./api";
import tokenService from "./token.service";

export const ChatApi = {

  // Obtener mensajes de una partida específica
  async getMyChat(matchId) {
    try {
      if (!matchId) {
        console.error("matchId es obligatorio para obtener mensajes");
        return [];
      }

      const token = tokenService.getLocalAccessToken();
      if (!token) {
        console.error("No hay token disponible. No puedes cargar el chat.");
        return [];
      }

      const response = await api.get(`/chat/my?matchId=${matchId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      return response.data || [];
    } catch (err) {
      console.error("Error cargando el chat:", err.response?.data || err);
      return [];
    }
  },

  // Enviar mensaje a la partida
  async sendMessage(message, matchId) {
    try {
      if (!message || !matchId) {
        console.error("matchId y message son obligatorios para enviar un mensaje");
        return;
      }

      const token = tokenService.getLocalAccessToken();
      if (!token) {
        console.error("No hay token disponible. No puedes enviar mensajes.");
        return;
      }

      const response = await api.post(
        "/chat",
        { matchId, message },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      return response.data;
    } catch (err) {
      console.error("Error enviando mensaje:", err.response?.data || err);
    }
  },
};
