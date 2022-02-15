package com.dinoTravel.flights;

import com.dinoTravel.reservations.flightRequest;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

  @Query(value = "Select count(1) from flights where departure_airport=?1 and arrival_airport=?2 and departure_time=?3 and arrival_time=?4 and flight_provider=?5 and flight_code=?6", nativeQuery = true)
  BigInteger existsByInfo(String departure_airport_code,
      String arrival_airport_code,
      String departure_time,
      String arrival_time,
      String flight_provider,
      String flight_code);
}
