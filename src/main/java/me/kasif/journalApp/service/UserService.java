package me.kasif.journalApp.service;

import me.kasif.journalApp.entity.User;
import me.kasif.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User saveEntry(User user) {
        user.setDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("USER"));
        return userRepository.save(user);
    }

    public User getEntry(ObjectId id) {
        Optional<User> result = userRepository.findById(id);
        return result.orElse(null);
    }


    public User deleteEntry(ObjectId id) {
        Optional<User> entry = userRepository.findById(id);
        entry.ifPresent(e -> userRepository.deleteById(id));
        return entry.orElse(null);
    }

    public User findByUserName(String username) {
       return userRepository.findByUsername(username);
    }
}


//  controller --> Service --> Repository