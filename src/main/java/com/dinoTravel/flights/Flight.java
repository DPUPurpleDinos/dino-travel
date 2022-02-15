package com.dinoTravel.flights;

import com.dinoTravel.reservations.flightRequest;
import lombok.Data;

import javax.persistence.*;

/**
 * A representation of a flight that allows values
 * to be mapped to keys in a relational database
 */

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id")
    // the flight_id will not need to be set since it is auto generated
    public int flight_id;

    @Column(name = "flight_code")
    public String flight_code;

    @Column(name = "flight_provider")
    public String flight_provider;

    @Column(name = "departure_airport")
    public String departure_airport;

    @Column(name = "departure_time")
    public String departure_time;

    @Column(name = "arrival_airport")
    public String arrival_airport;

    @Column(name = "arrival_time")
    public String arrival_time;

    /**
     * The constructor to create Flight objects
     * @param flight_code The code that an airline assigns to a flight (ex: DL1234)
     * @param flight_provider The name of the airline providing the flight(s)
     * @param departure_airport The airport code for the departure airport
     * @param departure_time The DateTime (YYYY-MM-DD HH-MM-SS) the flight is scheduled to depart
     * @param arrival_airport The airport code for the arrival airport
     * @param arrival_time The DateTime (YYYY-MM-DD HH-MM-SS) the flight is scheduled to land
     */
    public Flight(String flight_code, String flight_provider, String departure_airport, String departure_time, String arrival_airport, String arrival_time) {
        setFlight_code(flight_code);
        setFlight_provider(flight_provider);
        setDeparture_airport(departure_airport);
        setDeparture_time(departure_time);
        setArrival_airport(arrival_airport);
        setArrival_time(arrival_time);
    }


    /**
     * Default constructor
     */
    public Flight() {}


    // Getters and setters

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public String getFlight_code() {
        return flight_code;
    }

    public void setFlight_code(String flight_code) {
        this.flight_code = flight_code;
    }

    public String getFlight_provider() {
        return flight_provider;
    }

    public void setFlight_provider(String flight_provider) {
        this.flight_provider = flight_provider;
    }

    public String getDeparture_airport() {
        return departure_airport;
    }

    public void setDeparture_airport(String departure_airport) {
        this.departure_airport = departure_airport;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrival_airport() {
        return arrival_airport;
    }

    public void setArrival_airport(String arrival_airport) {
        this.arrival_airport = arrival_airport;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }
}
