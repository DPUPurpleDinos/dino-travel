package com.dinoTravel.locations;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.dinoTravel.Amadeus;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/locations", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationsController {
  private final Gson gson = new Gson();

  @GetMapping
  public String locations(@RequestParam() String keyword) throws ResponseException {
    Params p = Params.with("keyword", keyword);
    p.and("subType", Locations.ANY);
    p.and("page[limit]", 100);
    p.and("view", "LIGHT");
    return gson.toJson(Amadeus.Connect.getLocationNames(p));
  }
}
