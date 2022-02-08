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
     * @param ex serNotFoundException
     * @return Error message containing the user ID that caused the exception
     */
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) { return ex.getMessage(); }

    /**
     * generates a 401 error if user auth is wrong
     * @param ex TokenInvalid Exception
     * @return Error message saying the token is invalid
     */
    @ResponseBody
    @ExceptionHandler(TokenInvalid.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String userNotAuthorized(TokenInvalid ex) { return ex.getMessage();}

    /**
     * generates a 406 error if user already exists
     * @param ex UserExistsException
     * @return error message
     */
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
     * Updates a given users info
     * @param user the new user info
     * @param auth the authentication token
     * @return status of 200
     */
    @PutMapping()
    ResponseEntity<?> updateUser(@RequestBody User user, @RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);

        User existingUser = userRepository.findById(Response.getSubject())
            .map(newUser -> {
                newUser.setFirst_name(user.getFirst_name());
                newUser.setLast_name(user.getLast_name());
                newUser.setDob(user.getDob());
                newUser.setEmail(user.getEmail());
                return userRepository.save(newUser);
            }).orElseGet(() -> {
                user.setSubject_id(Response.getSubject());
                return userRepository.save(user);
            });
        return ResponseEntity.ok("User updated");
    }

    /**
     * Create a new user
     * @param user the info of the user to be made
     * @param auth the auth token
     * @return status 200
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
