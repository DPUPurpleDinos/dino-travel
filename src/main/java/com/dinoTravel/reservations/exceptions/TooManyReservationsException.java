package com.dinoTravel.reservations.exceptions;

public class TooManyReservationsException extends RuntimeException {
    public TooManyReservationsException(int limit) { super("Could not add that many reservations at once. Limit = " + limit);}
}
