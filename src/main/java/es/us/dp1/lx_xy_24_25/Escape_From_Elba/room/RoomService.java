package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;

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

    public Room save(RoomDTO room) {
        return roomRepository.save(room);
    }

    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }

    // Lógica adicional

    public boolean areAdjacent(Room room1, Room room2) {
        return room1.getAdjacencyList().contains(room2);
    }

    public void incrementTimesVisited(RoomDTO room) {
        room.setTimesVisited(room.getTimesVisited() + 1);
        roomRepository.save(room);
    }

    public List<RoomDTO> initializeRoomsForMatch(Match match) {
        List<Room> rooms = roomRepository.findAll();
        Room safeZone = roomRepository.findById(37).orElseThrow(() -> 
        new IllegalStateException("Safe zone (ID 37) not found")); //La safe zone es la habitacion con id 37
        List<Room> availableRooms = new ArrayList<>(rooms);
        availableRooms.remove(safeZone); //Quitamos la safe zone de las habitaciones disponibles
        Random rand = new Random();

        //Asignamos habitaciones aleatoriamente a los NPCs
        //Niall Campbell debe de ir en la safe zone
        for ( Npc npc: match.getNpcs()) {
        Room room;
        if (npc.getIsNiallCampbell()) {
        room = safeZone; // Niall Campbell va en la safe zone
        } else {
            room = availableRooms.remove(rand.nextInt(availableRooms.size())); //Los demas NPCs en habitaciones aleatorias
        }
        npc.setRoom(room);
        }
        //Asignamos habitaciones aleatoriamente a los jugadores 
        for (Player p: match.getPlayers()) {
        Room room = availableRooms.remove(rand.nextInt(availableRooms.size()));
        p.setRoom(room);
        }


        Map<Integer, RoomDTO> map = new HashMap<>();

        for (Room r : rooms) {
            RoomDTO dto = new RoomDTO(r);
            map.put(r.getId(), dto);
        }

        if (match.getPlayers() != null) {
            for (Player p : match.getPlayers()) {
                if (p.getRoom() != null) {
                    RoomDTO dto = map.get(p.getRoom().getId());
                    if (dto != null) {
                        dto.setPlayerInside(p);
                        dto.setTimesVisited(1);
                    }
                }
            }
        }

        if (match.getNpcs() != null) {
            for (Npc n : match.getNpcs()) {
                if (n.getRoom() != null) {
                    RoomDTO dto = map.get(n.getRoom().getId());
                    if (dto != null) {
                        dto.getNpcsInside().add(n);
                    }
                }
            }
        }

        return new ArrayList<>(map.values());
    }
}