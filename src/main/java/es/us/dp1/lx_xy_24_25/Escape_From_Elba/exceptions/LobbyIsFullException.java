package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LobbyIsFullException extends RuntimeException {
    public LobbyIsFullException(String message) {
        super(message);
    }
}