package com.example.demo.flights;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class FlightModelAssembler implements RepresentationModelAssembler<Flight, EntityModel<Flight>> {

    @Override
    public EntityModel<Flight> toModel(Flight flight) {
        return EntityModel.of(flight,
                linkTo(methodOn(FlightController.class).getFlightById(flight.getFlight_id())).withSelfRel(),
                linkTo(methodOn(FlightController.class).getAllFlights()).withRel("flights"));
    }
}
