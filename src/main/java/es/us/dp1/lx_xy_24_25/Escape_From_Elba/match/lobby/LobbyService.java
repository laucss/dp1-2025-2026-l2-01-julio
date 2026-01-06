package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.LobbyNotFound;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.PlayerNotInTheGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.util.Checkers;
import jakarta.persistence.EntityManager;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchStatus;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.PlayerService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.UserService;
import jakarta.persistence.PersistenceContext;


@Service
public class LobbyService {

    public MatchRepository mrepo;
    public UserService userService;
    public Checkers checkers;
    public PlayerService playerService;
    public LobbyWebsocketController lobbyWebsocketController;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LobbyService(MatchRepository mrepo, Checkers checkers, UserService userService, PlayerService playerService, LobbyWebsocketController lobbyWebsocketController) {
        this.mrepo = mrepo;
        this.checkers = checkers;
        this.userService = userService;
        this.playerService = playerService;
        this.lobbyWebsocketController = lobbyWebsocketController;
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
        Match savedMatch = mrepo.save(m);
        
        // Notificar a todos en el lobby que alguien se unió
        LobbyUpdateDTO update = createLobbyUpdate(savedMatch, "JOIN", currentUser.getUsername());
        lobbyWebsocketController.notifyPlayerJoined(lobbyId, update);
        
        return savedMatch;
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
        Match savedMatch = mrepo.save(m);
        
        // Notificar a todos en el lobby que alguien se unió
        LobbyUpdateDTO update = createLobbyUpdate(savedMatch, "JOIN", currentUser.getUsername());
        lobbyWebsocketController.notifyPlayerJoined(m.getId(), update);
        
        return savedMatch;

    }
        
    
    //Funcion para crear un lobby
    @Transactional
    public  Match createLobby(Boolean isPrivate, String name, Integer maxPlayers, Integer numNpcs) {
        User currentUser = userService.findCurrentUser(); 
            if (currentUser == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario no autenticado");
            }
        Match game = new Match();
        Player player = new Player(); 
        player.setUser(currentUser);
        checkers.checkPlayerAlreadyInALobby(currentUser);
        game.setStatus(MatchStatus.WAITING);
        game.setPlayers(new ArrayList<>());
        game.getPlayers().add(player);
        player.setMatch(game);
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
        
       
        if (m.getCreatorId().equals(currentUser.getId())) {
            
            LobbyUpdateDTO update = createLobbyUpdate(m, "DELETED", currentUser.getUsername());
            lobbyWebsocketController.notifyPlayerLeft(matchId, update);
            
            
            mrepo.delete(m);
            return null;
        }
        
        
        m.getPlayers().remove(player);
        Match savedMatch = mrepo.save(m);
        
        // Notificar a todos en el lobby que alguien se fue
        LobbyUpdateDTO update = createLobbyUpdate(savedMatch, "LEAVE", currentUser.getUsername());
        lobbyWebsocketController.notifyPlayerLeft(matchId, update);
        
        return savedMatch;

    }
    
    private LobbyUpdateDTO createLobbyUpdate(Match match, String action, String username) {
        List<LobbyUpdateDTO.PlayerLobbyDTO> players = new ArrayList<>();
        for (Player p : match.getPlayers()) {
            players.add(new LobbyUpdateDTO.PlayerLobbyDTO(
                p.getUser().getId(),
                p.getUser().getUsername(),
                p.getUser().getAvatar()
            ));
        }
        return new LobbyUpdateDTO(match.getId(), players, action, username);
    }


    
}