package es.us.dp1.lx_xy_24_25.Escape_From_Elba.statistics.achievements;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achievement extends BaseEntity {

    @NotBlank
    private String description;

    private String badgeImage;

    @Min(0)
    private double threshold;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    Metric metric;

    @NotNull
    @Column(name="tier")
    @Enumerated(EnumType.STRING)
    TierType tier;

    public String getActualDescription(){
        return description.replace("<THRESHOLD>",String.valueOf(threshold));
    }

    public Achievement(Integer id, Integer threshold, TierType tier, String description, String badgeImage, Metric metric) {

            this.id = id;
            this.description = description;
            this.badgeImage = badgeImage;
            this.threshold = threshold;
            this.metric = metric;
            this.tier = tier;

    }
    
}