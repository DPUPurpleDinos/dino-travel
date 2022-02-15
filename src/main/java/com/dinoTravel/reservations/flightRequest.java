package com.dinoTravel.reservations;

public class flightRequest{
  public String departure_airport_code;
  public String arrival_airport_code;
  public String departure_time;
  public String arrival_time;
  public String flight_provider;
  public String flight_code;

  public flightRequest(String departureAirport, String arrivalAirport,
      String departure_time, String arrival_time, String flight_provider, int seats_available,
      String flight_code) {
    this.departure_airport_code = departureAirport;
    this.arrival_airport_code = arrivalAirport;
    this.departure_time = departure_time;
    this.arrival_time = arrival_time;
    this.flight_provider = flight_provider;
    this.flight_code = flight_code;
  }

  @Override
  public String toString() {
    return "flightRequest{" +
        "departure_airport='" + departure_airport_code + '\'' +
        ", arrival_airport='" + arrival_airport_code + '\'' +
        ", departure_time='" + departure_time + '\'' +
        ", arrival_time='" + arrival_time + '\'' +
        ", flight_provider='" + flight_provider + '\'' +
        ", flight_code='" + flight_code + '\'' +
        '}';
  }

  public String getDeparture_airport_code() {
    return departure_airport_code;
  }

  public void setDeparture_airport_code(String departureAirport) {
    this.departure_airport_code = departureAirport;
  }

  public String getArrival_airport_code() {
    return arrival_airport_code;
  }

  public void setArrival_airport_code(String arrival_airport_code) {
    this.arrival_airport_code = arrival_airport_code;
  }

  public String getDeparture_time() {
    return departure_time;
  }

  public void setDeparture_time(String departure_time) {
    this.departure_time = departure_time;
  }

  public String getArrival_time() {
    return arrival_time;
  }

  public void setArrival_time(String arrival_time) {
    this.arrival_time = arrival_time;
  }

  public String getFlight_provider() {
    return flight_provider;
  }

  public void setFlight_provider(String flight_provider) {
    this.flight_provider = flight_provider;
  }

  public String getFlight_code() {
    return flight_code;
  }

  public void setFlight_code(String flight_code) {
    this.flight_code = flight_code;
  }
}
