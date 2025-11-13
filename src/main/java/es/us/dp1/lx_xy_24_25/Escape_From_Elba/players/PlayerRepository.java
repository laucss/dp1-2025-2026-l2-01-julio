package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    Player save(Player player);

    Optional<Player> findByUser(User user);

    @Query("SELECT p FROM Player p WHERE p.statistic.id = :statisticId")
    Player findByStatisticId(Integer statisticId);
}
