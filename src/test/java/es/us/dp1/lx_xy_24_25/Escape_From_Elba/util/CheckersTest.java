package es.us.dp1.lx_xy_24_25.Escape_From_Elba.util;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGameDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.*;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest.FriendRequestService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckersTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private FriendRequestService friendRequestService;

    @InjectMocks
    private Checkers checkers;

    private Match match;
    private User user;
    private Player player;

    @BeforeEach
    void setUp() {
        match = new Match();
        match.setId(1);
        match.setStatus(MatchStatus.WAITING); 
        match.setPlayers(new ArrayList<>());
        match.setMaxPlayers(4);
        match.setIsPrivate(false);

        user = new User();
        user.setId(10);

        player = new Player();
        player.setId(20);
        player.setUser(user);
        player.setCardsDrawnInTurn(0);
        player.setActionPoints(2);
    }


    @Test
    void checkGameStatus_IncorrectStatus_ThrowsGameIsNotALobbyException() {
        assertThrows(GameIsNotALobbyException.class, () -> checkers.checkGameStatus(match, MatchStatus.PLAYING.toString()));
    }

    @Test
    void checkGameIsNotPlaying_NotPlaying_DoesNotThrow() {
        match.setStatus(MatchStatus.WAITING); 
        assertDoesNotThrow(() -> checkers.checkGameIsNotPlaying(match));
    }

    @Test
    void checkGameIsNotPlaying_IsPlaying_ThrowsAlreadyPlayingException() {
        Match playingMatch = mock(Match.class);
        when(playingMatch.getStatus()).thenReturn(MatchStatus.PLAYING);
        assertThrows(AlreadyPlayingException.class, () -> checkers.checkGameIsNotPlaying(playingMatch));
    }

    @Test
    void checkNumberOfPlayers_LobbyHasSpace_DoesNotThrow() {
        match.setPlayers(List.of(player));
        match.setMaxPlayers(4);
        assertDoesNotThrow(() -> checkers.checkNumberOfPlayers(match));
    }

    @Test
    void checkNumberOfPlayers_LobbyIsFull_ThrowsLobbyIsFullException() {
        match.setPlayers(Arrays.asList(player, player, player, player));
        match.setMaxPlayers(4);
        assertThrows(LobbyIsFullException.class, () -> checkers.checkNumberOfPlayers(match));
    }

    @Test
    void checkPlayerAlreadyInALobby_NotInLobby_DoesNotThrow() {
        when(lobbyRepository.findLobbyWhereUserIsIn(user.getId())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> checkers.checkPlayerAlreadyInALobby(user));
    }

    @Test
    void checkPlayerIsInTheGame_InGame_DoesNotThrow() {
        when(playerRepository.findByMatchAndUser(match.getId(), user.getId())).thenReturn(Optional.of(player));
        assertDoesNotThrow(() -> checkers.checkPlayerIsInTheGame(match, user));
    }

    @Test
    void checkPlayerIsInTheGame_NotInGame_ThrowsPlayerNotInTheGame() {
        when(playerRepository.findByMatchAndUser(match.getId(), user.getId())).thenReturn(Optional.empty());
        assertThrows(PlayerNotInTheGame.class, () -> checkers.checkPlayerIsInTheGame(match, user));
    }

    @Test
    void checkCanSpectateGame_PublicGame_DoesNotThrow() {
        match.setIsPrivate(false);
        assertDoesNotThrow(() -> checkers.checkCanSpectateGame(match, user.getId()));
    }

    @Test
    void checkCanSpectateGame_PrivateGameAndFriendOfAll_DoesNotThrow() {
        match.setIsPrivate(true);
        match.setPlayers(List.of(player)); 

        when(friendRequestService.findFriendsByUserId(99)).thenReturn(List.of(user));

        assertDoesNotThrow(() -> checkers.checkCanSpectateGame(match, 99));
    }

    @Test
    void checkCanSpectateGame_PrivateGameAndNotFriendOfAll_ThrowsGameIsNotPublicException() {
        match.setIsPrivate(true);
        match.setPlayers(List.of(player));

        when(friendRequestService.findFriendsByUserId(99)).thenReturn(new ArrayList<>()); 

        assertThrows(GameIsNotPublicException.class, () -> checkers.checkCanSpectateGame(match, 99));
    }

    @Test
    void checkCardExists_NullCardOrNullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> checkers.checkCardExists(null));
        
        Card cardWithNullId = new Card();
        assertThrows(IllegalArgumentException.class, () -> checkers.checkCardExists(cardWithNullId));
    }

    @Test
    void checkCardExists_CardNotFoundInRepo_ThrowsResourceNotFoundException() {
        Card card = new Card();
        card.setId(100);
        when(cardRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> checkers.checkCardExists(card));
    }

    @Test
    void checkCardExists_CardFound_DoesNotThrow() {
        Card card = new Card();
        card.setId(100);
        when(cardRepository.findById(100)).thenReturn(Optional.of(card));

        assertDoesNotThrow(() -> checkers.checkCardExists(card));
    }


    @Test
    void checkWordIsValid_True_DoesNotThrow() {
        assertDoesNotThrow(() -> checkers.checkWordIsValid(true));
    }

    @Test
    void checkWordIsValid_False_ThrowsBagNotValidException() {
        assertThrows(BagNotValidException.class, () -> checkers.checkWordIsValid(false));
    }

    @Test
    void checkCardsDrawnInTurn_ValidDrawn_DoesNotThrow() {
        player.setCardsDrawnInTurn(5);
        assertDoesNotThrow(() -> checkers.checkCardsDrawnInTurn(player));
        assertDoesNotThrow(() -> checkers.checkCardsDrawnInTurn(null)); 
    }

    @Test
    void checkCardsDrawnInTurn_LimitExceeded_ThrowsMoreThan7CardsDrawnException() {
        player.setCardsDrawnInTurn(7);
        assertThrows(MoreThan7CardsDrawnException.class, () -> checkers.checkCardsDrawnInTurn(player));
    }

    @Test
    void chechPlayerExists_NullResult_ThrowsResourceNotFoundException() {
        when(playerRepository.findById(1)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> checkers.chechPlayerExists(1));
    }

    @Test
    void chechPlayerExists_Exists_DoesNotThrow() {
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        assertDoesNotThrow(() -> checkers.chechPlayerExists(1));
    }


    @Test
    void checkRoomIsAdyacent_IsAdjacent_DoesNotThrow() {
        Room current = new Room();
        Room target = new Room();
        target.setId(2);
        
        current.setAdjacencyList(List.of(target));

        assertDoesNotThrow(() -> checkers.checkRoomIsAdyacent(current, target));
    }

    @Test
    void checkRoomIsAdyacent_NotAdjacent_ThrowsInvalidMovementException() {
        Room current = new Room();
        Room target = new Room();
        target.setId(2);
        
        Room other = new Room();
        other.setId(3);
        current.setAdjacencyList(List.of(other));

        assertThrows(InvalidMovementException.class, () -> checkers.checkRoomIsAdyacent(current, target));
    }

    @Test
    void checkPlayerHasActionPoints_HasPoints_DoesNotThrow() {
        player.setActionPoints(1);
        assertDoesNotThrow(() -> checkers.checkPlayerHasActionPoints(player));
    }

    @Test
    void checkPlayerHasActionPoints_NoPoints_ThrowsNoActionPointsException() {
        player.setActionPoints(0);
        assertThrows(NoActionPointsException.class, () -> checkers.checkPlayerHasActionPoints(player));
    }
}