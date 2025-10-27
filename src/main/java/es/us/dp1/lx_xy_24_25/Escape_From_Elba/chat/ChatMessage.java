package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
//import lombok.EqualsAndHashCode;

@Getter
@Setter
@Entity
//@EqualsAndHashCode(of = {}, callSuper = true)
public class ChatMessage {
    private Player user;


    
}
