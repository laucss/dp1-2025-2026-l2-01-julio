package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LobbyNotFound extends RuntimeException {
    public LobbyNotFound(String message) {
        super(message);
    }
}
