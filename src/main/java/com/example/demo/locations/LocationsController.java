package com.example.demo.locations;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.example.demo.AmadeusConnect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LocationsController {
  @GetMapping("/locations")
  @JsonIgnore
  public Location[] locations(@RequestParam() String keyword) throws ResponseException {
    Location[] hello = AmadeusConnect.INSTANCE.location(keyword);
    for (Location loc : hello){
      System.out.println(loc.toString());
    }
    return hello;
  }
}
