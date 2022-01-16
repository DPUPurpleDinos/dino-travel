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

// Generate a 404 status and an exception message if a flight is not found
@ControllerAdvice
class FlightNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String flightNotFoundHandler(FlightNotFoundException ex) {
        return ex.getMessage();
    }
}

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    FlightController(FlightRepository repository, FlightModelAssembler assembler) {
        this.flightRepository = repository;
        this.flightAssembler = assembler;
    }

    @Autowired
    private FlightRepository flightRepository;
    private final FlightModelAssembler flightAssembler;

    @GetMapping()
    CollectionModel<EntityModel<Flight>> getAllFlights() {
        List<EntityModel<Flight>> flights = flightRepository.findAll().stream()
                .map(flightAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(flights);
    }

    @GetMapping("/{id}")
    EntityModel<Flight> getFlightById(@PathVariable ("id") int flightId) {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new FlightNotFoundException(flightId));

        return flightAssembler.toModel(flight);
    }

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

    @PostMapping
    ResponseEntity<?> createFlight(@RequestBody Flight flight) {
        EntityModel<Flight> entityModel = flightAssembler.toModel(flightRepository.save(flight));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping
    ResponseEntity<?> deleteFlight(@PathVariable("id") int flightId) {

        flightRepository.deleteById(flightId);

        return ResponseEntity.noContent().build();
    }
}
