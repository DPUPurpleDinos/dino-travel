package com.dinoTravel.users;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(int userId) { super("Could not find user with id: " + userId);}
}
