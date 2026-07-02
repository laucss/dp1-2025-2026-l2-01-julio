package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyUpdateDTO {
    private Integer lobbyId;
    private List<PlayerLobbyDTO> players;
    private List<UserLobbyDTO> spectators;
    private String action; 
    private String username;

    public LobbyUpdateDTO() {}

    public LobbyUpdateDTO(Integer lobbyId, List<PlayerLobbyDTO> players, List<UserLobbyDTO> spectators, String action, String username) {
        this.lobbyId = lobbyId;
        this.players = players;
        this.spectators = spectators;
        this.action = action;
        this.username = username;
    }

    @Override
    public String toString() {
        return "LobbyUpdateDTO{" +
                "lobbyId=" + lobbyId +
                ", action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", players=" + players.size() +
                ", spectators=" + (spectators != null ? spectators.size() : 0) + 
                '}';
    }

    // Inner class para los jugadores
    public static class PlayerLobbyDTO {
        private Integer userId;
        private String username;
        private String avatar;

        public PlayerLobbyDTO() {}

        public PlayerLobbyDTO(Integer userId, String username, String avatar) {
            this.userId = userId;
            this.username = username;
            this.avatar = avatar;
        }

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }

    public static class UserLobbyDTO {
        private Integer id; 
        private String username;
        private String avatar;

        public UserLobbyDTO() {}
        public UserLobbyDTO(Integer id, String username, String avatar) {
            this.id = id;
            this.username = username;
            this.avatar = avatar;
        }
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }
}
