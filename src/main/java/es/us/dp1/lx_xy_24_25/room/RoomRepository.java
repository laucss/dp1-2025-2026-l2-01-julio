package es.us.dp1.lx_xy_24_25.room;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Integer> {

    List<Room> findAll();

    Optional<Room> findByName(String name);

    @Query("SELECT r FROM Room r WHERE r.blackDice = :blackDice AND r.whiteDice = :whiteDice")
    Optional<Room> findByDices(Integer blackDice, Integer whiteDice);

    Optional<Room> findById(int id);

}
