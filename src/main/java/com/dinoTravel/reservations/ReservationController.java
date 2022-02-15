package com.dinoTravel.reservations;

import com.dinoTravel.TokenVerifier;
import com.dinoTravel.TokenVerifierResponse;
import com.dinoTravel.flights.Flight;
import com.dinoTravel.flights.FlightRepository;
import com.dinoTravel.reservations.exceptions.InvalidCredentials;
import com.dinoTravel.reservations.exceptions.ReservationNotFoundException;
import com.dinoTravel.reservations.exceptions.TooManyReservationsException;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles ReservationNotFoundExceptions thrown by the controller
 */
@ControllerAdvice
class ReservationErrorHandler {
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

    /**
     * Generate a 422 status if too many reservations are requested to be added
     * @param ex TooManyReservationsException
     * @return Error message notifying user about the reservation limit
     */
    @ResponseBody
    @ExceptionHandler(TooManyReservationsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String tooManyReservationsHandler(TooManyReservationsException ex) { return ex.getMessage(); }

    @ResponseBody
    @ExceptionHandler(InvalidCredentials.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String invalidCredentials(InvalidCredentials ex) {return ex.getMessage();}
}

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final ReservationModelAssembler reservationAssembler;
    private final FlightRepository flightRepository;

    /**
     * Constructor to create a RestController for Reservation objects
     * @param repository Repository to save Reservation objects
     * @param assembler Assembler to create the JSON response
     * @param flightRepository Repository to save flights
     */
    ReservationController(ReservationRepository repository, ReservationModelAssembler assembler,
        FlightRepository flightRepository) {
        this.reservationRepository = repository;
        this.reservationAssembler = assembler;
        this.flightRepository = flightRepository;
    }

    @GetMapping("/all")
    List<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    @GetMapping("/user")
    List<Reservation> getReservationsByUser(@RequestHeader("Authorization") String auth){
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        return reservationRepository.findBySubjectId(response.getSubject());
    }

    @GetMapping("/{id}")
    Reservation getReservationById(@RequestHeader("Authorization") String auth, @PathVariable ("id") int reservationId) {
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));
        if (!reservation.getSubject_id().equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to view the requested reservation.");
        }else {
            return reservation;
        }
    }


    @PostMapping
    ReservationRequest createReservation(@RequestHeader("Authorization") String auth, @RequestBody ReservationRequest [] requestedReservation) {
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnorePaths("flight_id");
        long bookingID = System.currentTimeMillis();
        for (ReservationRequest request : requestedReservation){
            for (Flight requestedFlight: request.getFlight_request_info()){
                Example<Flight> example = Example.of(requestedFlight, matcher);
                List<Flight> p = flightRepository.findAll(example);
                System.out.println(p.toString());
                //boolean n = flightRepository.exists(requestedFlight);
                //int got = flightRepository.existsByInfo(requestedFlight.getDeparture_airport(), requestedFlight.getArrival_airport(), requestedFlight.getDeparture_time(), requestedFlight.getArrival_time(), requestedFlight.getFlight_provider(), requestedFlight.getFlight_code()).intValue();
                //System.out.println(got);
                //if (got != 1){
                //    flightRepository.save(requestedFlight);
                //}
            }
        }
        System.out.println(Arrays.toString(requestedReservation));
        return requestedReservation[0];
    }


    /**
     * Returns all Reservations saved in the ReservationRepository
     * @return A collection of Reservations and their bodies as an EntityModel

    @GetMapping("/multi")
    CollectionModel<EntityModel<Reservation>> getAllReservationsMulti() {
        return getAllReservations();
    }*/

    /**
     * Update an existing reservation already contained in the ReservationRepository
     * Otherwise save it to the ReservationRepository
     * @param reservation The body of the reservation
     * @param reservationId The ID for an existing reservation
     * @return The body of the updated reservation as a ResponseEntity

    @PutMapping("/{id}")
    ResponseEntity<?> updateReservation(@RequestBody Reservation reservation, @PathVariable ("id") int reservationId) {
        Reservation existingReservation = reservationRepository.findById(reservationId)
            .map(newReservation -> {
                newReservation.setUser_id(reservation.getUser_id());
                newReservation.setTrip_type(reservation.getTrip_type());
                newReservation.setFlight_id(reservation.getFlight_id());
                newReservation.setTraveler_type(reservation.getTraveler_type());
                newReservation.setTraveler_name(reservation.getTraveler_name());
                newReservation.setNum_checked_bags(reservation.getNum_checked_bags());
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
    }*/

    /**
     * Bulk add reservations to the ReservationRepository
     * @param reservations A JSON array of Reservations
     * @return A collection of Reservations and their bodies as an EntityModel
     */
    @PostMapping("/multi")
    CollectionModel<EntityModel<Reservation>> createReservations(@RequestBody Reservation [] reservations) {

        // Throw an exception if the amount of reservations exceeds the limit
        int limit = 50;
        if (reservations.length > limit) {
            throw new TooManyReservationsException(limit);
        }

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
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteReservation(@PathVariable ("id") int reservationId) {
        reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));

        reservationRepository.deleteById(reservationId);

        return ResponseEntity.noContent().build();
    }
}
