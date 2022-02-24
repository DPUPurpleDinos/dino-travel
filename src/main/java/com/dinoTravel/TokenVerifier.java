package com.dinoTravel;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/***
 * Class for verifying any incoming tokens
 */
public class TokenVerifier {

  /***
   * The google ID verifier
   */
  private final static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
      .setAudience(Collections.singleton("1042234633479-gpprc2adcpltfjnaij7gib55ko91441n.apps.googleusercontent.com"))
      .build();

  /***
   * Class for verifying if a given token string is valid
   * @param tokenString the token string to verify
   * @return Token Verifier Response. Returned if valid
   * @throws TokenInvalid Error thrown if the token is invalid
   */
  public static TokenVerifierResponse verifyToken(String tokenString) throws TokenInvalid{
    GoogleIdToken idToken;
    //Try to verify the token return a null or token if no exceptions occur
    try {
      //automatically verifies the jwt signature, audience claim, the issuer claim, and the expiration
      //if any fail it will give a null. Most likely the token expired
      idToken = verifier.verify(tokenString);
      //have to catch this general exceptions for the verifier
    } catch (GeneralSecurityException | IOException e) {
      throw new TokenInvalid("A general google exception occurred reason: " + e.getMessage());
      //thrown if the token is too short or too long
    } catch (IllegalArgumentException e){
      throw new TokenInvalid("Invalid Input Length reason: " + e.getMessage());
      //catch any other exceptions
    } catch (Exception e){
      throw new TokenInvalid("An exception occurred reason: " + e.getMessage());
    }
    //Check if the token is not null if, if it is not everything is good
    //else it probably expired
    if (idToken != null) {
      return new TokenVerifierResponse(idToken.getPayload(), true);
    }else{
      throw new TokenInvalid("Your token most likely expired.");
    }
  }
}
