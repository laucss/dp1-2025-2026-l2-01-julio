package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

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
public class RoomServiceTests {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void findAllRoomsReturnsEmptyListInitially() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        List<Room> rooms = roomService.findAllRooms();
        assertTrue(rooms.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Sala A", "Sala B", "Sala C"})
    public void findByNameNoRoomReturnsEmptyOptional(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        assertTrue(room.isEmpty());
    }

    @Test
    public void saveRoomAndRetrieveById() {
        Room room = new Room();
        room.setName("Sala Test");
        room.setBlackDice(2);
        room.setWhiteDice(3);
        room.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room);

        Room retrieved = roomService.findById(room.getId());
        assertEquals(room.getName(), retrieved.getName());
        assertEquals(room.getBlackDice(), retrieved.getBlackDice());
        assertEquals(room.getWhiteDice(), retrieved.getWhiteDice());
    }

    @Test
    public void saveRoomAndRetrieveByDices() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        Room room = new Room();
        room.setName("Sala Dices");
        room.setBlackDice(4);
        room.setWhiteDice(5);
        room.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room);

        Room retrieved = roomService.findByDice(4, 5);
        assertEquals(room.getName(), retrieved.getName());
    }

    @Test
    public void findAllRoomsMultipleRoomsReturnsAll() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();
        Room room1 = new Room();
        room1.setName("Sala 1");
        room1.setBlackDice(1);
        room1.setWhiteDice(1);
        room1.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setName("Sala 2");
        room2.setBlackDice(2);
        room2.setWhiteDice(2);
        room2.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room2);

        List<Room> rooms = roomService.findAllRooms();
        assertEquals(2, rooms.size());
        assertTrue(rooms.contains(room1));
        assertTrue(rooms.contains(room2));
    }

    @Test
    public void findByIdReturnsNullWhenNotExists() {
        Room found = roomService.findById(999999);
        assertNull(found);
    }

}

