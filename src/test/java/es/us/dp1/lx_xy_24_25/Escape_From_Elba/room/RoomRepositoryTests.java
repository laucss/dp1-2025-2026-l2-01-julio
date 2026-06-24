package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

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
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void findByNameNoRoomReturnsEmpty() {
        Optional<Room> room = roomRepository.findByName("NoExiste");
        assertTrue(room.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "Sala1", "Sala2", "Sala3" })
    public void findByNameParameterized(String name) {
        Optional<Room> room = roomRepository.findByName(name);
        assertTrue(room.isEmpty());
    }

    @Test
    public void saveAndFindByNameReturnsRoom() {
        Room room = new Room();
        room.setName("SalaPrueba");
        room.setBlackDice(3);
        room.setWhiteDice(5);
        room.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room);

        Optional<Room> found = roomRepository.findByName("SalaPrueba");
        assertTrue(found.isPresent());
        assertEquals(room.getName(), found.get().getName());
        assertEquals(room.getBlackDice(), found.get().getBlackDice());
        assertEquals(room.getWhiteDice(), found.get().getWhiteDice());
    }

    @Test
    public void findByDicesReturnsCorrectRoom() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();

        Room room = new Room();
        room.setName("SalaDados");
        room.setBlackDice(4);
        room.setWhiteDice(2);
        room.setAdjacencyList(new ArrayList<>());
        roomRepository.save(room);

        Optional<Room> found = roomRepository.findByDices(4, 2);
        assertTrue(found.isPresent());
        assertEquals("SalaDados", found.get().getName());
    }

    @Test
    public void findAllReturnsAllRooms() {
        Room room1 = new Room("Sala1_" + System.nanoTime(), 1, 1, new ArrayList<>());
        Room room2 = new Room("Sala2_" + System.nanoTime(), 2, 2, new ArrayList<>());
        roomRepository.save(room1);
        roomRepository.save(room2);

        List<Room> rooms = roomRepository.findAll();
        long count = rooms.stream()
            .filter(r -> r.getName().equals(room1.getName()) || r.getName().equals(room2.getName()))
            .count();
        assertEquals(2, count);
        assertTrue(rooms.contains(room1));
        assertTrue(rooms.contains(room2));
    }


    @Test
    public void findByIdReturnsRoom() {
        Room room = new Room("SalaID", 3, 3, new ArrayList<>());
        roomRepository.save(room);

        Optional<Room> found = roomRepository.findById(room.getId());
        assertTrue(found.isPresent());
        assertEquals("SalaID", found.get().getName());
    }

    @Test
    public void deleteAllRemovesSavedRooms() {
        roomRepository.findAll().forEach(r -> r.setAdjacencyList(new ArrayList<>()));
        roomRepository.deleteAll();

        Room a = new Room("TmpA", 1, 1, new ArrayList<>());
        Room b = new Room("TmpB", 2, 2, new ArrayList<>());
        roomRepository.save(a);
        roomRepository.save(b);

        List<Room> before = roomRepository.findAll();
        assertTrue(before.size() >= 2);

        roomRepository.deleteAll();
        List<Room> after = roomRepository.findAll();
        assertTrue(after.isEmpty());
    }
}

