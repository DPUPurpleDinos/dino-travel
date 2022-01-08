package com.example.demo.flights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://35.171.66.24:8080/")
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flight getFlightById(@PathVariable ("id") int flightId) {
        return flightRepository.findById(flightId).orElseThrow(() -> new NoSuchElementException("Could not find flight with id: " + flightId));
    }

    @PutMapping("/{id}")
    public Flight updateFlight(@RequestBody Flight flight, @PathVariable("id") int flightId) {
        Flight existingFlight = flightRepository.findById(flightId).orElseThrow(() -> new NoSuchElementException("Could not find flight with id: " + flightId));

        existingFlight.setSeats_available(flight.getSeats_available());
        existingFlight.setFlight_provider(flight.getFlight_provider());
        existingFlight.setDeparture_airport(flight.getDeparture_airport());
        existingFlight.setDeparture_time(flight.getDeparture_time());
        existingFlight.setArrival_airport(flight.getArrival_airport());
        existingFlight.setArrival_time(flight.getArrival_time());

        return flightRepository.save(existingFlight);
    }

    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    @DeleteMapping
    public ResponseEntity<Flight> deleteFlight(@PathVariable("id") int flightId) {
        Flight existingFlight = flightRepository.findById(flightId).orElseThrow(() -> new NoSuchElementException("Could not find flight with id: " + flightId));

        flightRepository.delete(existingFlight);

        return ResponseEntity.ok().build();
    }
}
