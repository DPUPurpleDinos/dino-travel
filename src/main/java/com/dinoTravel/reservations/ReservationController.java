package com.dinoTravel.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public List<Reservation> getAllReservations() { return reservationRepository.findAll();}

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable ("id") int reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Could not find reservation with id: " + reservationId));
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(@RequestBody Reservation reservation, @PathVariable("id") int reservationId) {
        Reservation existingReservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Could not find reservation with id: " + reservationId));

        existingReservation.setUser_id(reservation.getUser_id());
        existingReservation.setTrip_type(reservation.getTrip_type());
        existingReservation.setOutgoing_flight_type(reservation.getOutgoing_flight_type());
        existingReservation.setOutgoing_flight_id(reservation.getOutgoing_flight_id());
        existingReservation.setReturning_flight_type(reservation.getReturning_flight_type());
        existingReservation.setReturning_flight_id(reservation.getReturning_flight_id());
        existingReservation.setPrice(reservation.getPrice());

        return reservationRepository.save(existingReservation);
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @DeleteMapping
    public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") int reservationId) {
        Reservation existingReservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Could not find reservation with id: " + reservationId));

        reservationRepository.delete(existingReservation);

        return ResponseEntity.ok().build();
    }
}
