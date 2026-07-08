package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void adjacencyIsReflectedInDto() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();

        Room r1 = new Room();
        r1.setName("AdjA");
        r1.setBlackDice(1);
        r1.setWhiteDice(1);
        r1.setAdjacencyList(new ArrayList<>());

        Room r2 = new Room();
        r2.setName("AdjB");
        r2.setBlackDice(2);
        r2.setWhiteDice(2);
        r2.setAdjacencyList(new ArrayList<>());

        roomRepository.save(r2);
        r1.getAdjacencyList().add(r2);
        roomRepository.save(r1);

        RoomDTO dto = roomController.getRoomById(r1.getId());
        assertEquals(1, dto.getAdjacencyList().size());
        assertEquals("AdjB", dto.getAdjacencyList().get(0).getName());
    }

    @Test
public void getAllRoomsReturnsDtos() {

    List<RoomDTO> rooms = roomController.getAllRooms();

    assertFalse(rooms.isEmpty());

    for (RoomDTO room : rooms) {
        assertNotNull(room.getId());
        assertNotNull(room.getName());
    }
}

@Test
public void getRoomByIdNonExistingReturnsException() {

    assertThrows(NullPointerException.class,
            () -> roomController.getRoomById(999999));
}

@Test
public void getRoomByDiceNotFound() {

    assertThrows(NullPointerException.class,
            () -> roomController.getRoomByDices(99, 99));
}



@Test
public void updateRoomNotExistingThrowsException() {

    Room room = new Room();
    room.setName("Fake");

    RoomDTO dto = new RoomDTO(room);

    assertThrows(NullPointerException.class,
            () -> roomController.updateRoom(9999, dto));
}

@Test
public void roomDtoStartsEmpty() {

    Room room = new Room();
    room.setName("Room");
    room.setBlackDice(1);
    room.setWhiteDice(2);
    room.setAdjacencyList(new ArrayList<>());

    RoomDTO dto = new RoomDTO(room);

    assertEquals(0, dto.getTimesVisited());
    assertTrue(dto.getNpcsInside().isEmpty());
    assertNull(dto.getPlayerInside());
}

@Test
public void getAllRoomsContainsAllIds() {

    List<Room> rooms = roomRepository.findAll();

    List<RoomDTO> dtos = roomController.getAllRooms();

    assertEquals(rooms.size(), dtos.size());

    for(Room room : rooms){

        assertTrue(
            dtos.stream()
                .anyMatch(dto -> dto.getId().equals(room.getId()))
        );
    }
}

@Test
public void roomDtoContainsAdjacencyList() {

    Room room = roomRepository.findById(2).get();

    RoomDTO dto = roomController.getRoomById(room.getId());

    assertNotNull(dto.getAdjacencyList());
}


}
