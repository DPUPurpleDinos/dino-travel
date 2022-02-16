package com.dinoTravel.reservations.exceptions;

public class InvalidBagAmountException extends RuntimeException{
  public InvalidBagAmountException(String message) {
    super(message);
  }
}
