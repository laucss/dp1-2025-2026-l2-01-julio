// ...existing code...
package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChatMessageDTO {
    private Integer playerId;
    private Integer matchId;

    @NotBlank
    @Size(max = 500)
    private String message;

    private LocalDateTime time;

    public ChatMessageDTO() { }

    public ChatMessageDTO(ChatMessage chatMessage) {
        if (chatMessage == null) return;
        if (chatMessage.getPlayer() != null) {
            this.playerId = chatMessage.getPlayer().getId();
        }
        if (chatMessage.getMatch() != null) {
            this.matchId = chatMessage.getMatch().getId();
        }
        this.message = chatMessage.getMessage();
        this.time = chatMessage.getTime();
    }

    // Getters y setters
    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}