package ir.hamyblog.services;

import ir.hamyblog.entities.User;
import ir.hamyblog.exceptions.UserNotExistException;
import ir.hamyblog.exceptions.UsernameAlreadyExistException;
import ir.hamyblog.model.UserRegisterIn;
import ir.hamyblog.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserRegisterIn userRegisterIn) {
        // check username not exists
        String username = userRegisterIn.getUsername();
        String password = userRegisterIn.getPassword();
        String fullName = userRegisterIn.getFullName();
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistException("username already exist " + username);
        }
        User user = new User(username, password, fullName);
        userRepository.save(user);
        return user;
    }


    public User getUserByUsername(String username) {
        final Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotExistException("username not exist " + username);
        }
        return optionalUser.get();
    }
}
