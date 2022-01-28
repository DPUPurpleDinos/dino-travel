package com.dinoTravel;

import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Public enum that has the connection to the Amadeus API
 * use these methods if you want to get flight data
 */
public enum Amadeus {
  Connect;
  private final com.amadeus.Amadeus amadeus;

  /**
   * Connects to the Amadeus Flight API
   * with key and secretKey stored in
   * application.properties
   */
  Amadeus() {
    //make a properties object
    Properties properties = new Properties();
    //open the application file from the classpath
    try(InputStream is = getClass().getResourceAsStream("/application.properties")){
      properties.load(is);
    }catch (IOException e){
      e.printStackTrace();
    }
    //set the key and secretKey
    String key = properties.getProperty("key");
    String secretKey = properties.getProperty("secretKey");
    //connect to the api
    this.amadeus = com.amadeus.Amadeus
        .builder(key, secretKey)
        .build();
  }

  /**
   * @param parameters the parameters for the function
   * @return returns an array of location objects
   * @throws ResponseException Amadeus response error
   */
  public Location[] getLocationNames(Params parameters) throws ResponseException {
    return amadeus.referenceData.locations.get(parameters);
  }

  /**
   * Amadeus api function to request flight offers
   * @param parameters the paramaters of the request
   * @return an array with all the offers available
   * @throws ResponseException Amadeus response error
   */
  public FlightOfferSearch[] getFlightOffers(Params parameters) throws ResponseException {
    return amadeus.shopping.flightOffersSearch.get(parameters);
  }
}
