package com.dinoTravel.flights;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Provides methods to turn a Flight into a RepresentationModel
 * and allows links to be added to the Model.
 */
@Component
class FlightModelAssembler implements RepresentationModelAssembler<Flight, EntityModel<Flight>> {

    /**
     * Converts a Flight to a EntityModel of a Flight object
     * @param flight the Flight object to convert to an EntityModel
     * @return the flight as an EntityModel
     */
    @Override
    public EntityModel<Flight> toModel(Flight flight) {
        return EntityModel.of(flight,
                linkTo(methodOn(FlightController.class).getFlightById(flight.getFlight_id())).withSelfRel());
    }

    /**
     * Update an existing complaint already contained in the ComplaintRepository
     * Otherwise save it to the FlightRepository
     * @param flight The body of the flight
     * @param flightId The id for the existing flight
     * @return The body of the updated flight as a ResponseEntity
     */
}
