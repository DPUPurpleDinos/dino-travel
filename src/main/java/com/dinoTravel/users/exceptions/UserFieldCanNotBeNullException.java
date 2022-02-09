package com.dinoTravel.users.exceptions;

public class UserFieldCanNotBeNullException extends RuntimeException{
  public UserFieldCanNotBeNullException(String message) {super("The field " + message + " cannot be empty.");}
}
