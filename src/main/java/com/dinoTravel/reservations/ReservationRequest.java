package com.dinoTravel.reservations;

import com.dinoTravel.flights.Flight;
import com.dinoTravel.reservations.enums.travelerType;
import com.dinoTravel.reservations.enums.tripType;

import com.dinoTravel.travelClass;
import java.util.Arrays;

public class ReservationRequest {
  public double price;
  // enum ONEWAY, ROUNDTRIP, MULTICITY
  public tripType trip_type;
  //enum ADULT (12+), CHILD (2-12), INFANT (2-0)
  public travelerType traveler_type;
  //name of the traveler for the ticket
  public String traveler_name;
  // ID is saved as a String to use the seat's natural key (ex: "27A")
  public String seat_id;
  //class of the seat
  public travelClass seat_class;
  public int num_checked_bags;

  //flight info
  public Flight[] flight_request_info;

  public ReservationRequest(double price, com.dinoTravel.reservations.enums.tripType tripType,
      travelerType traveler_type, String traveler_name, String seat_id,
      travelClass seat_class, int num_checked_bags,
      Flight [] flight_request_info) {
    this.price = price;
    this.trip_type = tripType;
    this.traveler_type = traveler_type;
    this.traveler_name = traveler_name;
    this.seat_id = seat_id;
    this.seat_class = seat_class;
    this.num_checked_bags = num_checked_bags;
    this.flight_request_info = flight_request_info;
  }

  @Override
  public String toString() {
    return "ReservationRequest{" +
        "price=" + price +
        ", trip_type=" + trip_type +
        ", traveler_type=" + traveler_type +
        ", traveler_name='" + traveler_name + '\'' +
        ", seat_id='" + seat_id + '\'' +
        ", seat_class=" + seat_class +
        ", num_checked_bags=" + num_checked_bags +
        ", flightRequestInfo=" + Arrays.toString(flight_request_info) +
        '}';
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public com.dinoTravel.reservations.enums.tripType getTrip_type() {
    return trip_type;
  }

  public void setTrip_type(com.dinoTravel.reservations.enums.tripType trip_type) {
    this.trip_type = trip_type;
  }

  public travelerType getTraveler_type() {
    return traveler_type;
  }

  public void setTraveler_type(travelerType traveler_type) {
    this.traveler_type = traveler_type;
  }

  public String getTraveler_name() {
    return traveler_name;
  }

  public void setTraveler_name(String traveler_name) {
    this.traveler_name = traveler_name;
  }

  public String getSeat_id() {
    return seat_id;
  }

  public void setSeat_id(String seat_id) {
    this.seat_id = seat_id;
  }

  public travelClass getSeat_class() {
    return seat_class;
  }

  public void setSeat_class(travelClass seat_class) {
    this.seat_class = seat_class;
  }

  public int getNum_checked_bags() {
    return num_checked_bags;
  }

  public void setNum_checked_bags(int num_checked_bags) {
    this.num_checked_bags = num_checked_bags;
  }

  public Flight[] getFlight_request_info() {
    return flight_request_info;
  }

  public void setFlight_request_info(Flight[] flight_request_info) {
    this.flight_request_info = flight_request_info;
  }
}
