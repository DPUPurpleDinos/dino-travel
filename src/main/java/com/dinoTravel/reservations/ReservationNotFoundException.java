package com.dinoTravel.reservations;

public class ReservationNotFoundException extends RuntimeException {
    ReservationNotFoundException(int reservationId) { super("Could not find reservation with id: " + reservationId);}
}
