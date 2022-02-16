package com.dinoTravel.reviews;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles exceptions that get thrown by the ReviewController
 */
@ControllerAdvice
class ReviewNotFoundAdvice {

  /**
   * Generates a 404 status if a requested ID is not found and
   * returns an error message as a String
   * @param ex ComplaintNotFoundException
   * @return Error message containing the review_id that caused the exception
   */

  @ResponseBody
  @ExceptionHandler(ReviewNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String ReviewNotFoundHandler(ReviewNotFoundException ex){
    return ex.getMessage();
  }

  /**
   *  Handles HTTP requests for Review objects
   */
}
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

  @Autowired
  private ReviewRepository reviewRepository;
  private final ReviewModelAssembler reviewAssembler;

  /**
   * Constructor to create a RestController for Review objects
   *
   * @param repository Repository to save the Review objects
   * @param assembler  Assembler to create the JSON response
   */

  ReviewController(ReviewRepository repository, ReviewModelAssembler assembler) {
    this.reviewRepository = repository;
    this.reviewAssembler = assembler;
  }

  /**
   * Returns all reviews saved in the ReviewRepository
   *
   * @return A collection of Reviews and their bodies represented as an EntityModel
   */
  @GetMapping()
  CollectionModel<EntityModel<Review>> GetAllReviews() {
    List<EntityModel<Review>> reviews = reviewRepository.findAll().stream()
        .map(reviewAssembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(reviews);
  }
  /**
   * Return the body for a single review
   * @param reviewId the ID for the review
   * @return The body of the review as an EntityModel
   */
  @GetMapping("/{id}")
  EntityModel<Review> getReviewById(@PathVariable ("id") int reviewId) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));

    return reviewAssembler.toModel(review);
  }

  /**
   * Update an existing review already contained in the ReviewRepository Otherwise save it to the
   * ReviewRepository
   *
   * @param review   The body of the review
   * @param reviewId The id for the existing review
   * @return The body of the updated review as a ResponseEntity
   */
  @PutMapping("/{id}")
  ResponseEntity<?> updateFlight(@RequestBody Review review, @PathVariable("id") int reviewId) {
    Review existingReview = reviewRepository.findById(reviewId)
        .map(newReview -> {
          newReview.setReview_id(review.getReview_id());
          newReview.setFullName(review.getFullName());
          newReview.setEmail(review.getEmail());
          newReview.setExperience_rating(review.getExperience_rating());
          newReview.setRecommendation_rating(review.getExperience_rating());
          newReview.setReview(review.getReview());
          return reviewRepository.save(newReview);
        }).orElseGet(() -> {
          review.setReview_id(reviewId);
          return reviewRepository.save(review);
        });
    EntityModel<Review> entityModel = reviewAssembler.toModel(existingReview);

    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }
  /**
   * Create a new review to be added to the ReviewRepository
   * @param review The body of the review
   * @return The body of the created review as a ResponseEntity
   */
  @PostMapping
  ResponseEntity<?> createReview(@RequestBody Review review) {
    EntityModel<Review> entityModel = reviewAssembler.toModel(reviewRepository.save(review));

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
  }
  /**
   * Delete a review from the ReviewRepository
   * @param reviewId The id for a review to be deleted
   * @return An empty body as a ResponseEntity
   */
  @DeleteMapping("/{id}")
  ResponseEntity<?> deleteReview(@PathVariable ("id") int reviewId) {
    reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(reviewId));

    reviewRepository.deleteById(reviewId);

    return ResponseEntity.noContent().build();
  }
}

