package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class MoreVotesThanPlayersException  extends RuntimeException {
    public MoreVotesThanPlayersException(String message) {
        super(message);
    }
    
}
