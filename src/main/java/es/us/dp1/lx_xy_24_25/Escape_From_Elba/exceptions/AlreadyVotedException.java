package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException(String message){
        super(message);
    }
    
}
