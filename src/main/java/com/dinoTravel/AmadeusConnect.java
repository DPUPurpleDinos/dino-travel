package com.dinoTravel;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Public enum
 */
public enum AmadeusConnect {
  INSTANCE;
  private final Amadeus amadeus;

  /**
   * Connects to the Amadeus Flight API
   * with key and secretKey stored in
   * application.properties
   */
  AmadeusConnect() {
    Properties properties = new Properties();
    try(FileReader fileReader = new FileReader("src/main/resources/application.properties")){
      properties.load(fileReader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String key = properties.getProperty("key");
    String secretKey = properties.getProperty("secretKey");
    this.amadeus = Amadeus
        .builder(key, secretKey)
        .build();
  }

  /**
   * @param keyword Beginning couple letters you want to match
   * @return returns an array of location objects
   * @throws ResponseException Amadeus response error
   */
  public Location[] location(String keyword) throws ResponseException {
    return amadeus.referenceData.locations.get(Params
        .with("keyword", keyword)
        .and("subType", Locations.AIRPORT));
  }


}
