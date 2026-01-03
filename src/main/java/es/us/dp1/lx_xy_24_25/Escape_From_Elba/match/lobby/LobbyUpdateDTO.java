package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import java.util.List;

public class LobbyUpdateDTO {
    private Integer lobbyId;
    private List<PlayerLobbyDTO> players;
    private String action; 
    private String username;

    public LobbyUpdateDTO() {}

    public LobbyUpdateDTO(Integer lobbyId, List<PlayerLobbyDTO> players, String action, String username) {
        this.lobbyId = lobbyId;
        this.players = players;
        this.action = action;
        this.username = username;
    }

    public Integer getLobbyId() { return lobbyId; }
    public void setLobbyId(Integer lobbyId) { this.lobbyId = lobbyId; }

    public List<PlayerLobbyDTO> getPlayers() { return players; }
    public void setPlayers(List<PlayerLobbyDTO> players) { this.players = players; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return "LobbyUpdateDTO{" +
                "lobbyId=" + lobbyId +
                ", action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", players=" + players.size() +
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
}
