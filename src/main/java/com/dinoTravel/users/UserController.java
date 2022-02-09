package com.dinoTravel.users;

import com.dinoTravel.TokenInvalid;
import com.dinoTravel.TokenVerifier;
import com.dinoTravel.TokenVerifierResponse;
import com.dinoTravel.users.exceptions.UserExistsException;
import com.dinoTravel.users.exceptions.UserFieldCanNotBeNullException;
import com.dinoTravel.users.exceptions.UserNotFoundException;
import com.dinoTravel.users.exceptions.UserVariableIsNotValidException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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

    @ResponseBody
    @ExceptionHandler(UserFieldCanNotBeNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userFieldCanNotBeNull(UserFieldCanNotBeNullException ex) {return ex.getMessage();}

    @ResponseBody
    @ExceptionHandler(UserVariableIsNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userFieldInValid(UserVariableIsNotValidException ex) {return ex.getMessage();}
}

/**
 * Handles HTTP requests for User objects
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;
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
     * Updates a given users info, provide any info to be changed
     * If user not found throw an error
     * @param changes the map of the provides fields to change
     * @param auth the authentication token
     * @return status of 200
     */
    @PutMapping()
    UserRequest updateUser(@RequestBody Map<String, String> changes, @RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);

        User existingUser = userRepository.findById(Response.getSubject()).orElseThrow(UserNotFoundException::new);

        existingUser.update(changes);

        userRepository.save(existingUser);

        return new UserRequest(existingUser);
    }

    /**
     * Create a new user with the given info in the request
     * @param userRequest the info of the user to be made
     * @param auth the auth token
     * @return the
     */
    @PostMapping
    UserRequest createUser(@RequestBody UserRequest userRequest, @RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);
        if (userRepository.existsById(Response.getSubject())){
            throw new UserExistsException();
        }else {
            User user = new User(Response.getSubject(), userRequest);
            userRepository.save(user);
            return userRequest;
        }
    }

    /**
     * Delete a user from the UserRepository
     * I am hesitant to put this in
     * 1 this will have to cause knock on effects in the database
     * 2 this is very powerful have to be careful with this one
     * @param auth The auth string
     * @return status code 200
     */
    @DeleteMapping
    ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String auth) {
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);
        userRepository.findById(Response.getSubject()).orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(Response.getSubject());

        return ResponseEntity.ok("User Deleted");
    }
}
