package ir.hamyblog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
