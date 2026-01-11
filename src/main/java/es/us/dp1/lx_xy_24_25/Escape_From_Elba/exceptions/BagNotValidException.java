package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BagNotValidException extends RuntimeException {
    public BagNotValidException(String message) {
        super(message);
    }
    
}
 
