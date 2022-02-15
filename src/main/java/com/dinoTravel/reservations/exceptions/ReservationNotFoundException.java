package com.dinoTravel.reservations.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(int reservationId) { super("Could not find reservation with id: " + reservationId);}
}
