package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.achievements;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer>{

    @Query("SELECT a FROM Achievement a WHERE a.metric = :metric AND a.tier = :tier")
    List<Achievement> findByTier(TierType tier);

}