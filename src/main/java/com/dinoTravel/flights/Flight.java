package com.dinoTravel.flights;

import com.dinoTravel.users.exceptions.UserVariableIsNotValidException;
import java.util.Map;
import java.util.function.Consumer;
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

    @Column(name = "seats_available")
    public int seats_available;

    /**
     * The constructor to create Flight objects
     * @param flight_code The code that an airline assigns to a flight (ex: DL1234)
     * @param flight_provider The name of the airline providing the flight(s)
     * @param departure_airport The airport code for the departure airport
     * @param departure_time The DateTime (YYYY-MM-DD HH-MM-SS) the flight is scheduled to depart
     * @param arrival_airport The airport code for the arrival airport
     * @param arrival_time The DateTime (YYYY-MM-DD HH-MM-SS) the flight is scheduled to land
     * @param seats_available The seats_available in the flight
     */
    public Flight(int flight_id, String flight_code, String flight_provider,
        String departure_airport, String departure_time, String arrival_airport,
        String arrival_time, int seats_available) {
        this.flight_id = flight_id;
        this.flight_code = flight_code;
        this.flight_provider = flight_provider;
        this.departure_airport = departure_airport;
        this.departure_time = departure_time;
        this.arrival_airport = arrival_airport;
        this.arrival_time = arrival_time;
        this.seats_available = seats_available;
    }

    @Override
    public String toString() {
        return "Flight{" +
            "flight_id=" + flight_id +
            ", flight_code='" + flight_code + '\'' +
            ", flight_provider='" + flight_provider + '\'' +
            ", departure_airport='" + departure_airport + '\'' +
            ", departure_time='" + departure_time + '\'' +
            ", arrival_airport='" + arrival_airport + '\'' +
            ", arrival_time='" + arrival_time + '\'' +
            ", seats_available=" + seats_available +
            '}';
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

    public int getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(int seats_available) {
        this.seats_available = seats_available;
    }

    public void addSeats_available(int add){
        this.seats_available += add;
    }


}
