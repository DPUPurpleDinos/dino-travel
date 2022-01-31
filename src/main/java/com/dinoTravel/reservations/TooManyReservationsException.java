package com.dinoTravel.reservations;

public class TooManyReservationsException extends RuntimeException {
    TooManyReservationsException(int limit) { super("Could not add that many reservations at once. Limit = " + limit);}
}
