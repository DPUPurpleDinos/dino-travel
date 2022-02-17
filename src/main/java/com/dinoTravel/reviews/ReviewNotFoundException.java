package com.dinoTravel.reviews;

public class ReviewNotFoundException extends RuntimeException {
  ReviewNotFoundException(int review_id) {super("Could not find review with ID: " + review_id);}
}
