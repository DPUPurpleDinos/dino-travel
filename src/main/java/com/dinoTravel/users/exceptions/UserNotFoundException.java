package com.dinoTravel.users.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() { super("Could not find the provided user");}
}
