package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/rooms")
@SecurityRequirement(name = "bearerAuth")
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
    //Probar API
    @GetMapping("/dices/{blackDice}/{whiteDice}")
    public RoomDTO getRoomByDices(@PathVariable Integer blackDice, @PathVariable Integer whiteDice) {
        return new RoomDTO(roomService.findByDice(blackDice, whiteDice));
    }

    @PutMapping("/{roomId}")
    public RoomDTO updateRoom(@PathVariable Integer roomId, RoomDTO room) {
        RoomDTO existing = new RoomDTO(roomService.findById(roomId));
        if (existing != null) {
            existing.setTimesVisited(room.getTimesVisited());
            existing.setNpcsInside(room.getNpcsInside());
            existing.setPlayerInside(room.getPlayerInside());
        }
        return new RoomDTO(roomService.save(room));
    }

}
