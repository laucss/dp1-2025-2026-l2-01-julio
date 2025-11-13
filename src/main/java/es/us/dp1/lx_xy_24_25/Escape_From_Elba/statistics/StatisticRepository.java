package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public class StatisticRepository extends CrudRepository<Statistic, Integer>{
    
    List<Statistic> findAll();

}
