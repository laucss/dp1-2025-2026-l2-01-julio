package es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.DTOs;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.Card;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.CardDTO;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.fights.FightResultType;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightResolvedDTO {

    private String action = "RESOLVE"; // Para distinguir de otros eventos de fight

    private Integer matchId;

    private Integer winnerId;

    private Integer loserId;

    private Integer chainRoomId;
    private Integer movedPlayerId;
    private Integer movedNpcId;

    // si por ejemplo, gana contra npcs recibe directamente la carta
    private CardDTO card; 

    @NonNull
    @Enumerated(EnumType.STRING)
    private FightResultType fightResultType; 


    public boolean hasChainFight() {
    return chainRoomId != null;
    }

    public FightResolvedDTO(Integer matchId, Integer winnerId, Integer loserId, Card card, FightResultType fightResultType){
        this.card = new CardDTO(card);
        this.fightResultType = fightResultType; 
        this.winnerId = winnerId; 
        this.loserId = loserId; 
        this.matchId = matchId;
    }

    public FightResolvedDTO(Integer matchId, Integer winnerId, Integer loserId, FightResultType fightResultType){
        this.fightResultType = fightResultType;
        this.winnerId = winnerId; 
        this.loserId = loserId; 
        this.matchId = matchId;
    }

        public void setPlayerChainFight(Integer roomId, Integer playerId) {
        this.chainRoomId = roomId;
        this.movedPlayerId = playerId;
        this.movedNpcId = null;
    }

    public void setNpcChainFight(Integer roomId, Integer npcId) {
        this.chainRoomId = roomId;
        this.movedNpcId = npcId;
        this.movedPlayerId = null;
    }

    public FightResolvedDTO(){}


    
}
