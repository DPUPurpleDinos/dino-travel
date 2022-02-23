package com.dinoTravel;

public enum travelClass {
  ECONOMY("ECONOMY"),
  PREMIUM_ECONOMY("PREMIUM_ECONOMY"),
  BUSINESS("BUSINESS"),
  FIRST("FIRST");

  private final String name;
  private travelClass(String name){
    this.name = name;
  }

  @Override
  public String toString(){
    return this.name;
  }
}
