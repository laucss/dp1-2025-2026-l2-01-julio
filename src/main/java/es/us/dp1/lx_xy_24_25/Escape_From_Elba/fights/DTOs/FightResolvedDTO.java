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

    private Integer winnerUserId; // id del user ganador en caso de que haya sido un player, 
    // lo ponemos para que en el frontend siempre se capte bien el ganador, porque currentPlayer es undefined muchas veces

    private Integer loserUserId; // igual que con winner

    private Integer winnerId; // el id del player o el de npc

    private Integer loserId;

    private Integer chainRoomId;


    // si por ejemplo, gana contra npcs recibe directamente la carta
    private CardDTO card; 

    @NonNull
    @Enumerated(EnumType.STRING)
    private FightResultType fightResultType; // en verdad sobra un poco pero bueno, extra confirmación


    public boolean hasChainFight() {
        return chainRoomId != null;
    }

    // player beats npc
    public FightResolvedDTO(Integer matchId, Integer winnerUserId, Integer winnerId, Integer loserId, Integer chainRoomId, Card card, FightResultType fightResultType){
        this.card = new CardDTO(card);
        this.fightResultType = fightResultType; 
        this.winnerId = winnerId; 
        this.loserId = loserId; 
        this.matchId = matchId;
        this.chainRoomId = chainRoomId; 
        this.winnerUserId = winnerUserId;
    }


    //  npc beats player
    public FightResolvedDTO(Integer matchId, Integer winnerId, Integer loserId, Integer loserUserId, Integer chainRoomId, FightResultType fightResultType){
        this.fightResultType = fightResultType;
        this.winnerId = winnerId; 
        this.loserId = loserId; 
        this.matchId = matchId;
        this.chainRoomId = chainRoomId; 
        this.loserUserId = loserUserId; 
    }

    // player beats player
    public FightResolvedDTO(Integer matchId, Integer winnerUserId, Integer winnerId, Integer loserId, Integer loserUserId, Integer chainRoomId, FightResultType fightResultType){
        this.fightResultType = fightResultType;
        this.winnerId = winnerId; 
        this.loserId = loserId; 
        this.matchId = matchId;
        this.chainRoomId = chainRoomId; 
        this.winnerUserId = winnerUserId;
        this.loserUserId = loserUserId; 
    }


    public FightResolvedDTO(){}


    
}
