package com.dinoTravel.complaints;

import javax.persistence.*;


/**
 * A representation of a complaint that allows values
 * to be mapped to keys in a relational database
 */

@Entity
@Table(name = "Complaints")

public class Complaint {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "complaint_id")
  // the complaint_id will not need to be set since it is auto generated
  public int complaint_id;

  @Column(name = "fullName")
  public String fullName;

  @Column(name = "email")
  public String email;

  @Column(name = "reservation_id")
  public int reservation_id;

  @Column(name = "complaint")
  public String complaint;

  /**
   * The constructor to create Flight objects
   * @param complaint_id The integer assigned to a specific complaint (ex: DL1234)
   * @param fullName The name of the customer that is complaining
   * @param email The email of the customer that is complaining
   * @param reservation_id The integer assigned to the reservation associated with the complaint
   * @param complaint The complaint itself
   */
  public Complaint(int complaint_id, String fullName, String email, int reservation_id, String complaint){
    setComplaint_id(complaint_id);
    setFullName(fullName);
    setEmail(email);
    setReservation_id(reservation_id);
    setComplaint(complaint);
  }

  /**
   * Default constructor
   */

  public Complaint() {
  }


  //Getters and Setters

  public int getComplaint_id() {
    return complaint_id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public int getReservation_id() {
    return reservation_id;
  }

  public String getComplaint() {
    return complaint;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setComplaint_id(int complaint_id) {
    this.complaint_id = complaint_id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setReservation_id(int reservation_id) {
    this.reservation_id = reservation_id;
  }

  public void setComplaint(String complaint) {
    this.complaint = complaint;
  }
}
