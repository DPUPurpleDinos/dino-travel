package com.dinoTravel.reservations.exceptions;

public class FlightIsFull extends RuntimeException{
  public FlightIsFull(String departureAirport, String arrivalAirport) {
    super("Sorry the flight leaving from " + departureAirport + " and going to " + arrivalAirport + " is full, please choose another flight.");
  }
}
