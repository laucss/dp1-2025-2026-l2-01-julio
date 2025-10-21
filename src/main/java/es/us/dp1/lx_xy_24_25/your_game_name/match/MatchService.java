package es.us.dp1.lx_xy_24_25.your_game_name.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public class MatchService {

    MatchRepository mrepo;

    @Autowired
    public MatchService(MatchRepository mrepo) {
        this.mrepo = mrepo;
    }

    @Transactional(readOnly = true)
    public List<Match> getAllMatchs() {
        return mrepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Match> getMatchsByName(String name) {
        return mrepo.findByName(name);
    }

    @Transactional(readOnly = true) //????
    public List<Match> getRunningMatches(){
        return mrepo.findAll();
    }

    @Transactional
    public Match save(Match m) {
        mrepo.save(m);
        return m;
    }

    @Transactional(readOnly=true)
    public Optional<Match> getMatchById(Integer id) {
        return mrepo.findById(id);
    }

    @Transactional
    public void delete(Integer id) {
        mrepo.deleteById(id);
    }




    
}
