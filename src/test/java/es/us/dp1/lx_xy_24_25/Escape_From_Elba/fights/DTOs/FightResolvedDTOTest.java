package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.FightResultType;


@DisplayName("FightResolvedDTO Tests")
class FightResolvedDTOTest {

    @Test
    @DisplayName("Debería crear el DTO correctamente para el escenario PLAYER_BEATS_NPC")
    void testPlayerBeatsNpcScenario() {
        Card card = new Card();
        card.setId(50);

        FightResolvedDTO playerBeatsNpc = new FightResolvedDTO(1, 10, 11, 12, 101, card, FightResultType.PLAYER_BEATS_NPC);
        
        assertEquals("RESOLVE", playerBeatsNpc.getAction());
        assertEquals(1, playerBeatsNpc.getMatchId());
        assertEquals(10, playerBeatsNpc.getWinnerUserId());
        assertEquals(11, playerBeatsNpc.getWinnerId());
        assertEquals(12, playerBeatsNpc.getLoserId());
        assertEquals(101, playerBeatsNpc.getChainRoomId());
        assertNotNull(playerBeatsNpc.getCard());
        assertEquals(50, playerBeatsNpc.getCard().getId());
        assertEquals(FightResultType.PLAYER_BEATS_NPC, playerBeatsNpc.getFightResultType());
    }

    @Test
    @DisplayName("Debería crear el DTO correctamente para el escenario NPC_BEATS_PLAYER")
    void testNpcBeatsPlayerScenario() {
        FightResolvedDTO npcBeatsPlayer = new FightResolvedDTO(1, 15, 16, 17, null, FightResultType.NPC_BEATS_PLAYER);
        
        assertEquals(1, npcBeatsPlayer.getMatchId());
        assertEquals(15, npcBeatsPlayer.getWinnerId());
        assertEquals(16, npcBeatsPlayer.getLoserId());
        assertEquals(17, npcBeatsPlayer.getLoserUserId());
        assertNull(npcBeatsPlayer.getChainRoomId());
        assertEquals(FightResultType.NPC_BEATS_PLAYER, npcBeatsPlayer.getFightResultType());
    }

    @Test
    @DisplayName("Debería crear el DTO correctamente para el escenario PLAYER_BEATS_PLAYER")
    void testPlayerBeatsPlayerScenario() {
        FightResolvedDTO playerBeatsPlayer = new FightResolvedDTO(1, 20, 21, 22, 23, 102, FightResultType.PLAYER_BEATS_PLAYER);
        
        assertEquals(1, playerBeatsPlayer.getMatchId());
        assertEquals(20, playerBeatsPlayer.getWinnerUserId());
        assertEquals(21, playerBeatsPlayer.getWinnerId());
        assertEquals(22, playerBeatsPlayer.getLoserId());
        assertEquals(23, playerBeatsPlayer.getLoserUserId());
        assertEquals(102, playerBeatsPlayer.getChainRoomId());
        assertEquals(FightResultType.PLAYER_BEATS_PLAYER, playerBeatsPlayer.getFightResultType());

        playerBeatsPlayer.setAction("CUSTOM_RESOLVE");
        assertEquals("CUSTOM_RESOLVE", playerBeatsPlayer.getAction());
    }
}