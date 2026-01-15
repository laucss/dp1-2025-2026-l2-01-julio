package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;

@Service
public class StatisticService {

    
    private PlayerRepository playerRepository;
    private MatchService matchService;

    @Autowired
    public StatisticService( PlayerRepository playerRepository, MatchService matchService) {
        this.playerRepository = playerRepository;
        this.matchService = matchService;
    }

    //puntos de accion totales de un usuario
    public Integer getTotalAccionPointsByUser(Integer currentUserId) {
        return playerRepository.getTotalAccionPointsByUser(currentUserId);
    }

    //Todas las victorias de un usuario
    public Integer getTotalVictoriesByUser(Integer currentUserId) {
        List<Player> players = playerRepository.findByUserId(currentUserId);
        int victories = 0;
        for (Player player : players) {
            if (player.getMatch() != null && player.getMatch().getWinner() != null) {
                if (player.getId().equals(player.getMatch().getWinner().getId())) {
                    victories++;
                }
            }
        }
        return victories;
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
}