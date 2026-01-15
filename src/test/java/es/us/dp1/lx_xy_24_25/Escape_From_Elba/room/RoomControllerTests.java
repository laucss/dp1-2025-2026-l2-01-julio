package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class RoomControllerTests {

    @Autowired
    private RoomController roomController;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void getAllRoomsWhenNoRoomsReturnsEmptyList() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        List<RoomDTO> rooms = roomController.getAllRooms();
        assertTrue(rooms.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "Room1", "Room2", "Room3" })
    public void findByNameNoRoomsReturnsEmpty(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        assertTrue(room.isEmpty());
    }

    @Test
    public void createRoomAndRetrieveById() {
        Room room = new Room();
        room.setName("TestRoom");
        room.setBlackDice(3);
        room.setWhiteDice(4);
        room.setAdjacencyList(List.of());
        roomRepository.save(room);

        RoomDTO roomDTO = roomController.getRoomById(room.getId());
        assertEquals("TestRoom", roomDTO.getName());
        assertEquals(3, roomDTO.getBlackDice());
        assertEquals(4, roomDTO.getWhiteDice());
    }

    @Test
    public void createRoomAndRetrieveAllRooms() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        Room room1 = new Room("Room1", 1, 2, List.of());
        Room room2 = new Room("Room2", 2, 3, List.of());
        roomRepository.save(room1);
        roomRepository.save(room2);

        List<RoomDTO> rooms = roomController.getAllRooms();
        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().anyMatch(r -> r.getName().equals("Room1")));
        assertTrue(rooms.stream().anyMatch(r -> r.getName().equals("Room2")));
    }

    @Test
    public void findRoomByDicesReturnsCorrectRoom() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        Room room = new Room("DiceRoom", 5, 6, List.of());
        roomRepository.save(room);

        RoomDTO dto = roomController.getRoomByDices(5, 6);
        assertEquals("DiceRoom", dto.getName());
    }


}
