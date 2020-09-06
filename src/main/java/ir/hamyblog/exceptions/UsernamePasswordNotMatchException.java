package ir.hamyblog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernamePasswordNotMatchException extends RuntimeException {

    public UsernamePasswordNotMatchException() {
    }

    public UsernamePasswordNotMatchException(String message) {
        super(message);
    }

}
