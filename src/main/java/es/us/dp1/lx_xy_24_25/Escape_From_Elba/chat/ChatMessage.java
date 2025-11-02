package es.us.dp1.lx_xy_24_25.Escape_From_Elba.chat;

import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.players.Player;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.Match;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Chat")
@EqualsAndHashCode(of = "id")

public class ChatMessage extends BaseEntity implements Comparable<ChatMessage>{
    
    @NotNull
    @ManyToOne
    private Player player;

    @NotBlank
    @Size(max = 500)
    private String message;

    @NotNull
    private LocalDateTime time;

    //para que salga la fechahora a la que se mandó el mensaje
    public void setDate(LocalDateTime time) {
        this.time = LocalDateTime.now();
    }


    @ManyToOne
    private Match match;


    @Override
    public int compareTo(ChatMessage o) {
        if (o == null) return 1;
        if (this.time == null && o.getTime() == null) return 0;
        if (this.time == null) return -1;
        if (o.getTime() == null) return 1;
        return this.time.compareTo(o.getTime());
    }

    

}
