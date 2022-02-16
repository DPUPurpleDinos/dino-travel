package com.dinoTravel.reviews;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Provides methods to turn a Review into a RepresentationModel
 * and allows links to be added to the Model.
 */

  @Component
  class ReviewModelAssembler implements RepresentationModelAssembler<Review, EntityModel<Review>> {

    /**
     * Converts a Review to a EntityModel of a Review object
     * @param review the review object to convert to an EntityModel
     * @return the Review as an EntityModel
     */

    @Override
    public EntityModel<Review> toModel(Review review) {
      return EntityModel.of(review, linkTo(methodOn(ReviewController.class).getReviewById(review.getReview_id())).withSelfRel());
    }
  }

