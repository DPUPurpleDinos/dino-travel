package com.dinoTravel.reservations;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    public int reservation_id;

    @Column(name = "user_id")
    public int user_id;

    @Column(name = "trip_type")
    public String trip_type;

    @Column(name = "outgoing_flight_type")
    public String outgoing_flight_type;

    @Column(name = "outgoing_flight_id")
    public int outgoing_flight_id;

    @Column(name = "returning_flight_type")
    public String returning_flight_type;

    @Column(name = "returning_flight_id")
    public int returning_flight_id;

    @Column(name = "price")
    public double price;

    public Reservation(int user_id, String trip_type, String outgoing_flight_type, int outgoing_flight_id, String returning_flight_type, int returning_flight_id, double price) {
        setUser_id(user_id);
        setTrip_type(trip_type);
        setOutgoing_flight_type(outgoing_flight_type);
        setOutgoing_flight_id(outgoing_flight_id);
        setReturning_flight_type(returning_flight_type);
        setReturning_flight_id(returning_flight_id);
        setPrice(price);
    }

    public Reservation() {}

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

    public String getOutgoing_flight_type() {
        return outgoing_flight_type;
    }

    public void setOutgoing_flight_type(String outgoing_flight_type) {
        this.outgoing_flight_type = outgoing_flight_type;
    }

    public int getOutgoing_flight_id() {
        return outgoing_flight_id;
    }

    public void setOutgoing_flight_id(int outgoing_flight_id) {
        this.outgoing_flight_id = outgoing_flight_id;
    }

    public String getReturning_flight_type() {
        return returning_flight_type;
    }

    public void setReturning_flight_type(String returning_flight_type) {
        this.returning_flight_type = returning_flight_type;
    }

    public int getReturning_flight_id() {
        return returning_flight_id;
    }

    public void setReturning_flight_id(int returning_flight_id) {
        this.returning_flight_id = returning_flight_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
