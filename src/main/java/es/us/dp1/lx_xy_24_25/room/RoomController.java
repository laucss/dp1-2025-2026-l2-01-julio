package es.us.dp1.lx_xy_24_25.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/match/{matchId}/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> rooms = roomService.findAllRooms().stream()
            .map(room -> new RoomDTO(room))
            .toList();
        return rooms;
    }

    @GetMapping("/{roomId}")
    public RoomDTO getRoomById(@PathVariable Integer roomId) {
        return new RoomDTO(roomService.findById(roomId));
    }

    @GetMapping("/dices/{blackDice}/{whiteDice}")
    public RoomDTO getRoomByDices(@PathVariable Integer blackDice, @PathVariable Integer whiteDice) {
        return new RoomDTO(roomService.findByDice(blackDice, whiteDice));
    }

    //DTO?
    @PostMapping
    public Room createRoom(Room room) {
        return roomService.save(room);
    }

    //DTO?
    @PutMapping("/{roomId}")
    public RoomDTO updateRoom(@PathVariable Integer roomId, Room room) {
        RoomDTO existing = new RoomDTO(roomService.findById(roomId));
        if (existing != null) {
            existing.setName(room.getName());
            existing.setBlackDice(room.getBlackDice());
            existing.setWhiteDice(room.getWhiteDice());
            existing.setTimesVisited(room.getTimesVisited());
            existing.setAdjacencyList(room.getAdjacencyList());
            existing.setNpcsInside(room.getNpcsInside());
            existing.setPlayerInside(room.getPlayerInside());
        }
        return new RoomDTO(roomService.save(room));
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Integer roomId) {
        roomService.deleteById(roomId);
    }
}
