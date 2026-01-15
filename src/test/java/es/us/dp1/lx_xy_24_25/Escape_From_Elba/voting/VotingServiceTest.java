package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyVotedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreVotesThanPlayersException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchWebsocketController;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

@ExtendWith(MockitoExtension.class)
public class VotingServiceTest {

    private VotingService votingService;

    @Mock
    private VotingRepository votingRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchWebsocketController matchWebsocketController;

    @BeforeEach
    public void setup() {
        //Mockito.reset(matchRepository, votingRepository, matchWebsocketController);
        votingService = new VotingService(
                votingRepository,
                matchRepository,
                matchWebsocketController
        );
    }

    /* 
     * ---------------------------- getVotingsByMatchId ----------------------------
     */

    // devuelve excepcion cuando no hay votaciones asociadas a la partida
    @Test
    public void getVotingsByMatchIdNoVotingsThrows() {
        when(votingRepository.findByMatchId(1)).thenReturn(List.of());
       
        assertThrows(ResourceNotFoundException.class,
                () -> votingService.getVotingsByMatchId(1));
    }

    // caso positivo: 
    @Test
    public void getVotingsByMatchIdReturnsList() {
        Voting voting = new Voting();
        voting.setMatchId(1);
        voting.setVotes(List.of());

        when(votingRepository.findByMatchId(1))
                .thenReturn(List.of(voting));

        List<VotingDTO> result = votingService.getVotingsByMatchId(1);

        assertEquals(1, result.size());
    }


    /* 
     * ---------------------------- startVoting ----------------------------
     */

    // caso negativo: no existe la partida
    @Test
    public void startVotingMatchNotFoundThrows() {
        doReturn(Optional.empty()).when(matchRepository).findById(anyInt());

        assertThrows(ResourceNotFoundException.class,
                () -> votingService.startVoting(1, "Sword", 10));
    }

    // caso positivo: 
    @Test
    public void startVotingSuccess() {
        Match match = new Match();
        match.setId(1);
        match.setStatus(MatchStatus.PLAYING);

        Player p1 = new Player();
        p1.setId(9);
        p1.setMatch(match);

        Player p2 = new Player();
        p2.setId(8);
        p2.setMatch(match);

        match.setPlayers(List.of(p1, p2));

        when(matchRepository.findById(anyInt())).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(votingRepository.save(any(Voting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VotingDTO result =
                votingService.startVoting(1, "Sword", 9);

        assertEquals("Sword", result.getWeaponProposed());
        assertEquals(VotingStatus.PENDING, result.getStatus());
        assertEquals(MatchStatus.VOTING, match.getStatus());
    }


    /* 
     * ---------------------------- submitVote ----------------------------
     */

    // caso negativo: no hay votación pendiente en la partida
    @Test
    public void submitVoteNoPendingVotingThrows() {
        when(votingRepository.findPendingVotingByMatchId(1))
                .thenReturn(Optional.empty());

        VoteDTO vote = new VoteDTO();
        vote.setPlayerId(10);
        vote.setInFavor(VoteValue.YES);

        assertThrows(ResourceNotFoundException.class,
                () -> votingService.submitVote(1, vote));
    }

    // caso negativo: el jugador que se pasa no está en la votación (ni en la partida claro)
    @Test
    public void submitVotePlayerNotInVotingThrows() {
        Voting voting = new Voting();
        voting.setVotes(new ArrayList<>());
        voting.setStatus(VotingStatus.PENDING);

        when(votingRepository.findPendingVotingByMatchId(1))
                .thenReturn(Optional.of(voting));

        VoteDTO vote = new VoteDTO();
        vote.setPlayerId(99);
        vote.setInFavor(VoteValue.YES);

        assertThrows(ResourceNotFoundException.class,
                () -> votingService.submitVote(1, vote));
    }

    // caso negativo: el jugador ya ha votado
    @Test
    public void submitVoteAlreadyVotedThrows() {
        Vote existingVote = new Vote();
        existingVote.setPlayerId(10);
        existingVote.setInFavor(VoteValue.YES);

        Voting voting = new Voting();
        voting.setVotes(List.of(existingVote));
        voting.setStatus(VotingStatus.PENDING);

        when(votingRepository.findPendingVotingByMatchId(1))
                .thenReturn(Optional.of(voting));

        VoteDTO vote = new VoteDTO();
        vote.setPlayerId(10);
        vote.setInFavor(VoteValue.YES);

        assertThrows(AlreadyVotedException.class,
                () -> votingService.submitVote(1, vote));
    }

    // caso negativo: se reciben más votos que jugadores en la votación
    @Test
    public void submitVoteMoreVotesThanPlayersThrows() {
        Vote voteInBd = new Vote();
        voteInBd.setPlayerId(10);

        Voting voting = new Voting();
        voting.setVotes(List.of(voteInBd));
        voting.setVotesInFavor(1);
        voting.setVotesAgainst(1);
        voting.setNumPlayers(1);
        voting.setStatus(VotingStatus.PENDING);

        when(votingRepository.findPendingVotingByMatchId(1))
                .thenReturn(Optional.of(voting));

        VoteDTO vote = new VoteDTO();
        vote.setPlayerId(10);
        vote.setInFavor(VoteValue.YES);

        assertThrows(MoreVotesThanPlayersException.class,
                () -> votingService.submitVote(1, vote));
    }

    // caso positivo: todo hay ido bien y la votación se pone como FINISHED y match vuelve al estado de PLAYING
    @Test
    public void submitVoteFinishesVoting() {
        Vote voteInBd = new Vote();
        voteInBd.setPlayerId(10);

        Voting voting = new Voting();
        voting.setVotes(new ArrayList<>(List.of(voteInBd)));
        voting.setVotesInFavor(0);
        voting.setVotesAgainst(0);
        voting.setNumPlayers(1);
        voting.setStatus(VotingStatus.PENDING);

        Match match = new Match();
        match.setId(1);
        match.setStatus(MatchStatus.VOTING);

        when(votingRepository.findPendingVotingByMatchId(anyInt()))
                .thenReturn(Optional.of(voting));
        when(matchRepository.findById(1))
                .thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(votingRepository.save(any(Voting.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VoteDTO vote = new VoteDTO();
        vote.setPlayerId(10);
        vote.setInFavor(VoteValue.YES);

        VotingDTO result = votingService.submitVote(1, vote);

        assertEquals(VotingStatus.FINISHED, result.getStatus());
        assertEquals(VotingResult.ACCEPTED, result.getResult());
        assertEquals(MatchStatus.PLAYING, match.getStatus());

        verify(matchRepository).save(any(Match.class));
    }
    
}