package ir.hamyblog.web;

import ir.hamyblog.entities.User;
import ir.hamyblog.model.PasswordIn;
import ir.hamyblog.model.Role;
import ir.hamyblog.model.UserRegisterIn;
import ir.hamyblog.services.HamyblogUserDetails;
import ir.hamyblog.services.UserService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> register(@RequestBody UserRegisterIn userRegisterIn) {
        final User user = userService.addUser(userRegisterIn);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username,
                                                  Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (authentication.getName().equals(username) || isAdmin) {
            User user = userService.getUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/users/{username}/role/{role}")
    public ResponseEntity<User> changeUserRole(@PathVariable("username") String username,
                                               @PathVariable("role") String role) {
        User user = userService.updateRole(username, role);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{username}/password")
    public ResponseEntity<?> changePassword(@PathVariable("username") String username,
                                            @RequestBody PasswordIn passwordIn,
                                            Authentication authentication) {

        log.info("oldPassword: {}", passwordIn.getOldPassword());
        log.info("newPassword: {}", passwordIn.getNewPassword());

        if (authentication!= null && authentication.getName().equals(username)) {
            userService.changePassword(username, passwordIn);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
