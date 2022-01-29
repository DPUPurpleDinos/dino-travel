package com.dinoTravel.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles exceptions that get thrown by the ReservationController
 */
@ControllerAdvice
class ReservationNotFoundAdvice {

    /**
     * Generate a 404 status is a requested ID is not found
     * and return an error message as a String
     * @param ex ReservationNotFoundException
     * @return Error message containing the reservation ID that caused the exception
     */
    @ResponseBody
    @ExceptionHandler(ReservationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String reservationNotFoundHandler(ReservationNotFoundException ex) { return ex.getMessage(); }
}

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    private final ReservationModelAssembler reservationAssembler;

    /**
     * Constructor to create a RestController for Reservation objects
     * @param repository Repository to save Reservation objects
     * @param assembler Assembler to create the JSON response
     */
    ReservationController(ReservationRepository repository, ReservationModelAssembler assembler) {
        this.reservationRepository = repository;
        this.reservationAssembler = assembler;
    }

    /**
     * Returns all Reservations saved in the ReservationRepository
     * @return A collection of Reservations and their bodies as an EntityModel
     */
    @GetMapping()
    CollectionModel<EntityModel<Reservation>> getAllReservations() {
        List<EntityModel<Reservation>> reservations = reservationRepository.findAll().stream()
            .map(reservationAssembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(reservations);
    }

    /**
     * Returns all Reservations saved in the ReservationRepository
     * @return A collection of Reservations and their bodies as an EntityModel
     */
    @GetMapping("/multi")
    CollectionModel<EntityModel<Reservation>> getAllReservationsMulti() {
        return getAllReservations();
    }

    /**
     * Return the body for a single reservation
     * @param reservationId the ID for the reservation
     * @return The body of the reservation as an EntityModel
     */
    @GetMapping("/{id}")
    EntityModel<Reservation> getReservationById(@PathVariable ("id") int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));

        return reservationAssembler.toModel(reservation);
    }

    /**
     * Update an existing reservation already contained in the ReservationRepository
     * Otherwise save it to the ReservationRepository
     * @param reservation The body of the reservation
     * @param reservationId The ID for an existing reservation
     * @return The body of the updated reservation as a ResponseEntity
     */
    @PutMapping("/{id}")
    ResponseEntity<?> updateReservation(@RequestBody Reservation reservation, @PathVariable ("id") int reservationId) {
        Reservation existingReservation = reservationRepository.findById(reservationId)
            .map(newReservation -> {
                newReservation.setUser_id(reservation.getUser_id());
                newReservation.setTrip_type(reservation.getTrip_type());
                newReservation.setFlight_id(reservation.getFlight_id());
                newReservation.setTraveler_type(reservation.getTraveler_type());
                newReservation.setTraveler_name(reservation.getTraveler_name());
                newReservation.setSeat_id(reservation.getSeat_id());
                newReservation.setSeat_type(reservation.getSeat_type());
                newReservation.setPrice(reservation.getPrice());
                return reservationRepository.save(newReservation);
            }).orElseGet(() -> {
                reservation.setReservation_id(reservationId);
                return reservationRepository.save(reservation);
            });
        EntityModel<Reservation> entityModel = reservationAssembler.toModel(existingReservation);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    /**
     * Create a new reservation to be added to the ReservationRepository
     * @param reservation The body of the reservation
     * @return The body of the created reservation as a ResponseEntity
     */
    @PostMapping
    ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        EntityModel<Reservation> entityModel = reservationAssembler.toModel(reservationRepository.save(reservation));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Bulk add reservations to the ReservationRepository
     * @param reservations A JSON array of Reservations
     * @return A collection of Reservations and their bodies as an EntityModel
     */
    @PostMapping("/multi")
    CollectionModel<EntityModel<Reservation>> createReservations(@RequestBody Reservation [] reservations) {
        for (Reservation res : reservations) {
            reservationAssembler.toModel(reservationRepository.save(res));
        }

        List<EntityModel<Reservation>> newReservations = Arrays.stream(reservations)
                .map(reservationAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(newReservations);
    }

    /**
     * Delete a reservation from the ReservationRepository
     * @param reservationId The ID for a reservation to delete
     * @return An empty body as a ResponseEntity
     */
    @DeleteMapping
    ResponseEntity<?> deleteReservation(@PathVariable ("id") int reservationId) {
        reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));

        reservationRepository.deleteById(reservationId);

        return ResponseEntity.noContent().build();
    }
}
