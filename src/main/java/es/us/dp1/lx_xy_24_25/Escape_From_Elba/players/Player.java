package es.us.dp1.lx_xy_24_25.Escape_From_Elba.players;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.bag.BagInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.cards.hand.HandInGame;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.room.Room;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

// import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends BaseEntity {


	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "match_id")
	@JsonIgnore
	private Match match;

	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name = "room_id")
	private Room room;

	@Min(0)
	//@Max(7)
	private Integer cardsDrawnInTurn; 

    private Integer strength; 

    private Integer actionPoints;

	// Estadísticas de batallas
	private Integer battlesWon = 0;

	private Integer battlesPlayed = 0;

	// Estadística de habitaciones visitadas
	private Integer roomsVisited = 0;
	

	private Integer orderInMatch; // Indica el orden de turno del jugador en la partida

	private Integer diceOrder; // Es el número que ha salido al tirar los dados para determinar el orden del turno.
        
	//private Statistic statistic;






}



