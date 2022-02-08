package com.dinoTravel.users;

import com.dinoTravel.TokenInvalid;
import com.dinoTravel.TokenVerifier;
import com.dinoTravel.TokenVerifierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles exceptions that get thrown by the UserController
 */
@ControllerAdvice
class UserExceptionController {

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

    @ResponseBody
    @ExceptionHandler(TokenInvalid.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String userNotAuthorized(TokenInvalid ex) { return ex.getMessage();}

    @ResponseBody
    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String userExists(UserExistsException ex) {return ex.getMessage();}

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
     * Returns the user with the provided token
     * @return A collection of Users and their bodies represented as an EntityModel
     */
    @GetMapping()
    EntityModel<User> getUser(@RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);
        User currentUser = userRepository.findById(Response.getSubject())
            .orElseThrow(UserNotFoundException::new);

        return userAssembler.toModel(currentUser);
    }

    /**
     * Update an existing user already contained in the UserRepository
     * Otherwise save it to the UserRepository
     * @param user The body of the user
     * @param userId The ID for an existing user
     * @return The body of the updated user as a ResponseEntity

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
                user.setSubject_id(userId);
                return userRepository.save(user);
            });
        EntityModel<User> entityModel = userAssembler.toModel(existingUser);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }*/

    /**
     *
     * @param auth
     * @return
     */
    @PostMapping
    ResponseEntity<?> createUser(@RequestBody User user, @RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);
        if (userRepository.existsById(Response.getSubject())){
            throw new UserExistsException();
        }

        user.setSubject_id(Response.getSubject());
        userRepository.save(user);

        return ResponseEntity.ok("User Created");
    }

    /**
     * Delete a user from the UserRepository
     * @param userId The ID for a user to delete
     * @return An empty body as a ResponseEntity

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable ("id") int userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(userId);

        return ResponseEntity.noContent().build();
    } */
}
