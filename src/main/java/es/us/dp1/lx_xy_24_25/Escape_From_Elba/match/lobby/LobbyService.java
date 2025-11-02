package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
@Service
public class LobbyService {

    public MatchRepository mrepo;
    public UserService userService;
    public Checkers checkers;

    @Autowired
    public LobbyService(MatchRepository mrepo, Checkers checkers, UserService userService) {
        this.mrepo = mrepo;
        this.checkers = checkers;
        this.userService = userService;
    }

    
    public void save(Match m) {
        mrepo.save(m);
    }

    @Transactional(readOnly = true)
    public List<Match> getAllPublicLobbies() {
        return mrepo.findPublicLobbies();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getPrivateLobby(String codeLobby) {
        return mrepo.findPrivateLobbieById(codeLobby);
    }

    //Crear metodo para unirse a una partida publica
    @Transactional
    public void joinLobby(Integer lobbyId) {
        //Comprueba que existe un lobby con ese id
        Match m = mrepo.findById(lobbyId).orElseThrow(() -> new LobbyNotFound("Lobby not found"));
        //Comprueba que el lobby esta en estado WAITING
        checkers.checkGameStatus(m, "WAITING");
        //Comprueba que el lobby no esta lleno
        checkers.checkNumberOfPlayers(m);
        //Comprueba que el jugador no esta ya en otro lobby
        
        checkers.checkPlayerAlreadyInALobby(userService.findCurrentUser().toPlayer());
        m.getPlayers().add(userService.findCurrentUser().toPlayer());
        save(m);
         
        
        
    }
    
}