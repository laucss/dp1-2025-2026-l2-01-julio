package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;


import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;

@Service
public class StatisticService {

    
    private PlayerRepository playerRepository;
    private MatchService matchService;
    private UserService userService;

    @Autowired
    public StatisticService( PlayerRepository playerRepository, MatchService matchService, UserService userService) {
        this.playerRepository = playerRepository;
        this.matchService = matchService;
        this.userService = userService;
    }

    //puntos de accion totales de un usuario
    public Integer getTotalAccionPointsByUser(Integer currentUserId) {
        return playerRepository.getTotalAccionPointsByUser(currentUserId);
    }

    //Todas las victorias de un usuario
    public Integer getTotalVictoriesByUser(Integer currentUserId) {
        Integer victories = playerRepository.getTotalVictoriesByUser(currentUserId);
        return victories != null ? victories : 0;
    }

    //partidas jugadas por un usuario
    public Integer getMatchesPlayedByUser(Integer currentUserId) {
        List<Player> players = playerRepository.findByUserId(currentUserId);
        return players.size();
    }

    //total tiempo jugado por un usuario
    public Integer getTotalTimePlayedByUserFOR(Integer currentUserId) {
        List<Player> players = playerRepository.findByUserId(currentUserId);
        int totalTime = 0;
        for (Player player : players) {
            if (player.getMatch() != null && player.getMatch().getStartTime() != null && player.getMatch().getEndTime() != null) {
                totalTime += java.time.Duration.between(player.getMatch().getStartTime(), player.getMatch().getEndTime()).toMinutes();
            }
        }
        return totalTime;
    }

    //media de jugadores por partida
    public Double getAveragePlayersPerMatch() {
        List<Match> matches = matchService.getAllMatchs();
        if (matches.isEmpty()) {
            return 0.0;
        }
        Double numPlayers = 0.0;
        for (Match m : matches) {
            numPlayers += m.getMaxPlayers();
        }
        return numPlayers / matches.size();
    }

    //total de partidas jugadas
    public Integer getTotalMatchesPlayed() {
        List<Match> matches = matchService.getAllMatchs();
        return matches.size();
    }

    // batallas ganadas por un usuario
    public Integer getBattlesWonByUser(Integer currentUserId) {
        Integer battlesWon = playerRepository.getBattlesWonByUser(currentUserId);
        return battlesWon != null ? battlesWon : 0;
    }

    // total de batallas disputadas (suma de batallas jugadas por todos los jugadores)
    public Integer getTotalBattlesDisputed() {
        Integer totalBattlesDisputed = playerRepository.getTotalBattlesDisputed();
        return totalBattlesDisputed != null ? totalBattlesDisputed : 0;
    }

    // habitaciones visitadas por un usuario
    public Integer getTotalRoomsVisitedByUser(Integer currentUserId) {
        Integer total = playerRepository.getTotalRoomsVisitedByUser(currentUserId);
        return total != null ? total : 0;
    }

    // media de habitaciones visitadas por partida
    public Double getAverageRoomsVisitedPerMatch() {
        List<Match> matches = matchService.getAllMatchs();
        if (matches.isEmpty()) {
            return 0.0;
        }
        Double totalRoomsVisited = 0.0;
        for (Match m : matches) {
            for (Player p : m.getPlayers()) {
                if (p.getRoomsVisited() != null) {
                    totalRoomsVisited += p.getRoomsVisited();
                }
            }
        }
        return totalRoomsVisited / matches.size();
    }

    public Double getAverageMatchDuration() {
    List<Match> matches = matchService.getAllMatchs();

    if(matches.isEmpty()) {
        return 0.0;
    }

    double totalMinutes = 0;

    for(Match m : matches) {
        if(m.getStartTime() != null && m.getEndTime() != null) {
            totalMinutes += Duration.between(
                m.getStartTime(),
                m.getEndTime()
            ).toMinutes();
        }
    }

    return totalMinutes / matches.size();
    }

    public Integer getBattlesPlayedByUser(Integer currentUserId) {
    Integer battlesPlayed = playerRepository.getBattlesPlayedByUser(currentUserId);
    return battlesPlayed != null ? battlesPlayed : 0;
    }

    public Integer getMaxRoomsVisitedInMatch(Integer currentUserId) {
        List<Player> players = playerRepository.findByUserId(currentUserId);

        int maxRooms = 0;

        for (Player player : players) {
            if (player.getRoomsVisited() != null &&
                player.getRoomsVisited() > maxRooms) {

                maxRooms = player.getRoomsVisited();
            }
        }

        return maxRooms;
    }

    public Integer getLongestMatchDuration() {
    List<Match> matches = matchService.getAllMatchs();

    int longest = 0;

    for (Match m : matches) {
        if (m.getStartTime() != null && m.getEndTime() != null) {

            int duration = (int) Duration.between(
                m.getStartTime(),
                m.getEndTime()
            ).toMinutes();

            longest = Math.max(longest, duration);
        }
    }

    return longest;
    }

    public Integer getShortestMatchDuration() {
    List<Match> matches = matchService.getAllMatchs();

    Integer shortest = null;

    for (Match m : matches) {
        if (m.getStartTime() != null && m.getEndTime() != null) {

            int duration = (int) Duration.between(
                m.getStartTime(),
                m.getEndTime()
            ).toMinutes();

            if (shortest == null || duration < shortest) {
                shortest = duration;
            }
        }
    }

    return shortest != null ? shortest : 0;
    }

    public String getPlayerType(Integer userId) {

        Integer battlesWon = getBattlesWonByUser(userId);
        Integer roomsVisited = getTotalRoomsVisitedByUser(userId);

        if (battlesWon > roomsVisited) {
            return "Aggressive";
        }

        if (roomsVisited > battlesWon * 2) {
            return "Explorer";
        }

        return "Balanced";
    }

    public Double getWinRateByUser(Integer userId){

    Integer victories = getTotalVictoriesByUser(userId);
    Integer matches = getMatchesPlayedByUser(userId);

    return matches > 0
        ? (victories * 100.0) / matches
        : 0.0;
}



// Devuelve el ranking de usuarios basado en victorias
    public List<RankingDTO> getRanking() {

        List<User> users = (List<User>) userService.findAll();

        return users.stream()
            .map(user -> new RankingDTO(
                user.getId(),
                user.getUsername(),
                user.getAvatar(),
                getTotalVictoriesByUser(user.getId())
            ))
            .sorted((a, b) ->
                Integer.compare(b.getTotalVictories(), a.getTotalVictories()))
            .toList();
    }

}