package com.example.demo.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable ("id") int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Could not find user with id: " + userId));
    }

    @PutMapping("/{id}")
    public User updateFlight(@RequestBody User user, @PathVariable("id") int userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Could not find user with id: " + userId));

        existingUser.setFirst_name(user.getFirst_name());
        existingUser.setLast_name(user.getLast_name());
        existingUser.setEmail(user.getEmail());
        existingUser.setDob(user.getDob());

        return userRepository.save(existingUser);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping
    public ResponseEntity<User> deleteUser(@PathVariable("id") int userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Could not find user with id: " + userId));

        userRepository.delete(existingUser);

        return ResponseEntity.ok().build();
    }
}
