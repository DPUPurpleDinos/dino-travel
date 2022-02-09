package com.dinoTravel.users.exceptions;

public class UserVariableIsNotValidException extends RuntimeException{
  public UserVariableIsNotValidException(String message) {super("The field " + message + " is not valid.");}
}
