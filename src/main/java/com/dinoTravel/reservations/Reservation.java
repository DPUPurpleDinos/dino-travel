package com.dinoTravel.reservations;

import lombok.Data;

import javax.persistence.*;

/**
 * A representation of a reservation that allows values
 * to be mapped to keys in a relational database
 */
@Data
@Entity
@Table(name = "reservations")
public class Reservation {


    // the reservation_id will not need to be set since it is auto generated
    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    public int reservation_id;

    // Foreign key
    @Column(name = "user_id")
    public int user_id;

    @Column(name = "trip_type")
    public String trip_type;

    // Foreign key
    @Column(name = "flight_id")
    public int flight_id;

    @Column(name = "traveler_type")
    public String traveler_type;

    @Column(name = "traveler_name")
    public String traveler_name;

    @Column(name = "num_checked_bags")
    public int num_checked_bags;

    // ID is saved as a String to use the seat's natural key (ex: "27A")
    @Column(name = "seat_id")
    public String seat_id;

    @Column(name = "seat_type")
    public String seat_type;

    @Column(name = "price")
    public double price;

    /**
     * The constructor to create Reservation objects
     * @param user_id The ID of the user creating the reservation. Relates to a user in the Users table
     * @param trip_type ONE_WAY, ROUND_TRIP, MULTI_CITY
     * @param flight_id The ID of the flight. Relates to a flight in the Flights table
     * @param traveler_type ADULT, CHILD
     * @param traveler_name The name of the traveler reserving the seat
     * @param num_checked_bags The number of checked bags a user
     * @param seat_id The ID of the seat
     * @param seat_type FIRST_CLASS, ECONOMY, BUSINESS
     * @param price The price of the reserved seat
     */
    public Reservation(int user_id, String trip_type, int flight_id, String traveler_type,
           String traveler_name, int num_checked_bags, String seat_id, String seat_type, double price) {
        setUser_id(user_id);
        setTrip_type(trip_type);
        setFlight_id(flight_id);
        setTraveler_type(traveler_type);
        setTraveler_name(traveler_name);
        setNum_checked_bags(num_checked_bags);
        setSeat_id(seat_id);
        setSeat_type(seat_type);
        setPrice(price);
    }

    /**
     * Default constructor
     */
    public Reservation() {}

    // Getters and setters

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public String getTraveler_type() {
        return traveler_type;
    }

    public void setTraveler_type(String traveler_type) {
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

    public String getSeat_type() {
        return seat_type;
    }

    public void setSeat_type(String seat_type) {
        this.seat_type = seat_type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum_checked_bags() { return num_checked_bags; }

    public void setNum_checked_bags(int num_checked_bags) { this.num_checked_bags = num_checked_bags; }
}
