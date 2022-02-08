package com.dinoTravel.users;

public class UserExistsException extends RuntimeException{
  public UserExistsException() {super("Provided User already exists");}
}
