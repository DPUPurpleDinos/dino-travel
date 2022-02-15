package com.dinoTravel.reservations;

import com.dinoTravel.reservations.enums.travelerType;
import com.dinoTravel.reservations.enums.tripType;
import com.dinoTravel.travelClass;
import javax.persistence.*;

/**
 * A representation of a reservation that allows values
 * to be mapped to keys in a relational database
 */
@Entity
@Table(name = "auth_reservations")
public class Reservation {

    // the reservation_id will not need to be set since it is auto generated
    @Id
    @GeneratedValue
    @Column(name = "reservation_id", nullable = false, updatable = false)
    public int reservation_id;

    //the id of the booking the reservation is apart of
    @Column(name = "booking_id", nullable = false, updatable = false)
    public long booking_id;

    // Foreign key
    @Column(name = "subject_id", nullable = false, updatable = false)
    public String subject_id;

    @Column(name = "price", nullable = false)
    public double price;

    //enum ONEWAY, ROUNDTRIP, MULTICITY
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "trip_type", nullable = false)
    public tripType tripType;

    // Foreign key
    @Column(name = "flight_id", nullable = false)
    public int flight_id;

    //enum ADULT (12+), CHILD (2-12), INFANT (2-0)
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "traveler_type", nullable = false)
    public travelerType traveler_type;

    //name of the traveler for the ticket
    @Column(name = "traveler_name", nullable = false)
    public String traveler_name;

    // ID is saved as a String to use the seat's natural key (ex: "27A")
    @Column(name = "seat_id", nullable = false)
    public String seat_id;

    //class of the seat
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "seat_class", nullable = false)
    public travelClass seat_class;

    @Column(name = "num_checked_bags", nullable = false)
    public int num_checked_bags;


    public Reservation(int reservation_id, long booking_id, String subject_id, double price,
        tripType trip_Type, int flight_id, travelerType traveler_type, String traveler_name,
        String seat_id, travelClass seat_class, int num_checked_bags) {
        this.reservation_id = reservation_id;
        this.booking_id = booking_id;
        this.subject_id = subject_id;
        this.price = price;
        this.tripType = trip_Type;
        this.flight_id = flight_id;
        this.traveler_type = traveler_type;
        this.traveler_name = traveler_name;
        this.seat_id = seat_id;
        this.seat_class = seat_class;
        this.num_checked_bags = num_checked_bags;
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

    public long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(long booking_id) {
        this.booking_id = booking_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public com.dinoTravel.reservations.enums.tripType getTripType() {
        return tripType;
    }

    public void setTripType(com.dinoTravel.reservations.enums.tripType tripType) {
        this.tripType = tripType;
    }

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
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
}
