package com.dinoTravel.users.exceptions;

public class UserExistsException extends RuntimeException{
  public UserExistsException() {super("Provided User already exists");}
}
