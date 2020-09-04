package ir.hamyblog.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestToken {
    private String username;
    private String password;
}
