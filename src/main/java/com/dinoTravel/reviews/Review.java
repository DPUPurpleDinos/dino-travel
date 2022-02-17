package com.dinoTravel.reviews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;



  /**
   * A representation of a review that allows values
   * to be mapped to keys in a relational database
   */

  @Data
  @Entity
  @Table(name = "reviews")

  public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id")
    // the review_id will not need to be set since it is auto generated
    public int review_id;

    @Column(name = "fullName")
    public String fullName;

    @Column(name = "email")
    public String email;

    @Column(name = "experience_rating")
    public int experience_rating;

    @Column(name = "recommendation_rating")
    public int recommendation_rating;

    @Column(name = "review")
    public String review;

    /**
     * The constructor to create Review objects
     * @param review_id The integer assigned to a specific review (ex: DL1234)
     * @param fullName The name of the customer that is reviewing
     * @param email The email of the customer that is reviewing
     * @param experience_rating The integer the user gives to represent their experience out of 5
     * @param recommendation_rating The integer rating out of 5 that represents if the customer would recommend this application to others.
     * @param review The review itself
     */
    public Review(int review_id, String fullName, String email, int experience_rating, int recommendation_rating, String review){
      setReview_id(review_id);
      setFullName(fullName);
      setEmail(email);
      setExperience_rating(experience_rating);
      setRecommendation_rating(recommendation_rating);
      setReview(review);
    }

    /**
     * Default constructor
     */

    public Review() {
    }


    //Getters and Setters
    public int getReview_id() {
      return review_id;
    }

    public void setReview_id(int review_id) {
      this.review_id = review_id;
    }

    public String getFullName() {
      return fullName;
    }

    public void setFullName(String fullName) {
      this.fullName = fullName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public int getExperience_rating() {
      return experience_rating;
    }

    public void setExperience_rating(int experience_rating) {
      this.experience_rating = experience_rating;
    }

    public int getRecommendation_rating() {
      return recommendation_rating;
    }

    public void setRecommendation_rating(int recommendation_rating) {
      this.recommendation_rating = recommendation_rating;
    }

    public String getReview() {
      return review;
    }

    public void setReview(String review) {
      this.review = review;
    }
  }

