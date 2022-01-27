package com.dinoTravel.flightOffers;

import com.amadeus.Params;
import com.amadeus.exceptions.ClientException;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.dinoTravel.Amadeus;
import com.dinoTravel.flights.FlightNotFoundException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
class flightOfferExceptionController{
  @ResponseBody
  @ExceptionHandler(flightOfferException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String flightNotFoundHandler(flightOfferException ex) {
    return ex.getMessage();
  }
}

@RestController
@RequestMapping("/api/flightOffers")
public class flightOfferController {

  @GetMapping
  public FlightOfferSearch[] getFlight (@RequestParam Map<String, String> requestParameters) throws ResponseException
  {
    Params p = Params.with("currencyCode", "USD");
    for(Map.Entry<String, String> pair : requestParameters.entrySet()){
      System.out.println(pair.getKey() + pair.getValue());
      p.and(pair.getKey(), pair.getValue());
    }
    try{
      return Amadeus.Connect.getFlightOffers(p);
    }catch (ClientException e){
      throw new flightOfferException(e.getMessage());
    }
  }
}
