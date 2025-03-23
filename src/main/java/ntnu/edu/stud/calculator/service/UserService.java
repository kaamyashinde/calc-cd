package ntnu.edu.stud.calculator.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import ntnu.edu.stud.calculator.model.User;
import ntnu.edu.stud.calculator.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //Register a new user
    public User registerUser(String username, String password) throws IllegalArgumentException {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        try {
            User user = new User(username.trim(), password);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Could not register user. Please try again.");
        }
    }

    //Log in an user
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username).filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    

}
