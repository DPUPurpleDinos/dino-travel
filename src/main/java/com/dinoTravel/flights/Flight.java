package com.dinoTravel.flights;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id")
    public int flight_id;

    @Column(name = "seats_available")
    public int seats_available;

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

    public Flight(int seats_available, String flight_provider, String departure_airport, String departure_time, String arrival_airport, String arrival_time) {
        setSeats_available(seats_available);
        setFlight_provider(flight_provider);
        setDeparture_airport(departure_airport);
        setDeparture_time(departure_time);
        setArrival_airport(arrival_airport);
        setArrival_time(arrival_time);
    }

    public Flight() {}

    public int getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(int flight_id) {
        this.flight_id = flight_id;
    }

    public int getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(int seats_available) {
        this.seats_available = seats_available;
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
