package com.dinoTravel.flights;

public class FlightNotFoundException extends RuntimeException {
    FlightNotFoundException(int flightId) {
        super("Could not find flight with id: " + flightId);
    }
}
