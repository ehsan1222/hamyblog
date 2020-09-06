package ir.hamyblog.services;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import ir.hamyblog.entities.User;
import ir.hamyblog.exceptions.UserNotExistException;
import ir.hamyblog.exceptions.UsernameAlreadyExistException;
import ir.hamyblog.exceptions.UsernamePasswordNotMatchException;
import ir.hamyblog.model.PasswordIn;
import ir.hamyblog.model.Role;
import ir.hamyblog.model.UserRegisterIn;
import ir.hamyblog.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(UserRegisterIn userRegisterIn) {
        // check username not exists
        String username = userRegisterIn.getUsername();
        String password = passwordEncoder.encode(userRegisterIn.getPassword());
        String fullName = userRegisterIn.getFullName();
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistException("username already exist " + username);
        }

        String bas64EncodedSecretKey = createSecretKey();

        User user = new User(username, password, fullName, bas64EncodedSecretKey);
        userRepository.save(user);
        return user;
    }

    public User updateRole(String username, String newStrRole) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found " + username));

        Role newRole;
        try {

            newRole = Role.valueOf(newStrRole.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid role " + newStrRole);
        }

        user.setRole(newRole);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotExistException("username not exist " + username);
        }
        return optionalUser.get();
    }


    public void changePassword(String username, PasswordIn passwordIn) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found " + username));

        if (passwordEncoder.matches(passwordIn.getOldPassword(), user.getPassword())) {
            user.setPassword(
                    passwordEncoder.encode(passwordIn.getNewPassword())
            );
            userRepository.save(user);
        } else {
            throw new UsernamePasswordNotMatchException("password not match");
        }
    }

    private String createSecretKey() {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
