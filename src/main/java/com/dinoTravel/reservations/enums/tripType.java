package com.dinoTravel.reservations.enums;

public enum tripType {
  ONEWAY("one way"),
  ROUNDTRIP("round trip"),
  MULTICITY("multi city");

  private final String name;
  private tripType(String name){ this.name = name;}

  @Override
  public String toString(){
    return this.name;
  }
}
