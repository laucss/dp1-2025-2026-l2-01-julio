package es.us.dp1.lx_xy_24_25.your_game_name.match;


import es.us.dp1.lx_xy_24_25.your_game_name.model.NamedEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Match extends NamedEntity {
    String code;
    LocalDateTime start;
    LocalDateTime finish;
}
