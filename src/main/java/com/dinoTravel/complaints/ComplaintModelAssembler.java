package com.dinoTravel.complaints;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Provides methods to turn a Complaint into a RepresentationModel
 * and allows links to be added to the Model.
 */

@Component
class ComplaintModelAssembler implements RepresentationModelAssembler<Complaint, EntityModel<Complaint>> {

  /**
   * Converts a Complaint to a EntityModel of a Complaint object
   * @param complaint the complaint object to convert to an EntityModel
   * @return the Complaint as an EntityModel
   */

  @Override
  public EntityModel<Complaint> toModel(Complaint complaint) {
    return EntityModel.of(complaint, linkTo(methodOn(ComplaintController.class).getComplaintById(complaint.getComplaint_id())).withSelfRel());
  }
}
