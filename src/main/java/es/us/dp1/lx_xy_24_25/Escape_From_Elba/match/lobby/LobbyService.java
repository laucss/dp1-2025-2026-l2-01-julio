package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
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


    //Crear metodo para unirse a una partida publica
    @Transactional
    public Match joinLobby(Integer lobbyId) {
        Player currentPlayer = userService.findCurrentUser().toPlayer();        
        Match m = mrepo.findById(lobbyId).orElseThrow(() -> new LobbyNotFound("Lobby not found"));
        checkers.checkNumberOfPlayers(m);
        checkers.checkPlayerAlreadyInALobby(currentPlayer);
        m.getPlayers().add(currentPlayer);
        mrepo.save(m); 
        return m;
}


    @Transactional
    public Match joinPrivateLobby(String code){

        Player currentPlayer = userService.findCurrentUser().toPlayer();

        Match m = mrepo.findPrivateLobbyByCode(code).orElseThrow(() -> new LobbyNotFound("Lobby not found"));
        checkers.checkNumberOfPlayers(m);
        checkers.checkPlayerAlreadyInALobby(currentPlayer);
        m.getPlayers().add(currentPlayer);
        mrepo.save(m); 
        return m;

    }
        
    
    //Funcion para crear un lobby
    @Transactional
    public  Match createLobby(Match game, Boolean isPrivate, String name, Integer maxPlayers) {
        User currentUser = userService.findCurrentUser();
        Player currentPlayer = playerService.findById(currentUser.getId())
                .orElseGet(() -> {
                    Player newPlayer = currentUser.toPlayer(); 
                    if (newPlayer == null) {
                        throw new IllegalStateException("El usuario no tiene un Player asociado");
                    }
                    return playerService.save(newPlayer);
                });
        checkers.checkPlayerAlreadyInALobby(currentPlayer);
        game.setStatus(MatchStatus.WAITING);
        currentPlayer = entityManager.merge(currentPlayer); //Esto me lo dijo chatgpt
        game.setPlayers(new ArrayList<>(List.of(currentPlayer)));
        game.setName(name);
        game.setMaxPlayers(maxPlayers);
        game.setCreatorId(currentPlayer.getId());
        game.setIsPrivate(isPrivate);
        if(game.getIsPrivate()){
            String code=game.generateCodeLobby();
            game.setCode(code);
        }
        mrepo.save(game);
        return game;
    }




    /*@Transactional
    public Match leaveLobby(Integer lobbyId) {
        User currentUser = userService.findCurrentUser();
        Player player = playerService.findByUser(currentUser)
                        .orElseThrow(() -> new IllegalStateException("El usuario no tiene un Player asociado"));

        Match m = mrepo.findById(lobbyId)
                        .orElseThrow(() -> new LobbyNotFound("Lobby no encontrado"));

        // Crear checker
        if (!m.getPlayers().contains(player)) {
            throw new IllegalStateException("El jugador no está en este lobby");
        }


        m.getPlayers().remove(player);
        player.setMatch(null);


        return mrepo.save(m);

    } */



    
}