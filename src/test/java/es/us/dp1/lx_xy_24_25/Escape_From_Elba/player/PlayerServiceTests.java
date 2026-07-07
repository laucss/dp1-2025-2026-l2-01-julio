package es.us.dp1.lx_xy_24_25.Escape_From_Elba.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTests {

    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private HandService handService;

    @BeforeEach
    public void setup() {
        playerService = new PlayerService(playerRepository, handService);
    }

    
    @Test
    public void findAllReturnsPlayers() {
        when(playerRepository.findAll()).thenReturn(List.of(new Player()));
        List<Player> players = playerService.findAll();
        assertEquals(1, players.size());
    }



    @Test
    public void findByIdExistingReturnsPlayer() {
        Player player = new Player();
        player.setId(1);

        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        Player result = playerService.findById(1);
        assertEquals(1, result.getId());
    }

    @Test
    public void findByIdNonExistingThrowsException() {
        when(playerRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> playerService.findById(99));
    }

    

    @Test
    public void findByUserIdReturnsPlayers() {
        when(playerRepository.findByUserId(1))
            .thenReturn(List.of(new Player()));

        List<Player> players = playerService.findByUserId(1);
        assertEquals(1, players.size());
    }

    

    /* 
    @Test
    public void findByMatchIdAndUserIdReturnsOptional() {
        Player player = new Player();
        when(playerRepository.findByMatchAndUser(1, 1))
            .thenReturn(Optional.of(player));

        Optional<Player> result =
            playerService.findByMatchIdAndUserId(1, 1);

        assertTrue(result.isPresent());
    }
        */

 

    @Test
    public void savePlayerReturnsSavedPlayer() {
        Player player = new Player();
        when(playerRepository.save(player)).thenReturn(player);

        Player saved = playerService.save(player);
        assertNotNull(saved);
    }

    

    @Test
    public void deleteByIdCallsRepository() {
        playerService.deleteById(1);
        verify(playerRepository).deleteById(1);
    }

   

    @Test
    public void getPlayersByMatchIdReturnsPlayers() {
        when(playerRepository.findByMatchId(1))
            .thenReturn(List.of(new Player()));

        List<Player> players = playerService.getPlayersByMatchId(1);
        assertEquals(1, players.size());
    }

   
    /*
    @Test
    public void getPlayerActionPointsLessThan7Cards() {
        Player player = new Player();
        player.setId(1);

        HandInGame hand = new HandInGame();
        hand.setCards(List.of(new Object(), new Object(), new Object()));

        when(playerRepository.findById(1))
            .thenReturn(Optional.of(player));
        when(handService.findPlayerHand(1, 1))
            .thenReturn(hand);
        when(playerRepository.save(any(Player.class)))
            .thenReturn(player);

        Integer points = playerService.getPlayerActionPoints(1, 1);

        assertEquals(4, points); // 7 - 3 cartas
    }

    @Test
    public void getPlayerActionPointsMoreThan7Cards() {
        Player player = new Player();
        player.setId(1);

        HandInGame hand = new HandInGame();
        hand.setCards(List.of(
            new Object(), new Object(), new Object(),
            new Object(), new Object(), new Object(),
            new Object(), new Object()
        ));

        when(playerRepository.findById(1))
            .thenReturn(Optional.of(player));
        when(handService.findPlayerHand(1, 1))
            .thenReturn(hand);

        Integer points = playerService.getPlayerActionPoints(1, 1);
        assertEquals(0, points);
    }

 */

    @Test
    public void removePlayerActionPointSuccess() {
        Match match = new Match();
        match.setId(1);

        Player player = new Player();
        player.setId(1);
        player.setMatch(match);
        player.setActionPoints(2);

        when(playerRepository.findById(1))
            .thenReturn(Optional.of(player));

        playerService.removePlayerActionPoint(1, 1);

        assertEquals(1, player.getActionPoints());
        verify(playerRepository).save(player);
    }

    @Test
    public void removePlayerActionPointNoPlayerDoesNothing() {
        when(playerRepository.findById(1))
            .thenReturn(Optional.empty());

        playerService.removePlayerActionPoint(1, 1);

        verify(playerRepository, never()).save(any());
    }
}

