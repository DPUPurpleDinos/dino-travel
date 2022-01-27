package com.dinoTravel.flightOffers;

import com.amadeus.Params;
import com.amadeus.exceptions.ClientException;
import com.amadeus.exceptions.ResponseException;
import com.dinoTravel.Amadeus;
import com.google.gson.Gson;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles exceptions for the flight Offer class
 */
@ControllerAdvice
class flightOfferExceptionController{

  /**
   * Generates a 400 error if the inputs are invalid
   * @param e flightOfferException
   * @return String stating the error
   */
  @ResponseBody
  @ExceptionHandler(flightOfferException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String flightNotFoundHandler(flightOfferException e) {
    return e.getMessage();
  }
}

/**
 * Handles HTTP requests for flight Offers
 */
@RestController
@RequestMapping(value = "/api/flightOffers", produces = MediaType.APPLICATION_JSON_VALUE)
public class flightOfferController {
  private final Gson gson = new Gson();

  /**
   * Gateway into the Amadeus flight offer search api
   * @param requestParameters A map of all the parameters given
   * @return A json as a string of the response from the api
   * @throws ResponseException Amadeus standard error
   */
  @GetMapping
  public String getFlight (@RequestParam Map<String, String> requestParameters) throws ResponseException
  {
    //Make a new parameter object and add the request to it
    Params p = Params.with("currencyCode", "USD");
    for(Map.Entry<String, String> pair : requestParameters.entrySet()){
      p.and(pair.getKey(), pair.getValue());
    }

    //Try and make the request if it fails the parameters are wrong and
    //raise an exception
    try{
      //The returned Objects are serialized with GSON, have to convert it to a string
      return gson.toJson(Amadeus.Connect.getFlightOffers(p));
    }catch (ClientException e){
      throw new flightOfferException(e.getMessage());
    }
  }

}
