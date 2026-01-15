package es.us.dp1.lx_xy_24_25.Escape_From_Elba.npcs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.RoomDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NpcDTO Tests")
public class NpcDTOTest {

    private NpcDTO npcDTO;
    private Npc testNpc;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        testRoom = new Room();
        testRoom.setId(1);
        testRoom.setName("Test Room");
        testRoom.setBlackDice(1);
        testRoom.setWhiteDice(1);

        testNpc = new Npc();
        testNpc.setId(1);
        testNpc.setStrength(5);
        testNpc.setIsNiallCampbell(false);
        testNpc.setRoom(testRoom);

        npcDTO = new NpcDTO();
    }

    @Test
    @DisplayName("Should create empty NpcDTO with default constructor")
    void testDefaultConstructor() {
        assertThat(npcDTO.getId()).isNull();
        assertThat(npcDTO.getStrength()).isNull();
        assertThat(npcDTO.getIsNiallCampbell()).isNull();
        assertThat(npcDTO.getRoom()).isNull();
    }

    @Test
    @DisplayName("Should create NpcDTO from Npc with all fields populated")
    void testConstructorWithNpc() {
        NpcDTO dto = new NpcDTO(testNpc);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getStrength()).isEqualTo(5);
        assertThat(dto.getIsNiallCampbell()).isFalse();
        assertThat(dto.getRoom()).isNotNull();
        assertThat(dto.getRoom().getId()).isEqualTo(1);
        assertThat(dto.getRoom().getName()).isEqualTo("Test Room");
    }

    @Test
    @DisplayName("Should handle Npc with null room")
    void testConstructorWithNpcNullRoom() {
        testNpc.setRoom(null);
        NpcDTO dto = new NpcDTO(testNpc);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getStrength()).isEqualTo(5);
        assertThat(dto.getIsNiallCampbell()).isFalse();
        assertThat(dto.getRoom()).isNull();
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testSetAndGetId() {
        npcDTO.setId(42);

        assertThat(npcDTO.getId()).isEqualTo(42);
    }

    @Test
    @DisplayName("Should set and get strength correctly")
    void testSetAndGetStrength() {
        npcDTO.setStrength(8);

        assertThat(npcDTO.getStrength()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should set and get isNiallCampbell flag correctly")
    void testSetAndGetIsNiallCampbell() {
        npcDTO.setIsNiallCampbell(true);

        assertThat(npcDTO.getIsNiallCampbell()).isTrue();
    }

    @Test
    @DisplayName("Should set and get room correctly")
    void testSetAndGetRoom() {
        Room anotherRoom = new Room();
        anotherRoom.setId(2);
        anotherRoom.setName("Another Room");
        RoomDTO roomDTO = new RoomDTO(anotherRoom);

        npcDTO.setRoom(roomDTO);

        assertThat(npcDTO.getRoom()).isEqualTo(roomDTO);
        assertThat(npcDTO.getRoom().getId()).isEqualTo(2);
        assertThat(npcDTO.getRoom().getName()).isEqualTo("Another Room");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testHandleNullValues() {
        npcDTO.setId(10);
        npcDTO.setStrength(5);
        npcDTO.setIsNiallCampbell(true);
        Room tempRoom = new Room();
        npcDTO.setRoom(new RoomDTO(tempRoom));

        npcDTO.setId(null);
        npcDTO.setStrength(null);
        npcDTO.setIsNiallCampbell(null);
        npcDTO.setRoom(null);

        assertThat(npcDTO.getId()).isNull();
        assertThat(npcDTO.getStrength()).isNull();
        assertThat(npcDTO.getIsNiallCampbell()).isNull();
        assertThat(npcDTO.getRoom()).isNull();
    }

    @Test
    @DisplayName("Should handle Niall Campbell NPC specifically")
    void testNiallCampbellNpc() {
        testNpc.setIsNiallCampbell(true);
        NpcDTO dto = new NpcDTO(testNpc);

        assertThat(dto.getIsNiallCampbell()).isTrue();
    }

    @Test
    @DisplayName("Should handle different strength values")
    void testDifferentStrengthValues() {
        NpcDTO dto1 = new NpcDTO();
        NpcDTO dto2 = new NpcDTO();

        dto1.setStrength(1);
        dto2.setStrength(10);

        assertThat(dto1.getStrength()).isEqualTo(1);
        assertThat(dto2.getStrength()).isEqualTo(10);
        assertThat(dto1.getStrength()).isNotEqualTo(dto2.getStrength());
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void testIndependentInstances() {
        NpcDTO dto1 = new NpcDTO(testNpc);
        NpcDTO dto2 = new NpcDTO(testNpc);

        dto1.setStrength(15);

        assertThat(dto1.getStrength()).isEqualTo(15);
        assertThat(dto2.getStrength()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should allow multiple field changes")
    void testMultipleFieldChanges() {
        npcDTO.setId(5);
        npcDTO.setStrength(7);
        npcDTO.setIsNiallCampbell(true);

        assertThat(npcDTO.getId()).isEqualTo(5);
        assertThat(npcDTO.getStrength()).isEqualTo(7);
        assertThat(npcDTO.getIsNiallCampbell()).isTrue();

        npcDTO.setId(10);
        npcDTO.setStrength(3);
        npcDTO.setIsNiallCampbell(false);

        assertThat(npcDTO.getId()).isEqualTo(10);
        assertThat(npcDTO.getStrength()).isEqualTo(3);
        assertThat(npcDTO.getIsNiallCampbell()).isFalse();
    }

    @Test
    @DisplayName("Should create RoomDTO correctly from Room")
    void testRoomDTOCreation() {
        NpcDTO dto = new NpcDTO(testNpc);
        RoomDTO roomDTO = dto.getRoom();

        assertThat(roomDTO).isNotNull();
        assertThat(roomDTO).isInstanceOf(RoomDTO.class);
        assertThat(roomDTO.getId()).isEqualTo(testRoom.getId());
    }

    @Test
    @DisplayName("Should handle large id values")
    void testLargeIdValues() {
        npcDTO.setId(Integer.MAX_VALUE);

        assertThat(npcDTO.getId()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should handle negative strength values")
    void testNegativeStrengthValues() {
        npcDTO.setStrength(-1);

        assertThat(npcDTO.getStrength()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should properly initialize all fields from Npc entity")
    void testCompleteFieldCopyFromNpc() {
        testNpc.setId(100);
        testNpc.setStrength(9);
        testNpc.setIsNiallCampbell(true);

        NpcDTO dto = new NpcDTO(testNpc);

        assertThat(dto.getId()).isEqualTo(100);
        assertThat(dto.getStrength()).isEqualTo(9);
        assertThat(dto.getIsNiallCampbell()).isTrue();
    }
}
