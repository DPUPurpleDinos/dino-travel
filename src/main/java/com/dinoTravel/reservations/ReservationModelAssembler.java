package com.dinoTravel.reservations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Provides methods to turn a Reservation into a RepresentationModel
 * and allows links to be added to the Model.
 */
@Component
public class ReservationModelAssembler implements RepresentationModelAssembler<Reservation, EntityModel<Reservation>> {

    /**
     * Converts a Reservation to a EntityModel of a Reservation object
     * @param  reservation the Reservation object to convert to an EntityModel
     * @return the reservation as an EntityModel
     */
    @Override
    public EntityModel<Reservation> toModel(Reservation reservation) {
        return EntityModel.of(reservation);
    }
}
