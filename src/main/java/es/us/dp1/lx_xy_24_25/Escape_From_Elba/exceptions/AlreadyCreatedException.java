package es.us.dp1.lx_xy_24_25.Escape_From_Elba.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class AlreadyCreatedException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    public AlreadyCreatedException() {
        super("Entity already created.");
    }

    public AlreadyCreatedException(String entity) {
        super(String.format("Entity %s already created.", entity));
    }

    public AlreadyCreatedException(String entity, String message) {
        super(String.format("%s", message));
    }

}
