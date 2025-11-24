package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.PlayerNotInTheGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;
import jakarta.persistence.EntityManager;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.Authorities;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class LobbyService {

    public MatchRepository mrepo;
    public UserService userService;
    public Checkers checkers;
    public PlayerService playerService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LobbyService(MatchRepository mrepo, Checkers checkers, UserService userService, PlayerService playerService) {
        this.mrepo = mrepo;
        this.checkers = checkers;
        this.userService = userService;
        this.playerService = playerService;
    }

    
    public void save(Match m) {
        mrepo.save(m);
    }

    @Transactional(readOnly = true)
    public List<Match> getAllPublicLobbies() {
        return mrepo.findPublicLobbies();
    }

        @Transactional(readOnly = true)
    public List<Match> getAllPrivateLobbies() {
        return mrepo.findPrivateLobbies();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getPrivateLobby(String codeLobby) {
        return mrepo.findPrivateLobbyByCode(codeLobby);
    }

    @Transactional(readOnly = true)
    public Optional<Match> getById(Integer id) {
        return mrepo.findById(id);
    }

    
    //Crear metodo para unirse a una partida publica
    @Transactional
    public Match joinLobby(Integer lobbyId) {
        Match m = mrepo.findById(lobbyId).orElseThrow(() -> new LobbyNotFound("Lobby not found")); 
        checkers.checkNumberOfPlayers(m);
        User currentUser = userService.findCurrentUser(); 
        checkers.checkPlayerAlreadyInALobby(currentUser);
        Player player = new Player(); 
        player.setUser(currentUser);     
        player.setMatch(m);
        m.getPlayers().add(player);
        mrepo.save(m); 
        return m;
}


    @Transactional
    public Match joinPrivateLobby(String code){
        Match m = mrepo.findPrivateLobbyByCode(code).orElseThrow(() -> new LobbyNotFound("Lobby not found"));
        User currentUser = userService.findCurrentUser(); 
        checkers.checkNumberOfPlayers(m);
        checkers.checkPlayerAlreadyInALobby(currentUser);
        Player player = new Player(); 
        player.setUser(currentUser);
        player.setMatch(m);
        m.getPlayers().add(player);
        mrepo.save(m); 
        return m;

    }
        
    
    //Funcion para crear un lobby
    @Transactional
    public  Match createLobby(Match game, Boolean isPrivate, String name, Integer maxPlayers, Integer numNpcs) {
        User currentUser = userService.findCurrentUser(); 
        Player player = new Player(); 
        player.setUser(currentUser);
        player.setMatch(game);
        checkers.checkPlayerAlreadyInALobby(currentUser);
        game.setStatus(MatchStatus.WAITING);
        game.setPlayers(new ArrayList<>(List.of(player)));
        game.setName(name);
        game.setMaxPlayers(maxPlayers);
        game.setNumNpcs(numNpcs);
        game.setCreatorId(currentUser.getId());
        game.setIsPrivate(isPrivate);
        if(game.getIsPrivate()){
            String code=game.generateCodeLobby();
            game.setCode(code);
        }
        mrepo.save(game);
        return game;
    }




    @Transactional
    public Match leaveLobby(Integer matchId) {

        Match m = mrepo.findById(matchId).orElseThrow(() -> new LobbyNotFound("Lobby no encontrado"));
        User currentUser = userService.findCurrentUser();
        Player player = playerService.findByMatchIdAndUserId(m.getId(), currentUser.getId())
                .orElseThrow(() -> new PlayerNotInTheGame("El jugador no está en este lobby"));
        m.getPlayers().remove(player);
        return mrepo.save(m);

    } 



    
}