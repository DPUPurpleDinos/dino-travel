package com.dinoTravel.flightOffers;

public class flightOffer{
  public String originLocationCode;
  public String destinationLocationCode;
  public String departureDate;
  public String returnDate;
  public int numAdults;
  //  public int numChildren;
  //  public int numInfants;
  //  public String travelClass;
  //  public boolean nonStop;
  public flightOffer(String originLocationCode, String destinationLocationCode,
      String departureDate, String returnDate, int numAdults) {
    this.originLocationCode = originLocationCode;
    this.destinationLocationCode = destinationLocationCode;
    this.departureDate = departureDate;
    this.returnDate = returnDate;
    this.numAdults = numAdults;
  }

  public String getOriginLocationCode() {
    return originLocationCode;
  }

  public void setOriginLocationCode(String originLocationCode) {
    this.originLocationCode = originLocationCode;
  }

  public String getDestinationLocationCode() {
    return destinationLocationCode;
  }

  public void setDestinationLocationCode(String destinationLocationCode) {
    this.destinationLocationCode = destinationLocationCode;
  }

  public String getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(String departureDate) {
    this.departureDate = departureDate;
  }

  public String getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(String returnDate) {
    this.returnDate = returnDate;
  }

  public int getNumAdults() {
    return numAdults;
  }

  public void setNumAdults(int numAdults) {
    this.numAdults = numAdults;
  }
}
