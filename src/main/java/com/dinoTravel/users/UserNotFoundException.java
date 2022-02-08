package com.dinoTravel.users;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() { super("Could not find the provided user");}
}
