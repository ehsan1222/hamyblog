package ir.hamyblog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileException extends RuntimeException {
    public FileException() {
    }

    public FileException(String message) {
        super(message);
    }
}
