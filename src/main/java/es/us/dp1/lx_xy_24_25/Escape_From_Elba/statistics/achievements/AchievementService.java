package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.achievements;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.StatisticService;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.validation.Valid;

@Service
public class AchievementService {
        
    AchievementRepository repo;
    StatisticService statisticService;

    @Autowired
    public AchievementService(AchievementRepository repo, StatisticService statisticService){
        this.repo=repo;
        this.statisticService=statisticService;
    }

    @Transactional(readOnly = true)    
    List<Achievement> getAchievements(){
        List<Achievement> achievements=new ArrayList<>();
        repo.findAll().forEach(achievements::add);
        return achievements;
    }
    
    @Transactional(readOnly = true)    
    public Achievement getById(int id){
        Optional<Achievement> result=repo.findById(id);
        return result.isPresent()?result.get():null;
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return repo.save(newAchievement);
    }

    
    @Transactional
    public void deleteAchievementById(int id){
        repo.deleteById(id);
    }

    public boolean isAchievementUnlocked(Achievement achievement, User user){
        Boolean unlocked=false;
        if(achievement.metric==Metric.GAMES_PLAYED){
            Integer gamesPlayed=statisticService.getMatchesPlayedByUser(user.getId());
            unlocked=gamesPlayed>=achievement.getThreshold();
        }
        else if(achievement.metric==Metric.VICTORIES){
            Integer victories=statisticService.getTotalVictoriesByUser(user.getId());
            unlocked=victories>=achievement.getThreshold();
        }
        else if(achievement.metric==Metric.TOTAL_PLAY_TIME){
            Integer totalPlayTime=statisticService.getTotalTimePlayedByUserFOR(user.getId());
            unlocked=totalPlayTime>=achievement.getThreshold();
        }
        else if(achievement.metric==Metric.ACTION_POINTS_EARNED){
            Integer actionPoints=statisticService.getTotalAccionPointsByUser(user.getId());
            unlocked=actionPoints>=achievement.getThreshold();
        }
        return unlocked;
    }

    public List<Achievement> getAchievementsByTier(TierType tier){
        return repo.findByTier(tier);
    }
}