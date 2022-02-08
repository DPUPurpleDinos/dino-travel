package com.dinoTravel;

public class TokenInvalid extends RuntimeException{
  TokenInvalid(String message) {super("The provided token is invalid. Reason: " + message);}
}
