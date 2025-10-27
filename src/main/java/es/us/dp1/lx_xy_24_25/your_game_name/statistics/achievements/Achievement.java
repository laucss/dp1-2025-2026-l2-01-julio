package es.us.dp1.lx_xy_24_25.your_game_name.statistics.achievements;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import es.us.dp1.lx_xy_24_25.your_game_name.model.NamedEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Achievement")
public class Achievement extends NamedEntity{

    @NotNull
    @NotBlank
    private String description;

    
    private String badgeImage;

    @Min(0)
    private double threshold;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    Metric metric;

    public String getActualDescription(){
        return description.replace("<THRESHOLD>",String.valueOf(threshold));
    }
    
}