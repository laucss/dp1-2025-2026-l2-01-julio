package es.us.dp1.lx_xy_24_25.Escape_From_Elba.match.lobby;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LobbyDTO {
    @NotNull
    private Boolean isPrivate;

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    @Min(3)
    @Max(6)
    private Integer maxPlayers;
}

