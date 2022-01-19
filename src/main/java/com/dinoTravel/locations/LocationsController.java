package com.dinoTravel.locations;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.dinoTravel.Amadeus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LocationsController {
  @GetMapping("/locations")
  @JsonIgnore
  public Location[] locations(@RequestParam() String keyword) throws ResponseException {
    Location[] hello = Amadeus.Connect.location(keyword);
    for (Location loc : hello){
      System.out.println(loc.toString());
    }
    return hello;
  }
}
