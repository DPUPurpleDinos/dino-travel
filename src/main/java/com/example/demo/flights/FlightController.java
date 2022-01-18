package com.example.demo.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles exceptions that get thrown by the FlightController
 */
@ControllerAdvice
class FlightNotFoundAdvice {
    /**
     * Generates a 404 status if a requested ID is not found and
     * returns an error message as a String
     * @param ex FlightNotFoundException
     * @return Error message containing the flight id that caused the exception
     */
    @ResponseBody
    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String flightNotFoundHandler(FlightNotFoundException ex) {
        return ex.getMessage();
    }
}

/**
 *  Handles HTTP requests for Flight objects
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;
    private final FlightModelAssembler flightAssembler;

    /**
     * Constructor to create a RestController for Flight objects
     * @param repository Repository to save the Flight objects
     * @param assembler Assembler to create the JSON response
     */
    FlightController(FlightRepository repository, FlightModelAssembler assembler) {
        this.flightRepository = repository;
        this.flightAssembler = assembler;
    }

    /**
     * Returns all flights saved in the FlightRepository
     * @return A collection of Flights and their bodies represented as an EntityModel
     */
    @GetMapping()
    CollectionModel<EntityModel<Flight>> getAllFlights() {
        List<EntityModel<Flight>> flights = flightRepository.findAll().stream()
                .map(flightAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(flights);
    }

    /**
     * Return the body for a single flight
     * @param flightId the ID for the flight
     * @return The body of the flight as an EntityModel
     */
    @GetMapping("/{id}")
    EntityModel<Flight> getFlightById(@PathVariable ("id") int flightId) {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));

        return flightAssembler.toModel(flight);
    }

    /**
     * Update an existing flight already contained in the FlightRepository
     * @param flight The body of the flight
     * @param flightId The id for the existing flight
     * @return The body of the updated flight as a ResponseEntity
     */
    @PutMapping("/{id}")
    ResponseEntity<?> updateFlight(@RequestBody Flight flight, @PathVariable("id") int flightId) {
        Flight existingFlight = flightRepository.findById(flightId)
                .map(newFlight -> {
                    newFlight.setSeats_available(flight.getSeats_available());
                    newFlight.setFlight_provider(flight.getFlight_provider());
                    newFlight.setDeparture_airport(flight.getDeparture_airport());
                    newFlight.setDeparture_time(flight.getDeparture_time());
                    newFlight.setArrival_airport(flight.getArrival_airport());
                    newFlight.setArrival_time(flight.getArrival_time());
                    return flightRepository.save(newFlight);
                }).orElseGet(() -> {
                    flight.setFlight_id(flightId);
                    return flightRepository.save(flight);
                });
        EntityModel<Flight> entityModel = flightAssembler.toModel(existingFlight);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Create a new flight to be added to the FlightRepository
     * @param flight The body of the flight
     * @return The body of the created flight as a ResponseEntity
     */
    @PostMapping
    ResponseEntity<?> createFlight(@RequestBody Flight flight) {
        EntityModel<Flight> entityModel = flightAssembler.toModel(flightRepository.save(flight));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Delete a flight from the FlightRepository
     * @param flightId The id for a flight to be deleted
     * @return An empty body as a ResponseEntity
     */
    @DeleteMapping
    ResponseEntity<?> deleteFlight(@PathVariable("id") int flightId) {

        flightRepository.deleteById(flightId);

        return ResponseEntity.noContent().build();
    }
}
