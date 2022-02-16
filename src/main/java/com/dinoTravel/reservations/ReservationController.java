package com.dinoTravel.reservations;

import com.dinoTravel.TokenVerifier;
import com.dinoTravel.TokenVerifierResponse;
import com.dinoTravel.flights.Flight;
import com.dinoTravel.flights.FlightRepository;
import com.dinoTravel.reservations.exceptions.FlightIsFull;
import com.dinoTravel.reservations.exceptions.InvalidCredentials;
import com.dinoTravel.reservations.exceptions.ReservationNotFoundException;
import com.dinoTravel.reservations.exceptions.TooManyReservationsException;
import com.dinoTravel.users.User;
import com.dinoTravel.users.UserRequest;
import com.dinoTravel.users.exceptions.UserNotFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
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
class ReservationExceptionHandler {
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

    @ResponseBody
    @ExceptionHandler(FlightIsFull.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String flightIsFull(FlightIsFull ex) {return ex.getMessage();}
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
    HttpStatus createReservation(@RequestHeader("Authorization") String auth, @RequestBody ReservationRequest [] requestedReservations) {
        //verify token
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        //make matcher to match any flights that are exactly the same
        //we use a matcher because it is better than making a long sql
        //query with 5 params
        //and rand
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnorePaths("flight_id", "seats_available");
        Random rand = new Random();
        //get bookingID off system time
        long bookingID = System.currentTimeMillis();

        //for every given reservation book it
        for (ReservationRequest request : requestedReservations){
            //for every given flight check if
            for (Flight requestedFlight: request.getFlight_request_info()){
                List<Flight> flightMatches = flightRepository.findAll(Example.of(requestedFlight, matcher));
                int reservationFlightID;
                if (flightMatches.isEmpty()){
                    //the requested flight exists
                    requestedFlight.setSeats_available(rand.nextInt(10, 50));
                    flightRepository.save(requestedFlight);
                    reservationFlightID = requestedFlight.getFlight_id();
                }else{
                    //else remove a passenger from the flight
                    Flight matchedFlight = flightMatches.get(0);
                    if (matchedFlight.getSeats_available() > 0) {
                        matchedFlight.addSeats_available(-1);
                        flightRepository.save(flightMatches.get(0));
                        reservationFlightID = matchedFlight.getFlight_id();
                    }else{
                        throw new FlightIsFull(matchedFlight.getDeparture_airport(), matchedFlight.getArrival_airport());
                    }
                }
                //for every flight make the appropriate reservation for the customer
                Reservation newReservation = new Reservation(request, bookingID, reservationFlightID, response.getSubject());
                reservationRepository.save(newReservation);
            }
        }
        //return created status all good!
        return HttpStatus.CREATED;
    }

    //put mod reservations
    @PutMapping("/{id}")
    Reservation changeReservation(@RequestHeader("Authorization") String auth,@RequestBody Map<String, String> changes, @PathVariable("id") int reservationId){
        TokenVerifierResponse Response = TokenVerifier.verifyToken(auth);

        Reservation existingReservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));

        existingReservation.update(changes);

        reservationRepository.save(existingReservation);

        return existingReservation;
    }

    //put mod bookings

    //delete bookings
    @DeleteMapping("/booking/{id}")
    HttpStatus deleteBooking(@RequestHeader("Authorization") String auth, @PathVariable("id") long bookingID) {
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        String bookingOwner = reservationRepository.findBookingOwner(bookingID);
        if (!bookingOwner.equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to delete the requested booking.");
        }
        List<Reservation> reservationsInBooking = reservationRepository.findByBookingId(bookingID);
        for (Reservation r : reservationsInBooking){
            reservationRepository.deleteById(r.getReservation_id());
        }
        return HttpStatus.OK;
    }

    //delete reservations
    @DeleteMapping("/{id}")
    HttpStatus deleteReservation(@RequestHeader("Authorization") String auth, @PathVariable("id") int reservationId){
        TokenVerifierResponse response = TokenVerifier.verifyToken(auth);
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));
        //Check if the user owns the requested reservation to be deleted
        if (!reservation.getSubject_id().equals(response.getSubject())){
            throw new InvalidCredentials("You are not authorized to delete the requested reservation.");
        }
        reservationRepository.deleteById(reservationId);

        return HttpStatus.OK;
    }
}
