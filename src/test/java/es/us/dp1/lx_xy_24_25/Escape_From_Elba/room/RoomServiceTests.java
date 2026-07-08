package es.us.dp1.lx_xy_24_25.Escape_From_Elba.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs.Npc;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

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

    @Test
void areAdjacentReturnsTrue() {

    Room room1 = new Room();
    Room room2 = new Room();

    room1.setAdjacencyList(List.of(room2));

    assertTrue(roomService.areAdjacent(room1, room2));
}

@Test
void areAdjacentReturnsFalse() {

    Room room1 = new Room();
    Room room2 = new Room();

    room1.setAdjacencyList(new ArrayList<>());

    assertFalse(roomService.areAdjacent(room1, room2));
}

@Test
void getWordEscapeTower1() {
    assertEquals("ESCAPE", roomService.getWordOfEscapeFromTower(1));
}

@Test
void getWordEscapeTower6() {
    assertEquals("FROM", roomService.getWordOfEscapeFromTower(6));
}

@Test
void getWordEscapeTower31() {
    assertEquals("ELBA", roomService.getWordOfEscapeFromTower(31));
}

@Test
void getWordEscapeTower36() {
    assertEquals("PEACE", roomService.getWordOfEscapeFromTower(36));
}

@Test
void getWordEscapeInvalidTower() {
    assertNull(roomService.getWordOfEscapeFromTower(500));
}

@Test
void getAllTowersReturnsFourRooms() {

    List<Room> towers = roomService.getAllTowers();

    assertEquals(4, towers.size());

    assertTrue(
        towers.stream().anyMatch(r -> r.getId().equals(1))
    );

    assertTrue(
        towers.stream().anyMatch(r -> r.getId().equals(6))
    );

    assertTrue(
        towers.stream().anyMatch(r -> r.getId().equals(31))
    );

    assertTrue(
        towers.stream().anyMatch(r -> r.getId().equals(36))
    );
}
@Test
void getRandomRoomNeverReturnsTower() {

    for(int i=0;i<30;i++){

        Room room = roomService.getRandomRoom();

        assertFalse(
            room.getId()==1 ||
            room.getId()==6 ||
            room.getId()==31 ||
            room.getId()==36
        );
    }
}

@Test
void randomFightRoomIsNeverSafeZone() {

    Room room = roomService.getRandomFightRoom(null);

    assertNotEquals(37, room.getId());
}

@Test
void getRandomUnoccupiedRoomWorks() {

    Match match = new Match();

    Player player = new Player();

    player.setRoom(roomRepository.findById(15).get());

    match.setPlayers(List.of(player));

    match.setNpcs(new ArrayList<>());

    Room room = roomService.getRandomUnoccupiedRoom(match);

    assertNotEquals(15, room.getId());
}

@Test
void randomUnoccupiedRoomWithNullMatch() {

    Room room = roomService.getRandomUnoccupiedRoom(null);

    assertNotNull(room);
}

@Test
void initializeRoomsAssignsRoomsToPlayers() {

    Match match = new Match();

    Player p1 = new Player();
    Player p2 = new Player();

    match.setPlayers(List.of(p1,p2));
    match.setNpcs(new ArrayList<>());

    roomService.initializeRoomsForMatch(match);

    assertNotNull(p1.getRoom());
    assertNotNull(p2.getRoom());
}

@Test
void initializeRoomsAssignsRoomsToNpcs() {

    Match match = new Match();

    Npc npc = new Npc();
    npc.setIsNiallCampbell(false);

    match.setPlayers(new ArrayList<>());
    match.setNpcs(List.of(npc));

    roomService.initializeRoomsForMatch(match);

    assertNotNull(npc.getRoom());
}

@Test
void niallStartsInSafeArea() {

    Match match = new Match();

    Npc npc = new Npc();
    npc.setIsNiallCampbell(true);

    match.setPlayers(new ArrayList<>());
    match.setNpcs(List.of(npc));

    roomService.initializeRoomsForMatch(match);

    assertEquals(37, npc.getRoom().getId());
}

@Test
void playersNeverStartInTower() {

    Match match = new Match();

    Player player = new Player();

    match.setPlayers(List.of(player));
    match.setNpcs(new ArrayList<>());

    roomService.initializeRoomsForMatch(match);

    Integer id = player.getRoom().getId();

    assertFalse(id==1 || id==6 || id==31 || id==36);
}

@Test
void initializeRoomsCreatesDtoForEveryRoom() {

    Match match = new Match();

    match.setPlayers(new ArrayList<>());
    match.setNpcs(new ArrayList<>());

    List<RoomDTO> rooms = roomService.initializeRoomsForMatch(match);

    assertEquals(roomRepository.count(), rooms.size());
}

}

