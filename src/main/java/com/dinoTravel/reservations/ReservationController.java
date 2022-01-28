package com.dinoTravel.reservations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                newReservation.setOutgoing_flight_type(reservation.getOutgoing_flight_type());
                newReservation.setOutgoing_flight_id(reservation.getOutgoing_flight_id());
                newReservation.setReturning_flight_type(reservation.getReturning_flight_type());
                newReservation.setReturning_flight_id(reservation.getReturning_flight_id());
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
     *
     * @param reservations
     * @return
     */
    @PostMapping("/multi")
    CollectionModel<EntityModel<Reservation>> createReservations(@RequestBody Reservation [] reservations) {
        for (Reservation res : reservations) {
            reservationAssembler.toModel(reservationRepository.save(res));
        }

        // TODO change return body and add a GET /multi
        List<EntityModel<Reservation>> allReservations = reservationRepository.findAll().stream()
                .map(reservationAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(allReservations);
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
