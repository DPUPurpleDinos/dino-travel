package com.dinoTravel.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles exceptions that get thrown by the UserController
 */
@ControllerAdvice
class UserNotFoundAdvice {

    /**
     * Generate a 404 status if a requested ID is not not found
     * and returns an error message as a String
     * @Param ex UserNotFoundException
     * @Return Error message containing the user ID that caused the exception
     */
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) { return ex.getMessage(); }
}

/**
 * Handles HTTP requests for User objects
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final UserModelAssembler userAssembler;

    /**
     * Constructor to create a RestController for User objects
     * @param repository Repository to save the User objects
     * @param assembler Assembler to create the JSON response
     */
    UserController(UserRepository repository, UserModelAssembler assembler) {
        this.userRepository = repository;
        this.userAssembler = assembler;
    }

    /**
     * Returns all Users saved in the UserRepository
     * @return A collection of Users and their bodies represented as an EntityModel
     */
    @GetMapping()
    CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
            .map(userAssembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(users);
    }

    /**
     * Return the body for a single user
     * @param userId the ID for the user
     * @return The body of the user as an EntityModel
     */
    @GetMapping("/{id}")
    EntityModel<User> getUserById(@PathVariable ("id") int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        return userAssembler.toModel(user);
    }

    /**
     * Update an existing user already contained in the UserRepository
     * Otherwise save it to the UserRepository
     * @param user The body of the user
     * @param userId The ID for an existing user
     * @return The body of the updated user as a ResponseEntity
     */
    @PutMapping("/{id}")
    ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable ("id") int userId) {
        User existingUser = userRepository.findById(userId)
            .map(newUser -> {
                newUser.setFirst_name(user.getFirst_name());
                newUser.setLast_name(user.getLast_name());
                newUser.setDob(user.getDob());
                newUser.setEmail(user.getEmail());
                return userRepository.save(newUser);
            }).orElseGet(() -> {
                user.setUser_id(userId);
                return userRepository.save(user);
            });
        EntityModel<User> entityModel = userAssembler.toModel(existingUser);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    /**
     * Create a new user to be added to the UserRepository
     * @param user The body of the user
     * @return The body of the created user as a ResponseEntity
     */
    @PostMapping
    ResponseEntity<?> createUser(@RequestBody User user) {
        EntityModel<User> entityModel = userAssembler.toModel(userRepository.save(user));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Delete a user from the UserRepository
     * @param userId The ID for a user to delete
     * @return An empty body as a ResponseEntity
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable ("id") int userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.deleteById(userId);

        return ResponseEntity.noContent().build();
    }
}
