package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;

import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.MatchRepository;
@Service
public class LobbyService {

    MatchRepository mrepo;

    @Autowired
    public LobbyService(MatchRepository mrepo) {
        this.mrepo = mrepo;
    }

    
    @Transactional(readOnly = true)
    public List<Match> getAllPublicLobbies() {
        return mrepo.findPublicLobbies();
    }

    @Transactional(readOnly = true)
    public Optional<Match> getPrivateLobby(String codeLobby) {
        return mrepo.findPrivateLobbieById(codeLobby);
    }
    
}
