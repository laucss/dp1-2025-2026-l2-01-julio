package es.us.dp1.lx_xy_24_25.room;
//cambio para merge en FSS8078
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public Room findById(Integer id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room findByDice(Integer blackDice, Integer whiteDice) {
        return roomRepository.findByDices(blackDice, whiteDice).orElse(null);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }

    // Lógica adicional

    public boolean areAdjacent(Room room1, Room room2) {
        return room1.getAdjacencyList().contains(room2);
    }

    public void incrementTimesVisited(Room room) {
        room.setTimesVisited(room.getTimesVisited() + 1);
        roomRepository.save(room);
    }
}