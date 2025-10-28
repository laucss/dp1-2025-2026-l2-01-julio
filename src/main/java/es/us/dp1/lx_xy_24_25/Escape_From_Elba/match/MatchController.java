package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby.LobbyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/matches")
@Tag(name = "Matches", description = "API for the management of Matches")
@SecurityRequirement(name = "bearerAuth")
public class MatchController {
    MatchService ms;
    LobbyService ls;
    @Autowired
    public MatchController(MatchService ms, LobbyService ls){
        this.ms=ms;
        this.ls=ls;
    }

    @GetMapping
    public List<Match> getAllGames(@ParameterObject() @RequestParam(value="name",required = false) String name, @ParameterObject @RequestParam(value="status",required = false) MatchStatus status){
        return ms.getAllMatchs();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable("id")Integer id){
        Optional<Match> m=ms.getMatchById(id);
        if(!m.isPresent())
            throw new ResourceNotFoundException("Match", "id", id);
        return m.get();
    }

    @GetMapping("/lobbies")
    public List<Match> getPublicGames(){
        return ls.getAllPublicLobbies();
    }

    @GetMapping("/lobbies/{matchId}")
    public Optional<Match> getPrivateGame(@ParameterObject String code){
        return ls.getPrivateLobby(code);
    }

    @PostMapping("/lobbies/{matchId}/join")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Match> joinLobby(@Parameter(description = "Id of the lobby to join") @PathVariable Integer gameId) {
        ls.joinLobby(gameId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Match> createGame(@Valid @RequestBody Match m){
        m=ms.save(m);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(m.getId())
                .toUri();
        return ResponseEntity.created(location).body(m);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Void> updateGame(@Valid @RequestBody Match m,@PathVariable("id")Integer id){
        Match mToUpdate=getMatchById(id); // error???
        BeanUtils.copyProperties(m,mToUpdate, "id");
        ms.save(mToUpdate);
        return ResponseEntity.noContent().build(); // error???
    } // error???

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable("id")Integer id){
        if(getMatchById(id)!=null)
            ms.delete(id);
        return ResponseEntity.noContent().build();
    }
}

