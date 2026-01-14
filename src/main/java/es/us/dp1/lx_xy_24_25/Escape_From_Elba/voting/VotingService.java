package es.us.dp1.lx_xy_24_25.Escape_From_Elba.voting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.AlreadyVotedException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.MoreVotesThanPlayersException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;

@Service
public class VotingService {

    private static final Integer BONUS_WEAPONS = 1; // la bonificación que te llevas al formar un arma válida 

    VotingRepository votingRepository; 
    MatchRepository matchRepository;
    
    @Autowired
    public VotingService(VotingRepository votingRepository, MatchRepository matchRepository){
        this.votingRepository=votingRepository;
        this.matchRepository=matchRepository;
    }

    public Voting getVotingById(Integer id){
        Voting voting = votingRepository.findById(id).orElse(null); 
        return voting;
    }

    // ESTO ERA PARA HACER COMPROBACIONES probablemente luego no lo use
    public List<VotingDTO> getVotingsByMatchId(Integer matchId){
        List<Voting> votings = votingRepository.findByMatchId(matchId); 
        if (votings.isEmpty()){
            throw new ResourceNotFoundException("The is no voting in the match with id: " + matchId);
        }
        return votings.stream().map(voting -> new VotingDTO(voting)).toList();
    }

    @Transactional(rollbackFor = ResourceNotFoundException.class)
    public VotingDTO startVoting(Integer matchId, String weaponProposed, Integer proposingPlayerId){
        // buscamos la partida y checkeamos además que exista
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId));
        
        // buscamos la lista de jugadores excluyendo a quien propone el arma 
        List<Player> playersInVoting = match.getPlayers().stream()
            .filter(player -> !player.getId().equals(proposingPlayerId)).toList();
        
            // creamos la votación
        Voting voting = new Voting(matchId, weaponProposed, playersInVoting); 
        
        // cambiamos el estado de la partida a voting y el numero de jugadores que van a participar en la votación
        match.setStatus(MatchStatus.VOTING);
        
        voting.setStatus(VotingStatus.PENDING);
        voting.setVotesInFavor(0);
        voting.setVotesAgainst(0);
        
        // guardamos ambos
        matchRepository.save(match);
        return new VotingDTO(votingRepository.save(voting));
    }


    @Transactional(rollbackFor = {ResourceNotFoundException.class, MoreVotesThanPlayersException.class, AlreadyVotedException.class})
    public VotingDTO submitVote(Integer matchId, VoteDTO vote){
        // buscamos la votación de la partida pasada y que esté en estado pendiente (solo debe haber una)
        Voting voting = votingRepository.findPendingVotingByMatchId(matchId).orElseThrow(() -> new ResourceNotFoundException("The is no pending voting in match with id: " + matchId));
        
        // buscamos el voto del jugador que ha votado
        Vote voteInBd = voting.getVotes().stream()
            .filter(v -> v.getPlayerId().equals(vote.getPlayerId()))
            .findFirst().orElseThrow(() -> new ResourceNotFoundException("Player is not part of this voting"));

        // checkeamos que el jugador no haya votado ya
        if (voteInBd.getInFavor() != null){
            throw new AlreadyVotedException("You have already voted in this voting.");
        }

        // si no ha votado ya, guardamos el voto y lo añadimos según si está a favor o en contra
        voteInBd.setInFavor(vote.getInFavor());
        
        if (vote.getInFavor().equals(VoteValue.YES)){
            voting.setVotesInFavor(voting.getVotesInFavor() + 1);
        } if (vote.getInFavor().equals(VoteValue.NO)){
            voting.setVotesAgainst(voting.getVotesAgainst() + 1);
        }

        // calculamos cuantos votos llevamos 
        Integer currentTotalVotes = voting.getVotesInFavor() + voting.getVotesAgainst();

        // checkeamos por si acaso que puedan haber más votos que jugadores participantes en la votación 
        if (currentTotalVotes > voting.getNumPlayers()){
            throw new MoreVotesThanPlayersException("There are more votes than players in the match.");
        }

        // comprobamos si ya han votado todos los jugadores
        if (currentTotalVotes.equals(voting.getNumPlayers())){
            // si han votado todos, calculamos el resultado
            return new VotingDTO(finishedVoting(voting, matchId));
        }
        
        // guardamos la votación
        votingRepository.save(voting);
        
        return new VotingDTO(voting);
    }


    public Voting finishedVoting(Voting voting, Integer matchId){
        if (voting.getVotesInFavor() > voting.getVotesAgainst()){ // hay más votos a favor, se lleva la bonificación
            voting.setResult(VotingResult.ACCEPTED);
            voting.setFinalBonus(BONUS_WEAPONS);
        } 
        if (voting.getVotesInFavor() <= voting.getVotesAgainst()){ // en caso de empate, no se lleva nada 
            voting.setResult(VotingResult.REJECTED); // (esto no viene especificado en las reglas así que se decidido por consenso en el grupo)
            voting.setFinalBonus(0);
        }
        voting.setStatus(VotingStatus.FINISHED);

        // si la votación ha terminado, cambiamos el estado de la partida a playing y guardamos 
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId));
        match.setStatus(MatchStatus.PLAYING);
        matchRepository.save(match);
        
        return votingRepository.save(voting);
    }

}
