package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.AllCardsStatusDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardsUpdateDTO {
    @NotNull
    private Integer matchId;

    @NotNull
    private AllCardsStatusDTO winner;

    @NotNull
    private AllCardsStatusDTO loser;

    public CardsUpdateDTO(Integer matchId, AllCardsStatusDTO winner, AllCardsStatusDTO loser) {
        this.matchId = matchId;
        this.winner = winner;
        this.loser = loser;
    }

    public CardsUpdateDTO() {}
}
